package cloudspec;

import cloudspec.model.Provider;
import cloudspec.model.ResourceDef;
import cloudspec.model.ResourceValidator;
import cloudspec.provider.ProvidersRegistry;
import cloudspec.spec.CloudSpec;
import cloudspec.spec.GroupExpr;
import cloudspec.spec.RuleExpr;
import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CloudSpecValidator {
    private static final ColoredPrinter cp = new ColoredPrinter.Builder(1, false).build();

    private final String version;
    private final ProvidersRegistry providersRegistry;

    @Inject
    public CloudSpecValidator(String version, ProvidersRegistry providersRegistry) {
        this.version = version;
        this.providersRegistry = providersRegistry;
    }

    public void validate(CloudSpec spec) {
        printBanner();

        cp.println(spec.getName(), Ansi.Attribute.BOLD, Ansi.FColor.WHITE, Ansi.BColor.NONE);

        try {
            validateGroups(spec.getGroups());
        } catch (CloudSpecRunnerException exception) {
            // manage unhandled exceptions
            System.out.println("");
            cp.errorPrint(exception.getMessage(), Ansi.Attribute.NONE, Ansi.FColor.RED, Ansi.BColor.NONE);
        }
    }

    private void validateGroups(List<GroupExpr> groups) {
        groups.forEach(this::validateGroup);
    }

    private void validateGroup(GroupExpr group) {
        cp.println("  " + group.getName(), Ansi.Attribute.BOLD, Ansi.FColor.BLUE, Ansi.BColor.NONE);

        validateRules(group.getRules());
    }

    private void validateRules(List<RuleExpr> rules) {
        rules.forEach(this::validateRule);
    }

    private void validateRule(RuleExpr rule) {
        cp.print(String.format("    %s ... ", rule.getTitle()), Ansi.Attribute.BOLD, Ansi.FColor.GREEN, Ansi.BColor.NONE);

        Long start = new Date().getTime();

        try {
            String providerName = rule.getResourceTypeFQName().split(":")[0];

            // Lookup provider
            Provider provider = providersRegistry.getProvider(providerName);
            if (Objects.isNull(provider)) {
                throw new CloudSpecRunnerException(String.format("Provider '%s' not found.", providerName));
            }

            // lookup resource definition
            ResourceDef resourceDef = provider.getResourceDef(rule.getResourceTypeFQName());
            if (Objects.isNull(resourceDef)) {
                throw new CloudSpecRunnerException(String.format("Rule validator for resource of type %s not found.", rule.getResourceTypeFQName()));
            }

            // run test
            Boolean result = ResourceValidator.forDef(resourceDef).validate(rule.getWiths(), rule.getAsserts());

            Long end = new Date().getTime();

            cp.println(
                    String.format("%s (%dms)", result ? "OK" : "FAIL", end - start),
                    Ansi.Attribute.BOLD, result ? Ansi.FColor.GREEN : Ansi.FColor.RED, Ansi.BColor.NONE);
        } catch (RuntimeException exception) {
            cp.println(String.format("%s", "FAIL"), Ansi.Attribute.BOLD, Ansi.FColor.RED, Ansi.BColor.NONE);
            throw exception;
        }
    }

    private void printBanner() {
        cp.println(String.format("CloudSpec v%s", version));

        printProviders();

        System.out.println("");
    }

    private void printProviders() {
        List<String> providersNames = providersRegistry.getProviders().stream().map(Provider::getProviderName).collect(Collectors.toList());

        cp.println(String.format("Providers: %s", String.join(", ", providersNames)));
    }
}
