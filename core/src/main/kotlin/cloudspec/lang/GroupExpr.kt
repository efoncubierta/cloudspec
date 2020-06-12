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
 * Define a group expression.
 *
 * Rules in a spec are grouped in groups.
 */
data class GroupExpr(
        val name: String,
        val rules: List<RuleExpr>
) : CloudSpecSyntaxProducer {
    override fun toCloudSpecSyntax(spaces: Int): String {
        val sb = StringBuilder()

        sb.appendln("${" ".repeat(spaces)}Group \"${name}\"")

        // add rules
        rules.forEach { rule -> sb.append(rule.toCloudSpecSyntax(4)) }

        sb.appendln()

        return sb.toString()
    }

    class GroupExprBuilder {
        private var name: String = ""
        private var rules = mutableListOf<RuleExpr>()

        fun setName(name: String): GroupExprBuilder {
            this.name = name
            return this
        }

        fun addRules(vararg rules: RuleExpr): GroupExprBuilder {
            this.rules.addAll(listOf(*rules))
            return this
        }

        fun build(): GroupExpr {
            return GroupExpr(name, rules)
        }
    }

    companion object {
        @JvmStatic
        fun builder(): GroupExprBuilder {
            return GroupExprBuilder()
        }
    }

}
