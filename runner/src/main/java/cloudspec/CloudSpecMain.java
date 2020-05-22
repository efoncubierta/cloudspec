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
    public Integer call() throws Exception {
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
