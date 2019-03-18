package io.jochimsen.cahgameserver.repository;

import io.jochimsen.cahgameserver.model.Game;
import io.jochimsen.cahgameserver.model.Player;
import io.jochimsen.cahprotocol.message.response.WaitForGameResponse;
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

    public void register(final Player player) {
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
}
