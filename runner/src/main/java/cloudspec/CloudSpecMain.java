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

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "cloudspec",
        mixinStandardHelpOptions = true,
        version = "0.0.1",
        subcommands = {
                CloudSpecRunCommand.class
        }
)
public class CloudSpecMain implements Callable<Integer> {
    @Override
    public Integer call() {
        System.out.println("Please, use one of the following commands: ");
        System.out.println(" - run");
        System.out.println(" - doc");
        return null;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CloudSpecMain()).execute(args);
        System.exit(exitCode);
    }
}
