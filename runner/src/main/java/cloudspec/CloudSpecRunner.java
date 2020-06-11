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
package cloudspec;

import cloudspec.lang.CloudSpec;
import cloudspec.model.Provider;
import cloudspec.validator.*;
import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class CloudSpecRunner {
    private static final ColoredPrinter cp = new ColoredPrinter.Builder(1, false).build();

    private final String version;
    private final ProvidersRegistry providersRegistry;
    private final CloudSpecManager cloudSpecManager;

    @Inject
    public CloudSpecRunner(String version, ProvidersRegistry providersRegistry, CloudSpecManager cloudSpecManager) {
        this.version = version;
        this.providersRegistry = providersRegistry;
        this.cloudSpecManager = cloudSpecManager;
    }

    public void validate(CloudSpec spec) {
        printBanner();

//        try {
        // init manager
        cloudSpecManager.init();

        // preflight spec
        cloudSpecManager.preflight(spec);

        // load resources
        cloudSpecManager.loadResources(spec);

        // validate spec
        printResult(cloudSpecManager.validate(spec));
//        } catch (RuntimeException exception) {
//            // manage unhandled exceptions
//            System.out.println("");
//            System.err.println(exception.getMessage());
//        }
    }

    private void printBanner() {
        System.out.println(String.format("CloudSpec v%s", version));

        printProviders();

        System.out.println();
    }

    private void printResult(CloudSpecValidatorResult result) {
        // print out spec name
        cp.println(result.getSpecName(), Ansi.Attribute.BOLD, Ansi.FColor.WHITE, Ansi.BColor.NONE);

        result.getGroupResults().forEach(this::printGroupResult);
    }

    private void printProviders() {
        List<String> providersNames = providersRegistry.getProviders().stream().map(Provider::getName).collect(Collectors.toList());

        System.out.println(String.format("Providers: %s", String.join(", ", providersNames)));
    }

    private void printGroupResult(GroupResult groupResult) {
        cp.println("  " + groupResult.getGroupName(), Ansi.Attribute.BOLD, Ansi.FColor.BLUE, Ansi.BColor.NONE);
        groupResult.getRuleResults().forEach(this::printRuleResult);
    }

    private void printRuleResult(RuleResult ruleResult) {
        cp.println(
                String.format("    %s ... %s", ruleResult.getRuleName(), ruleResult.isSuccess() ? "OK" : "FAIL"),
                Ansi.Attribute.BOLD,
                ruleResult.isSuccess() ? Ansi.FColor.GREEN : Ansi.FColor.RED,
                Ansi.BColor.NONE
        );

        if (!ruleResult.isSuccess()) {
            System.out.println();

            if (ruleResult.getThrowable() != null) {
                cp.println(ruleResult.getThrowable().getMessage(), Ansi.Attribute.NONE, Ansi.FColor.RED, Ansi.BColor.NONE);
            } else {
                ruleResult.getResourceValidationResults()
                        .stream()
                        .filter(resourceValidationResult -> !resourceValidationResult.isSuccess())
                        .peek(resourceValidationResult ->
                                cp.println(
                                        String.format(
                                                "Resource '%s'",
                                                resourceValidationResult.getRef()
                                        ),
                                        Ansi.Attribute.BOLD, Ansi.FColor.RED, Ansi.BColor.NONE
                                )
                        )
                        .flatMap(resourceValidationResult -> resourceValidationResult.getAssertResults().stream())
                        .filter(assertValidationResult -> !assertValidationResult.getSuccess())
                        .forEach(assertValidationResult ->
                                {
                                    AssertValidationError assertValidationError = assertValidationResult.getError();

                                    if (assertValidationError != null) {
                                        if (assertValidationError instanceof AssertValidationMismatchError) {
                                            AssertValidationMismatchError mismatchError = (AssertValidationMismatchError) assertValidationError;
                                            cp.println(
                                                    String.format(
                                                            " - %s: expected = %s, actual = %s",
                                                            String.join(".", assertValidationResult.getPath()),
                                                            valueToCloudSpecSyntax(mismatchError.getExpected()),
                                                            valueToCloudSpecSyntax(mismatchError.getActual())
                                                    ),
                                                    Ansi.Attribute.NONE, Ansi.FColor.RED, Ansi.BColor.NONE);
                                        } else if (assertValidationError instanceof AssertValidationContainError) {
                                            AssertValidationContainError containError = (AssertValidationContainError) assertValidationError;
                                            cp.println(
                                                    String.format(
                                                            " - %s: value (%s) - list (%s)",
                                                            String.join(".", assertValidationResult.getPath()),
                                                            valueToCloudSpecSyntax(containError.getValue()),
                                                            valueToCloudSpecSyntax(containError.getValueList())
                                                    ),
                                                    Ansi.Attribute.NONE, Ansi.FColor.RED, Ansi.BColor.NONE);
                                        } else if (assertValidationError instanceof AssertValidationMemberNotFoundError) {
                                            AssertValidationMemberNotFoundError notFoundError = (AssertValidationMemberNotFoundError) assertValidationError;
                                            cp.println(
                                                    String.format(
                                                            " - %s: %s",
                                                            String.join(".", assertValidationResult.getPath()),
                                                            notFoundError.getMessage()
                                                    ),
                                                    Ansi.Attribute.NONE, Ansi.FColor.RED, Ansi.BColor.NONE);
                                        } else if (assertValidationError instanceof AssertValidationKeyNotFoundError) {
                                            AssertValidationKeyNotFoundError notFoundError = (AssertValidationKeyNotFoundError) assertValidationError;
                                            cp.println(
                                                    String.format(
                                                            " - %s: %s",
                                                            String.join(".", assertValidationResult.getPath()),
                                                            notFoundError.getMessage()
                                                    ),
                                                    Ansi.Attribute.NONE, Ansi.FColor.RED, Ansi.BColor.NONE);
                                        } else if (assertValidationError instanceof AssertValidationUnknownError) {
                                            AssertValidationUnknownError unknownError = (AssertValidationUnknownError) assertValidationError;
                                            cp.println(
                                                    String.format(
                                                            " - %s: %s",
                                                            String.join(".", assertValidationResult.getPath()),
                                                            unknownError.getMessage()
                                                    ),
                                                    Ansi.Attribute.NONE, Ansi.FColor.RED, Ansi.BColor.NONE);
                                        }

                                    }
                                }
                        );

            }

            System.out.println();
        }
    }

    private String valueToCloudSpecSyntax(Object value) {
        if (value instanceof List) {
            return String.format(
                    "[%s]",
                    ((List<?>) value).stream()
                            .map(this::valueToCloudSpecSyntax)
                            .collect(Collectors.joining(", "))
            );

        } else if (value instanceof String) {
            return String.format("\"%s\"", value);
        } else {
            return value.toString();
        }
    }

}
