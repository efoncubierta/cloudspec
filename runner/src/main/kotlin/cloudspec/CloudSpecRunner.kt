/*-
 * #%L
 * CloudSpec Runner
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
package cloudspec

import cloudspec.model.Plan
import cloudspec.reporter.ConsoleReporter
import org.slf4j.LoggerFactory
import javax.inject.Inject

class CloudSpecRunner @Inject constructor(private val version: String,
                                          private val providersRegistry: ProvidersRegistry,
                                          private val cloudSpecManager: CloudSpecManager) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun validate(plan: Plan) {
        // init manager
        cloudSpecManager.init()

        // load plan
        cloudSpecManager.preflight(plan)

        // load resources
        cloudSpecManager.loadResources(plan)

        // validate spec
        val result = cloudSpecManager.validate(plan)

        logger.info("Generating report")
        ConsoleReporter.report(result)
    }
}
