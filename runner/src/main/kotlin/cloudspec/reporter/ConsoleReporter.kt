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
package cloudspec.reporter

import cloudspec.model.toPathString
import cloudspec.validator.*
import com.diogonunes.jcdp.color.ColoredPrinter
import com.diogonunes.jcdp.color.api.Ansi
import org.apache.commons.lang3.StringUtils

class ConsoleReporter {
    companion object {
        private val cp = ColoredPrinter.Builder(1, false).build()
        private val errors = mutableListOf<RuleResult>()

        fun report(moduleResult: ModuleResult) {
            PrintTable(cp,
                       listOf(
                               PrintTableColumn("Test", 50),
                               PrintTableColumn("#Re", 4),
                               PrintTableColumn("%RS", 4),
                               PrintTableColumn("#Va", 4),
                               PrintTableColumn("%VS", 4)
                       ),
                       listOf(
                               PrintTableRow(
                                       toTableCells("${printMargin(0)}${moduleResult.name}",
                                                    moduleResult.stats)
                               )
                       ).plus(
                               moduleResult.results.flatMap { toTableRows(it, 2) }
                       )).print()

            printErrors()
        }

        private fun toTableRows(result: Result, margin: Int): List<PrintTableRow> {
            return when (result) {
                is ModuleResult -> toTableRows(result, margin)
                is RuleResult -> toTableRows(result, margin)
                else -> emptyList()
            }
        }

        private fun toTableRows(moduleResult: ModuleResult, margin: Int): List<PrintTableRow> {
            return listOf(
                    PrintTableRow(
                            toTableCells("[M]${printMargin(margin)}${moduleResult.name}", moduleResult.stats)
                    )
            ).plus(
                    moduleResult.results.flatMap { toTableRows(it, margin + 2) }
            )
        }

        private fun toTableRows(ruleResult: RuleResult, margin: Int): List<PrintTableRow> {
            if (!ruleResult.success) {
                errors.add(ruleResult)
            }

            return listOf(
                    PrintTableRow(
                            toTableCells("[R]${printMargin(margin)}${ruleResult.name}",
                                         ruleResult.stats)
                    )
            )
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
                            cp.println("${printMargin(1)}${path.toPathString()}",
                                       Ansi.Attribute.BOLD, Ansi.FColor.WHITE, Ansi.BColor.NONE)
                            when (assertError) {
                                is AssertMismatchError -> {
                                    val (condition, expected, actual) = assertError
                                    cp.print("${printMargin(2)}Expected: ",
                                             Ansi.Attribute.BOLD, Ansi.FColor.RED, Ansi.BColor.NONE)
                                    cp.println("$condition ${valueToCloudSpecSyntax(expected)}",
                                               Ansi.Attribute.NONE, Ansi.FColor.WHITE, Ansi.BColor.NONE)
                                    cp.print("${printMargin(2)}Actual: ",
                                             Ansi.Attribute.BOLD, Ansi.FColor.GREEN, Ansi.BColor.NONE)
                                    cp.println(valueToCloudSpecSyntax(actual),
                                               Ansi.Attribute.NONE, Ansi.FColor.WHITE, Ansi.BColor.NONE)
                                }
                                is AssertRangeError -> {
                                    val (condition, expectedLeft, expectedRight, actual) = assertError
                                    cp.print("${printMargin(2)}Expected: ",
                                             Ansi.Attribute.BOLD, Ansi.FColor.RED, Ansi.BColor.NONE)
                                    cp.println("$condition ${valueToCloudSpecSyntax(expectedLeft)} and ${valueToCloudSpecSyntax(expectedRight)}",
                                               Ansi.Attribute.NONE, Ansi.FColor.WHITE, Ansi.BColor.NONE)
                                    cp.print("${printMargin(2)}Actual: ",
                                             Ansi.Attribute.BOLD, Ansi.FColor.GREEN, Ansi.BColor.NONE)
                                    cp.println(valueToCloudSpecSyntax(actual),
                                               Ansi.Attribute.NONE, Ansi.FColor.WHITE, Ansi.BColor.NONE)
                                }
                                is AssertNotFoundError -> {
                                    cp.print("${printMargin(2)}Error: ",
                                             Ansi.Attribute.BOLD, Ansi.FColor.RED, Ansi.BColor.NONE)
                                    cp.println(assertError.message,
                                               Ansi.Attribute.NONE, Ansi.FColor.RED, Ansi.BColor.NONE)
                                }
                                is AssertUnknownError -> {
                                    cp.print("${printMargin(2)}Error: ",
                                             Ansi.Attribute.BOLD, Ansi.FColor.RED, Ansi.BColor.NONE)
                                    cp.println(assertError.message,
                                               Ansi.Attribute.NONE, Ansi.FColor.RED, Ansi.BColor.NONE)
                                }
                            }
                        }
                }
            }
        }

        private fun toTableCells(item: String, stats: Stats): List<PrintTableCell> {
            val rs = (stats.resourcesSuccess.toDouble() / stats.resourcesTotal) * 100
            val vs = (stats.validationsSuccess.toDouble() / stats.validationsTotal) * 100

            val mainColor = if (rs < 100) Ansi.FColor.RED else Ansi.FColor.GREEN

            return listOf(
                    PrintTableCell(item, false, mainColor),
                    PrintTableCell(stats.resourcesTotal.toString(), true),
                    PrintTableCell("%.0f%%".format(rs), true, mainColor),
                    PrintTableCell(stats.validationsTotal.toString(), true),
                    PrintTableCell("%.0f%%".format(vs), true, mainColor)
            )
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

        private fun printMargin(n: Int): String {
            return " ".repeat(n)
        }
    }
}

