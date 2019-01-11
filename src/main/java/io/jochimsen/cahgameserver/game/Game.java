package io.jochimsen.cahgameserver.game;

import io.jochimsen.cahframework.protocol.object.message.response.start_game.StartGameResponse;
import io.jochimsen.cahframework.protocol.object.model.player.PlayerProtocolModel;

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

        startGameResponse.players = players.stream()
                .map(p -> {
                    final PlayerProtocolModel playerProtocolModel = new PlayerProtocolModel();
                    playerProtocolModel.nickName = p.getNickName();
                    playerProtocolModel.playerId = p.getPlayerId();

                    return playerProtocolModel;
                }).collect(Collectors.toList());

        player.say(startGameResponse);
    }
}
