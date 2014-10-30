package appstraction.tools.dev_server;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.StreamSourceFrameChannel;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.spi.WebSocketHttpExchange;

public class WebSocketApiHandler implements WebSocketConnectionCallback {

	PathHandler handler = new PathHandler();
	
	
	
	static ObjectMapper mapper = new ObjectMapper();
	
	Map<String, CommanddHandler> commandHandlers = new HashMap<String, CommanddHandler>();
	
	public WebSocketApiHandler(){
		commandHandlers.put("echo",new CommanddHandler() {
			
			public void handleCommand(JsonNode cmd, WebSocketChannel socket) {
				for(WebSocketChannel s: clients){
					WebSockets.sendText(Responses.message(cmd.get("cmd").asText(), cmd.get("msg").asText()), s, null );
				}
			}
		});
	}
	
	List<WebSocketChannel> clients = Collections.synchronizedList(new ArrayList<WebSocketChannel>());
			
	public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {

		clients.add(channel);
		channel.getReceiveSetter().set(new AbstractReceiveListener() {

			
			
			@Override
			protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) {
				final String messageData = message.getData();
				String cmd = "";
				try {
					JsonNode msg = mapper.readTree(messageData);
					 cmd = msg.get("cmd").asText();
					CommanddHandler cmdHandler = commandHandlers.get(cmd);
					if(cmdHandler != null)
						cmdHandler.handleCommand(msg, channel);
					else{
						WebSockets.sendText(messageData, channel, null );
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					WebSockets.sendText(Errors.error( e, cmd ).toString(), channel, null );
				}
				
				
			}
			@Override
			protected void onClose(WebSocketChannel webSocketChannel, StreamSourceFrameChannel channel)
					throws IOException {
				clients.remove(webSocketChannel);
				super.onClose(webSocketChannel, channel);
			}
		});
		
		channel.resumeReceives();

	}
	
	
	public static class Errors{

		private static final String CMD_ERROR_MSG_UNKNOWN_ERROR = "{\"cmd\":\"error\", \"msg\":\"unknown error\"}";
		public static String error(Exception e, String cmd) {
			try {
				Map<String, String> resp = new HashMap<String, String>();
				resp.put("cmd", "error");
				resp.put("msg", e.getMessage());
				resp.put("srcCmd", cmd);
				return mapper.writeValueAsString(resp);
			} catch (Exception ex) {
				e.printStackTrace();
				ex.printStackTrace();
				return CMD_ERROR_MSG_UNKNOWN_ERROR;
			}
		}
		public static String error(String msg) {
			
			try {
				Map<String, String> resp = new HashMap<String, String>();
				resp.put("cmd", "error");
				resp.put("msg", msg);
				return mapper.writeValueAsString(resp);
			} catch (Exception e) {
				e.printStackTrace();
				return CMD_ERROR_MSG_UNKNOWN_ERROR;
			}
		}
		
	}
	
	public static class Responses{
		public static String message( String cmd, Object o ){
			Map<String, Object> resp = new HashMap<String, Object>();
			resp.put("cmd", cmd);
			resp.put("msg", o);
			try {
				return mapper.writeValueAsString(resp);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return Errors.error(e, cmd);
			}
			
		}
	}
	

}
