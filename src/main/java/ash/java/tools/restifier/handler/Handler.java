package ash.java.tools.restifier.handler;

import java.util.Map;
import java.util.Properties;

public interface Handler {

	String handle(Properties svcProps, String pipelineId, String input, Map<String, Object> params);

	String processTemplate(String template, Map<String, Object> params);

}
