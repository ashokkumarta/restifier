package ash.java.tools.restifier.commons;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class RestifierBase {

	public static final String PATH_SEP = "/";
	public static final String CONFIG_DL = ", ";
	public static final String ENV_SEPERATOR = ".";
	public static final String ENV_PARAM = "_env_";
	public static final String BLOCKED_ENV_DENY = "_access_to_the_environment_denied_";
	public static final String CONFIG_PATH = "config";
	public static final String CONFIG_SCAN_PATTERN = "/**";
	public static final String PROPERTIES_EXT = ".properties";
	public static final String EMPTY = "";
	public static final String ADMIN_RUN_MODE = "admin";
	public static final String REGISTERED_WORKERS_LST = "config/admin/registered.restifier-workers.lst";
	public static final String ENDPOINTS_URL = "/endpoints";
	public static final String ADMIN_SERVER_KEY = "Admin Server";

	public static final String RESPONSE_KEY = "restifier-response";

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	protected String[] split(String value) {
		return split(value, CONFIG_DL);
	}

	protected String[] split(String value, String delim) {
		if (null == value) {
			return new String[0];
		}
		return value.split(delim);
	}

	protected boolean isAnyEmpty(List<String> authValueList) {
		if (null == authValueList || authValueList.isEmpty()) {
			return true;
		}
		for (String value : authValueList) {
			if (value == null || value.equals(""))
				return true;
		}
		return false;
	}

	protected boolean isEmpty(String value) {
		if (null == value || value.length() < 1) {
			return true;
		}
		return false;
	}

	protected String convertToJsonString(HashMap<?, ?> map) throws IOException {
		logger.trace("Coverting map to response: {}", map);
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(map);
	}

	protected JsonNode convertToJson(String strJson) throws IOException {
		logger.debug("Converting to Json....");
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readTree(strJson);
	}

	protected Properties getFileProperties(String path, Resource resource) {

		Properties props = new Properties();
		try {
			props.load(resource.getInputStream());
		} catch (IOException e) {
			logger.error("Configuration not maintained at the path: {}; Error: {}", path, e);
		}
		return props;

	}

	protected Properties getCPRproperties(String path) {
		Properties props = new Properties();
		try {

			props.load(new ClassPathResource(path).getInputStream());
		} catch (IOException e) {
			logger.error("Configuration not maintained for the service: {}; Error: {}", path, e);
		}
		return props;
	}

	@Value("#{environment.DEF_ENV?:''}")
	private String def_env;

	@Value("#{environment.BLOCKED_ENV?:''}")
	private String blocked_env;

	@Value("#{environment.RUN_MODE?:'worker'}")
	private String run_mode;

	protected boolean isAdmin() {
		logger.debug("RUN_MODE set to: {}", run_mode);
		return ADMIN_RUN_MODE.equals(run_mode);
	}

	private static ThreadLocal<String> __envParam = new ThreadLocal<>();

	protected void setEnvParam(String envParam) {
		if (isEmpty(envParam)) {
			return;
		}
		if (blocked_env.indexOf(envParam) < 0) {
			__envParam.set(envParam);
		} else {
			logger.error("Request for environment that is blocked: {} in the blocked env configuration: {}", envParam,
					blocked_env);
			__envParam.set(BLOCKED_ENV_DENY);
		}
	}

	private String getEnv() {
		String envParam = __envParam.get();
		if (!isEmpty(envParam)) {
			logger.trace("Env from param choosen: {}", envParam);
			return envParam;
		}
		logger.trace("Default env choosen: {}", def_env);
		return def_env;
	}

	protected String getConfigPath() {
		String configPath = CONFIG_PATH;
		String env = getEnv();
		if (!isEmpty(env)) {
			configPath = CONFIG_PATH + ENV_SEPERATOR + env;
		}
		logger.info("Using config path: {}", configPath);
		return configPath;
	}

	protected Properties getSvcIdproperties(String svcId) {
		return getCPRproperties(getConfigPath() + PATH_SEP + svcId + PROPERTIES_EXT);
	}

	protected HashMap<String, Object> getResultsMap(Map<String, Object> params) {
		HashMap<String, Object> resultsMap = (HashMap<String, Object>) params.get(RESPONSE_KEY);
		return resultsMap;
	}

}
