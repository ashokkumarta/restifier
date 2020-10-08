package ash.java.tools.restifier.service;

import java.util.Map;
import java.util.Properties;

import ash.java.tools.restifier.commons.RestifierBase;

public abstract class ServiceProcessorBase extends RestifierBase implements ServiceProcessor {
	
	@Override
	public String processPipeline(String svcId, Properties svcProps, Map<String, Object> params){
		throw new RuntimeException("Unsupported method call");
	}

}
