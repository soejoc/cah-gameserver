package io.jochimsen.cahgameserver.repository;

import io.jochimsen.cahgameserver.game.Player;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PlayerRepository {
    private final Map<ChannelHandlerContext, Player> sessionMap = new ConcurrentHashMap<>();

    public Player getOrCreate(final ChannelHandlerContext channelHandlerContext) {
        Player player = sessionMap.get(channelHandlerContext);

        if(player == null) {
            player = new Player(channelHandlerContext);
            sessionMap.put(channelHandlerContext, player);
        }

        return player;
    }

    public Player getPlayerBySessionId(final UUID sessionId) {
        final Collection<Player> playerCollection = sessionMap.values();

        for(final Player player : playerCollection) {
            if(sessionId.equals(player.getSessionId())) {
                return player;
            }
        }

        return null;
    }

    public void reassignPlayer(final Player activePlayer, final Player newPlayer) {
        sessionMap.remove(activePlayer.getChannelHandlerContext());

        activePlayer.setChannelHandlerContext(newPlayer.getChannelHandlerContext());
        sessionMap.put(activePlayer.getChannelHandlerContext(), activePlayer);
    }

    public void removePlayer(final Player player) {
        sessionMap.remove(player.getChannelHandlerContext());
    }
}
