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

import cloudspec.lang.PlanDecl
import cloudspec.validator.*
import com.diogonunes.jcdp.color.ColoredPrinter
import com.diogonunes.jcdp.color.api.Ansi
import org.slf4j.LoggerFactory
import javax.inject.Inject

class CloudSpecRunner @Inject constructor(private val version: String,
                                          private val providersRegistry: ProvidersRegistry,
                                          private val cloudSpecManager: CloudSpecManager) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val errors = mutableListOf<RuleResult>()

    fun validate(plan: PlanDecl) {
        // init manager
        cloudSpecManager.init()

        // preflight spec
        cloudSpecManager.preflight(plan)

        // load resources
        cloudSpecManager.loadResources(plan)

        // validate spec
        printResult(cloudSpecManager.validate(plan))
    }

    private fun printResult(planResult: PlanResult) {
        logger.info("Generating report")

        println()
        planResult.moduleResults.forEach { printModuleResult(it) }
        printErrors()
    }

    private fun printModuleResult(moduleResult: ModuleResult) {
        cp.println("${margin(1)}${moduleResult.moduleName}",
                   Ansi.Attribute.NONE, Ansi.FColor.BLUE, Ansi.BColor.NONE)
        moduleResult.groupResults.forEach { printGroupResult(it) }
    }

    private fun printGroupResult(groupResult: GroupResult) {
        cp.println("${margin(2)}${groupResult.groupName}",
                   Ansi.Attribute.NONE, Ansi.FColor.YELLOW, Ansi.BColor.NONE)
        groupResult.ruleResults.forEach { printRuleResult(it) }
    }

    private fun printRuleResult(ruleResult: RuleResult) {
        when {
            ruleResult.isSuccess ->
                cp.println("${margin(3)}✓ ${ruleResult.ruleName}",
                           Ansi.Attribute.NONE, Ansi.FColor.GREEN, Ansi.BColor.NONE)
            else -> {
                cp.println("${margin(3)}✗ ${ruleResult.ruleName} [${errors.size + 1}]",
                           Ansi.Attribute.NONE, Ansi.FColor.RED, Ansi.BColor.NONE)

                errors.add(ruleResult)
            }
        }
    }

    private fun printErrors() {
        (errors.indices).forEach {
            println()
            cp.println("[${it + 1}] ${errors[it].ruleName}",
                       Ansi.Attribute.NONE, Ansi.FColor.WHITE, Ansi.BColor.NONE)

            printErrors(errors[it])
        }
    }

    private fun printErrors(ruleResult: RuleResult) {
        when {
            ruleResult.throwable != null ->
                cp.println(ruleResult.throwable?.message,
                           Ansi.Attribute.NONE, Ansi.FColor.RED, Ansi.BColor.NONE)
            else -> {
                ruleResult.resourceValidationResults
                    .filter { !it.isSuccess }
                    .onEach {
                        cp.print("On resource ",
                                 Ansi.Attribute.NONE, Ansi.FColor.WHITE, Ansi.BColor.NONE)
                        cp.println(it.ref,
                                   Ansi.Attribute.BOLD, Ansi.FColor.WHITE, Ansi.BColor.NONE)
                    }
                    .flatMap { it.assertResults }
                    .filter { !it.success }
                    .forEach { (path, _, assertError) ->
                        println()
                        cp.println("${margin(1)}${path.toPathString()}",
                                   Ansi.Attribute.BOLD, Ansi.FColor.WHITE, Ansi.BColor.NONE)
                        when (assertError) {
                            is AssertMismatchError -> {
                                val (condition, expected, actual) = assertError
                                cp.print("${margin(2)}Expected: ",
                                         Ansi.Attribute.BOLD, Ansi.FColor.RED, Ansi.BColor.NONE)
                                cp.println("$condition ${valueToCloudSpecSyntax(expected)}",
                                           Ansi.Attribute.NONE, Ansi.FColor.WHITE, Ansi.BColor.NONE)
                                cp.print("${margin(2)}Actual: ",
                                         Ansi.Attribute.BOLD, Ansi.FColor.GREEN, Ansi.BColor.NONE)
                                cp.println(valueToCloudSpecSyntax(actual),
                                           Ansi.Attribute.NONE, Ansi.FColor.WHITE, Ansi.BColor.NONE)
                            }
                            is AssertRangeError -> {
                                val (condition, expectedLeft, expectedRight, actual) = assertError
                                cp.print("${margin(2)}Expected: ",
                                         Ansi.Attribute.BOLD, Ansi.FColor.RED, Ansi.BColor.NONE)
                                cp.println("$condition ${valueToCloudSpecSyntax(expectedLeft)} and ${valueToCloudSpecSyntax(expectedRight)}",
                                           Ansi.Attribute.NONE, Ansi.FColor.WHITE, Ansi.BColor.NONE)
                                cp.print("${margin(2)}Actual: ",
                                         Ansi.Attribute.BOLD, Ansi.FColor.GREEN, Ansi.BColor.NONE)
                                cp.println(valueToCloudSpecSyntax(actual),
                                           Ansi.Attribute.NONE, Ansi.FColor.WHITE, Ansi.BColor.NONE)
                            }
                            is AssertNotFoundError -> {
                                cp.print("${margin(2)}Error: ",
                                         Ansi.Attribute.BOLD, Ansi.FColor.RED, Ansi.BColor.NONE)
                                cp.println(assertError.message,
                                           Ansi.Attribute.NONE, Ansi.FColor.RED, Ansi.BColor.NONE)
                            }
                            is AssertUnknownError -> {
                                cp.print("${margin(2)}Error: ",
                                         Ansi.Attribute.BOLD, Ansi.FColor.RED, Ansi.BColor.NONE)
                                cp.println(assertError.message,
                                           Ansi.Attribute.NONE, Ansi.FColor.RED, Ansi.BColor.NONE)
                            }
                        }
                    }
            }
        }
    }

    private fun valueToCloudSpecSyntax(value: Any): String {
        return when (value) {
            is List<*> -> {
                "[${value.joinToString(", ") { valueToCloudSpecSyntax(it!!) }}]"
            }
            is String -> {
                String.format("\"%s\"", value)
            }
            else -> {
                value.toString()
            }
        }
    }

    private fun margin(n: Int): String {
        return " ".repeat(n * MARGIN_SIZE)
    }

    companion object {
        private const val MARGIN_SIZE = 2
        private val cp = ColoredPrinter.Builder(1, false).build()
    }
}
