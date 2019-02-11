package io.jochimsen.cahgameserver.game;

import io.jochimsen.cahframework.session.Session;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class Player extends Session {

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
