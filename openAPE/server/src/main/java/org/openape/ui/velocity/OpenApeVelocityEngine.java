package org.openape.ui.velocity;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.Properties;


public class OpenApeVelocityEngine extends VelocityTemplateEngine {
	private static final VelocityEngine velocityEngine = new VelocityEngine();
	private static final Logger logger = LoggerFactory.getLogger(OpenApeVelocityEngine.class);

	static {
		logger.info("Initializing OpenApeVelocityEngine!");

		final Properties properties = new Properties();
		properties.setProperty("runtime.log.logsystem.class", NullLogChute.class.getName());
		properties.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		properties.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		velocityEngine.init(properties);
		logger.info("Initialized OpenApeVelocityEngine!");

	}

	public OpenApeVelocityEngine() {
		super(velocityEngine);
		logger.info("Creating new OpenApeVelocityEngine!");
	}

}

