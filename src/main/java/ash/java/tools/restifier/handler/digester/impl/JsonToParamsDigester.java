package ash.java.tools.restifier.handler.digester.impl;

import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

import ash.java.tools.restifier.handler.digester.DigesterBase;
import net.minidev.json.JSONArray;

@Service(value = "JsonToParamsDigestor")
public class JsonToParamsDigester extends DigesterBase {

	@Override
	public String handle(Properties svcProps, String pipelineId, String input, Map<String, Object> params) {

		String paramsPathCfg = svcProps.getProperty(pipelineId + PK_PARAMS_PATH);
		String paramsNameCfg = svcProps.getProperty(pipelineId + PK_PARAMS_NAME);

		logger.debug("Using the paramsPathCfg: {}", paramsPathCfg);
		logger.debug("Using the paramsNameCfg: {}", paramsNameCfg);

		String[] paramsPath = split(paramsPathCfg);
		String[] paramsName = split(paramsNameCfg);

		Object json = buildJsonObject(input);

		for (int i = 0; i < paramsPath.length; i++) {

			String path = paramsPath[i];
			String name = paramsName[i];

			logger.info("Digesting for path: {} | name: {}", path, name);
			String value = getValue(json, path);
			logger.info("Value of name: {} is: {}", name, value);
			logger.info("Setting the values in params...");
			params.put(name, value);
		}
		logger.info("Params after digest: {}", params);
		logger.info("Execution completed!!! ");
		return input;
	}

	private Object buildJsonObject(String input) {

		Configuration conf = Configuration.defaultConfiguration().addOptions(Option.ALWAYS_RETURN_LIST);
		return conf.jsonProvider().parse(input);
	}

	private String getValue(Object json, String path) {
		
		JSONArray obj = JsonPath.read(json, path);
		logger.debug("Digested... : {}", obj);
		return obj.get(0).toString();
	}
}
