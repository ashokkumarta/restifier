package ash.java.tools.restifier.service;

import java.util.Map;
import java.util.Properties;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public interface ServiceProcessor {

	// Constants
	public static final String KEY_URL = "url";
	public static final String KEY_NAME = "name";
	public static final String KEY_PARAMETERS = "parameters";
	public static final String KEY_DESCRIPTION = "description";

	public static final String PK_PIPELINE = "processing.pipeline";
	public static final String PK_HANDLER = ".handler";
	public static final String PK_OUTPUT_CONTENT_TYPE = "output.content-type";

	// Default values
	public static final MediaType DEFAULT_MEDIA_TYPE = MediaType.ALL;

	ResponseEntity<String> process(String svcId, Map<String, Object> params);
	String processPipeline(String svcId, Properties svcProps, Map<String, Object> params);

}
