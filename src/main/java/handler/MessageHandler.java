package handler;

import channel_handler.ProcessingHandler;
import io.netty.channel.ChannelHandlerContext;
import protocol.Message;
import protocol.object.request.StartGameRequest;
import session.PlayerSession;
import session.Session;
import throwable.exception.InvalidInputStreamException;
import util.ProtocolInputStream;

public class MessageHandler extends ProcessingHandler implements RequestMessages {
    @Override
    protected Session getOrCreateSession(ChannelHandlerContext ctx) {
        return new PlayerSession(ctx);
    }

    @Override
    protected void handleMessage(int messageId, ProtocolInputStream rawMessage, Session session) throws InvalidInputStreamException {
        PlayerSession playerSession = (PlayerSession)session;

        switch (messageId) {
            case Message.START_GAME_RQ: {
                StartGameRequest startGameRequest = new StartGameRequest();
                startGameRequest.fromStream(rawMessage);

                OnStartGame(startGameRequest, playerSession);
            }
        }
    }

    @Override
    public void OnStartGame(StartGameRequest startGameRequest, PlayerSession session) {
        session.startGame(startGameRequest.nickName);
    }
}
