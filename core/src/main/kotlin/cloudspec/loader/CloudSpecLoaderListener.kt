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

import cloudspec.CloudSpecBaseListener
import cloudspec.CloudSpecParser.*
import cloudspec.lang.*
import cloudspec.lang.predicate.DateP.Companion.after
import cloudspec.lang.predicate.DateP.Companion.before
import cloudspec.lang.predicate.DateP.Companion.between
import cloudspec.lang.predicate.DateP.Companion.notAfter
import cloudspec.lang.predicate.DateP.Companion.notBefore
import cloudspec.lang.predicate.IPAddressP.Companion.eq
import cloudspec.lang.predicate.IPAddressP.Companion.gt
import cloudspec.lang.predicate.IPAddressP.Companion.gte
import cloudspec.lang.predicate.IPAddressP.Companion.isIpv4
import cloudspec.lang.predicate.IPAddressP.Companion.isIpv6
import cloudspec.lang.predicate.IPAddressP.Companion.lt
import cloudspec.lang.predicate.IPAddressP.Companion.lte
import cloudspec.lang.predicate.IPAddressP.Companion.neq
import cloudspec.lang.predicate.IPAddressP.Companion.withinNetwork
import cloudspec.lang.predicate.IPAddressP.Companion.withoutNetwork
import org.apache.commons.lang3.time.DateUtils
import org.apache.tinkerpop.gremlin.process.traversal.P
import org.apache.tinkerpop.gremlin.process.traversal.TextP
import java.text.ParseException
import java.util.*

class CloudSpecLoaderListener : CloudSpecBaseListener() {
    // evaluator
    private val currentDefRef = Stack<String>()
    private val currentConfigs = Stack<Stack<ConfigDecl>>()
    private val currentPlans = Stack<PlanDecl>()
    private val currentModules = Stack<ModuleDecl>()
    private val currentGroups = Stack<GroupDecl>()
    private val currentRules = Stack<RuleDecl>()
    private val currentWiths = Stack<WithDecl>()
    private val currentAsserts = Stack<AssertDecl>()
    private val currentMemberNames = Stack<Stack<String>>()
    private val currentStatements = Stack<Stack<Statement>>()
    private val currentValues = Stack<Any>()
    private var currentPredicate: P<*>? = null

    init {
        currentConfigs.push(Stack()) // plan configs
    }

    val plan: PlanDecl
        get() = PlanDecl(currentConfigs.pop().toList(),
                         currentModules.toList())

    override fun exitSetDecl(ctx: SetDeclContext) {
        currentConfigs.peek().push(ConfigDecl(stripQuotes(ctx.CONFIG_REF().text), currentValues.pop()))
    }

    override fun enterModuleDecl(ctx: ModuleDeclContext) {
        currentConfigs.push(Stack())
    }

    override fun exitModuleDecl(ctx: ModuleDeclContext) {
        currentModules.push(ModuleDecl(stripQuotes(ctx.STRING().text),
                                       currentConfigs.pop().toList(),
                                       currentGroups.toList()))
        currentGroups.clear()
    }

    override fun enterGroupDecl(ctx: GroupDeclContext) {
        currentConfigs.push(Stack())
    }

    override fun exitGroupDecl(ctx: GroupDeclContext) {
        currentGroups.push(GroupDecl(stripQuotes(ctx.STRING().text),
                                     currentConfigs.pop().toList(),
                                     currentRules.toList()))
        currentRules.clear()
    }

    override fun enterRuleDecl(ctx: RuleDeclContext) {
        currentConfigs.push(Stack())
        currentDefRef.push(stripQuotes(ctx.STRING().text))
    }

    override fun exitRuleDecl(ctx: RuleDeclContext) {
        currentRules.push(RuleDecl(stripQuotes(ctx.STRING().text),
                                   currentDefRef.pop(),
                                   currentConfigs.pop().toList(),
                                   if (!currentWiths.empty()) currentWiths.pop() else WithDecl(emptyList()),
                                   currentAsserts.pop()))
    }

    override fun enterOnDecl(ctx: OnDeclContext) {
        currentDefRef.push(stripQuotes(ctx.RESOURCE_DEF_REF().text))
    }

    override fun enterWithDecl(ctx: WithDeclContext) {
        currentStatements.push(Stack())
        currentMemberNames.push(Stack())
    }

