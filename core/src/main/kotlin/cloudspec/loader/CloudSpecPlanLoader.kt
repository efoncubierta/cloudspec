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
import cloudspec.lang.PlanDecl
import cloudspec.model.Plan
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class CloudSpecPlanLoader {
    companion object {
        private val logger = LoggerFactory.getLogger(CloudSpecPlanLoader::class.java)

        @Throws(IOException::class)
        fun loadFromFile(planFile: File): Plan {
            return loadFromInputStream(FileInputStream(planFile), planFile.parentFile)
        }

        @Throws(IOException::class)
        fun loadFromInputStream(planIs: InputStream, parentDir: File): Plan {
            val lexer = BailCloudSpecLexer(ANTLRInputStream(planIs))
            val tokens = CommonTokenStream(lexer)
            val parser = CloudSpecParser(tokens)
            parser.errorHandler = BailErrorStrategy()
            parser.buildParseTree = true
            val tree: ParseTree = parser.planDecl()
            val walker = ParseTreeWalker()
            val listener = CloudSpecListener()
            walker.walk(listener, tree)
            return loadFromDecl(listener.plan, parentDir)
        }

        @Throws(IOException::class)
        fun loadFromDecl(planDecl: PlanDecl, parentDir: File): Plan {
            return Plan(planDecl.name,
                        planDecl.useModules.map {
                            CloudSpecModuleLoader.loadUseModule(it, parentDir, planDecl.sets)
                        },
                        planDecl.useGroups.map {
                            CloudSpecGroupLoader.loadUseGroup(it, parentDir, planDecl.sets)
                        }.plus(planDecl.groups.map {
                            CloudSpecGroupLoader.loadFromDecl(it, parentDir, planDecl.sets)
                        }),
                        planDecl.useRules.map {
                            CloudSpecRuleLoader.loadUseRule(it, parentDir, planDecl.sets)
                        }.plus(planDecl.rules.map {
                            CloudSpecRuleLoader.loadFromDecl(it, planDecl.sets)
                        }))

        }
    }
}
