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

import cloudspec.CloudSpecParser
import cloudspec.lang.GroupDecl
import cloudspec.lang.SetDecl
import cloudspec.lang.UseGroupDecl
import cloudspec.model.Group
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class CloudSpecGroupLoader {
    companion object {
        private val logger = LoggerFactory.getLogger(CloudSpecGroupLoader::class.java)

        @Throws(IOException::class)
        fun loadFromFile(groupFile: File, parentSets: List<SetDecl>): Group {
            return loadFromInputStream(FileInputStream(groupFile), groupFile.parentFile, parentSets)
        }


        @Throws(IOException::class)
        fun loadFromInputStream(groupIs: InputStream, parentDir: File, parentSets: List<SetDecl>): Group {
            val lexer = BailCloudSpecLexer(ANTLRInputStream(groupIs))
            val tokens = CommonTokenStream(lexer)
            val parser = CloudSpecParser(tokens)
            parser.errorHandler = BailErrorStrategy()
            parser.buildParseTree = true
            val tree: ParseTree = parser.groupDecl()
            val walker = ParseTreeWalker()
            val listener = CloudSpecListener()
            walker.walk(listener, tree)
            return loadFromDecl(listener.group, parentDir, parentSets)
        }

        @Throws(IOException::class)
        fun loadFromDecl(groupDecl: GroupDecl, parentDir: File, parentSets: List<SetDecl>): Group {
            val groupSets = parentSets.plus(groupDecl.sets)
            return Group(groupDecl.name,
                         groupDecl.useRules.map {
                             CloudSpecRuleLoader.loadUseRule(it, parentDir, groupSets)
                         }.plus(groupDecl.rules.map {
                             CloudSpecRuleLoader.loadFromDecl(it, groupSets)
                         }))
        }

        @Throws(IOException::class)
        fun loadUseGroup(useGroupDecl: UseGroupDecl, parentDir: File, parentSets: List<SetDecl>): Group {
            try {
                val groupFile = if (useGroupDecl.path.startsWith("/")) {
                    File(useGroupDecl.path)
                } else {
                    File(parentDir.absolutePath, useGroupDecl.path)
                }
                return loadFromFile(groupFile, parentSets)
            } catch (e: IOException) {
                logger.error("Error loading group ${useGroupDecl.path}: ${e.message}")
                throw e
            }
        }
    }
}
