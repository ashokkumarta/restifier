package ash.java.tools.restifier.handler.executor;

import ash.java.tools.restifier.handler.Handler;

public interface Executor extends Handler {
	
	public static final String PK_SERVICES_TO_EXECUTE = ".services-to-execute";
	public static final String PK_MAX_THREADS = ".max-threads";

	public static final int DEFAULT_MAX_THREADS = 5;
	public static final int MAX_MAX_THREADS = 20;
}
