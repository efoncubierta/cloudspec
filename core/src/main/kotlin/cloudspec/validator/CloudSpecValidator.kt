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

import arrow.core.getOrElse
import cloudspec.lang.GroupDecl
import cloudspec.lang.ModuleDecl
import cloudspec.lang.PlanDecl
import cloudspec.lang.RuleDecl
import cloudspec.model.ResourceDefRef.Companion.fromString

class CloudSpecValidator(private val resourceValidator: ResourceValidator) {
    fun validate(plan: PlanDecl): PlanResult {
        return PlanResult(validateModules(plan.modules))
    }

    private fun validateModules(modules: List<ModuleDecl>): List<ModuleResult> {
        return modules.map { validateModule(it) }
    }

    private fun validateModule(module: ModuleDecl): ModuleResult {
        return ModuleResult(module.name, validateGroups(module.groups))
    }

    private fun validateGroups(groups: List<GroupDecl>): List<GroupResult> {
        return groups.map { validateGroup(it) }
    }

    private fun validateGroup(group: GroupDecl): GroupResult {
        return GroupResult(group.name, validateRules(group.rules))
    }

    private fun validateRules(rules: List<RuleDecl>): List<RuleResult> {
        return rules.map { validateRule(it) }
    }

    private fun validateRule(rule: RuleDecl): RuleResult {
        return fromString(rule.defRef).map { resourceDefRef ->
            try {
                // validate all resources
                RuleResult(rule.name,
                           resourceValidator.validateAll(resourceDefRef,
                                                         rule.withs.statements,
                                                         rule.asserts.statements))
            } catch (e: RuntimeException) {
                RuleResult(rule.name, throwable = e)
            }
        }.getOrElse {
            RuleResult(rule.name,
                       throwable = RuntimeException("Malformed resource definition reference '${rule.defRef}'"))
        }
    }

}
