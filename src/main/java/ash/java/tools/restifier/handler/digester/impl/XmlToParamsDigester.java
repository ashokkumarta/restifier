package ash.java.tools.restifier.handler.digester.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ash.java.tools.restifier.handler.digester.DigesterBase;

@Service(value = "XmlToParamsDigestor")
public class XmlToParamsDigester extends DigesterBase {

	private final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

	@Override
	public String handle(Properties svcProps, String pipelineId, String input, Map<String, Object> params) {

		String paramsPathCfg = svcProps.getProperty(pipelineId + PK_PARAMS_PATH);
		String paramsNameCfg = svcProps.getProperty(pipelineId + PK_PARAMS_NAME);

		logger.debug("Using the paramsPathCfg: {}", paramsPathCfg);
		logger.debug("Using the paramsNameCfg: {}", paramsNameCfg);

		String[] paramsPath = split(paramsPathCfg);
		String[] paramsName = split(paramsNameCfg);

		try {

			Document xml = buildXmlDocument(input);

			for (int i = 0; i < paramsPath.length; i++) {

				String path = paramsPath[i];
				String name = paramsName[i];

				logger.info("Digesting for path: {} | name: {}", path, name);
				String value = getValue(xml, path);
				logger.info("Value of name: {} is: {}", name, value);
				logger.info("Setting the values in params...");
				params.put(name, value);
			}
			logger.info("Params after digest: {}", params);
			logger.info("Execution completed!!! ");
			return input;
		} catch (IOException e) {
			logger.debug("IOException in processing: {}", e);
		} catch (XPathExpressionException e) {
			logger.debug("XPathExpressionException in processing: {}", e);
		} catch (SAXException e) {
			logger.debug("SAXException in processing: {}", e);
		} catch (ParserConfigurationException e) {
			logger.debug("ParserConfigurationException in processing: {}", e);
		}
		return error(svcProps, pipelineId, input, params);
	}

	private Document buildXmlDocument(String input) throws SAXException, IOException, ParserConfigurationException {

		final DocumentBuilder builder = builderFactory.newDocumentBuilder();
		return builder.parse(new InputSource(new StringReader(input)));

	}

	private String getValue(Document xml, String path) throws XPathExpressionException {

		final XPath xPath = XPathFactory.newInstance().newXPath();
		String retrievedValue = xPath.compile(path).evaluate(xml);
		return retrievedValue;
	}
}
