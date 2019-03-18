package io.jochimsen.cahgameserver.message_handler;

import io.jochimsen.cahgameserver.model.Player;
import io.jochimsen.cahgameserver.repository.GameRepository;
import io.jochimsen.cahprotocol.message.request.StartGameRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class StartGameHandler extends ServerMessageHandler<StartGameRequest> {

    @Autowired
    GameRepository gameRepository;

    @Override
    public void handleMessage(final StartGameRequest protocolMessage, final Player session) {
        if(session.getCurrentGame() == null) {
            session.setNickName(protocolMessage.getNickName());
            gameRepository.register(session);
        }
    }
}
