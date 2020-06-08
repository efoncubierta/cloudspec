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

import cloudspec.lang.CloudSpec
import cloudspec.lang.GroupExpr
import cloudspec.lang.RuleExpr
import cloudspec.model.ResourceDefRef.Companion.fromString

class CloudSpecValidator(private val resourceValidator: ResourceValidator) {
    fun validate(spec: CloudSpec): CloudSpecValidatorResult {
        return CloudSpecValidatorResult(spec.name, validateGroups(spec.groups))
    }

    private fun validateGroups(groups: List<GroupExpr>): List<GroupResult> {
        return groups.map { validateGroup(it) }
    }

    private fun validateGroup(group: GroupExpr): GroupResult {
        return GroupResult(group.name, validateRules(group.rules))
    }

    private fun validateRules(rules: List<RuleExpr>): List<RuleResult> {
        return rules.map { validateRule(it) }
    }

    private fun validateRule(rule: RuleExpr): RuleResult {
        return fromString(rule.resourceDefRef).let { resourceDefRef ->
            if (resourceDefRef == null) {
                RuleResult(rule.name,
                           throwable = RuntimeException("Malformed resource definition reference '${rule.resourceDefRef}'"))
            } else try {
                // validate all resources
                RuleResult(rule.name,
                           resourceValidator.validateAll(resourceDefRef,
                                                         rule.withExpr.statements,
                                                         rule.assertExpr.statements)
                )
            } catch (e: RuntimeException) {
                RuleResult(rule.name, throwable = e)
            }
        }
    }

}
