package ash.java.tools.restifier.handler.converter.impl;

import java.util.Map;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import ash.java.tools.restifier.handler.converter.ConvertorBase;

@Service(value = "Xml2JsonConvertor")
public class Xml2JsonConvertor extends ConvertorBase {

	@Override
	public String handle(Properties svcProps, String pipelineId, String input, Map<String, Object> params) {

		logger.debug("Converting xml to json");
		logger.trace("Xml input: {}", input);

		String response = null;
		try {
			JSONObject jObject = XML.toJSONObject(input);
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			Object json = mapper.readValue(jObject.toString(), Object.class);
			response = mapper.writeValueAsString(json);
			logger.debug("Xml converted to json successfully");
			logger.trace("Json output: {}", response);
			return response;
		} catch (JSONException e) {
			logger.error("JSONException in conversion {}:", e);
		} catch (JsonMappingException e) {
			logger.error("JsonMappingException in conversion {}:", e);
		} catch (JsonProcessingException e) {
			logger.error("JsonProcessingException in conversion {}:", e);
		}
		return error(svcProps, pipelineId, input, params);
	}

}
