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
package cloudspec

import cloudspec.loader.CloudSpecLoader
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.Callable

@CommandLine.Command(name = "run",
                     description = ["Run CloudSpec tests"])
class CloudSpecRunCommand : Callable<Int> {
    private val logger = LoggerFactory.getLogger(javaClass)

    @CommandLine.Option(names = ["-s", "--spec"],
                        paramLabel = "<spec file>",
                        description = ["CloudSpec file"],
                        required = true)
    private lateinit var specFile: File

    override fun call(): Int {
        logger.info("Starting CloudSpec Runner")

        val inputStream = FileInputStream(specFile)

        // create loader
        val loader = CloudSpecLoader()

        // load spec
        val plan = loader.load(inputStream)
        // run spec
        val runner = DaggerCloudSpecRunnerComponent.create().buildCloudSpecRunner()
        runner.validate(plan)
        return 0
    }
}
