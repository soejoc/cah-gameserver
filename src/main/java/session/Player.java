package session;

import game.Game;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    private UUID playerId = UUID.randomUUID();
    private String nickName;
    private Game currentGame;

    private Player(final ChannelHandlerContext channelHandlerContext) {
        super(channelHandlerContext);
    }

    @Override
    public void close() {
        sessionMap.remove(channelHandlerContext);

        super.close();
    }

    public void startGame(final String nickName) {
        if(currentGame == null) {
            return;
        }

        this.nickName = nickName;

        currentGame = Game.register(this);
    }

    public String getNickName() {
        return nickName;
    }

    public UUID getPlayerId() {
        return playerId;
    }
}
