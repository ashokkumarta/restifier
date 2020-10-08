package ash.java.tools.restifier.handler.accumulator.impl;

import java.io.IOException;
import org.springframework.stereotype.Service;

@Service(value = "StringResponseAccumulator")
public class StringResponseAccumulator extends ResponseAccumulator {

	@Override
	protected Object convertToType(String type, String input) throws IOException {
		return input;
	}
}
