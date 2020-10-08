package ash.java.tools.restifier.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import ash.java.tools.restifier.commons.RestifierBase;
import ash.java.tools.restifier.service.ServiceProcessor;

@RestController
public class EndPointsController extends RestifierBase {

	@Autowired
	@Qualifier("AdminEndPointsProcessor")
	private ServiceProcessor endPointsProcessor;

	@GetMapping(path = { "/" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> fetchEndPoints(@PathVariable(required = false) String svcId,
			@RequestParam Map<String, Object> params, ModelMap model, UriComponentsBuilder uriComponentsBuilder) {
		
		String baseUrl = uriComponentsBuilder.build().toUri().toString()+PATH_SEP;
		logger.debug("Retrieving endpoint URLs from: {}", baseUrl);
		ResponseEntity<String> response = endPointsProcessor.process(baseUrl, params);
		logger.debug("Completed processing. Response: {}", response);
		return response;
	}
}
