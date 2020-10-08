package ash.java.tools.restifier.handler.accumulator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ash.java.tools.restifier.handler.Handler;

public interface Accumulator extends Handler {

	// Constants
	public static final String PK_INPUT_NAME = ".input-name";
	public static final String PK_INPUT_TYPE = ".input-type";

	public static final String INPUT_TYPE_JSON = "json";
	public static final String INPUT_TYPE_XML = "xml";
	public static final String INPUT_TYPE_STRING = "string";

	public static final String INPUT_JSON_STARTS = "{";
	public static final String INPUT_XML_STARTS = "<";
	
	HashMap<String, Object> accumulate(Map<String, Object> params, String name, String value)
			throws IOException;

	HashMap<String, Object> accumulate(Map<String, Object> params, String name, String type, String value)
			throws IOException;
}
