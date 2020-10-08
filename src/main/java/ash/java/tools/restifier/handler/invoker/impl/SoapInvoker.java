package ash.java.tools.restifier.handler.invoker.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import org.springframework.stereotype.Service;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ash.java.tools.restifier.handler.invoker.InvokerBase;

@Service(value = "SoapServiceInvoker")
public class SoapInvoker extends InvokerBase {

	@Override
	public String handle(Properties svcProps, String pipelineId, String input, Map<String, Object> params) {

		String endpointUrl = svcProps.getProperty(pipelineId + PK_ENDPOINT_URL);
		String requestTemplate = svcProps.getProperty(pipelineId + PK_REQUEST_TPL);
		String removeMimeHeadersCfg = svcProps.getProperty(pipelineId + PK_REMOVE_MIME_HEADERS);
		String responseCDataPath = svcProps.getProperty(pipelineId + PK_RESPONSE_CDATA_PATH);

		logger.debug("Parameters for Soap call - endpointUrl: {}", endpointUrl);
		logger.debug("Parameters for Soap call - requestTemplate: {}", requestTemplate);

		// Create SOAP Connection
		SOAPConnectionFactory soapConnectionFactory;
		SOAPConnection soapConnection = null;

		try {

			logger.debug("Creating Soap connection");
			soapConnectionFactory = SOAPConnectionFactory.newInstance();
			soapConnection = soapConnectionFactory.createConnection();

			logger.debug("Creating Soap request");
			String requestBody = processTemplate(requestTemplate, params);

			logger.debug("Creating Soap message");
			MessageFactory messageFactory = MessageFactory.newInstance();
			SOAPMessage soapRequest = messageFactory.createMessage(null,
					new ByteArrayInputStream(requestBody.getBytes(ENCODING)));

			soapRequest.writeTo(System.out);

			String[] removeMimeHeaders = split(removeMimeHeadersCfg);
			for (String header : removeMimeHeaders) {
				soapRequest.getMimeHeaders().removeHeader(header);
			}

			List<String[]> headersList = getHeaders(svcProps, pipelineId);
			for (String[] header : headersList) {
				soapRequest.getMimeHeaders().addHeader(header[0], header[1]);
			}

			logger.debug("Performing Soap call");
			SOAPMessage soapResponse = soapConnection.call(soapRequest, endpointUrl);
			logger.debug("Collecting response");
			String response = getResponseXML(soapResponse, responseCDataPath);
			logger.trace("Response: {}", response);
			return response;

		} catch (UnsupportedOperationException e) {
			logger.error("UnsupportedOperationException in invocation {}:", e);
		} catch (SOAPException e) {
			logger.error("SOAPException in invocation {}:", e);
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException in invocation {}:", e);
		} catch (IOException e) {
			logger.error("IOException in invocation {}:", e);
		} catch (XPathExpressionException e) {
			logger.error("XPathExpressionException in invocation {}:", e);
		} catch (Exception e) {
			logger.error("Unexpected Exception in invocation {}:", e);
		} finally {
			if (null != soapConnection) {
				try {
					soapConnection.close();
				} catch (SOAPException e) {
					logger.error("SOAPException in closing soap connection {}:", e);
				}
			}
		}
		return error(svcProps, pipelineId, input, params);
	}

	private Node getMatchNode(NodeList nodeList, String path) {
		String[] splits = path.split(PK_RESPONSE_NODE_CDATA_PATH_DELIM, 2);
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (splits[0].equals(nodeList.item(i).getNodeName())) {
				if (splits.length > 1) {
					return getMatchNode(nodeList.item(i).getChildNodes(), splits[1]);
				} else {
					return nodeList.item(i);
				}
			}
		}
		logger.warn("Returning NULL");
		return null;
	}

	private String getPathResponseXML(SOAPMessage soapResponse, String path) {
		String response = null;
		try {
			NodeList nodeList = soapResponse.getSOAPBody().getChildNodes();
			Node matched = getMatchNode(nodeList, path);
			response = matched.getTextContent();
		} catch (SOAPException e) {
			logger.error("SOAPException in getResponseXML {}:", e);
		}
		return response;
	}

	private String getNodeResponseXML(SOAPMessage soapResponse) {
		String response = null;

		logger.trace("SOAPMessage {}:", soapResponse);
		try {
			Node resNode = soapResponse.getSOAPBody().getChildNodes().item(0);
			DOMSource source = new DOMSource(resNode);
			StringWriter stringResult = new StringWriter();
			TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
			response = stringResult.toString();
		} catch (DOMException e) {
			logger.error("DOMException in getNodeResponseXML {}:", e);
		} catch (SOAPException e) {
			logger.error("SOAPException in getNodeResponseXML {}:", e);
		} catch (TransformerConfigurationException e) {
			logger.error("TransformerConfigurationException in getNodeResponseXML {}:", e);
		} catch (TransformerException e) {
			logger.error("TransformerException in getNodeResponseXML {}:", e);
		} catch (TransformerFactoryConfigurationError e) {
			logger.error("TransformerFactoryConfigurationError in getNodeResponseXML {}:", e);
		}
		return response;
	}

	private String getDirectResponseXML(SOAPMessage soapResponse) {
		try {
			return soapResponse.getSOAPBody().getTextContent();
		} catch (SOAPException e) {
			logger.error("SOAPException in getResponseXML {}:", e);
		}
		return null;
	}

	private String getResponseXML(SOAPMessage soapResponse, String path)
			throws XPathExpressionException, TransformerException {
		logger.trace("Path is: {}", path);
		if (isEmpty(path) || PK_RESPONSE_DEFAULT_NODE_CDATA_PATH.equals(path)) {
			logger.trace("In condition 1");
			return getNodeResponseXML(soapResponse);
		}
		if (PK_RESPONSE_DIRECT_CDATA_PATH.equals(path)) {
			logger.trace("In condition 2");
			return getDirectResponseXML(soapResponse);
		}
		logger.trace("Default handling");
		return getPathResponseXML(soapResponse, path);
	}
}
