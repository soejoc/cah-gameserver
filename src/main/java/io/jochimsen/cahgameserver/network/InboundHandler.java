package io.jochimsen.cahgameserver.network;

import io.jochimsen.cahframework.handler.inbound.InboundHandlerBase;
import io.jochimsen.cahframework.protocol.object.message.MessageCode;
import io.jochimsen.cahframework.protocol.object.message.ProtocolMessage;
import io.jochimsen.cahframework.protocol.object.message.RequestMessage;
import io.jochimsen.cahframework.protocol.object.message.error.ErrorMessage;
import io.jochimsen.cahframework.protocol.object.message.request.RestartGameRequest;
import io.jochimsen.cahframework.protocol.object.message.request.StartGameRequest;
import io.jochimsen.cahframework.protocol.object.message.response.FinishedGameResponse;
import io.jochimsen.cahframework.session.Session;
import io.jochimsen.cahframework.util.ProtocolInputStream;
import io.jochimsen.cahgameserver.message_handler.RestartGameHandler;
import io.jochimsen.cahgameserver.message_handler.ServerMessageHandler;
import io.jochimsen.cahgameserver.message_handler.StartGameHandler;
import io.jochimsen.cahgameserver.model.Game;
import io.jochimsen.cahgameserver.model.Player;
import io.jochimsen.cahgameserver.repository.BlackCardRepository;
import io.jochimsen.cahgameserver.repository.GameRepository;
import io.jochimsen.cahgameserver.repository.PlayerRepository;
import io.jochimsen.cahgameserver.repository.WhiteCardRepository;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

@Service
@ChannelHandler.Sharable
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class InboundHandler extends InboundHandlerBase {
    private static final Logger logger = LoggerFactory.getLogger(InboundHandler.class);

    private WhiteCardRepository whiteCardRepository;
    private BlackCardRepository blackCardRepository;
    private GameRepository gameRepository;
    private PlayerRepository playerRepository;

    @Override
    protected Session getSession(final ChannelHandlerContext ctx) {
        return playerRepository.getOrCreate(ctx);
    }

    @Override
    protected void handleMessage(final int messageId, final ProtocolInputStream protocolInputStream, final Session session) throws Exception {
        logger.debug("Message ({}) received from: {}", messageId, ((InetSocketAddress)session.getChannelHandlerContext().channel().remoteAddress()).getAddress().getHostAddress());

        final Player player = (Player)session;
        ServerMessageHandler messageHandler = null;
        RequestMessage protocolMessage = null;

        switch (messageId) {
            case MessageCode.START_GAME_RQ: {
                protocolMessage = protocolInputStream.readObject(StartGameRequest.class);
                messageHandler = new StartGameHandler();
                break;
            }

            case MessageCode.RESTART_GAME_RQ: {
                protocolMessage = protocolInputStream.readObject(RestartGameRequest.class);
                messageHandler = new RestartGameHandler();
                break;
            }

            default: {
                logger.info("Unknown message ({}) received from: {}", messageId, ((InetSocketAddress)session.getChannelHandlerContext().channel().remoteAddress()).getAddress().getHostAddress());
                closeSession(session);
            }
        }

        if(messageHandler != null) {
            //noinspection unchecked
            messageHandler.handleMessage(protocolMessage, player);
        }
    }

    @Override
    protected void onErrorReceived(final ErrorMessage errorMessage, final Session session) {

    }

    @Override
    protected void closeSession(final Session session) {
        final Player player = (Player)session;

        playerRepository.removePlayer(player);
        super.closeSession(session);
    }

    @Override
    protected void onUncaughtException(final Session session, final Throwable throwable) {

    }
}
