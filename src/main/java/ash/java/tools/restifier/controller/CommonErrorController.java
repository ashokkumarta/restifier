package ash.java.tools.restifier.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ash.java.tools.restifier.commons.RestifierBase;

@RestController
public class CommonErrorController extends RestifierBase {

	@GetMapping(path = { "/error" })
	public ResponseEntity<String> all(HttpServletRequest request, @RequestParam Map<String, Object> params,
			ModelMap model) {

		logger.error("Incorrect request for: {}", request.getRequestURI());
		logger.error("With parameters: {}", params);

		ResponseEntity<String> response = ResponseEntity.unprocessableEntity().contentType(MediaType.TEXT_HTML)
				.body(ERROR_RESPONSE);
		return response;
	}

	private static final String ERROR_RESPONSE = "<h1>Thanks for using Restifier</h1><b><font color=red>Your request could not be processed</font></b><pre>\nCheck if you are using the correct URL to access your service\n\nUsage:\n    GET .../service/$svcID?params] (or)\n    GET .../service/$modId/$svcID?[params] (or)\n    GET .../service/$grpId/$modId/$svcID?[params]\n\nContatc your application support team if the problem persists</pre>";

}
