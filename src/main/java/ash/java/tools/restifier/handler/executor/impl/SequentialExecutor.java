package ash.java.tools.restifier.handler.executor.impl;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ash.java.tools.restifier.handler.accumulator.Accumulator;
import ash.java.tools.restifier.handler.executor.ExecutorBase;
import ash.java.tools.restifier.service.ServiceProcessor;

@Service(value = "SequentialExecutor")
public class SequentialExecutor extends ExecutorBase {

	@Autowired
	@Qualifier("CommonServiceProcessor")
	private ServiceProcessor commonServiceProcessor;

	@Autowired
	@Qualifier("ResponseAccumulator")
	private Accumulator responseAccumulator;

	@Override
	public String handle(Properties svcProps, String pipelineId, String input, Map<String, Object> params) {


		String[] serviceList = getServiceList(svcProps, pipelineId);

		try {

			for (String service : serviceList) {

				logger.info("Executing service: {}", service);
				String handlerOutput = commonServiceProcessor.processPipeline(service, svcProps, params);
				logger.info("Accumulating the result for: {}", service);
				responseAccumulator.accumulate(params, service, EMPTY, handlerOutput);
			}
			logger.info("Execution completed!!! ");
			logger.debug("Preparing response");
			String response = convertToJsonString(getResultsMap(params));
			logger.debug("Response returned: {}", response);
			return response;
		} catch (IOException e) {
			logger.debug("IOException in processing: {}", e);
		}
		return error(svcProps, pipelineId, input, params);
	}

}
