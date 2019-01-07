package io.jochimsen.cahgameserver.repository;

import io.jochimsen.cahframework.protocol.object.message.response.WaitForGameResponse;
import io.jochimsen.cahgameserver.game.Game;
import io.jochimsen.cahgameserver.game.Player;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Repository
public class GameRepository {
    private static final int PLAYER_SIZE_FOR_GAME = 2;

    private final List<Game> games = new LinkedList<>();
    private final Queue<Player> playerQueue = new ConcurrentLinkedQueue<>();

    public Game register(final Player player) {
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

            return game;
        } else {
            final WaitForGameResponse waitForGameResponse = new WaitForGameResponse();
            player.say(waitForGameResponse);

            return null;
        }
    }

    public void unregister(final Player player) {
        playerQueue.remove(player);
    }
}
