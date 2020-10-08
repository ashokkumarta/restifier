package ash.java.tools.restifier.handler.transformer;

import java.util.Map;
import java.util.Properties;

import ash.java.tools.restifier.handler.HandlerBase;

public abstract class TransformerBase extends HandlerBase implements Transformer {

	@Override
	public String error(Properties svcProps, String pipelineId, String input, Map<String, Object> params) {
		logger.debug("Error. Returning response same as input {}:", input);
		return input;
	}

}
