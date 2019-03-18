package io.jochimsen.cahgameserver.message_handler;

import io.jochimsen.cahgameserver.model.Player;
import io.jochimsen.collo.message.MessageHandler;
import io.jochimsen.collo.protocol.RequestMessage;

public abstract class ServerMessageHandler<M extends RequestMessage> implements MessageHandler<M, Player> {
}
