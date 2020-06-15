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

import arrow.core.Option
import cloudspec.CloudSpecLexer
import cloudspec.CloudSpecParser
import cloudspec.lang.ModuleDecl
import cloudspec.lang.PlanDecl
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.io.IOException
import java.io.InputStream

class CloudSpecLoader {
    @Throws(IOException::class)
    fun load(`is`: InputStream?): PlanDecl {
        val lexer = CloudSpecLexer(ANTLRInputStream(`is`))
        val tokens = CommonTokenStream(lexer)
        val parser = CloudSpecParser(tokens)
        parser.buildParseTree = true
        val tree: ParseTree = parser.spec()
        val walker = ParseTreeWalker()
        val listener = CloudSpecLoaderListener()
        walker.walk(listener, tree)
        return listener.plan
    }
}
