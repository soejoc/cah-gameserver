package io.jochimsen.cahgameserver.message_handler;

import io.jochimsen.cahgameserver.model.Game;
import io.jochimsen.cahgameserver.model.Player;
import io.jochimsen.cahgameserver.repository.PlayerRepository;
import io.jochimsen.cahprotocol.message.request.RestartGameRequest;
import io.jochimsen.cahprotocol.message.response.FinishedGameResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class RestartGameHandler extends ServerMessageHandler<RestartGameRequest> {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void handleMessage(final RestartGameRequest protocolMessage, final Player session) {
        final Player activePlayer = playerRepository.getPlayerBySessionId(protocolMessage.getSessionKey());

        if(activePlayer != null) {
            playerRepository.reassignPlayer(activePlayer, session);

            final Game currentGame = activePlayer.getCurrentGame();
            currentGame.startGame(activePlayer);
        } else {
            final FinishedGameResponse finishedGameResponse = new FinishedGameResponse();
            session.say(finishedGameResponse);
        }
    }
}
