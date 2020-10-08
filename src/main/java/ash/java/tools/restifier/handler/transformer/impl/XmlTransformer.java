package ash.java.tools.restifier.handler.transformer.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import ash.java.tools.restifier.handler.transformer.TransformerBase;

@Service(value = "XmlTransformer")
public class XmlTransformer extends TransformerBase {

	private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	private TransformerFactory transformerFactory = TransformerFactory.newInstance();

	@Override
	public String handle(Properties svcProps, String pipelineId, String input, Map<String, Object> params) {

		logger.info("Perfroming xml transformation");
		logger.trace("Input xml: {}", input);

		String xsltPath = svcProps.getProperty(pipelineId + PK_XML_XSL_TRANSFORMER);

		logger.debug("Using xslt from: {}", xsltPath);

		String response = null;

		try {

			logger.debug("Preparing transfromation style");
			StreamSource style = new StreamSource(new ClassPathResource(xsltPath).getInputStream());
			javax.xml.transform.Transformer transformer = transformerFactory.newTransformer(style);

			logger.debug("Building DOM from input xml");
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMSource source = new DOMSource(builder.parse(new ByteArrayInputStream(input.getBytes())));

			logger.debug("Preparing output stream");
			StringWriter outWriter = new StringWriter();
			StreamResult result = new StreamResult(outWriter);

			logger.debug("Preforming transform");
			transformer.transform(source, result);

			logger.debug("Collecting response");
			response = outWriter.getBuffer().toString();

			logger.debug("Transfromation successful");
			logger.trace("Output: {}", response);

			return response;

		} catch (TransformerException | IOException e) {
			logger.error("TransformerException | IOException in transformation {}:", e);
		} catch (ParserConfigurationException e) {
			logger.error("ParserConfigurationException in transformation {}:", e);
		} catch (SAXException e) {
			logger.error("SAXException in transformation {}:", e);
		}
		return error(svcProps, pipelineId, input, params);
	}

}
