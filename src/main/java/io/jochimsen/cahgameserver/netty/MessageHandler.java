package io.jochimsen.cahgameserver.netty;

import io.jochimsen.cahframework.handler.inbound.InboundMessageHandlerBase;
import io.jochimsen.cahframework.protocol.object.message.MessageCode;
import io.jochimsen.cahframework.protocol.object.message.error.ErrorMessage;
import io.jochimsen.cahframework.protocol.object.message.request.RestartGameRequest;
import io.jochimsen.cahframework.protocol.object.message.request.StartGameRequest;
import io.jochimsen.cahframework.protocol.object.message.response.FinishedGameResponse;
import io.jochimsen.cahframework.session.Session;
import io.jochimsen.cahframework.util.ProtocolInputStream;
import io.jochimsen.cahgameserver.game.Game;
import io.jochimsen.cahgameserver.game.Player;
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

import java.net.InetSocketAddress;

@Component
@ChannelHandler.Sharable
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class MessageHandler extends InboundMessageHandlerBase {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

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

        switch (messageId) {
            case MessageCode.START_GAME_RQ: {
                final StartGameRequest startGameRequest = protocolInputStream.readObject();

                onStartGame(player, startGameRequest);
                break;
            }

            case MessageCode.RESTART_GAME_RQ: {
                final RestartGameRequest restartGameRequest = protocolInputStream.readObject();

                onRestartGame(player, restartGameRequest);
                break;
            }

            default: {
                logger.info("Unknown message ({}) received from: {}", messageId, ((InetSocketAddress)session.getChannelHandlerContext().channel().remoteAddress()).getAddress().getHostAddress());
                closeSession(session);
            }
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

    private void onStartGame(final Player player, final StartGameRequest startGameRequest) {
        if(player.getCurrentGame() == null) {
            player.setNickName(startGameRequest.getNickName());
            gameRepository.register(player);
        }
    }

    private void onRestartGame(final Player player, final RestartGameRequest restartGameRequest) {
        final Player activePlayer = playerRepository.getPlayerBySessionId(restartGameRequest.getSessionKey());

        if(activePlayer != null) {
            playerRepository.reassignPlayer(activePlayer, player);

            final Game currentGame = activePlayer.getCurrentGame();
            currentGame.startGame(activePlayer);
        } else {
            final FinishedGameResponse finishedGameResponse = new FinishedGameResponse();
            player.say(finishedGameResponse);
        }
    }

    @Override
    protected void onUncaughtException(final Session session, final Throwable throwable) {

    }
}
