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
import cloudspec.loader.CloudSpecLoader;
import picocli.CommandLine;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "run",
        description = "Run CloudSpec tests"
)
public class CloudSpecRunCommand implements Callable<Integer> {
    @CommandLine.Option(
            names = {"-s", "--spec"},
            paramLabel = "<spec file>",
            description = "CloudSpec file",
            required = true
    )
    private File specFile;

    @Override
    public Integer call() throws Exception {
        InputStream is = new FileInputStream(specFile);

        // create loader
        CloudSpecLoader loader = new CloudSpecLoader();

        // load spec
        CloudSpec spec = loader.load(is);

        // run spec
        CloudSpecRunner runner = DaggerCloudSpecRunnerComponent.create().buildCloudSpecRunner();
        runner.validate(spec);

        return 0;
    }
}
