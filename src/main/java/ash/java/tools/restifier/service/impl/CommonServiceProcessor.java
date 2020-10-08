package ash.java.tools.restifier.service.impl;

import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ash.java.tools.restifier.handler.Handler;
import ash.java.tools.restifier.handler.executor.Executor;
import ash.java.tools.restifier.handler.invoker.Invoker;
import ash.java.tools.restifier.service.ServiceProcessorBase;;

@Service(value = "CommonServiceProcessor")
public class CommonServiceProcessor extends ServiceProcessorBase {

	@Autowired
	private ApplicationContext appContext;

	private MediaType getMediaType(String type) {
		try {
			return MediaType.parseMediaType(type);
		} catch (InvalidMediaTypeException ex) {
			logger.warn("Invalid output.content-type configuration: {}", type);
		}
		return DEFAULT_MEDIA_TYPE;
	}

	@Override
	public String processPipeline(String svcId, Properties svcProps, Map<String, Object> params) {

		String pipelineCfg = svcProps.getProperty(PK_PIPELINE);
		logger.info("Pipeline configuration: {}", pipelineCfg);

		if (null == pipelineCfg || pipelineCfg.length() < 1) {
			logger.error("Incorrect pipeline configuration for the service: {} | {}", svcId, pipelineCfg);
			return "Incorrect configuration for: " + svcId;
		}

		String[] pipeline = split(svcProps.getProperty(PK_PIPELINE));
		logger.info("Processing steps: {}", pipeline);

		String handlerOutput = null;

		for (String handlerId : pipeline) {
			logger.debug("Executing step: {}", handlerId);
			String handlerName = svcProps.getProperty(handlerId + PK_HANDLER);
			logger.debug("handlerName: {}", handlerName);
			Handler handler = appContext.getBean(handlerName, Handler.class);
			logger.debug("handler: {}", handler);
			if (handlerOutput == null && !(handler instanceof Invoker)
					&& !(handler instanceof Executor)) {

				logger.error("Unable to proceed with pipeline step: {}", handlerId);
				return "Unable to proceed with pipeline step " + handlerId;
			}
			handlerOutput = handler.handle(svcProps, handlerId, handlerOutput, params);
			logger.trace("Output from step: {} is: {}", handlerId, handlerOutput);
		}
		return handlerOutput;

	}

	@Override
	public ResponseEntity<String> process(String svcId, Map<String, Object> params) {

		Properties svcProps = getSvcIdproperties(svcId);
		logger.debug("Service properties from configuration: {}", svcProps);

		try {
			String handlerOutput = processPipeline(svcId, svcProps, params);
			return ResponseEntity.ok().contentType(getMediaType(svcProps.getProperty(PK_OUTPUT_CONTENT_TYPE)))
					.body(handlerOutput);
		} catch (Exception ex) {
			logger.debug("Unexpected exception in processing: {} is: {}", svcId, ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.contentType(getMediaType(svcProps.getProperty(PK_OUTPUT_CONTENT_TYPE))).body(ex.getMessage());
		}
	}

}
