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
import cloudspec.model.Provider;
import cloudspec.model.ResourceDef;
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
        File indexFile = new File(outputDir, "_index.md");
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
        // create provider directory
        File providerDir = new File(outputDir, provider.getName());
        if (!providerDir.exists() && !providerDir.mkdirs()) {
            throw new RuntimeException("Cannot create provider directory " + providerDir.getAbsolutePath());
        }

        File providerFile = new File(providerDir, "_index.md");
        try (Writer out = new OutputStreamWriter(new FileOutputStream(providerFile))) {
            // create data dictionary
            Map<String, Object> data = new HashMap<>();
            data.put("provider", provider);

            // get provider template
            Template providerTpl = freemarkerCfg.getTemplate("provider.ftl");

            // write file
            providerTpl.process(data, out);
        }

        // write provider resources
        provider.getResourceDefs().forEach(resourceDef ->
                {
                    try {
                        writeResourceInMarkdown(providerDir, resourceDef);
                    } catch (IOException | TemplateException e) {
                        System.out.println(
                                String.format(
                                        "Error generating doc for resource '%s': %s",
                                        resourceDef.getRef(),
                                        e.getMessage()
                                )
                        );
                    }
                }
        );
    }

    private void writeResourceInMarkdown(File providerDir, ResourceDef resourceDef) throws IOException, TemplateException {
        // create group directory
//        File groupDir = new File(providerDir, resourceDef.getRef().getGroupName());
//        if (!groupDir.exists() && !groupDir.mkdirs()) {
//            throw new RuntimeException("Cannot create group directory " + groupDir.getAbsolutePath());
//        }

        // create resource file
        File resourceFile = new File(providerDir, resourceDef.getRef().getGroupName() + "_" + resourceDef.getRef().getResourceName() + ".md");
        try (Writer out = new OutputStreamWriter(new FileOutputStream(resourceFile))) {
            // create data dictionary
            Map<String, Object> data = new HashMap<>();
            data.put("resourceDef", resourceDef);

            // get resource template
            Template providerTpl = freemarkerCfg.getTemplate("resource.ftl");

            // write file
            providerTpl.process(data, out);
        }
    }
}
