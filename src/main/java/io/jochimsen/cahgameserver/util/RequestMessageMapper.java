package io.jochimsen.cahgameserver.util;

import io.jochimsen.cahframework.protocol.object.message.MessageCode;
import io.jochimsen.cahframework.protocol.object.message.RequestMessage;
import io.jochimsen.cahframework.protocol.object.message.request.RestartGameRequest;
import io.jochimsen.cahframework.protocol.object.message.request.StartGameRequest;
import io.jochimsen.cahframework.util.MessageMapper;
import io.jochimsen.cahgameserver.message_handler.RestartGameHandler;
import io.jochimsen.cahgameserver.message_handler.StartGameHandler;
import io.jochimsen.cahgameserver.model.Player;
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
