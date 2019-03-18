package io.jochimsen.cahgameserver.model;

import io.jochimsen.collo.protocol.ResponseMessage;
import io.jochimsen.collo.session.Session;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class Player extends Session<ResponseMessage> {

    @Getter
    private UUID playerId = UUID.randomUUID();

    @Getter
    @Setter
    private String nickName;

    @Getter
    @Setter
    private Game currentGame;

    @Getter
    @Setter
    private UUID sessionId;

    public Player(final ChannelHandlerContext channelHandlerContext) {
        super(channelHandlerContext);
    }
}
