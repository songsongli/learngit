package dnn.auth;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/** 身份握手处理器 */
public class PrincipalHandshakeHandler extends DefaultHandshakeHandler {

	@Override
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
		return new Principal() {
			@Override
			public String getName() { return (String) attributes.get("token"); }
		};
	}

}
