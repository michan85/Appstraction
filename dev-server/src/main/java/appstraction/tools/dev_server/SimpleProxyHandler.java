package appstraction.tools.dev_server;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;


public class SimpleProxyHandler implements HttpHandler {
	public SimpleProxyHandler(String baseUrl){
		this.baseUrl = baseUrl;
	}
	String baseUrl = "";
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		
		String url =  baseUrl+ exchange.getRelativePath()+ "?"+exchange.getQueryString();
		
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		for( HeaderValues header: exchange.getRequestHeaders() ){
			conn.addRequestProperty(header.getHeaderName().toString(), header.getFirst());	
		}
		
		if(exchange.getRequestMethod().equalToString("post")){
		
			pipe(exchange.getInputStream(),  conn.getOutputStream());
			
		}
		
		exchange.setResponseCode(conn.getResponseCode());
		for( Entry<String, List<String>> h : conn.getHeaderFields().entrySet()){
			//app.log.info("header: "+h.getKey() + "    value: "+h.getValue().get(0));
			if(h.getKey()!=null)
			exchange.getResponseHeaders().add(new HttpString(h.getKey()), h.getValue().get(0));
		}
		exchange.startBlocking();
		pipe( conn.getInputStream(), exchange.getOutputStream() );
		
		exchange.endExchange();
	}

	public void pipe(InputStream is, OutputStream os) throws IOException {
			int n;
		  byte[] buffer = new byte[1024];
		  while((n = is.read(buffer)) > -1) {
		    os.write(buffer, 0, n);   // Don't allow any extra bytes to creep in, final write
		  } 
		 os.close ();
	}
}