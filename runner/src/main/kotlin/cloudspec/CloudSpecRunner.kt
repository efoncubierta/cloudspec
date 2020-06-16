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
import cloudspec.model.toPathString
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

    fun validate(plan: Plan) {
        // init manager
        cloudSpecManager.init()

        // load plan
        cloudSpecManager.preflight(plan)

        // load resources
        cloudSpecManager.loadResources(plan)

        // validate spec
        printResult(cloudSpecManager.validate(plan))
    }

    private fun print(str: String, error: Boolean? = null) {
        when {
            error == null -> cp.print(str, Ansi.Attribute.NONE, Ansi.FColor.WHITE, Ansi.BColor.NONE)
            error -> cp.print(str, Ansi.Attribute.NONE, Ansi.FColor.RED, Ansi.BColor.NONE)
            else -> cp.print(str, Ansi.Attribute.NONE, Ansi.FColor.GREEN, Ansi.BColor.NONE)
        }
    }

    private fun printRow(item: String, stats: Stats) {
        val max = itemsWidth() - 6
        // item cell
        print("| ")
        print("%-${itemsWidth()}s".format(if (item.length > max) item.subSequence(0, max) else item).padEnd(itemsWidth(), '.'),
              error = stats.resourcesFailed > 0)
        print(" |")

        // stats cells
        print(" %${statWidth()}s ".format(stats.resourcesTotal))
        print("|")
        print(" %${statWidth()}s ".format(stats.resourcesSuccess), error = stats.resourcesFailed == 0)
        print("|")
        print(" %${statWidth()}s ".format(stats.resourcesFailed), error = stats.resourcesFailed > 0)
        print("|")
        print(" %${statWidth()}s ".format(stats.validationsTotal))
        print("|")
        print(" %${statWidth()}s ".format(stats.validationsSuccess), error = stats.validationsFailed == 0)
        print("|")
        print(" %${statWidth()}s ".format(stats.validationsFailed), error = stats.validationsFailed > 0)
        print("|")
        println("")
    }

    private fun itemsWidth(): Int {
        return TABLE_WIDTH - (CELL_STAT_WIDTH + 3) * TABLE_HEADERS.size - 4;
    }

    private fun statWidth(): Int {
        return CELL_STAT_WIDTH
    }

    private fun printHeader() {
        cp.println(("| %-${itemsWidth()}s |" +
                " %${statWidth()}s |" +
                " %${statWidth()}s |" +
                " %${statWidth()}s |" +
                " %${statWidth()}s |" +
                " %${statWidth()}s |" +
                " %${statWidth()}s |").format(*TABLE_HEADERS.toTypedArray()))
        cp.println(("| %-${itemsWidth()}s |" +
                " %${statWidth()}s |" +
                " %${statWidth()}s |" +
                " %${statWidth()}s |" +
                " %${statWidth()}s |" +
                " %${statWidth()}s |" +
                " %${statWidth()}s |").format("".padEnd(itemsWidth(), '-'),
                                              "".padEnd(statWidth(), '-'),
                                              "".padEnd(statWidth(), '-'),
                                              "".padEnd(statWidth(), '-'),
                                              "".padEnd(statWidth(), '-'),
                                              "".padEnd(statWidth(), '-'),
                                              "".padEnd(statWidth(), '-')))
    }

    private fun printFooter() {
        cp.println(("|-%-${itemsWidth()}s-|" +
                "-%${statWidth()}s-|" +
                "-%${statWidth()}s-|" +
                "-%${statWidth()}s-|" +
                "-%${statWidth()}s-|" +
                "-%${statWidth()}s-|" +
                "-%${statWidth()}s-|").format("".padEnd(itemsWidth(), '-'),
                                              "".padEnd(statWidth(), '-'),
                                              "".padEnd(statWidth(), '-'),
                                              "".padEnd(statWidth(), '-'),
                                              "".padEnd(statWidth(), '-'),
                                              "".padEnd(statWidth(), '-'),
                                              "".padEnd(statWidth(), '-')))
    }

    private fun printResult(planResult: PlanResult) {
        logger.info("Generating report")

        println()
        printHeader()
        printRow("${margin(0)}${planResult.name}", planResult.stats)
        planResult.results.forEach { printModuleResult(it) }
        printFooter()
        printErrors()
    }

    private fun printModuleResult(moduleResult: ModuleResult) {
        printRow("${margin(1)}${moduleResult.name}", moduleResult.stats)
        moduleResult.results.forEach { printGroupResult(it) }
    }

    private fun printGroupResult(groupResult: GroupResult) {
        printRow("${margin(2)}${groupResult.name}", groupResult.stats)
        groupResult.results.forEach { printRuleResult(it) }
    }

    private fun printRuleResult(ruleResult: RuleResult) {
        when {
            ruleResult.success ->
                printRow("${margin(3)}${ruleResult.name}", ruleResult.stats)
            else -> {
                printRow("${margin(3)}${ruleResult.name} [${errors.size + 1}]", ruleResult.stats)
                errors.add(ruleResult)
            }
        }
    }

    private fun printErrors() {
        (errors.indices).forEach {
            println()
            cp.println("[${it + 1}] ${errors[it].name}",
                       Ansi.Attribute.NONE, Ansi.FColor.WHITE, Ansi.BColor.NONE)

            printErrors(errors[it])
        }
    }

    private fun printErrors(ruleResult: RuleResult) {
        when {
            ruleResult.error != null ->
                cp.println(ruleResult.error?.message,
                           Ansi.Attribute.NONE, Ansi.FColor.RED, Ansi.BColor.NONE)
            else -> {
                ruleResult.results
                    .filter { !it.success }
                    .onEach {
                        cp.print("On resource ",
                                 Ansi.Attribute.NONE, Ansi.FColor.WHITE, Ansi.BColor.NONE)
                        cp.println(it.ref,
                                   Ansi.Attribute.BOLD, Ansi.FColor.WHITE, Ansi.BColor.NONE)
                    }
                    .flatMap { it.results }
                    .filter { !it.success }
                    .forEach { (path, assertError) ->
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
        private val TABLE_WIDTH = 120
        private val CELL_STAT_WIDTH = 3;
        private val TABLE_HEADERS = listOf("Test", "TR", "SR", "FR", "TV", "SV", "FV")
        private val MARGIN_SIZE = 1

        private val cp = ColoredPrinter.Builder(1, false).build()
    }
}
