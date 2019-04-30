package dnn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import dnn.auth.PrincipalHandshakeHandler;
import dnn.auth.TokenHandshakeInterceptor;
import dnn.auth.TokenService;

/** WebSocket配置 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	/** 令牌服务 */
	@Autowired
	private TokenService tokenService;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("websocket")
			.addInterceptors(new TokenHandshakeInterceptor(tokenService))
			.setHandshakeHandler(new PrincipalHandshakeHandler())
			.setAllowedOrigins("*");//允许跨域
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();//
		taskScheduler.initialize();
		registry.setApplicationDestinationPrefixes("/carwashws")
			.enableSimpleBroker("/carwash")
			.setTaskScheduler(taskScheduler);
	}

}
