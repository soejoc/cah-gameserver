package session;

import io.netty.channel.ChannelHandlerContext;

public class PlayerSession extends Session {
    private String nickName;

    public PlayerSession(final ChannelHandlerContext channelHandlerContext) {
        super(channelHandlerContext);
    }

    public void startGame(final String nickName) {
        this.nickName = nickName;
    }
}
