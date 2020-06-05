/*-
 * #%L
 * CloudSpec Loader Library
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
package cloudspec.loader;

import cloudspec.CloudSpecLexer;
import cloudspec.CloudSpecParser;
import cloudspec.lang.CloudSpec;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.io.InputStream;

public class CloudSpecLoader {
    public CloudSpec load(InputStream is) throws IOException {
        CloudSpecLexer lexer = new CloudSpecLexer(new ANTLRInputStream(is));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CloudSpecParser parser = new CloudSpecParser(tokens);
        parser.setBuildParseTree(true);
        ParseTree tree = parser.spec();

        ParseTreeWalker walker = new ParseTreeWalker();
        CloudSpecLoaderListener listener = new CloudSpecLoaderListener();
        walker.walk(listener, tree);

        return listener.getCloudSpec();
    }
}
