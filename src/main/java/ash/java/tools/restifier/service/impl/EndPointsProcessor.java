package ash.java.tools.restifier.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ash.java.tools.restifier.service.ServiceProcessorBase;

@Service(value = "EndPointsProcessor")
public class EndPointsProcessor extends ServiceProcessorBase {

	private PathMatchingResourcePatternResolver scanner = new PathMatchingResourcePatternResolver();

	protected HashMap<String, HashMap<String, String>> fetchEndPointsMap(String baseUrl) throws IOException {

		HashMap<String, HashMap<String, String>> endPointsMap = new HashMap<String, HashMap<String, String>>();

		String configPath = getConfigPath();
		logger.debug("Config Path: {}", configPath);

		Resource[] resources = scanner.getResources(configPath + CONFIG_SCAN_PATTERN);

		if (resources == null || resources.length == 0) {
			logger.warn("Could not find any services configured");
			return endPointsMap;
		}

		for (Resource resource : resources) {
			String strSvcPath = resource.getURI().toString();
			logger.info("Checking file: {}", strSvcPath);
			if (strSvcPath.endsWith(PROPERTIES_EXT)) {
				logger.debug("Processing configuration: {}", strSvcPath);
				String svcName = getSvcName(strSvcPath);
				logger.debug("Service name: {}", svcName);
				HashMap<String, String> svcEndPoint = getEndPoint(baseUrl, strSvcPath, configPath, resource);
				endPointsMap.put(svcName, svcEndPoint);
				logger.info("Completed processing for: {}", strSvcPath);
			}
		}

		logger.trace("Returning endpoints map: {}", endPointsMap);
		return endPointsMap;

	}

	protected String fetchEndPoints(String baseUrl) throws IOException {
		logger.debug("Fetching endpoints Map...");
		HashMap<String, HashMap<String, String>> endPointsMap = fetchEndPointsMap(baseUrl);
		logger.debug("Converting to json...");
		return convertToJsonString(endPointsMap);
	}

	@Override
	public ResponseEntity<String> process(String baseUrl, Map<String, Object> params) {

		String response = null;
		try {
			response = fetchEndPoints(baseUrl);
		} catch (IOException e) {
			logger.error("Error in processing: {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		logger.trace("Returning response: {}", response);
		return ResponseEntity.ok().body(response);
	}

	private String getSvcUrlPath(String strSvcPath, String strConfigPath) {
		String svcUrlPath = strSvcPath.substring(strSvcPath.indexOf(strConfigPath) + strConfigPath.length() + 1);
		svcUrlPath = svcUrlPath.replace(PROPERTIES_EXT, EMPTY);
		return svcUrlPath;

	}

	private String getSvcName(String svcUrlPath) {
		return svcUrlPath.substring(svcUrlPath.lastIndexOf(PATH_SEP) + 1).replace(PROPERTIES_EXT, EMPTY);
	}

	private HashMap<String, String> getEndPoint(String baseUrl, String strSvcPath, String strConfigPath,
			Resource resource) {

		logger.debug("Processing service configuration: {}", strSvcPath);

		Properties props = getFileProperties(strSvcPath, resource);
		logger.debug("Service properties: {}", props);

		HashMap<String, String> svcMap = new HashMap<String, String>();
		logger.debug("Accumulating service properties");
		String svcUrl = baseUrl + getSvcUrlPath(strSvcPath, strConfigPath);
		svcMap.put(KEY_URL, svcUrl);
		svcMap.put(KEY_NAME, props.getProperty(KEY_NAME));
		svcMap.put(KEY_PARAMETERS, props.getProperty(KEY_PARAMETERS));
		svcMap.put(KEY_DESCRIPTION, props.getProperty(KEY_DESCRIPTION));
		return svcMap;
	}
}
