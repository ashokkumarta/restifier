package ash.java.tools.restifier.handler.executor.util;

import java.util.Map;
import java.util.Properties;

import ash.java.tools.restifier.commons.RestifierBase;
import ash.java.tools.restifier.handler.accumulator.Accumulator;
import ash.java.tools.restifier.service.ServiceProcessor;

public class ServiceExecutor extends RestifierBase implements Runnable {

	private final String svcId;
	private final Map<String, Object> params;
	
	private final ServiceProcessor commonServiceProcessor;
	
	private final Accumulator responseAccumulator;

	public ServiceExecutor(ServiceProcessor commonServiceProcessor, Accumulator responseAccumulator, String svcId, Map<String, Object> params) {
		this.commonServiceProcessor = commonServiceProcessor;
		this.responseAccumulator = responseAccumulator;
		this.svcId = svcId;
		this.params = params;
	}

	@Override
	public void run() {

		Properties svcProps = getSvcIdproperties(svcId);
		logger.debug("Service properties from configuration: {}", svcProps);

		try {
			String handlerOutput = commonServiceProcessor.processPipeline(svcId, svcProps, params);
			responseAccumulator.accumulate(params, svcId, EMPTY, handlerOutput);
		} catch (Exception ex) {
			logger.debug("Unexpected exception in processing: {} is: {}", svcId, ex);
		}
	}
}
