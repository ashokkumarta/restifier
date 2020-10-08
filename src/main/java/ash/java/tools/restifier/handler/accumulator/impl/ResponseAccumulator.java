package ash.java.tools.restifier.handler.accumulator.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;

import ash.java.tools.restifier.handler.accumulator.AccumulatorBase;

@Service(value = "ResponseAccumulator")
public class ResponseAccumulator extends AccumulatorBase {

	@Override
	public String handle(Properties svcProps, String pipelineId, String input, Map<String, Object> params) {

		String inputName = svcProps.getProperty(pipelineId + PK_INPUT_NAME);
		String inputType = svcProps.getProperty(pipelineId + PK_INPUT_TYPE);
		logger.debug("Using the inputName: {}", inputName);
		logger.debug("Using the inputType: {}", inputType);
		
		logger.trace("Input: {}", input);
		logger.debug("Accumulating results in chain");
		try {
			HashMap<String, Object> resultsMap = accumulate(params, inputName, inputType, input);
			logger.trace("Accumulated the input in the reponse: {}",resultsMap);
			String response = convertToJsonString(resultsMap);
			logger.trace("Final response from accumulator: {}",response);
			return response;
		} catch (IOException e) {
			logger.error("IOException in conversion {}:", e);
		}
		return error(svcProps, pipelineId, input, params);
	}
}
