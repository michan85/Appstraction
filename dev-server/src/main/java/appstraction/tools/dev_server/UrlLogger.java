package appstraction.tools.dev_server;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class UrlLogger implements HttpHandler {
	
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		ObjectMapper mapper= new ObjectMapper();
		SimpleModule m = new SimpleModule();
		m.addSerializer(HttpString.class, new StdScalarSerializer<HttpString>( HttpString.class){

			@Override
			public void serialize(HttpString str, JsonGenerator gen, SerializerProvider arg2)
					throws IOException, JsonGenerationException {
				
				gen.writeString(str.toString());
			
			}
		});
		mapper.registerModule(m);
		 exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/json");
		 Map<String,Object> map  = new HashMap<String, Object>();
		 
		 map.put("class",exchange.getClass().getName());
		 map.put("destinationAddress",exchange.getDestinationAddress());
		 map.put("hostName",exchange.getHostName());
		 map.put("hostAndPort",exchange.getHostAndPort());
		 map.put("protocol",exchange.getProtocol());
		 map.put("queryString",exchange.getQueryString());
		 map.put("queryStringParams",exchange.getQueryParameters());
		 map.put("pathParams",exchange.getPathParameters());
		 map.put("relativePath",exchange.getRelativePath());
		 map.put("requestCookies",exchange.getRequestCookies());
		 map.put("requestHeaders",exchange.getRequestHeaders());
		 map.put("requestMethod",exchange.getRequestMethod());
		 map.put("requestPath",exchange.getRequestPath());
		 map.put("requestScheme",exchange.getRequestScheme());
		 map.put("requestURI",exchange.getRequestURI());
		 map.put("requestURL",exchange.getRequestURL());
		 map.put("resolvedPAth",exchange.getResolvedPath());
		 map.put("responseHeaders",exchange.getResponseHeaders());
		 map.put("responseCookies",exchange.getResponseCookies());
		 map.put("sourceAddress",exchange.getSourceAddress());
		 exchange.getResponseSender().send(mapper.writeValueAsString(map));
	}
}