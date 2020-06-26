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

import arrow.core.*
import arrow.core.extensions.fx
import cloudspec.CloudSpecLexer
import cloudspec.CloudSpecParser
import cloudspec.lang.*
import cloudspec.model.*
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.misc.ParseCancellationException
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException

/**
 * Load modules from different sources: directory, string, object...
 */
class ModuleLoader {
    companion object {
        private val DEFAULT_MODULE_NAME: String = "main"
        private val DEFAULT_INHERITED_SETS: List<SetDecl> = emptyList()
        private val logger = LoggerFactory.getLogger(ModuleLoader::class.java)

        /**
         * Load a module from a directory in the filesystem.
         *
         * @param moduleDir Module directory
         * @param moduleName Module name (default: main)
         * @param inheritedSets List of inherited sets (default: empty list)
         * @return Either an error or a module.
         */
        fun loadFromDir(moduleDir: File, moduleName: String = DEFAULT_MODULE_NAME,
                        inheritedSets: List<SetDecl> = DEFAULT_INHERITED_SETS): Either<ModuleLoaderError, Module> {
            logger.debug("Loading module '${moduleName}' from directory '${moduleDir}'")

            // directory must exists
            if (!moduleDir.exists()) {
                return Left(ModuleLoaderError.ModuleDirectoryNotFound(moduleDir.path))
            }

            // directory can be read
            if (!moduleDir.canRead()) {
                return Left(ModuleLoaderError.ModuleDirectoryAccessDenied(moduleDir.path))
            }

            // list and concatenate the .cs files in the module directory
            val moduleStr = moduleDir
                .listFiles { _, name -> name.endsWith(".cs") }!!
                .apply {
                    logger.debug("Found ${this.size} files")
                }
                .flatMap {
                    logger.debug("Reading spec file ${it.path}")
                    it.readLines()
                }
                .joinToString("\n")

            // load module from string
            return loadFromString(moduleStr, moduleDir, moduleName, inheritedSets)
        }

        /**
         * Load a module from a string.
         *
         * @param moduleStr Module in string format.
         * @param moduleDir Module directory. Needed to lookup dependencies.
         * @param moduleName Module name (default: main)
         * @param inheritedSets List of inherited sets (default: empty list)
         * @return Either an error or a module.
         */
        fun loadFromString(moduleStr: String, moduleDir: File, moduleName: String = DEFAULT_MODULE_NAME,
                           inheritedSets: List<SetDecl> = DEFAULT_INHERITED_SETS): Either<ModuleLoaderError, Module> {
            // load module declaration from string and map it to module
            return loadDeclFromString(moduleStr).flatMap { loadFromDecl(it, moduleDir, moduleName, inheritedSets) }
        }

        /**
         * Load a module declaration from a string.
         *
         * @param moduleStr Module in string format.
         * @return Either an error or a module declaration.
         */
        fun loadDeclFromString(moduleStr: String): Either<ModuleLoaderError, ModuleDecl> {
            try {
                val lexer = CloudSpecLexer(CharStreams.fromString(moduleStr))
                val tokens = CommonTokenStream(lexer)
                val parser = CloudSpecParser(tokens)
                parser.errorHandler = CloudSpecErrorStrategy()
                parser.buildParseTree = true
                val tree: ParseTree = parser.moduleDecl()
                val walker = ParseTreeWalker()
                val listener = CloudSpecListener()
                walker.walk(listener, tree)
                return Right(listener.module)
            } catch (e: CloudSpecListenerException) {
                return Left(ModuleLoaderError.SyntaxError(e.message!!))
            }
        }

        /**
         * Load a module from a declaration object.
         *
         * @param moduleDecl Module declaration.
         * @param moduleDir Module directory. Needed to lookup dependencies.
         * @param moduleName Module name (default: main)
         * @param inheritedSets List of inherited sets (default: empty list)
         * @return Either an error or a module.
         */
        fun loadFromDecl(moduleDecl: ModuleDecl, moduleDir: File, moduleName: String = DEFAULT_MODULE_NAME,
                         inheritedSets: List<SetDecl> = DEFAULT_INHERITED_SETS): Either<ModuleLoaderError, Module> {
            // join inherited and module sets
            val moduleSets = inheritedSets.plus(moduleDecl.sets)

            return Either.fx {
                val initialModules: Either<ModuleLoaderError, List<Module>> = Right(emptyList())
                val initialRules: Either<ModuleLoaderError, List<Rule>> = Right(emptyList())

                // iterate and load uses. If any iteration fails, the first error encountered is returned
                val (modules) = moduleDecl.uses.fold(initialModules) { acc, useDecl ->
                    when (acc) {
                        is Either.Right -> loadFromUseDecl(useDecl, moduleDir, moduleSets).map { acc.b.plus(it) }
                        else -> acc
                    }
                }

                // iterate and load rules. If any iteration fails, the first error encountered is returned
                val (rules) = moduleDecl.rules.fold(initialRules) { acc, ruleDecl ->
                    when (acc) {
                        is Either.Right -> loadRuleFromDecl(ruleDecl, moduleSets).map { acc.b.plus(it) }
                        else -> acc
                    }
                }

                // return module
                Module(moduleName,
                       modules,
                       rules)
            }
        }

        /**
         * Load a module from a use declaration object.
         *
         * @param useDecl Use declaration.
         * @param moduleDir Module directory. Needed to lookup dependencies.
         * @param inheritedSets List of inherited sets (default: empty list)
         * @return Either an error or a module.
         */
        fun loadFromUseDecl(useDecl: UseDecl, moduleDir: File,
                            inheritedSets: List<SetDecl> = DEFAULT_INHERITED_SETS): Either<ModuleLoaderError, Module> {
            try {
                val moduleFile = when {
                    useDecl.path.startsWith("/") -> File(useDecl.path)
                    else -> File(moduleDir.absolutePath).resolve(useDecl.path).normalize()
                }
                return loadFromDir(moduleFile, useDecl.name, inheritedSets)
            } catch (e: IOException) {
                logger.error("Error loading module ${useDecl.path}: ${e.message}")
                throw e
            }
        }

        /**
         * Load a module from a rule declaration object.
         *
         * @param ruleDecl Rule declaration.
         * @param inheritedSets List of inherited sets (default: empty list)
         * @return Either an error or a rule.
         */
        private fun loadRuleFromDecl(ruleDecl: RuleDecl, inheritedSets: List<SetDecl>): Either<ModuleLoaderError, Rule> {
            return when (val resourceDefRefOpt = ResourceDefRef.fromString(ruleDecl.defRef)) {
                is Some ->
                    Right(Rule(ruleDecl.name,
                               resourceDefRefOpt.t,
                               loadWithDecl(ruleDecl.withs),
                               loadAssertDecl(ruleDecl.asserts),
                               loadSetDecls(inheritedSets.plus(ruleDecl.sets))))
                else ->
                    Left(ModuleLoaderError.SyntaxError("Malformed resource definition reference ${ruleDecl.defRef}"))
            }
        }

        /**
         * Load a with declaration.
         *
         * @param withDecl With declaration.
         * List of statements.
         */
        private fun loadWithDecl(withDecl: WithDecl): List<Statement> {
            return withDecl.statements
        }

        /**
         * Load an assert declaration.
         *
         * @param assertDecl Assert declaration.
         * List of statements.
         */
        private fun loadAssertDecl(assertDecl: AssertDecl): List<Statement> {
            return assertDecl.statements
        }

        /**
         * Load a list of set declarations.
         *
         * @param setDecls Set declarations.
         * Set values.
         */
        private fun loadSetDecls(setDecls: List<SetDecl>): SetValues {
            return setDecls.mapNotNull { c ->
                when (val configRefOpt = ConfigRef.fromString(c.configRef)) {
                    is Some ->
                        when (c.value) {
                            is Number -> NumberSetValue(configRefOpt.t, c.value)
                            is String -> StringSetValue(configRefOpt.t, c.value)
                            is Boolean -> BooleanSetValue(configRefOpt.t, c.value)
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
