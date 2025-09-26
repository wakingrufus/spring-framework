/*
 * Copyright 2002-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.web.servlet.function

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.BeanRegistrarDsl
import org.springframework.beans.factory.getBean
import org.springframework.beans.factory.support.BeanRegistryAdapter
import org.springframework.context.support.GenericApplicationContext
import org.springframework.mock.env.MockEnvironment
import org.springframework.web.servlet.handler.PathPatternsTestUtils

/**
 * Tests for WebMvc.fn [RoutesDsl].
 *
 * @author John Burns
 */
class RoutesDslTests {
	class InjectedBean() {
		fun sayHi(): String {
			return "world"
		}
	}

	@Test
	fun path() {
		val servletRequest = PathPatternsTestUtils.initRequest("GET", "/baz", true)
		val request = DefaultServerRequest(servletRequest, emptyList())
		val beans = BeanRegistrarDsl({
			registerBean<InjectedBean>()
			registerBean {
				router(bean()) { bean: InjectedBean ->
					{
						GET("/baz") { ok().header("hello", bean.sayHi()).build() }
					}
				}
			}
		})
		GenericApplicationContext().use { context ->
			val env = MockEnvironment()
			beans.register(BeanRegistryAdapter(context, context, env, BeanRegistrarDsl::class.java), env)
			context.refresh()
			val function = context.getBean<RouterFunction<ServerResponse>>().route(request)
			assertThat(function).isPresent
			assertThat(function.get().handle(request).headers().get("hello")).containsExactly("world")
		}
	}

	@Test
	fun decomposed() {
		val servletRequest = PathPatternsTestUtils.initRequest("GET", "/baz", true)
		val request = DefaultServerRequest(servletRequest, emptyList())
		val myRoutes = router { bean: InjectedBean ->
			{
				GET("/baz") { ok().header("hello", bean.sayHi()).build() }
			}
		}
		val beans = BeanRegistrarDsl({
			registerBean<InjectedBean>()
			registerBean {
				myRoutes(bean())
			}
		})
		GenericApplicationContext().use { context ->
			val env = MockEnvironment()
			beans.register(BeanRegistryAdapter(context, context, env, BeanRegistrarDsl::class.java), env)
			context.refresh()
			val function = context.getBean<RouterFunction<ServerResponse>>().route(request)
			assertThat(function).isPresent
			assertThat(function.get().handle(request).headers().get("hello")).containsExactly("world")
		}
	}
}

