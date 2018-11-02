package handler;

import channel_handler.ProcessingHandler;
import io.netty.channel.ChannelHandlerContext;
import protocol.object.message.MessageCode;
import protocol.object.message.error.ErrorObject;
import protocol.object.message.request.StartGameRequest;
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

                player.startGame(startGameRequest.nickName);
                break;
            }
        }
    }

    @Override
    protected void onErrorReceived(final ErrorObject errorObject, final Session session) {

    }
}
