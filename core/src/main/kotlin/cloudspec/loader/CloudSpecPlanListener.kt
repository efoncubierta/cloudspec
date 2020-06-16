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
package cloudspec.loader

import cloudspec.CloudSpecPlanBaseListener
import cloudspec.CloudSpecPlanParser.*
import cloudspec.lang.PlanDecl
import cloudspec.lang.SetDecl
import cloudspec.lang.UseModuleDecl
import org.apache.commons.lang3.time.DateUtils
import java.text.ParseException
import java.util.*

class CloudSpecPlanListener : CloudSpecPlanBaseListener() {
    private val currentPlans = Stack<PlanDecl>()
    private val currentSets = Stack<SetDecl>()
    private val currentModules = Stack<UseModuleDecl>()
    private val currentValues = Stack<Any>()

    val plan: PlanDecl
        get() = currentPlans.pop()

    override fun exitPlanDecl(ctx: PlanDeclContext) {
        currentPlans.push(PlanDecl(stripQuotes(ctx.STRING().text),
                                   currentSets.toList(),
                                   currentModules.toList()))
    }

    override fun exitUseModuleDecl(ctx: UseModuleDeclContext) {
        currentModules.push(UseModuleDecl(stripQuotes(ctx.STRING().text)))
    }

    override fun exitSetDecl(ctx: SetDeclContext) {
        currentSets.push(SetDecl(stripQuotes(ctx.CONFIG_REF().text),
                                 currentValues.pop()))
    }

    override fun exitStringValue(ctx: StringValueContext) {
        currentValues.add(stripQuotes(ctx.STRING().text))
    }

    override fun exitBooleanValue(ctx: BooleanValueContext) {
        currentValues.add(BOOLEAN_TRUE_VALUES.contains(ctx.BOOLEAN().text.toLowerCase()))
    }

    override fun exitDateValue(ctx: DateValueContext) {
        try {
            val dateTime = DateUtils.parseDate(
                    stripQuotes(ctx.DATE_STRING().text),
                    "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"
            )
            currentValues.add(dateTime)
        } catch (e: ParseException) {
            throw RuntimeException(e.message, e)
        }
    }

    override fun exitNumberValue(ctx: NumberValueContext) {
        if (ctx.INTEGER() != null && ctx.INTEGER().text != null && ctx.INTEGER().text.isNotEmpty()) {
            currentValues.add(ctx.INTEGER().text.toInt())
        } else {
            currentValues.add(ctx.DOUBLE().text.toDouble())
        }
    }

    private fun stripQuotes(s: String): String {
        return if (s[0] != '"') s else s.substring(1, s.length - 1)
    }

    companion object {
        private val BOOLEAN_TRUE_VALUES = listOf("true", "enabled")
    }
}
