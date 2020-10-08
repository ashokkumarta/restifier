package ash.java.tools.restifier.handler.executor;

import java.util.Properties;

import ash.java.tools.restifier.handler.HandlerBase;

public abstract class ExecutorBase extends HandlerBase implements Executor {

	protected int maxThreads(String maxThreadsCfg) {
		try {
			int maxThreads = Integer.parseInt(maxThreadsCfg);
			if (maxThreads < 1) {
				return DEFAULT_MAX_THREADS;
			}
			return maxThreads > MAX_MAX_THREADS ? MAX_MAX_THREADS : maxThreads;
		} catch (Exception any) {
			logger.info("Max thread configuration undefined or incorrect. Using default: {}", DEFAULT_MAX_THREADS);
		}
		return DEFAULT_MAX_THREADS;
	}

	protected String[] getServiceList(Properties svcProps, String pipelineId) {

		String servicesToExec = svcProps.getProperty(pipelineId + PK_SERVICES_TO_EXECUTE);
		logger.debug("Using the servicesToExec: {}", servicesToExec);

		String[] serviceList = split(servicesToExec);
		logger.info("Executing services: {}", serviceList);
		return serviceList;

	}

}
