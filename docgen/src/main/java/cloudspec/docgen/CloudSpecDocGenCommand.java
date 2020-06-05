/*-
 * #%L
 * CloudSpec Documentation Generator
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
package cloudspec.docgen;

import cloudspec.ProvidersRegistry;
import picocli.CommandLine;

import javax.inject.Inject;
import java.io.File;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "cloudspec-docgen",
        mixinStandardHelpOptions = true,
        version = "0.0.1"
)
public class CloudSpecDocGenCommand implements Callable<Integer> {
    @CommandLine.Option(
            names = {"-f", "--format"},
            paramLabel = "<output format>",
            description = "Output format",
            defaultValue = "markdown",
            required = true,
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS
    )
    private String format;

    @CommandLine.Option(
            names = {"-o", "--output-dir"},
            paramLabel = "<output directory>",
            description = "Output directory for the documentation",
            required = true
    )
    private File outputDir;

    private final CloudSpecDocGenProcess docGenProcess;

    @Inject
    public CloudSpecDocGenCommand(String version, ProvidersRegistry providersRegistry) {
        docGenProcess = new CloudSpecDocGenProcess(version, providersRegistry);
    }

    @Override
    public Integer call() throws Exception {
        docGenProcess.generate(outputDir, format);
        return 0;
    }
}
