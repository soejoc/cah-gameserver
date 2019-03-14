package io.jochimsen.cahgameserver.message_handler;

import io.jochimsen.cahframework.handler.message.MessageHandler;
import io.jochimsen.cahframework.protocol.object.message.RequestMessage;
import io.jochimsen.cahgameserver.model.Player;

public abstract class ServerMessageHandler<M extends RequestMessage> implements MessageHandler<M, Player> {
}
