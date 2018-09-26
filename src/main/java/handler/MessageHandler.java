package handler;

import protocol.object.request.StartGameRequest;
import session.PlayerSession;

public class MessageHandler extends ProcessingHandler {
    @Override
    public void OnStartGame(StartGameRequest startGameRequest, PlayerSession session) {
        session.startGame(startGameRequest.nickName);
    }
}
