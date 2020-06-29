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

import org.antlr.v4.runtime.*


class CloudSpecErrorStrategy : DefaultErrorStrategy() {
    override fun recover(recognizer: Parser, e: RecognitionException) {
        setExceptionContext(recognizer, e)
        throw CloudSpecListenerException(getErrorMessage(recognizer, e), e)
    }

    override fun recoverInline(recognizer: Parser): Token {
        val e = InputMismatchException(recognizer)
        setExceptionContext(recognizer, e)
        throw CloudSpecListenerException(getErrorMessage(recognizer, e), e)
    }

    private fun setExceptionContext(recognizer: Parser, e: RuntimeException) {
        var context = recognizer.context
        while (context != null) {
            context.exception = e as RecognitionException
            context = context.getParent()
        }
    }

    private fun getErrorMessage(recognizer: Parser, e: RecognitionException): String {
        val loc = "${e.offendingToken.line}:${e.offendingToken.charPositionInLine}"
        beginErrorCondition(recognizer)
        if (e is NoViableAltException) {
            val tokens = recognizer.inputStream
            val input: String
            input = if (tokens != null) {
                if (e.startToken.type == Token.EOF) "<EOF>" else tokens.getText(e.startToken, e.offendingToken)
            } else {
                "<unknown input>"
            }
            return "$loc no viable alternative at input ${escapeWSAndQuote(input)}"
        } else if (e is InputMismatchException) {
            return "$loc mismatched input ${getTokenErrorDisplay(e.offendingToken)}" +
                    " expecting ${e.expectedTokens.toString(recognizer.vocabulary)}"
        } else if (e is FailedPredicateException) {
            val ruleName = recognizer.ruleNames[recognizer.context.ruleIndex]
            return "$loc rule $ruleName ${e.message}"
        } else {
            return "$loc unknown error: ${e.offendingToken} ${e.message}"
        }
    }

    override fun reportError(recognizer: Parser, e: RecognitionException) {
        // silence report error
    }
}
