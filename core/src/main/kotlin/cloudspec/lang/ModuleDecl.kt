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
package cloudspec.lang

/**
 * CloudSpec 'module' declaration.
 */
data class ModuleDecl(
        val inputs: List<InputDecl>,
        val sets: List<SetDecl>,
        val uses: List<UseDecl>,
        val rules: List<RuleDecl>
) : CloudSpecSyntaxProducer {
    override fun toCloudSpecSyntax(tabs: Int): String {
        val sb = StringBuilder()

        // add inputs
        inputs.forEach { input ->
            sb.append(input.toCloudSpecSyntax(tabs + 1))
        }

        // add configs
        sets.forEach { config ->
            sb.append(config.toCloudSpecSyntax(tabs + 1))
        }

        // add uses
        uses.forEach { module ->
            sb.append(module.toCloudSpecSyntax(tabs + 1))
        }

        // add rules
        rules.forEach { rule ->
            sb.append(rule.toCloudSpecSyntax(tabs + 1))
        }

        return sb.toString()
    }
}
