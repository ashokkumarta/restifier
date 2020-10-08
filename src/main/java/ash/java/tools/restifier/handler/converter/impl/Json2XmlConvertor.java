package ash.java.tools.restifier.handler.converter.impl;

import java.util.Map;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;

import ash.java.tools.restifier.handler.converter.ConvertorBase;

@Service(value = "Json2XmlConvertor")
public class Json2XmlConvertor extends ConvertorBase {

	@Override
	public String handle(Properties svcProps, String pipelineId, String input, Map<String, Object> params) {

		logger.debug("Converting json to xml");
		logger.trace("Json input: {}", input);
		JSONObject json;
		try {
			json = new JSONObject(input);
			logger.debug("Converting from json to xml");
			String response = XML.toString(json);
			logger.debug("Json converted to xml successfully");
			logger.trace("Xml output: {}", response);
			return response;
		} catch (JSONException e) {
			logger.error("JSONException in conversion {}:", e);
		}
		return error(svcProps, pipelineId, input, params);
	}

}
