package ash.java.tools.restifier.handler.invoker;

import ash.java.tools.restifier.handler.Handler;

public interface Invoker extends Handler {

	// Constants
	public static final String PK_FILE_PATH = ".file-path";
	public static final String PK_ENDPOINT_URL = ".endpoint-url";
	public static final String PK_REQUEST_TPL = ".request-tpl";
	public static final String PK_DB_CONN_URL = ".db-conn-url";
	public static final String PK_QUERY_TPL = ".query-tpl";
	public static final String PK_METHOD = ".method";
	public static final String PK_CONTENT_TYPE = ".content-type";
	public static final String PK_REMOVE_MIME_HEADERS = ".remove-mime-headers";
	public static final String PK_RESPONSE_CDATA_PATH = ".response-cdata-path";

	public static final String PK_REQUEST_HEADERS = ".headers";

	// Default values
	public static final String ENCODING = "UTF-8";
	public static final String HEADER_NAMEVAL_DELIM = ":";
	public static final String PK_RESPONSE_DIRECT_CDATA_PATH = "direct";
	public static final String PK_RESPONSE_DEFAULT_NODE_CDATA_PATH = "default-node";
	public static final String PK_RESPONSE_NODE_CDATA_PATH_DELIM = "/";

}
