package io.jochimsen.cahgameserver.game;

import io.jochimsen.cahframework.protocol.object.message.response.StartGameResponse;
import io.jochimsen.cahframework.protocol.object.model.PlayerProtocolModel;

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
        final UUID sessionId = UUID.randomUUID();
        player.setSessionId(sessionId);

        final List<PlayerProtocolModel> playerProtocolModels = players.stream()
                .map(p -> new PlayerProtocolModel(p.getPlayerId(), p.getNickName()))
                .collect(Collectors.toList());

        final StartGameResponse startGameResponse = new StartGameResponse(playerProtocolModels, sessionId);
        player.say(startGameResponse);
    }
}
