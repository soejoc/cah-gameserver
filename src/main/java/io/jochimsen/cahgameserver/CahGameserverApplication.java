package io.jochimsen.cahgameserver;

import io.jochimsen.cahframework.initializer.SslServerInitializer;
import io.jochimsen.cahgameserver.config.ServerProperties;
import io.jochimsen.cahgameserver.netty.MessageHandler;
import io.jochimsen.cahgameserver.netty.TCPServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;

@SpringBootApplication
@EnableConfigurationProperties(ServerProperties.class)
public class CahGameserverApplication {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(CahGameserverApplication.class, args);

		TCPServer tcpServer = context.getBean(TCPServer.class);
		tcpServer.start();
	}

	@Autowired
	private ServerProperties serverProperties;

	@Bean(name = "serverBootstrap")
	public ServerBootstrap bootstrap(final ChannelHandler channelHandler) {
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup(), workerGroup())
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.DEBUG))
				.childHandler(channelHandler)
				.option(ChannelOption.SO_BACKLOG, serverProperties.getBacklog())
				.childOption(ChannelOption.SO_KEEPALIVE, serverProperties.isKeepAlive());

		return b;
	}

	@Bean(destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup bossGroup() {
		return new NioEventLoopGroup(serverProperties.getBossCount());
	}

	@Bean(destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup workerGroup() {
		return new NioEventLoopGroup(serverProperties.getWorkerCount());
	}

	@Bean
	public InetSocketAddress tcpSocketAddress() {
		return new InetSocketAddress(serverProperties.getPort());
	}

	@Bean
	public ChannelHandler channelHandler(final MessageHandler messageHandler) throws Exception {
		final SelfSignedCertificate ssc = new SelfSignedCertificate();
		final SslContext sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey())
				.build();

		//ToDO: Appropriate exception handling

		return new SslServerInitializer(sslCtx, messageHandler);
	}
}

