package appstraction.tools.restool;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class ResToolHandler implements HttpHandler {

	public void handleRequest(HttpServerExchange exchange) throws Exception {
		new ResTool();
	}

}
