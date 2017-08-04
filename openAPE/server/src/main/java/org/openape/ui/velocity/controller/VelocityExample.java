/*
 * Copyright 2013 Per Wendel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openape.ui.velocity.controller;

import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

/**
 * VelocityTemplateRoute example.
 */
public final class VelocityExample {

	public static void main(final String[] args) {

		Spark.get("/hello", (request, response) -> {
			final Map<String, Object> model = new HashMap<>();
			model.put("message", "Hello Velocity");
			return new ModelAndView(model, "velocityTemplates/hello.vm"); // located
																			// in
																			// the
																			// resources
																			// directory
		} , new VelocityTemplateEngine());

	}

}
