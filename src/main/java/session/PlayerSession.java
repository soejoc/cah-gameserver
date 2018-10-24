package session;

import io.netty.channel.ChannelHandlerContext;
import protocol.error.exception.InvalidMessageException;

import java.util.HashMap;
import java.util.Map;

public class PlayerSession extends Session {
    private static Map<ChannelHandlerContext, PlayerSession> sessionMap = new HashMap<>();

    public static PlayerSession getOrCreate(final ChannelHandlerContext channelHandlerContext) {
        PlayerSession playerSession = sessionMap.get(channelHandlerContext);

        if(playerSession == null) {
            playerSession = new PlayerSession(channelHandlerContext);
            sessionMap.put(channelHandlerContext, playerSession);
        }

        return playerSession;
    }

    private String nickName;

    private PlayerSession(final ChannelHandlerContext channelHandlerContext) {
        super(channelHandlerContext);
    }

    @Override
    public void close() {
        sessionMap.remove(channelHandlerContext);

        super.close();
    }

    public void startGame(final String nickName) {
        this.nickName = nickName;
    }
}
