package ash.java.tools.restifier.handler.accumulator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ash.java.tools.restifier.handler.HandlerBase;

public abstract class AccumulatorBase extends HandlerBase implements Accumulator {

	public HashMap<String, Object> accumulate(Map<String, Object> params, String name, String value) throws IOException {
		return accumulate(params, name, EMPTY, value);
	}

	public HashMap<String, Object> accumulate(Map<String, Object> params, String name, String type, String value) throws IOException {
		HashMap<String, Object> response = null;
		synchronized (this) {
			response = (HashMap<String, Object>) params.get(RESPONSE_KEY);
			if (null == response) {
				response = new HashMap<String, Object>();
				params.put(RESPONSE_KEY, response);
			}
		}
		Object formattedValue = convertToType(type, value);
		logger.trace("Formatted the input for accumulation: {}", formattedValue);
		response.put(name, formattedValue);
		return response;
	}

	protected Object convertToType(String type, String input)  throws IOException {
		logger.debug("Converting to type: {}", type);
		if(EMPTY.equals(type)) {
			type = detectType(input);
			logger.debug("Auto detected type to: {}", type);
		}
		if (INPUT_TYPE_JSON.equals(type)) {
			logger.debug("Conversion in progress...");
			return convertToJson(input);
		}
		return input;
	}
	
	private String detectType(String input) {
		if (! isEmpty(input)) {
			if(input.startsWith(INPUT_JSON_STARTS)) {
				return INPUT_TYPE_JSON;
			}
			if (input.startsWith(INPUT_XML_STARTS)) {
				return INPUT_TYPE_XML;
			}
		}
		return INPUT_TYPE_STRING;
	}
}
