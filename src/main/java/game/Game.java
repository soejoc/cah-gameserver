package game;

import protocol.object.response.StartGameResponse;
import protocol.object.response.WaitForGameResponse;
import session.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game {
    public static final int PLAYER_SIZE_FOR_GAME = 4;
    private static List<Game> games = new LinkedList<>();

    public static void register(final Player player) {
        Game game = (!games.isEmpty()) ? games.get(games.size() - 1) : null;

        if(game == null || game.getPlayerSize() == PLAYER_SIZE_FOR_GAME) {
            game = new Game();
            games.add(game);
        }

        game.addPlayer(player);
    }

    private List<Player> players = new ArrayList<>();

    private void addPlayer(final Player newPlayer) {
        players.add(newPlayer);

        if(getPlayerSize() == PLAYER_SIZE_FOR_GAME) {
            for(Player player : players) {
                final StartGameResponse startGameResponse = new StartGameResponse();
                startGameResponse.nickName = player.getNickName();

                player.say(startGameResponse);
            }
        } else {
            final WaitForGameResponse waitForGameResponse = new WaitForGameResponse();

            newPlayer.say(waitForGameResponse);
        }
    }

    public int getPlayerSize() {
        return players.size();
    }
}
