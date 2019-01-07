package io.jochimsen.cahgameserver.game;

import io.jochimsen.cahframework.protocol.object.message.response.SelectCardResponse;
import io.jochimsen.cahframework.protocol.object.message.response.StartGameResponse;
import io.jochimsen.cahframework.protocol.object.message.response.WaitForGameResponse;
import io.jochimsen.cahframework.protocol.object.model.PlayerModel;
import io.jochimsen.cahframework.protocol.object.model.WhiteCardModel;
import io.jochimsen.cahgameserver.game.card.BlackCard;
import io.jochimsen.cahgameserver.game.card.WhiteCard;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private final List<Player> players = new ArrayList<>();
    private final List<WhiteCard> usedWhiteCards = new ArrayList<>();
    private final List<BlackCard> usedBlackCards = new ArrayList<>();
    private final List<Player> playersSelectedCards = new ArrayList<>();
    private int round;

    public void addPlayer(final Player player) {
        players.add(player);
    }

    public void startGameForAll() {
        for(final Player player : players) {
            startGame(player);
        }
    }

    public void startGame(final Player player) {
        final StartGameResponse startGameResponse = new StartGameResponse();

        final UUID sessionId = UUID.randomUUID();
        player.setSessionId(sessionId);
        startGameResponse.sessionId = sessionId;

        final PlayerModel me = new PlayerModel();
        me.playerId = player.getPlayerId();
        me.nickName = player.getNickName();

        startGameResponse.me = me;
        startGameResponse.player = players.stream()
                .map(p -> {
                    final PlayerModel playerModel = new PlayerModel();
                    playerModel.nickName = p.getNickName();
                    playerModel.playerId = p.getPlayerId();

                    return playerModel;
                }).collect(Collectors.toList());

        player.say(startGameResponse);
    }

    public List<WhiteCard> getUsedWhiteCards() {
        return usedWhiteCards;
    }

    public List<BlackCard> getUsedBlackCards() {
        return usedBlackCards;
    }

    public void addUsedWhiteCards(final List<WhiteCard> whiteCards) {
        usedWhiteCards.addAll(whiteCards);
    }

    public void addUsedBlackCard(final BlackCard blackCard) {
        usedBlackCards.add(blackCard);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getGameMaster() {
        return players.get(round % players.size());
    }

    public void incrementRound() {
        ++round;
    }

    public boolean isPlayer(final Player player) {
        return players.contains(player);
    }

    public void playerSelectedWhiteCards(final Player player) {
        if(isPlayer(player)) {
            playersSelectedCards.add(player);

            if(playersSelectedCards.size() == players.size() - 1) {
                final List<WhiteCard> selectedCards = new ArrayList<>();

                for(final Player playerSelectedWhiteCards : playersSelectedCards) {
                    selectedCards.addAll(playerSelectedWhiteCards.getSelectedCards());
                }

                final Player gameMaster = getGameMaster();

                final SelectCardResponse selectCardResponse = new SelectCardResponse();
                selectCardResponse.whiteCardModelList = selectedCards.stream()
                        .map(whiteCard -> {
                            final WhiteCardModel whiteCardModel = new WhiteCardModel();
                            whiteCardModel.whiteCardId = whiteCard.getWhiteCardId();

                            return whiteCardModel;
                        }).collect(Collectors.toList());

                gameMaster.say(selectCardResponse);
            }
        }
    }
}
