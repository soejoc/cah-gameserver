package io.jochimsen.cahgameserver.game;

import io.jochimsen.cahframework.session.Session;
import io.jochimsen.cahgameserver.game.card.WhiteCard;
import io.netty.channel.ChannelHandlerContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Player extends Session {

    private UUID playerId = UUID.randomUUID();
    private String nickName;
    private Game currentGame;
    private UUID sessionId;
    private final List<WhiteCard> whiteCards = new ArrayList<>();
    private List<WhiteCard> selectedCards;

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

    public void addWhiteCard(final WhiteCard whiteCard) {
        whiteCards.add(whiteCard);
    }
    public void addWhiteCard(final List<WhiteCard> whiteCards) {
        this.whiteCards.addAll(whiteCards);
    }

    public void removeWhiteCard(final WhiteCard whiteCard) {
        whiteCards.remove(whiteCard);
    }

    public void removeWhiteCard(final List<WhiteCard> whiteCards) {
        this.whiteCards.removeAll(whiteCards);
    }

    public boolean hasWhiteCard(final WhiteCard whiteCard) {
        return whiteCards.contains(whiteCard);
    }

    public void selectCards(final List<WhiteCard> selectedCards) {
        for(final WhiteCard whiteCard : selectedCards) {
            if(!hasWhiteCard(whiteCard)) {
                return;
            }
        }

        this.selectedCards = selectedCards;
        currentGame.playerSelectedWhiteCards(this);
    }

    public List<WhiteCard> getSelectedCards() {
        return selectedCards;
    }
}