    override fun exitWithDecl(ctx: WithDeclContext) {
        currentWiths.push(WithDecl(currentStatements.pop().toList()))
    }

    override fun enterAssertDecl(ctx: AssertDeclContext) {
        currentStatements.push(Stack())
        currentMemberNames.push(Stack())
    }

    override fun exitAssertDecl(ctx: AssertDeclContext) {
        currentAsserts.push(AssertDecl(currentStatements.pop().toList()))
    }

    override fun enterAndDecl(ctx: AndDeclContext) {
        currentMemberNames.push(Stack())
    }

    override fun exitAndDecl(ctx: AndDeclContext) {
        currentMemberNames.pop()
    }

    override fun enterPropertyStatement(ctx: PropertyStatementContext) {
        currentMemberNames.peek().add(stripQuotes(ctx.MEMBER_NAME().text))
    }

    override fun exitPropertyStatement(ctx: PropertyStatementContext) {
        currentStatements.peek().add(PropertyStatement(currentMemberNames.peek().pop(),
                                                       currentPredicate!!))
    }

    override fun enterKeyValuePropertyStatement(ctx: KeyValuePropertyStatementContext) {
        currentMemberNames.peek().add(stripQuotes(ctx.MEMBER_NAME().text))
    }

    override fun exitKeyValuePropertyStatement(ctx: KeyValuePropertyStatementContext) {
        currentStatements.peek().add(KeyValueStatement(currentMemberNames.peek().pop(),
                                                       stripQuotes(ctx.STRING().text),
                                                       currentPredicate!!))
    }

    override fun enterNestedPropertyStatement(ctx: NestedPropertyStatementContext) {
        currentMemberNames.peek().add(stripQuotes(ctx.MEMBER_NAME().text))
        currentMemberNames.push(Stack())
        currentStatements.push(Stack())
    }

    override fun exitNestedPropertyStatement(ctx: NestedPropertyStatementContext) {
        currentMemberNames.pop()
        val statement = NestedStatement(currentMemberNames.peek().pop(),
                                        currentStatements.pop())
        currentStatements.peek().add(statement)
    }

    override fun enterAssociationStatement(ctx: AssociationStatementContext) {
        currentMemberNames.peek().add(stripQuotes(ctx.MEMBER_NAME().text))
        currentMemberNames.push(Stack())
        currentStatements.push(Stack())
    }

    override fun exitAssociationStatement(ctx: AssociationStatementContext) {
        currentMemberNames.pop()
        val statement = AssociationStatement(currentMemberNames.peek().pop(),
                                             currentStatements.pop())
        currentStatements.peek().add(statement)
    }

    override fun exitValueNullPredicate(ctx: ValueNullPredicateContext) {
        currentPredicate = P.eq<Any?>(null)
    }

    override fun exitValueNotNullPredicate(ctx: ValueNotNullPredicateContext) {
        currentPredicate = P.neq<Any?>(null)
    }

    override fun exitValueEqualPredicate(ctx: ValueEqualPredicateContext) {
        currentPredicate = P.eq(currentValues.pop())
    }

    override fun exitValueNotEqualPredicate(ctx: ValueNotEqualPredicateContext) {
        currentPredicate = P.neq(currentValues.pop())
    }

    override fun exitValueWithinPredicate(ctx: ValueWithinPredicateContext) {
        currentPredicate = P.within(*currentValues.toTypedArray())
        currentValues.clear()
    }

    override fun exitValueNotWithinPredicate(ctx: ValueNotWithinPredicateContext) {
        currentPredicate = P.without(*currentValues.toTypedArray())
        currentValues.clear()
    }

    override fun exitNumberLessThanPredicate(ctx: NumberLessThanPredicateContext) {
        currentPredicate = P.lt(currentValues.pop())
    }

    override fun exitNumberLessThanEqualPredicate(ctx: NumberLessThanEqualPredicateContext) {
        currentPredicate = P.lte(currentValues.pop())
    }

    override fun exitNumberGreaterThanPredicate(ctx: NumberGreaterThanPredicateContext) {
        currentPredicate = P.gt(currentValues.pop())
    }

    override fun exitNumberGreaterThanEqualPredicate(ctx: NumberGreaterThanEqualPredicateContext) {
        currentPredicate = P.gte(currentValues.pop())
    }

    override fun exitNumberBetweenPredicate(ctx: NumberBetweenPredicateContext) {
        currentPredicate = P.between(currentValues[0], currentValues[1])
        currentValues.clear()
    }

