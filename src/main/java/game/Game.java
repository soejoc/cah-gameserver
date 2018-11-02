package game;

import protocol.object.message.response.StartGameResponse;
import protocol.object.message.response.WaitForGameResponse;
import protocol.object.model.PlayerModel;
import session.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    public static final int PLAYER_SIZE_FOR_GAME = 4;
    private static List<Game> games = new LinkedList<>();

    public static Game register(final Player player) {
        Game game = (!games.isEmpty()) ? games.get(games.size() - 1) : null;

        if(game == null || game.getPlayerSize() == PLAYER_SIZE_FOR_GAME) {
            game = new Game();
            games.add(game);
        }

        game.addPlayer(player);

        return game;
    }

    private List<Player> players = new ArrayList<>();

    private void addPlayer(final Player newPlayer) {
        players.add(newPlayer);

        if(getPlayerSize() == PLAYER_SIZE_FOR_GAME) {
            startGame();
        } else {
            final WaitForGameResponse waitForGameResponse = new WaitForGameResponse();

            newPlayer.say(waitForGameResponse);
        }
    }

    private void startGame() {
        for(final Player player : players) {
            final StartGameResponse startGameResponse = new StartGameResponse();

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

    public int getPlayerSize() {
        return players.size();
    }
}
