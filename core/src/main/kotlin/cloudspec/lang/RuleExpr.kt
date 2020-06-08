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
 * Define a rule expression.
 *
 *
 * A rule filters and validates resources.
 */
data class RuleExpr(
        val name: String,
        val resourceDefRef: String,
        val withExpr: WithExpr,
        val assertExpr: AssertExpr
) : CloudSpecSyntaxProducer {
    override fun toCloudSpecSyntax(spaces: Int): String {
        val sb = StringBuilder()

        sb.appendln("${" ".repeat(spaces)}Rule \"${name}\"")
        sb.appendln("${" ".repeat(spaces)}On $resourceDefRef")

        // add with statements
        sb.append(withExpr.toCloudSpecSyntax(spaces))

        // add assert statements
        sb.append(assertExpr.toCloudSpecSyntax(spaces))

        return sb.toString()
    }

    class RuleExprBuilder {
        private var name: String = ""
        private var resourceDefRef: String = ""
        private var withExpr = WithExpr(emptyList())
        private var assertExpr = AssertExpr(emptyList())

        fun setName(name: String): RuleExprBuilder {
            this.name = name
            return this
        }

        fun setResourceDefRef(resourceDefRef: String): RuleExprBuilder {
            this.resourceDefRef = resourceDefRef
            return this
        }

        fun setWithExpr(withExpr: WithExpr): RuleExprBuilder {
            this.withExpr = withExpr
            return this
        }

        fun setAssertExp(assertExpr: AssertExpr): RuleExprBuilder {
            this.assertExpr = assertExpr
            return this
        }

        fun build(): RuleExpr {
            return RuleExpr(name, resourceDefRef, withExpr, assertExpr)
        }
    }

    companion object {
        @JvmStatic
        fun builder(): RuleExprBuilder {
            return RuleExprBuilder()
        }
    }

}
