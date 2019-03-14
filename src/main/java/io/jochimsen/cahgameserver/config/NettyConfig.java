package io.jochimsen.cahgameserver.config;

import io.jochimsen.cahframework.initializer.ProtocolMessageChannelInitializer;
import io.jochimsen.cahgameserver.network.InboundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class NettyConfig {
    private static final Logger logger = LoggerFactory.getLogger(NettyConfig.class);

    private final ServerProperties serverProperties;

    @Bean(name = "serverBootstrap")
    public ServerBootstrap bootstrap(final ChannelHandler channelHandler) {
        final ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
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
    public SslContext sslContext() {
        try {
            //ToDo: Replace with actual private key and certificate
            final SelfSignedCertificate ssc = new SelfSignedCertificate();
            final SslContext sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey())
                    .build();

            return sslCtx;
        } catch (final Exception e) {
            logger.error("Exception caught while creating SSLContext: {}", e);
        }

        return null;
    }

    @Bean
    public ChannelHandler channelHandler(final InboundHandler inboundHandler) {
        return new ProtocolMessageChannelInitializer(inboundHandler);
    }

    @Bean(name = "baseUrl")
    public String baserUrl() {
        return serverProperties.getWebserviceUrl();
    }
}
