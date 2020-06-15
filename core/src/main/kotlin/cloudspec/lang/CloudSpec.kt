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
 * Define the structure of a Cloud Spec.
 */
data class CloudSpec(
        val name: String,
        val config: Set<ConfigExpr>,
        val groups: List<GroupExpr>
) : CloudSpecSyntaxProducer {
    override fun toCloudSpecSyntax(spaces: Int): String {
        val sb = StringBuilder()

        sb.appendln("${" ".repeat(spaces)}Spec \"${name}\"")

        // add configs
        config.forEach { config ->
            sb.append(config.toCloudSpecSyntax(spaces))
        }

        // add groups
        groups.forEach { group ->
            sb.append(group.toCloudSpecSyntax(spaces))
        }

        sb.appendln()

        return sb.toString()
    }

    class CloudSpecBuilder {
        private var name: String = ""
        private var config = mutableSetOf<ConfigExpr>()
        private var groups = mutableListOf<GroupExpr>()

        fun setName(name: String): CloudSpecBuilder {
            this.name = name
            return this
        }

        fun addConfig(vararg config: ConfigExpr): CloudSpecBuilder {
            this.config.addAll(setOf(*config))
            return this
        }

        fun addGroups(vararg groups: GroupExpr): CloudSpecBuilder {
            this.groups.addAll(listOf(*groups))
            return this
        }

        fun build(): CloudSpec {
            // TODO validate
            return CloudSpec(name, config, groups)
        }
    }

    companion object {
        fun builder(): CloudSpecBuilder {
            return CloudSpecBuilder()
        }
    }

}
