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
package io.github.binout.soccer.infrastructure.web

import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.util.StringUtils
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.resource.ResourceResolver
import org.springframework.web.servlet.resource.ResourceResolverChain
import java.io.IOException
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * Redirects every page to index.html
 * Used to handle the router
 */
@Configuration
class FrontAppConfig : WebMvcConfigurerAdapter() {

    override fun addCorsMappings(registry: CorsRegistry?) {
        registry!!.addMapping("/**")
                .allowCredentials(true)
                .exposedHeaders(HttpHeaders.LOCATION)
                .allowedHeaders(HttpHeaders.ORIGIN, HttpHeaders.ACCEPT, HttpHeaders.CONTENT_TYPE, HttpHeaders.AUTHORIZATION, HttpHeaders.CACHE_CONTROL, "x-requested-with")
                .allowedMethods(
                        HttpMethod.GET.name,
                        HttpMethod.PUT.name,
                        HttpMethod.POST.name,
                        HttpMethod.DELETE.name,
                        HttpMethod.OPTIONS.name,
                        HttpMethod.HEAD.name,
                        HttpMethod.PATCH.name,
                        HttpMethod.TRACE.name)
                .allowedOrigins("*")

    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry?) {
        registry!!.addResourceHandler("/**")
                .addResourceLocations("classpath:/public/")
                .resourceChain(false)
                .addResolver(PushStateResourceResolver())
    }

    private inner class PushStateResourceResolver : ResourceResolver {
        private val index = ClassPathResource("/public/index.html")
        private val handledExtensions = Arrays.asList("html", "js", "json", "csv", "css", "png", "svg", "eot", "ttf", "woff", "appcache", "jpg", "jpeg", "gif", "ico")
        private val ignoredPaths = Arrays.asList("rest")

        override fun resolveResource(request: HttpServletRequest?, requestPath: String, locations: List<Resource>, chain: ResourceResolverChain): Resource? {
            return resolve(requestPath, locations)
        }

        override fun resolveUrlPath(resourcePath: String, locations: List<Resource>, chain: ResourceResolverChain): String? {
            val resolvedResource = resolve(resourcePath, locations) ?: return null
            try {
                return resolvedResource.url.toString()
            } catch (e: IOException) {
                return resolvedResource.filename
            }

        }

        private fun resolve(requestPath: String, locations: List<Resource>): Resource? {
            if (isIgnored(requestPath)) {
                return null
            }
            return if (isHandled(requestPath)) {
                locations.stream()
                        .map<Resource> { loc -> createRelative(loc, requestPath) }
                        .filter { resource -> resource != null && resource.exists() }
                        .findFirst()
                        .orElseGet(null)
            } else index
        }

        private fun createRelative(resource: Resource, relativePath: String): Resource? {
            try {
                return resource.createRelative(relativePath)
            } catch (e: IOException) {
                return null
            }

        }

        private fun isIgnored(path: String): Boolean {
            return ignoredPaths.contains(path)
        }

        private fun isHandled(path: String): Boolean {
            val extension = StringUtils.getFilenameExtension(path)
            return handledExtensions.stream().anyMatch { ext -> ext == extension }
        }
    }
}
