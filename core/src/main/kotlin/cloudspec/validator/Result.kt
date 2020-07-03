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
package cloudspec.validator

import cloudspec.model.Path
import cloudspec.model.ResourceRef

sealed class Result(open val name: String) {
    abstract val success: Boolean
    abstract val stats: Stats
}

data class ModuleResult(override val name: String,
                        val results: List<Result>) : Result(name) {
    override val success
        get() = results.all { it.success }

    override val stats
        get() = if(results.isEmpty()) Stats() else results.map { it.stats }.reduce { acc, stats -> acc.sum(stats) }
}

data class RuleResult(override val name: String,
                      val results: List<ResourceResult> = emptyList(),
                      val error: Throwable? = null) : Result(name) {
    override val success
        get() = error == null && results.all { it.success }

    override val stats
        get() = if(results.isEmpty()) Stats() else results.map { it.stats }.reduce { acc, stats -> acc.sum(stats) }
}

data class ResourceResult(val ref: ResourceRef,
                          val results: List<ValidationResult>) {
    val success
        get() = results.all { it.success }

    val stats
        get() = Stats(1,
                      if (success) 1 else 0,
                      if (!success) 1 else 0,
                      results.size,
                      results.count { it.success },
                      results.count { !it.success })
}

data class ValidationResult(val path: List<Path>,
                            val error: AssertError? = null) {
    val success
        get() = error == null
}

sealed class AssertError

data class AssertNotFoundError(val message: String) : AssertError()

data class AssertMismatchError(val condition: String,
                               val expected: Any,
                               val actual: Any) : AssertError()

data class AssertRangeError(val condition: String,
                            val expectedLeft: Any,
                            val expectedRight: Any,
                            val actual: Any) : AssertError()

data class AssertUnknownError(val message: String) : AssertError()

data class Stats(val resourcesTotal: Int = 0,
                 val resourcesSuccess: Int = 0,
                 val resourcesFailed: Int = 0,
                 val validationsTotal: Int = 0,
                 val validationsSuccess: Int = 0,
                 val validationsFailed: Int = 0) {
    fun sum(stats: Stats): Stats {
        return Stats(resourcesTotal + stats.resourcesTotal,
                     resourcesSuccess + stats.resourcesSuccess,
                     resourcesFailed + stats.resourcesFailed,
                     validationsTotal + stats.validationsTotal,
                     validationsSuccess + stats.validationsSuccess,
                     validationsFailed + stats.validationsFailed)
    }
}
