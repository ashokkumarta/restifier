package ash.java.tools.restifier.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service(value = "AdminEndPointsProcessor")
public class AdminEndPointsProcessor extends EndPointsProcessor {

	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public ResponseEntity<String> process(String baseUrl, Map<String, Object> params) {

		String response = null;

		if (!isAdmin()) {
			logger.info("Worker instance...");
			return super.process(baseUrl, params);
		}

		Properties workersList = getCPRproperties(REGISTERED_WORKERS_LST);
		HashMap<Object, Object> allEndPointsMap = new HashMap<Object, Object>();
		workersList.forEach((key, value) -> {
			String result = restTemplate.getForObject(value + ENDPOINTS_URL, String.class);
			try {
				allEndPointsMap.put(key, convertToJson(result));
			} catch (Exception e) {
				logger.error("Error in worker end point fetch: {} / {}", key, e);
				allEndPointsMap.put(ADMIN_SERVER_KEY, EMPTY);
			}
		});
		try {
			allEndPointsMap.put(ADMIN_SERVER_KEY, fetchEndPointsMap(baseUrl));
		} catch (IOException e) {
			logger.error("Error in Admin server end point fetch: {}", e);
			allEndPointsMap.put(ADMIN_SERVER_KEY, EMPTY);
		}

		try {
			response = convertToJsonString(allEndPointsMap);
		} catch (IOException e) {
			logger.error("Error in converting all end points to json: {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		logger.trace("Returning response: {}", response);
		return ResponseEntity.ok().body(response);
	}
}
