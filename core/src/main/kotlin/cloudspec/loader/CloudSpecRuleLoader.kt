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
import cloudspec.CloudSpecParser
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

class CloudSpecRuleLoader {
    companion object {
        private val logger = LoggerFactory.getLogger(CloudSpecRuleLoader::class.java)

        @Throws(IOException::class)
        fun loadFromFile(ruleFile: File, parentSets: List<SetDecl> = emptyList()): Rule {
            return loadFromInputStream(FileInputStream(ruleFile), parentSets)
        }

        @Throws(IOException::class)
        fun loadFromInputStream(ruleIs: InputStream, parentSets: List<SetDecl>): Rule {
            val lexer = BailCloudSpecLexer(ANTLRInputStream(ruleIs))
            val tokens = CommonTokenStream(lexer)
            val parser = CloudSpecParser(tokens)
            parser.errorHandler = BailErrorStrategy()
            parser.buildParseTree = true
            val tree: ParseTree = parser.ruleDecl()
            val walker = ParseTreeWalker()
            val listener = CloudSpecListener()
            walker.walk(listener, tree)
            return loadFromDecl(listener.rule, parentSets)
        }

        @Throws(IOException::class)
        fun loadFromDecl(ruleDecl: RuleDecl, parentSets: List<SetDecl>): Rule {
            when (val resourceDefRefOpt = ResourceDefRef.fromString(ruleDecl.defRef)) {
                is Some ->
                    return Rule(ruleDecl.name,
                                resourceDefRefOpt.t,
                                loadWiths(ruleDecl.withs),
                                loadAsserts(ruleDecl.asserts),
                                loadSets(parentSets.plus(ruleDecl.sets)))
                else ->
                    throw Exception("Malformed resource definition reference ${ruleDecl.defRef}")
            }
        }

        @Throws(IOException::class)
        fun loadUseRule(useRuleDecl: UseRuleDecl, parentDir: File, parentSets: List<SetDecl>): Rule {
            try {
                val ruleFile = if (useRuleDecl.path.startsWith("/")) {
                    File(useRuleDecl.path)
                } else {
                    File(parentDir.absolutePath, useRuleDecl.path)
                }
                return loadFromFile(ruleFile, parentSets)
            } catch (e: IOException) {
                logger.error("Error loading rule ${useRuleDecl.path}: ${e.message}")
                throw e
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
