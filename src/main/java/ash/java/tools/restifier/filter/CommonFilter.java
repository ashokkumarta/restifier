package ash.java.tools.restifier.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RestController;

import ash.java.tools.restifier.commons.RestifierBase;

@RestController
public class CommonFilter extends RestifierBase implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		long start = System.currentTimeMillis();
		HttpServletRequest httpReq = (HttpServletRequest) req;
		String path = httpReq.getRequestURI();
		logger.info("Incoming request for: {}", path);
		String envParam = httpReq.getParameter(ENV_PARAM);
		logger.trace("Env set in the request: {}", envParam);
		setEnvParam(envParam);

		chain.doFilter(req, res);
		long end = System.currentTimeMillis();
		logger.info("Completed processing the request for:{} in :{}ms", path, (end - start));
	}

}
