package ash.java.tools.restifier.handler.invoker.impl;

import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;

import ash.java.tools.restifier.handler.invoker.InvokerBase;

@Service(value = "FileServiceInvoker")
public class FileInvoker extends InvokerBase {

	@Override
	public String handle(Properties svcProps, String pipelineId, String input, Map<String, Object> params) {

		String requestTemplate = svcProps.getProperty(pipelineId + PK_FILE_PATH);
		logger.debug("Using file path: {}", requestTemplate);

		String response = processTemplate(requestTemplate, params);
		logger.trace("Processed response: {}", response);

		return response;
	}

}
