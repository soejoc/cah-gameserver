package session;

import game.Game;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

public class Player extends Session {
    private static Map<ChannelHandlerContext, Player> sessionMap = new HashMap<>();

    public static Player getOrCreate(final ChannelHandlerContext channelHandlerContext) {
        Player player = sessionMap.get(channelHandlerContext);

        if(player == null) {
            player = new Player(channelHandlerContext);
            sessionMap.put(channelHandlerContext, player);
        }

        return player;
    }

    private String nickName;

    private Player(final ChannelHandlerContext channelHandlerContext) {
        super(channelHandlerContext);
    }

    @Override
    public void close() {
        sessionMap.remove(channelHandlerContext);

        super.close();
    }

    public void startGame(final String nickName) {
        this.nickName = nickName;

        Game.register(this);
    }

    public String getNickName() {
        return nickName;
    }
}
