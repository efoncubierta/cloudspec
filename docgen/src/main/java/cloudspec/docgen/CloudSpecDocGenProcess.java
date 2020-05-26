/*-
 * #%L
 * CloudSpec Documentation Generator
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
package cloudspec.docgen;

import cloudspec.ProvidersRegistry;
import cloudspec.model.Provider;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CloudSpecDocGenProcess {
    private final String version;
    private final ProvidersRegistry providersRegistry;
    private final Configuration freemarkerCfg = new Configuration(Configuration.VERSION_2_3_30);

    {
        freemarkerCfg.setClassForTemplateLoading(this.getClass(), "/freemarker");
        freemarkerCfg.setDefaultEncoding("UTF-8");
    }

    public CloudSpecDocGenProcess(String version, ProvidersRegistry providersRegistry) {
        this.version = version;
        this.providersRegistry = providersRegistry;
    }

    public void generate(File outputDir, String format) {
        // if output dir exists, throw exception
        if (outputDir.exists()) {
            throw new RuntimeException("Output directory already exists");
        }

        // create output dir
        if (!outputDir.mkdir()) {
            throw new RuntimeException("Couldn't create output directory");
        }

        // TODO manage different formats

        try {
            writeIndexInMarkdown(outputDir);
        } catch (IOException | TemplateException e) {
            System.out.println(
                    String.format(
                            "Error generating index doc: %s",
                            e.getMessage()
                    )
            );
        }

        providersRegistry.getProviders().forEach(provider -> {
            try {
                writeProviderInMarkdown(outputDir, provider);
            } catch (IOException | TemplateException e) {
                System.out.println(
                        String.format(
                                "Error generating doc for provider '%s': %s",
                                provider.getName(),
                                e.getMessage()
                        )
                );
            }
        });
    }

    private void writeIndexInMarkdown(File outputDir) throws IOException, TemplateException {
        // create index file
        File indexFile = new File(outputDir, "index.md");
        try (Writer out = new OutputStreamWriter(new FileOutputStream(indexFile))) {
            // create data dictionary
            Map<String, Object> data = new HashMap<>();
            data.put("providers", providersRegistry.getProviders());

            // get index template
            Template indexTpl = freemarkerCfg.getTemplate("index.ftl");

            // write file
            indexTpl.process(data, out);
        }
    }

    private void writeProviderInMarkdown(File outputDir,
                                         Provider provider) throws IOException, TemplateException {
        // create provider file
        File providerFile = new File(outputDir, provider.getName() + ".md");
        try (Writer out = new OutputStreamWriter(new FileOutputStream(providerFile))) {
            // create data dictionary
            Map<String, Object> data = new HashMap<>();
            data.put("provider", provider);

            // get provider template
            Template providerTpl = freemarkerCfg.getTemplate("provider.ftl");

            // write file
            providerTpl.process(data, out);
        }
    }
}
