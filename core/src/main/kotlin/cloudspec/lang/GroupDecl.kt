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
 * CloudSpec 'group' declaration.
 */
data class GroupDecl(
        val name: String,
        val sets: List<SetDecl>,
        val useRules: List<UseRuleDecl>,
        val rules: List<RuleDecl>
) : CloudSpecSyntaxProducer {
    override fun toCloudSpecSyntax(tabs: Int): String {
        val sb = StringBuilder()

        sb.appendln("${printTabs(tabs)}group \"${name}\"")

        // add config
        sets.forEach {
            sb.append(it.toCloudSpecSyntax(tabs + 1))
        }

        // add use rules
        useRules.forEach { rule ->
            sb.append(rule.toCloudSpecSyntax(tabs + 1))
        }

        // add rules
        rules.forEach {
            sb.append(it.toCloudSpecSyntax(tabs + 1))
        }

        sb.appendln("${printTabs(tabs)}end group")

        return sb.toString()
    }
}