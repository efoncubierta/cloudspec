/*-
 * #%L
 * CloudSpec Runner
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
package cloudspec;

import cloudspec.lang.CloudSpec;
import cloudspec.model.Provider;
import cloudspec.validator.CloudSpecValidatorResult;
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

        try {
            // init manager
            cloudSpecManager.init();

            // preflight spec
            cloudSpecManager.preflight(spec);

            // load resources
            cloudSpecManager.loadResources();

            // validate spec
            printResult(cloudSpecManager.validate(spec));
        } catch (RuntimeException exception) {
            // manage unhandled exceptions
            System.out.println("");
            System.err.println(exception.getMessage());
        }
    }

    private void printBanner() {
        System.out.println(String.format("CloudSpec v%s", version));

        printProviders();

        System.out.println("");
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

    private void printGroupResult(CloudSpecValidatorResult.GroupResult groupResult) {
        cp.println("  " + groupResult.getGroupName(), Ansi.Attribute.BOLD, Ansi.FColor.BLUE, Ansi.BColor.NONE);
        groupResult.getRuleResults().forEach(this::printRuleResult);
    }

    private void printRuleResult(CloudSpecValidatorResult.RuleResult ruleResult) {
        cp.println(
                String.format("    %s ... %s", ruleResult.getRuleName(), ruleResult.isSuccess() ? "OK" : "FAIL"),
                Ansi.Attribute.BOLD,
                ruleResult.isSuccess() ? Ansi.FColor.GREEN : Ansi.FColor.RED,
                Ansi.BColor.NONE
        );

        if (ruleResult.isError()) {
            System.out.println("");

            if (ruleResult.getThrowable().isPresent()) {
                cp.println(ruleResult.getThrowable().get().getMessage(), Ansi.Attribute.NONE, Ansi.FColor.RED, Ansi.BColor.NONE);
            } else if (ruleResult.getReason().isPresent()) {
                cp.println(ruleResult.getReason().get(), Ansi.Attribute.NONE, Ansi.FColor.RED, Ansi.BColor.NONE);
            } else {
                cp.println("Unknown", Ansi.Attribute.NONE, Ansi.FColor.RED, Ansi.BColor.NONE);
            }
        }
    }

}
