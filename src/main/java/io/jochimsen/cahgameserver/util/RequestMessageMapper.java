package io.jochimsen.cahgameserver.util;

import io.jochimsen.cahgameserver.message_handler.RestartGameHandler;
import io.jochimsen.cahgameserver.message_handler.StartGameHandler;
import io.jochimsen.cahgameserver.model.Player;
import io.jochimsen.cahprotocol.message.MessageCode;
import io.jochimsen.cahprotocol.message.request.RestartGameRequest;
import io.jochimsen.cahprotocol.message.request.StartGameRequest;
import io.jochimsen.collo.message.MessageMapper;
import io.jochimsen.collo.protocol.RequestMessage;
import org.springframework.stereotype.Service;

@Service
public class RequestMessageMapper implements MessageMapper<RequestMessage, Player> {
    @Override
    public Pair<RequestMessage, Player> map(final int messageId) {
        switch (messageId) {
            case MessageCode.START_GAME_RQ: return new Pair<>(StartGameRequest.class, StartGameHandler.class);
            case MessageCode.RESTART_GAME_RQ: return new Pair<>(RestartGameRequest.class, RestartGameHandler.class);
            default: return null;
        }
    }
}
