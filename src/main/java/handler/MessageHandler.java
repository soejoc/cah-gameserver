package handler;

import channel_handler.ProcessingHandler;
import io.netty.channel.ChannelHandlerContext;
import protocol.MessageCode;
import protocol.object.error.ErrorObject;
import protocol.object.request.StartGameRequest;
import session.PlayerSession;
import session.Session;
import util.ProtocolInputStream;

public class MessageHandler extends ProcessingHandler implements RequestMessages {

    @Override
    protected Session getSession(final ChannelHandlerContext ctx) {
        return PlayerSession.getOrCreate(ctx);
    }

    @Override
    protected void handleMessage(final int messageId, final ProtocolInputStream rawMessage, final Session session) {
        final PlayerSession playerSession = (PlayerSession)session;

        switch (messageId) {
            case MessageCode.START_GAME_RQ: {
                final StartGameRequest startGameRequest = new StartGameRequest();
                startGameRequest.fromStream(rawMessage);

                OnStartGame(startGameRequest, playerSession);
            }
        }
    }

    @Override
    protected void onErrorReceived(final ErrorObject errorObject) {

    }

    @Override
    public void OnStartGame(final StartGameRequest startGameRequest, final PlayerSession session) {
        session.startGame(startGameRequest.nickName);
    }
}
