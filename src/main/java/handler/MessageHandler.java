package handler;

import channel_handler.ProcessingHandler;
import game.Game;
import io.netty.channel.ChannelHandlerContext;
import protocol.object.message.MessageCode;
import protocol.object.message.error.ErrorObject;
import protocol.object.message.request.RestartGameRequest;
import protocol.object.message.request.StartGameRequest;
import protocol.object.message.response.FinishedGameResponse;
import session.Player;
import session.Session;
import util.ProtocolInputStream;

public class MessageHandler extends ProcessingHandler {

    @Override
    protected Session getSession(final ChannelHandlerContext ctx) {
        return Player.getOrCreate(ctx);
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

    private void onStartGame(final Player player, final StartGameRequest startGameRequest) {
        if(player.getCurrentGame() == null) {
            player.setNickName(startGameRequest.nickName);
            Game.register(player);
        }
    }

    private void onRestartGame(final Player player, final RestartGameRequest restartGameRequest) {
        final Player activePlayer = Player.getPlayerBySessionId(restartGameRequest.sessionKey);

        if(activePlayer != null) {
            Player.reassignPlayer(activePlayer, player);

            final Game currentGame = activePlayer.getCurrentGame();
            currentGame.startGame(activePlayer);
        } else {
            final FinishedGameResponse finishedGameResponse = new FinishedGameResponse();
            player.say(finishedGameResponse);
        }
    }
}
