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

import cloudspec.lang.CloudSpec
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

    fun validate(spec: CloudSpec) {
        // init manager
        cloudSpecManager.init()

        // preflight spec
        cloudSpecManager.preflight(spec)

        // load resources
        cloudSpecManager.loadResources(spec)

        // validate spec
        printResult(cloudSpecManager.validate(spec))
    }

    private fun printResult(result: CloudSpecValidatorResult) {
        logger.info("Generating report")

        println()
        cp.println("${" ".repeat(2)}${result.specName}",
                   Ansi.Attribute.NONE,
                   Ansi.FColor.BLUE,
                   Ansi.BColor.NONE)
        result.groupResults.forEach { printGroupResult(it) }
        printErrors()
    }

    private fun printGroupResult(groupResult: GroupResult) {
        cp.println("${" ".repeat(4)}${groupResult.groupName}",
                   Ansi.Attribute.NONE,
                   Ansi.FColor.YELLOW,
                   Ansi.BColor.NONE)
        groupResult.ruleResults.forEach { printRuleResult(it) }
    }

    private fun printRuleResult(ruleResult: RuleResult) {
        when {
            ruleResult.isSuccess ->
                cp.println("${" ".repeat(6)}✓ ${ruleResult.ruleName}",
                           Ansi.Attribute.NONE,
                           Ansi.FColor.GREEN,
                           Ansi.BColor.NONE
                )
            else -> {
                cp.println("${" ".repeat(6)}✗ ${ruleResult.ruleName} [${errors.size + 1}]",
                           Ansi.Attribute.NONE,
                           Ansi.FColor.RED,
                           Ansi.BColor.NONE
                )

                errors.add(ruleResult)
            }
        }
    }

    private fun printErrors() {
        (errors.indices).forEach {
            println()
            cp.println("[${it + 1}]",
                       Ansi.Attribute.NONE,
                       Ansi.FColor.WHITE,
                       Ansi.BColor.NONE)

            printErrors(errors[it])
        }
    }

    private fun printErrors(ruleResult: RuleResult) {
        when {
            ruleResult.throwable != null ->
                cp.println(ruleResult.throwable?.message,
                           Ansi.Attribute.NONE,
                           Ansi.FColor.RED,
                           Ansi.BColor.NONE)
            else -> {
                ruleResult.resourceValidationResults
                    .filter { !it.isSuccess }
                    .onEach {
                        cp.print("On resource ",
                                 Ansi.Attribute.NONE,
                                 Ansi.FColor.WHITE,
                                 Ansi.BColor.NONE)
                        cp.println(it.ref,
                                   Ansi.Attribute.BOLD,
                                   Ansi.FColor.WHITE,
                                   Ansi.BColor.NONE)
                    }
                    .flatMap { it.assertResults }
                    .filter { !it.success }
                    .forEach { (path, _, assertError) ->
                        println()
                        cp.println("  $path",
                                   Ansi.Attribute.BOLD,
                                   Ansi.FColor.WHITE,
                                   Ansi.BColor.NONE)
                        when (assertError) {
                            is AssertMismatchError -> {
                                val (condition, expected, actual) = assertError
                                cp.println("""
                                    |    Expected: $condition ${valueToCloudSpecSyntax(expected)}
                                    |      Actual: ${valueToCloudSpecSyntax(actual)}
                                """.trimMargin(),
                                           Ansi.Attribute.NONE,
                                           Ansi.FColor.WHITE,
                                           Ansi.BColor.NONE)
                            }
                            is AssertRangeError -> {
                                val (condition, expectedLeft, expectedRight, actual) = assertError
                                cp.println("""
                                    |    Expected: $condition ${valueToCloudSpecSyntax(expectedLeft)} and ${valueToCloudSpecSyntax(expectedRight)} 
                                    |      Actual: ${valueToCloudSpecSyntax(actual)}
                                """.trimMargin(),
                                           Ansi.Attribute.NONE,
                                           Ansi.FColor.WHITE,
                                           Ansi.BColor.NONE)
                            }
                            is AssertNotFoundError -> {
                                cp.println("""
                                    |    Error: ${assertError.message}
                                """.trimMargin(),
                                           Ansi.Attribute.NONE,
                                           Ansi.FColor.WHITE,
                                           Ansi.BColor.NONE)
                            }
                            is AssertUnknownError -> {
                                cp.println("""
                                    |  - $path
                                    |    Error: ${assertError.message}
                                """.trimMargin(),
                                           Ansi.Attribute.NONE,
                                           Ansi.FColor.WHITE,
                                           Ansi.BColor.NONE)
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

    companion object {
        private val cp = ColoredPrinter.Builder(1, false).build()
    }
}
