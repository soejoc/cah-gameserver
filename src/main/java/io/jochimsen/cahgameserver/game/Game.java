package io.jochimsen.cahgameserver.game;

import io.jochimsen.cahframework.protocol.object.message.response.StartGameResponse;
import io.jochimsen.cahframework.protocol.object.message.response.WaitForGameResponse;
import io.jochimsen.cahframework.protocol.object.model.PlayerModel;
import io.jochimsen.cahgameserver.session.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private static final int PLAYER_SIZE_FOR_GAME = 4;
    private static final List<Game> games = new LinkedList<>();
    private static final Queue<Player> playerQueue = new LinkedList<>();

    public static synchronized void register(final Player player) {
        playerQueue.add(player);

        if(playerQueue.size() == PLAYER_SIZE_FOR_GAME) {
            final Game game = new Game();

            while (!playerQueue.isEmpty()) {
                final Player queuedPlayer = playerQueue.remove();
                game.addPlayer(queuedPlayer);
                queuedPlayer.setCurrentGame(game);
            }

            game.startGameForAll();
            games.add(game);
        } else {
            final WaitForGameResponse waitForGameResponse = new WaitForGameResponse();
            player.say(waitForGameResponse);
        }
    }

    private List<Player> players = new ArrayList<>();

    private void addPlayer(final Player player) {
        players.add(player);
    }

    private void startGameForAll() {
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
        startGameResponse.antagonists = players.stream()
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
