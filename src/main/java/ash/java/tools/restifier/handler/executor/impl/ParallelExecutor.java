package ash.java.tools.restifier.handler.executor.impl;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ash.java.tools.restifier.handler.accumulator.Accumulator;
import ash.java.tools.restifier.handler.executor.ExecutorBase;
import ash.java.tools.restifier.handler.executor.util.ServiceExecutor;
import ash.java.tools.restifier.service.ServiceProcessor;

@Service(value = "ParallelExecutor")
public class ParallelExecutor extends ExecutorBase {

	@Autowired
	@Qualifier("CommonServiceProcessor")
	private ServiceProcessor commonServiceProcessor;

	@Autowired
	@Qualifier("ResponseAccumulator")
	private Accumulator responseAccumulator;

	@Override
	public String handle(Properties svcProps, String pipelineId, String input, Map<String, Object> params) {


		String[] serviceList = getServiceList(svcProps, pipelineId);

		int maxThreads = maxThreads(svcProps.getProperty(pipelineId + PK_MAX_THREADS));
		logger.debug("Using the maxThreads: {}", maxThreads);
		
		try {

			logger.info("Creating thread pool...");
			ExecutorService executor = Executors.newFixedThreadPool(maxThreads);

			for (String service : serviceList) {

				logger.info("Initiating executor of service: {}", service);
				Runnable worker = new ServiceExecutor(commonServiceProcessor, responseAccumulator, service, params);
				logger.info("Starting execution of service: {}", service);
				executor.execute(worker);
			}
			executor.shutdown();
			logger.info("Execution completion initiated");
			while (!executor.isTerminated()) {
				// Waiting for all workers to complete
			}
			logger.info("Execution completed!!! ");
			logger.debug("Preparing response");
			String response = convertToJsonString(getResultsMap(params));
			logger.trace("Response returned: {}", response);
			return response;
		} catch (IOException e) {
			logger.debug("IOException in processing: {}", e);
		}
		return error(svcProps, pipelineId, input, params);
	}

}