data class PrintTable(val cp: ColoredPrinter,
                      val columns: List<PrintTableColumn>,
                      val rows: List<PrintTableRow>,
                      val cellMargin: Int = 1) {
    private val separator = '|'

    fun print() {
        printHeader()
        rows.forEach {
            printRow(it)
        }
        printFooter()
    }

    private fun printHeader() {
        printSep()
        columns.forEach {
            printCell(it.name, it.width, true)
            printSep()
        }
        println()

        printSep()
        columns.forEach {
            cp.print("-".repeat(it.width + cellMargin * 2))
            printSep()
        }
        println()
    }

    private fun printFooter() {
        printSep()
        columns.forEach {
            cp.print("-".repeat(it.width + cellMargin * 2))
            printSep()
        }
        println()
    }

    private fun printRow(row: PrintTableRow) {
        printSep()
        (columns.indices).forEach { i ->
            printCell(row.cells[i].value, columns[i].width, row.cells[i].centered, row.cells[i].fcolor, i == 0)
            printSep()
        }
        println()
    }

    private fun printSep() {
        cp.print(separator)
    }

    private fun printCell(value: String, width: Int, centered: Boolean = false,
                          fcolor: Ansi.FColor = Ansi.FColor.WHITE, trim: Boolean = false) {
        val strValue = if (centered) StringUtils.center(value, width) else value
        cp.print("${margin()}%-${width}s${margin()}".format(if (trim) trimAndPad(strValue, width) else strValue),
                 Ansi.Attribute.NONE, fcolor, Ansi.BColor.NONE)
    }

    private fun margin(): String {
        return " ".repeat(cellMargin)
    }

    private fun trimAndPad(str: String, width: Int): String {
        if (str.length > (width - 3)) {
            return str.substring(0, width - 3).padEnd(width, '.')
        }

        return str
    }
}

data class PrintTableColumn(val name: String, val width: Int)

data class PrintTableRow(val cells: List<PrintTableCell>)

data class PrintTableCell(val value: String, val centered: Boolean = false, val fcolor: Ansi.FColor = Ansi.FColor.WHITE)
