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

import cloudspec.model.Module
import cloudspec.model.Rule

class CloudSpecValidator(private val resourceValidator: ResourceValidator) {
    fun validate(module: Module): ModuleResult {
        return validateModule(module)
    }

    private fun validateModules(modules: List<Module>): List<ModuleResult> {
        return modules.map { validateModule(it) }
    }

    private fun validateModule(module: Module): ModuleResult {
        return ModuleResult(module.name,
                            validateModules(module.modules)
                                .plus(validateRules(module.rules)))
    }

    private fun validateRules(rules: List<Rule>): List<RuleResult> {
        return rules.map { validateRule(it) }
    }

    private fun validateRule(rule: Rule): RuleResult {
        return try {
            // validate all resources
            RuleResult(rule.name,
                       resourceValidator.validateAll(rule.defRef,
                                                     rule.filters,
                                                     rule.validations))
        } catch (e: RuntimeException) {
            RuleResult(rule.name, error = e)
        }
    }

}
