package ash.java.tools.restifier.handler.converter.impl;

import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;

import ash.java.tools.restifier.handler.converter.ConvertorBase;

@Service(value = "WrapperConvertor")
public class WrapperConvertor extends ConvertorBase {

	@Override
	public String handle(Properties svcProps, String pipelineId, String input, Map<String, Object> params) {

		logger.debug("Wrapping the input");
		String wrapperTpl = svcProps.getProperty(pipelineId + PK_WRAPPER_TEMPLATE);
		logger.debug("Using the template: {}", wrapperTpl);

		logger.trace("Input: {}", input);
		params.put("input", input);
		String response = processTemplate(wrapperTpl, params);

		logger.debug("Wrapped the input successfully");
		logger.trace("Wrapped output: {}", response);
		return response;
	}

}
