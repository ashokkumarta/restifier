package ash.java.tools.restifier.handler.invoker;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import ash.java.tools.restifier.handler.HandlerBase;

public abstract class InvokerBase extends HandlerBase implements Invoker {
	
	protected List<String[]> getHeaders(Properties svcProps, String pipelineId) {
		List<String[]> headers = new ArrayList<String[]>();

		String headersCfg = svcProps.getProperty(pipelineId + PK_REQUEST_HEADERS);
		logger.debug("Headers configuration - headersCfg: {}", headersCfg);

		String[] headersList = split(headersCfg);
		
		for (String header : headersList) {
			logger.debug("Procssing header: {}", header);
			String[] headerNameVal = split(header,HEADER_NAMEVAL_DELIM);
			if (headerNameVal.length == 2) {
				headers.add(headerNameVal);
			} else {
				logger.debug("Invalid header configuration: {}. Skipping it...", header);
			}
		}
		return headers;
		
	}

}
