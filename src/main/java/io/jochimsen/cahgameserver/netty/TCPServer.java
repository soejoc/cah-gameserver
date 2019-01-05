package io.jochimsen.cahgameserver.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Component
public class TCPServer {
    private static final Logger logger = LoggerFactory.getLogger(TCPServer.class);

    private final ServerBootstrap serverBootstrap;

    private final InetSocketAddress tcpPort;

    private Channel serverChannel;

    public TCPServer(final ServerBootstrap serverBootstrap, final InetSocketAddress tcpPort) {
        this.serverBootstrap = serverBootstrap;
        this.tcpPort = tcpPort;
    }

    public void start() {
        try {
            serverChannel = serverBootstrap.bind(tcpPort).sync().channel().closeFuture().sync().channel();
        } catch (InterruptedException e) {
            logger.error("Exception caught while server was running: {}", e);
        }
    }

    @PreDestroy
    public void stop() {
        if ( serverChannel != null ) {
            serverChannel.close();
            serverChannel.parent().close();
        }
    }
}
