package appstraction.tools.dev_server;

import com.fasterxml.jackson.databind.ObjectMapper;

import appstraction.tools.wsgen.WsGenerator;
import appstraction.tools.wsgen.WsGenerator.Options;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class WsGenHandler implements HttpHandler {

	public void handleRequest(HttpServerExchange exchange) throws Exception {
		WsGenerator.Options options = new WsGenerator.Options();
		/*
		ObjectMapper m = new ObjectMapper();
		m.convertValue(arg0, arg1)
		
		WsGenerator gen = new WsGenerator(options);
		
		gen.generate();
		*/
		
	}

}
