package dnn.auth;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import dnn.user.Token;

/** 令牌握手拦截器 */
public class TokenHandshakeInterceptor implements HandshakeInterceptor {

	/** 令牌服务 */
	private TokenService tokenService;

	public TokenHandshakeInterceptor(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
		if (!(request instanceof ServletServerHttpRequest)) { return false; }
		HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
		String id = req.getParameter("id");
		String tokenText = req.getParameter("token");
		if (!validAuth(id, tokenText)) { return false; }
		attributes.put("id", id);
		attributes.put("token", tokenText);
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) { }

	/**
	 * 授权是否有效
	 * @param	id				ID
	 * @param	tokenText		令牌文本
	 * @return	真，如果授权有效
	 */
	private boolean validAuth(String id, String tokenText) {
		if (id == null || tokenText == null) { return false; }
		Token token = tokenService.check(tokenText);
		if (token == null) { return false; }
		return id.equals(String.valueOf(token.getDpkid()));
	}

}
