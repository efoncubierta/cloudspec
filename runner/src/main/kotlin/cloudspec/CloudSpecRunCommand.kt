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

import arrow.core.Either
import cloudspec.loader.ModuleLoader
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.io.File
import java.util.concurrent.Callable

@CommandLine.Command(name = "run",
                     description = ["Run CloudSpec tests"])
class CloudSpecRunCommand : Callable<Int> {
    private val logger = LoggerFactory.getLogger(javaClass)

    @CommandLine.Option(names = ["-d", "--directory"],
                        paramLabel = "<module dir>",
                        description = ["CloudSpec module directory"],
                        required = true)
    private lateinit var moduleFile: File

    override fun call(): Int {
        logger.info("Starting CloudSpec Runner")

        // load module
        return when (val moduleE = ModuleLoader.loadFromDir(moduleFile)) {
            is Either.Right -> {
                // run spec
                val runner = DaggerCloudSpecRunnerComponent.create().buildCloudSpecRunner()
                runner.validate(moduleE.b)
                0
            }
            is Either.Left -> {
                logger.error(moduleE.a.message())
                -1
            }
        }
    }
}
