/*-
 * #%L
 * CloudSpec Core Library
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package cloudspec.loader

import arrow.core.Some
import cloudspec.CloudSpecModuleLexer
import cloudspec.CloudSpecModuleParser
import cloudspec.lang.*
import cloudspec.model.*
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class CloudSpecModuleLoader {
    companion object {
        private val logger = LoggerFactory.getLogger(CloudSpecModuleLoader::class.java)

        @Throws(IOException::class)
        fun loadFromFile(moduleFile: File, planSets: List<SetDecl> = emptyList()): Module {
            return loadFromInputStream(FileInputStream(moduleFile), planSets)
        }

        @Throws(IOException::class)
        fun loadDeclFromFile(moduleFile: File): ModuleDecl {
            return loadDeclFromInputStream(FileInputStream(moduleFile))
        }

        @Throws(IOException::class)
        fun loadDeclFromInputStream(moduleIs: InputStream): ModuleDecl {
            val lexer = CloudSpecModuleLexer(ANTLRInputStream(moduleIs))
            val tokens = CommonTokenStream(lexer)
            val parser = CloudSpecModuleParser(tokens)
            parser.buildParseTree = true
            val tree: ParseTree = parser.module()
            val walker = ParseTreeWalker()
            val listener = CloudSpecModuleListener()
            walker.walk(listener, tree)
            return listener.module
        }

        @Throws(IOException::class)
        fun loadFromInputStream(moduleIs: InputStream, planSets: List<SetDecl> = emptyList()): Module {
            return loadFromDecl(loadDeclFromInputStream(moduleIs), planSets)
        }

        @Throws(IOException::class)
        private fun loadFromDecl(moduleDecl: ModuleDecl, planSets: List<SetDecl> = emptyList()): Module {
            return Module(moduleDecl.name,
                          moduleDecl.groups.map { loadGroup(it, planSets.plus(moduleDecl.sets)) })
        }

        private fun loadGroup(groupDecl: GroupDecl, moduleSets: List<SetDecl>): Group {
            return Group(groupDecl.name,
                         groupDecl.rules.map { loadRule(it, moduleSets.plus(groupDecl.sets)) })
        }

        private fun loadRule(ruleDecl: RuleDecl, groupSets: List<SetDecl>): Rule {
            when (val resourceDefRefOpt = ResourceDefRef.fromString(ruleDecl.defRef)) {
                is Some ->
                    return Rule(ruleDecl.name,
                                resourceDefRefOpt.t,
                                loadWiths(ruleDecl.withs),
                                loadAsserts(ruleDecl.asserts),
                                loadSets(groupSets.plus(ruleDecl.sets)))
                else ->
                    throw Exception("Malformed resource definition reference ${ruleDecl.defRef}")
            }
        }

        private fun loadWiths(withDecl: WithDecl): List<Statement> {
            return withDecl.statements
        }

        private fun loadAsserts(assertDecl: AssertDecl): List<Statement> {
            return assertDecl.statements
        }

        private fun loadSets(set: List<SetDecl>): ConfigValues {
            return set.mapNotNull { c ->
                when (val configRefOpt = ConfigRef.fromString(c.configRef)) {
                    is Some ->
                        when (c.value) {
                            is Number -> NumberConfigValue(configRefOpt.t, c.value)
                            is String -> StringConfigValue(configRefOpt.t, c.value)
                            is Boolean -> BooleanConfigValue(configRefOpt.t, c.value)
                            else -> {
                                logger.error("Unsupported type of config '${c.configRef}'. Ignoring it.")
                                null
                            }
                        }
                    else -> {
                        logger.error("Malformed config definition '${c.configRef}'. Ignoring it.")
                        null
                    }
                }
            }
        }
    }
}
