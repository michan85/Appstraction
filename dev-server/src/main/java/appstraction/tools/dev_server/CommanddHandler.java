package appstraction.tools.dev_server;

import com.fasterxml.jackson.databind.JsonNode;

import io.undertow.websockets.core.WebSocketChannel;

public interface CommanddHandler {
	void handleCommand( JsonNode cmd, WebSocketChannel socket );
}
