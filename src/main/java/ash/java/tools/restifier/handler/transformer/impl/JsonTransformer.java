package ash.java.tools.restifier.handler.transformer.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;

import ash.java.tools.restifier.handler.transformer.TransformerBase;

@Service(value = "JsonTransformer")
public class JsonTransformer extends TransformerBase {

	@Override
	public String handle(Properties svcProps, String pipelineId, String input, Map<String, Object> params) {

		logger.info("Perfroming json transformation");
		logger.trace("Input json: {}", input);

		String specPath = svcProps.getProperty(pipelineId + PK_JSON_JOLT_TRANSFORMER);
		logger.debug("Using jolt from: {}", specPath);

		try {

			logger.debug("Preparing transfromation spec");
			List chainrSpecJSON = JsonUtils.jsonToList(this.getClass().getClassLoader().getResourceAsStream(specPath));
			Chainr chainr = Chainr.fromSpec(chainrSpecJSON);

			logger.debug("Processing input json");
			Object inputJSON = JsonUtils.jsonToObject(input);

			logger.debug("Performig transform");
			Object transformedOutput = chainr.transform(inputJSON);

			logger.debug("Collecting response");
			String response = JsonUtils.toJsonString(transformedOutput);

			logger.debug("Transfromation successful");
			logger.trace("Output: {}", response);
			return response;
		} catch (Exception e) {
			logger.error("Exception in transformation {}:", e);
		}
		return error(svcProps, pipelineId, input, params);
	}

}
