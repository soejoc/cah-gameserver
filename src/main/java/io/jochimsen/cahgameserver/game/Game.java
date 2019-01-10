package io.jochimsen.cahgameserver.game;

import io.jochimsen.cahframework.protocol.object.message.response.StartGameResponse;
import io.jochimsen.cahframework.protocol.object.message.response.WaitForGameResponse;
import io.jochimsen.cahframework.protocol.object.model.PlayerModel;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private final List<Player> players = new ArrayList<>();

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
                .filter(p -> p != player)
                .map(p -> {
                    final PlayerModel playerModel = new PlayerModel();
                    playerModel.nickName = p.getNickName();
                    playerModel.playerId = p.getPlayerId();

                    return playerModel;
                }).collect(Collectors.toList());

        player.say(startGameResponse);
    }
}
