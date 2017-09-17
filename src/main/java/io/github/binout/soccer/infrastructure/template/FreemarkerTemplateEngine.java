/*
 * Copyright 2016 Benoît Prioux
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.binout.soccer.infrastructure.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.github.binout.soccer.domain.notification.TemplateEngine;
import io.github.binout.soccer.infrastructure.log.LoggerService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@ApplicationScoped
public class FreemarkerTemplateEngine implements TemplateEngine {

    @Inject
    LoggerService loggerService;

    private final Configuration configuration;

    public FreemarkerTemplateEngine() {
        configuration = new Configuration();
        configuration.setClassForTemplateLoading(this.getClass(), "/templates");
    }

    @Override
    public String render(String templateName, Map<String, Object> params) {
        try {
            Template template = configuration.getTemplate(templateName);
            StringWriter writer = new StringWriter();
            template.process(params, writer);
            return writer.toString();
        } catch (TemplateException | IOException e) {
            loggerService.log(this.getClass(), e.getMessage());
            return "Error in mail templating : " + e.getMessage();
        }
    }
}
