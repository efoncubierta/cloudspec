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
 * Interface that each class of CloudSpec should implement to
 * write themselves in CloudSpec syntax.
 */
interface CloudSpecSyntaxProducer {
    /**
     * Write object in CloudSpec syntax.
     *
     * @return CloudSpec syntax.
     */
    fun toCloudSpecSyntax(): String {
        return toCloudSpecSyntax(0)
    }

    /**
     * Write object in CloudSpec syntax tabulated.
     *
     * @param tabs Number of tabs.
     * @return CloudSpec syntax.
     */
    fun toCloudSpecSyntax(tabs: Int): String
}

fun printTabs(tabs: Int): String {
    return " ".repeat(tabs*2)
}
