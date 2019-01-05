package io.jochimsen.cahgameserver.netty;

import io.jochimsen.cahframework.channel_handler.SslServerProcessingHandler;
import io.jochimsen.cahframework.session.Session;
import io.jochimsen.cahgameserver.game.Game;
import io.jochimsen.cahgameserver.repository.BlackCardRepository;
import io.jochimsen.cahgameserver.repository.GameRepository;
import io.jochimsen.cahgameserver.repository.PlayerRepository;
import io.jochimsen.cahgameserver.repository.WhiteCardRepository;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.jochimsen.cahframework.protocol.object.message.MessageCode;
import io.jochimsen.cahframework.protocol.object.message.error.ErrorObject;
import io.jochimsen.cahframework.protocol.object.message.request.RestartGameRequest;
import io.jochimsen.cahframework.protocol.object.message.request.StartGameRequest;
import io.jochimsen.cahframework.protocol.object.message.response.FinishedGameResponse;
import io.jochimsen.cahgameserver.game.Player;
import io.jochimsen.cahframework.util.ProtocolInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class MessageHandler extends SslServerProcessingHandler {

    @Autowired
    private WhiteCardRepository whiteCardRepository;

    @Autowired
    private BlackCardRepository blackCardRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    protected Session getSession(final ChannelHandlerContext ctx) {
        return playerRepository.getOrCreate(ctx);
    }

    @Override
    protected void handleMessage(final int messageId, final ProtocolInputStream rawMessage, final Session session) {
        final Player player = (Player)session;

        switch (messageId) {
            case MessageCode.START_GAME_RQ: {
                final StartGameRequest startGameRequest = new StartGameRequest();
                startGameRequest.fromStream(rawMessage);

                onStartGame(player, startGameRequest);
                break;
            }

            case MessageCode.RESTART_GAME_RQ: {
                final RestartGameRequest restartGameRequest = new RestartGameRequest();
                restartGameRequest.fromStream(rawMessage);

                onRestartGame(player, restartGameRequest);
                break;
            }
        }
    }

    @Override
    protected void onErrorReceived(final ErrorObject errorObject, final Session session) {

    }

    @Override
    protected void closeSession(final Session session) {
        final Player player = (Player)session;

        playerRepository.removePlayer(player);
        super.closeSession(session);
    }

    private void onStartGame(final Player player, final StartGameRequest startGameRequest) {
        if(player.getCurrentGame() == null) {
            player.setNickName(startGameRequest.nickName);
            gameRepository.register(player);
        }
    }

    private void onRestartGame(final Player player, final RestartGameRequest restartGameRequest) {
        final Player activePlayer = playerRepository.getPlayerBySessionId(restartGameRequest.sessionKey);

        if(activePlayer != null) {
            playerRepository.reassignPlayer(activePlayer, player);

            final Game currentGame = activePlayer.getCurrentGame();
            currentGame.startGame(activePlayer);
        } else {
            final FinishedGameResponse finishedGameResponse = new FinishedGameResponse();
            player.say(finishedGameResponse);
        }
    }
}
