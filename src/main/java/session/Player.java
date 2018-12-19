package session;

import game.Game;
import io.netty.channel.ChannelHandlerContext;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Player extends Session {
    private static final Map<ChannelHandlerContext, Player> sessionMap = new ConcurrentHashMap<>();

    public static Player getOrCreate(final ChannelHandlerContext channelHandlerContext) {
        Player player = sessionMap.get(channelHandlerContext);

        if(player == null) {
            player = new Player(channelHandlerContext);
            sessionMap.put(channelHandlerContext, player);
        }

        return player;
    }

    public static Player getPlayerBySessionId(final UUID sessionId) {
        final Collection<Player> playerCollection = sessionMap.values();

        for(final Player player : playerCollection) {
            if(sessionId.equals(player.getSessionId())) {
                return player;
            }
        }

        return null;
    }

    public static void reassignPlayer(final Player activePlayer, final Player newPlayer) {
        sessionMap.remove(activePlayer.channelHandlerContext);

        activePlayer.channelHandlerContext = newPlayer.channelHandlerContext;
        sessionMap.put(activePlayer.channelHandlerContext, activePlayer);
    }

    private UUID playerId = UUID.randomUUID();
    private String nickName;
    private Game currentGame;
    private UUID sessionId;

    private Player(final ChannelHandlerContext channelHandlerContext) {
        super(channelHandlerContext);
    }

    @Override
    public void close() {
        sessionMap.remove(channelHandlerContext);

        super.close();
    }

    public void setNickName(final String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setSessionId(final UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(final Game currentGame) {
        this.currentGame = currentGame;
    }
}
