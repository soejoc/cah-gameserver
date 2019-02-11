package io.jochimsen.cahgameserver;

import io.jochimsen.cahgameserver.config.ServerProperties;
import io.jochimsen.cahgameserver.netty.TCPServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(ServerProperties.class)
public class CahGameserverApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(CahGameserverApplication.class, args);

		TCPServer tcpServer = context.getBean(TCPServer.class);
		tcpServer.start();
	}
}

