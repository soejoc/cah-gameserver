package handler;

import protocol.object.request.StartGameRequest;
import session.PlayerSession;

public interface RequestMessages {
    void OnStartGame(StartGameRequest startGameRequest, PlayerSession session);
}
