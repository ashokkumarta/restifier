package ash.java.tools.restifier.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ash.java.tools.restifier.commons.RestifierBase;
import ash.java.tools.restifier.service.ServiceProcessor;

@RestController
public class CommonServiceController extends RestifierBase {

	@Autowired
	@Qualifier("CommonServiceProcessor")
	private ServiceProcessor commonServiceProcessor;

	@GetMapping(path = { "/**" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> all(HttpServletRequest request, @RequestParam Map<String, Object> params, ModelMap model) {
		
		String path = request.getRequestURI();

		logger.info("Received request for: {}", path);
		logger.info("With parameters: {}", params);
		ResponseEntity<String> response = commonServiceProcessor.process(path, params);
		logger.debug("Completed processing.");
		logger.trace("Response: {}", response);
		return response;
	}

}
