package ash.java.tools.restifier.handler.invoker.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ash.java.tools.restifier.handler.invoker.InvokerBase;

@Service(value = "RestServiceInvoker")
public class RestInvoker extends InvokerBase {

	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public String handle(Properties svcProps, String pipelineId, String input, Map<String, Object> params) {

		String endpointUrl = svcProps.getProperty(pipelineId + PK_ENDPOINT_URL);
		String requestTemplate = svcProps.getProperty(pipelineId + PK_REQUEST_TPL);
		HttpMethod method = HttpMethod.resolve(svcProps.getProperty(pipelineId + PK_METHOD));
		MediaType contentType = MediaType.valueOf(svcProps.getProperty(pipelineId + PK_CONTENT_TYPE));

		logger.debug("Rest service configuration - endpointUrl: {}", endpointUrl);
		logger.debug("Rest service configuration - requestTemplate: {}", requestTemplate);
		logger.debug("Rest service configuration - method: {}", method);
		logger.debug("Rest service configuration - contentType: {}", contentType);

		logger.debug("Creating and setting headers");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(contentType);

		List<String[]> headersList = getHeaders(svcProps, pipelineId);
		for (String[] header : headersList) {
			headers.add(header[0], header[1]);
		}

		logger.debug("Creating body content");
		String requestBody = processTemplate(requestTemplate, params);
		logger.debug("Creating http request");
		HttpEntity<String> request = new HttpEntity<String>(requestBody, headers);
		logger.debug("Calling REST service");
		ResponseEntity<String> responseEntity = restTemplate.exchange(endpointUrl, method, request, String.class);
		logger.debug("Call completed");
		String response = responseEntity.getBody();
		logger.trace("Response: {}", response);

		return response;
	}

}
