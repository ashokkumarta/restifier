package ash.java.tools.restifier.handler.invoker.impl;

import java.util.Map;
import java.util.Properties;

import org.jooq.JSONFormat;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;

import ash.java.tools.restifier.handler.invoker.InvokerBase;

@Service(value = "SQLQueryServiceInvoker")
public class SQLQueryInvoker extends InvokerBase {

	@Override
	public String handle(Properties svcProps, String pipelineId, String input, Map<String, Object> params) {

		String dbConnUrl = svcProps.getProperty(pipelineId + PK_DB_CONN_URL);
		String queryTemplate = svcProps.getProperty(pipelineId + PK_QUERY_TPL);

		logger.debug("Parameters for query call - endpointUrl: {}", dbConnUrl);
		logger.debug("Parameters for Soap call - queryTemplate: {}", queryTemplate);

		String sqlQuery = processTemplate(queryTemplate, params);

		logger.debug("Formatted query: {}", sqlQuery);

		logger.debug("Setting output json format");
		JSONFormat resFormat = new JSONFormat().header(false).recordFormat(JSONFormat.RecordFormat.OBJECT);

		logger.debug("Creating the collection and executing the query");
		String response = DSL.using(dbConnUrl).fetch(sqlQuery).formatJSON(resFormat);

		logger.trace("Response: {}", response);
		return response;

	}

}
