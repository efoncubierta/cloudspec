package cloudspec.reporter

import cloudspec.model.toPathString
import cloudspec.validator.*
import com.diogonunes.jcdp.color.ColoredPrinter
import com.diogonunes.jcdp.color.api.Ansi
import org.apache.commons.lang3.StringUtils
import kotlin.math.round

class ConsoleReporter {
    companion object {
        private val cp = ColoredPrinter.Builder(1, false).build()
        private val errors = mutableListOf<RuleResult>()

        fun report(planResult: PlanResult) {
            println()

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
                                       toTableCells("${margin(0)}${planResult.name}", planResult.stats)
                               )
                       ).plus(
                               planResult.results.flatMap { toTableRows(it) }
                       )).print()

            printErrors()
        }

        private fun toTableRows(moduleResult: ModuleResult): List<PrintTableRow> {
            return listOf(
                    PrintTableRow(
                            toTableCells("${margin(1)}${moduleResult.name}", moduleResult.stats)
                    )
            ).plus(
                    moduleResult.results.flatMap { toTableRows(it) }
            )
        }

        private fun toTableRows(groupResult: GroupResult): List<PrintTableRow> {
            return listOf(
                    PrintTableRow(
                            toTableCells("${margin(2)}${groupResult.name}", groupResult.stats)
                    )
            ).plus(
                    groupResult.results.flatMap { toTableRows(it) }
            )
        }

        private fun toTableRows(ruleResult: RuleResult): List<PrintTableRow> {
            if (!ruleResult.success) {
                errors.add(ruleResult)
            }

            return listOf(
                    PrintTableRow(
                            toTableCells("${margin(3)}${ruleResult.name}", ruleResult.stats)
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

        private fun margin(n: Int): String {
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
            printCell(row.cells[i].value, columns[i].width, row.cells[i].centered, row.cells[i].fcolor)
            printSep()
        }
        println()
    }

    private fun printSep() {
        cp.print(separator)
    }

    private fun printCell(value: String, width: Int, centered: Boolean = false, fcolor: Ansi.FColor = Ansi.FColor.WHITE) {
        val strValue = if(centered) StringUtils.center(value, width) else value
        cp.print("${margin()}%-${width}s${margin()}".format(strValue),
                 Ansi.Attribute.NONE, fcolor, Ansi.BColor.NONE)
    }

    private fun margin(): String {
        return " ".repeat(cellMargin)
    }
}

data class PrintTableColumn(val name: String, val width: Int)

data class PrintTableRow(val cells: List<PrintTableCell>)

data class PrintTableCell(val value: String, val centered: Boolean = false, val fcolor: Ansi.FColor = Ansi.FColor.WHITE)