    override fun exitStringEmptyPredicate(ctx: StringEmptyPredicateContext) {
        currentPredicate = P.eq("")
    }

    override fun exitStringNotEmptyPredicate(ctx: StringNotEmptyPredicateContext) {
        currentPredicate = P.neq("")
    }

    override fun exitStringStartingWithPredicate(ctx: StringStartingWithPredicateContext) {
        currentPredicate = TextP.startingWith(currentValues.pop() as String?)
    }

    override fun exitStringNotStartingWithPredicate(ctx: StringNotStartingWithPredicateContext) {
        currentPredicate = TextP.notStartingWith(currentValues.pop() as String?)
    }

    override fun exitStringEndingWithPredicate(ctx: StringEndingWithPredicateContext) {
        currentPredicate = TextP.endingWith(currentValues.pop() as String?)
    }

    override fun exitStringNotEndingWithPredicate(ctx: StringNotEndingWithPredicateContext) {
        currentPredicate = TextP.notEndingWith(currentValues.pop() as String?)
    }

    override fun exitStringContainingPredicate(ctx: StringContainingPredicateContext) {
        currentPredicate = TextP.containing(currentValues.pop() as String?)
    }

    override fun exitStringNotContainingPredicate(ctx: StringNotContainingPredicateContext) {
        currentPredicate = TextP.notContaining(currentValues.pop() as String?)
    }

    override fun exitIpAddressEqualPredicate(ctx: IpAddressEqualPredicateContext) {
        currentPredicate = eq(currentValues.pop() as String?)
    }

    override fun exitIpAddressNotEqualPredicate(ctx: IpAddressNotEqualPredicateContext) {
        currentPredicate = neq(currentValues.pop() as String?)
    }

    override fun exitIpAddressLessThanPredicate(ctx: IpAddressLessThanPredicateContext) {
        currentPredicate = lt(currentValues.pop() as String?)
    }

    override fun exitIpAddressLessThanEqualPredicate(ctx: IpAddressLessThanEqualPredicateContext) {
        currentPredicate = lte(currentValues.pop() as String?)
    }

    override fun exitIpAddressGreaterThanPredicate(ctx: IpAddressGreaterThanPredicateContext) {
        currentPredicate = gt(currentValues.pop() as String?)
    }

    override fun exitIpAddressGreaterThanEqualPredicate(ctx: IpAddressGreaterThanEqualPredicateContext) {
        currentPredicate = gte(currentValues.pop() as String?)
    }

    override fun exitIpWithinNetworkPredicate(ctx: IpWithinNetworkPredicateContext) {
        currentPredicate = withinNetwork(currentValues.pop() as String?)
    }

    override fun exitIpNotWithinNetworkPredicate(ctx: IpNotWithinNetworkPredicateContext) {
        currentPredicate = withoutNetwork(currentValues.pop() as String?)
    }

    override fun exitIpIsIpv4Predicate(ctx: IpIsIpv4PredicateContext) {
        currentPredicate = isIpv4
    }

    override fun exitIpIsIpv6Predicate(ctx: IpIsIpv6PredicateContext) {
        currentPredicate = isIpv6
    }

    override fun exitEnabledPredicate(ctx: EnabledPredicateContext) {
        currentPredicate = P.eq(true)
    }

    override fun exitDisabledPredicate(ctx: DisabledPredicateContext) {
        currentPredicate = P.eq(false)
    }

    override fun exitDateBeforePredicate(ctx: DateBeforePredicateContext) {
        currentPredicate = before((currentValues.pop() as Date?)?.toInstant())
    }

    override fun exitDateNotBeforePredicate(ctx: DateNotBeforePredicateContext) {
        currentPredicate = notBefore((currentValues.pop() as Date?)?.toInstant())
    }

    override fun exitDateAfterPredicate(ctx: DateAfterPredicateContext) {
        currentPredicate = after((currentValues.pop() as Date?)?.toInstant())
    }

    override fun exitDateNotAfterPredicate(ctx: DateNotAfterPredicateContext) {
        currentPredicate = notAfter((currentValues.pop() as Date?)?.toInstant())
    }

    override fun exitDateBetweenPredicate(ctx: DateBetweenPredicateContext) {
        currentPredicate = between((currentValues[0] as Date?)?.toInstant(), (currentValues[1] as Date?)?.toInstant())
        currentValues.clear()
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
