package handler;

import protocol.object.request.StartGameRequest;
import session.PlayerSession;

public interface RequestMessages {
    void OnStartGame(final StartGameRequest startGameRequest, final PlayerSession session);
}
