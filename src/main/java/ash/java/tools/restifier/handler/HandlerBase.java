package ash.java.tools.restifier.handler;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import ash.java.tools.restifier.commons.RestifierBase;

public abstract class HandlerBase extends RestifierBase implements Handler {

	public static final String ERROR_RESPONSE = null;

	protected ClassLoaderTemplateResolver templateResolver;
	protected TemplateEngine templateEngine;

	public HandlerBase() {
		logger.debug("Creating template resolver");
		templateResolver = new ClassLoaderTemplateResolver();
		logger.debug("Creating template engine");
		templateEngine = new TemplateEngine();
		logger.debug("Setting template resolver to template engine");
		templateEngine.setTemplateResolver(templateResolver);
	}

	@Override
	public String processTemplate(String template, Map<String, Object> params) {

		logger.debug("Processing template: {}", template);
		logger.debug("With parameters: {}", params);

		logger.debug("Creating context");
		Context context = new Context();
		context.setVariables(params);

		StringWriter stringWriter = new StringWriter();

		logger.debug("Processing the template");
		templateEngine.process(template, context, stringWriter);

		String response = stringWriter.toString();

		logger.trace("Processed. response: {}", response);

		return response;
	}

	public String error(Properties svcProps, String pipelineId, String input, Map<String, Object> params) {
		logger.debug("Returning error response {}:", ERROR_RESPONSE);
		return ERROR_RESPONSE;
	}

}
