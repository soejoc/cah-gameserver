package io.jochimsen.cahgameserver.game;

import io.jochimsen.cahframework.session.Session;
import io.netty.channel.ChannelHandlerContext;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Player extends Session {

    private UUID playerId = UUID.randomUUID();
    private String nickName;
    private Game currentGame;
    private UUID sessionId;

    public Player(final ChannelHandlerContext channelHandlerContext) {
        super(channelHandlerContext);
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
