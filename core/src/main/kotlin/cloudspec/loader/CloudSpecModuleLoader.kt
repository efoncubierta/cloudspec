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

import cloudspec.CloudSpecLexer
import cloudspec.CloudSpecParser
import cloudspec.lang.ModuleDecl
import cloudspec.lang.SetDecl
import cloudspec.lang.UseModuleDecl
import cloudspec.model.Module
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
        fun loadFromFile(moduleFile: File, parentSets: List<SetDecl>): Module {
            return loadFromInputStream(FileInputStream(moduleFile), moduleFile.parentFile, parentSets)
        }

        @Throws(IOException::class)
        fun loadFromInputStream(moduleIs: InputStream, parentDir: File, parentSets: List<SetDecl>): Module {
            return loadFromDecl(loadDeclFromInputStream(moduleIs, parentDir, parentSets),
                                parentDir, parentSets)
        }

        @Throws(IOException::class)
        fun loadDeclFromInputStream(moduleIs: InputStream, parentDir: File, parentSets: List<SetDecl>): ModuleDecl {
            val lexer = CloudSpecLexer(ANTLRInputStream(moduleIs))
            val tokens = CommonTokenStream(lexer)
            val parser = CloudSpecParser(tokens)
            parser.buildParseTree = true
            val tree: ParseTree = parser.moduleDecl()
            val walker = ParseTreeWalker()
            val listener = CloudSpecListener()
            walker.walk(listener, tree)
            return listener.module
        }


        @Throws(IOException::class)
        fun loadFromDecl(moduleDecl: ModuleDecl, parentDir: File, parentSets: List<SetDecl>): Module {
            val moduleSets = parentSets.plus(moduleDecl.sets)
            return Module(moduleDecl.name,
                          moduleDecl.useModules.map {
                              loadUseModule(it, parentDir, moduleSets)
                          },
                          moduleDecl.useGroups.map {
                              CloudSpecGroupLoader.loadUseGroup(it, parentDir, moduleSets)
                          }.plus(moduleDecl.groups.map {
                              CloudSpecGroupLoader.loadFromDecl(it, parentDir, moduleSets)
                          }),
                          moduleDecl.useRules.map {
                              CloudSpecRuleLoader.loadUseRule(it, parentDir, moduleSets)
                          }.plus(moduleDecl.rules.map {
                              CloudSpecRuleLoader.loadFromDecl(it, moduleSets)
                          }))
        }

        @Throws(IOException::class)
        fun loadUseModule(useModuleDecl: UseModuleDecl, parentDir: File, parentSets: List<SetDecl>): Module {
            try {
                val moduleFile = if (useModuleDecl.path.startsWith("/")) {
                    File(useModuleDecl.path)
                } else {
                    File(parentDir.absolutePath, useModuleDecl.path)
                }
                return loadFromFile(moduleFile, parentSets)
            } catch (e: IOException) {
                logger.error("Error loading module ${useModuleDecl.path}: ${e.message}")
                throw e
            }
        }
    }
}
