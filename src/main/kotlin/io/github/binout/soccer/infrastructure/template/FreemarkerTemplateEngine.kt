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
package io.github.binout.soccer.infrastructure.template

import freemarker.template.Configuration
import freemarker.template.TemplateException
import io.github.binout.soccer.domain.TemplateEngine
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.IOException
import java.io.StringWriter

@Component
class FreemarkerTemplateEngine : TemplateEngine {

    private val configuration: Configuration = Configuration().apply {
        setClassForTemplateLoading(this.javaClass, "/templates")
    }

    override fun render(templateName: String, params: Map<String, Any>): String {
        return try {
            val template = configuration.getTemplate(templateName)
            val writer = StringWriter()
            template.process(params, writer)
            writer.toString()
        } catch (e: TemplateException) {
            println(this.javaClass.name + " : " + e.message)
            "Error in mail templating : " + e.message
        } catch (e: IOException) {
            println(this.javaClass.name + " : " + e.message)
            "Error in mail templating : " + e.message
        }

    }
}
