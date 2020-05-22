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
