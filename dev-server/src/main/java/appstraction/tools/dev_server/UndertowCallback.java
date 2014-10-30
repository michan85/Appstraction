package appstraction.tools.dev_server;

import java.io.PrintWriter;
import java.io.StringWriter;

import appstraction.app.Callback;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;


public abstract class UndertowCallback<T> implements Callback<T>{

	private HttpServerExchange exchange;
	boolean hasExecuted = false;
	public UndertowCallback(HttpServerExchange exchange){
		
		this.exchange = exchange;
	}
	public void waitForResponse() {
		if(hasExecuted){
			// do nothing
		}
		else
			exchange.dispatch();
	}
	public final void onResult(final T result) {
		hasExecuted = true;
		 if (exchange.isInIoThread()) {
			 onResult(exchange, result);
	    }
		 
		 exchange.dispatch(new Runnable() {
			
			public void run() {
				onResult(exchange, result);
			}
		});
	}

	public abstract void onResult(HttpServerExchange exchange2, T result);
	public final void onError(final Throwable error) {
		hasExecuted = true;
		 if (exchange.isInIoThread()) {
			 onError(exchange, error);
	    }
		 
		 exchange.dispatch(new Runnable() {
			
			public void run() {
				onError(exchange, error);
			}
		});
	}
	public void onError(HttpServerExchange exchange, Throwable error){
		sendError(exchange,error);
	}
	
	public static void sendError(HttpServerExchange exchange, Throwable e) {
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		exchange.getResponseSender().send("<h1 style=\"color:red\">"+e.getMessage()+"</h1>"+sw.toString());
	}
	
}