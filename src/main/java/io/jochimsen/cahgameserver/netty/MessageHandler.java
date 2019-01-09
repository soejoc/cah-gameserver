package io.jochimsen.cahgameserver.netty;

import io.jochimsen.cahframework.channel_handler.SslServerProcessingHandler;
import io.jochimsen.cahframework.protocol.object.message.request.SelectCardsRequest;
import io.jochimsen.cahframework.protocol.object.message.request.SelectWinnerRequest;
import io.jochimsen.cahframework.protocol.object.message.response.GameMasterResponse;
import io.jochimsen.cahframework.protocol.object.message.response.NewRoundResponse;
import io.jochimsen.cahframework.protocol.object.model.BlackCardModel;
import io.jochimsen.cahframework.protocol.object.model.WhiteCardModel;
import io.jochimsen.cahframework.session.Session;
import io.jochimsen.cahgameserver.game.Game;
import io.jochimsen.cahgameserver.game.card.BlackCard;
import io.jochimsen.cahgameserver.game.card.WhiteCard;
import io.jochimsen.cahgameserver.repository.BlackCardRepository;
import io.jochimsen.cahgameserver.repository.GameRepository;
import io.jochimsen.cahgameserver.repository.PlayerRepository;
import io.jochimsen.cahgameserver.repository.WhiteCardRepository;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.jochimsen.cahframework.protocol.object.message.MessageCode;
import io.jochimsen.cahframework.protocol.object.message.error.ErrorObject;
import io.jochimsen.cahframework.protocol.object.message.request.RestartGameRequest;
import io.jochimsen.cahframework.protocol.object.message.request.StartGameRequest;
import io.jochimsen.cahframework.protocol.object.message.response.FinishedGameResponse;
import io.jochimsen.cahgameserver.game.Player;
import io.jochimsen.cahframework.util.ProtocolInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@ChannelHandler.Sharable
public class MessageHandler extends SslServerProcessingHandler {

    @Autowired
    private WhiteCardRepository whiteCardRepository;

    @Autowired
    private BlackCardRepository blackCardRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    protected Session getSession(final ChannelHandlerContext ctx) {
        return playerRepository.getOrCreate(ctx);
    }

    @Override
    protected void handleMessage(final int messageId, final ProtocolInputStream rawMessage, final Session session) {
        final Player player = (Player)session;

        switch (messageId) {
            case MessageCode.START_GAME_RQ: {
                final StartGameRequest startGameRequest = new StartGameRequest();
                startGameRequest.fromStream(rawMessage);

                onStartGame(player, startGameRequest);
                break;
            }

            case MessageCode.RESTART_GAME_RQ: {
                final RestartGameRequest restartGameRequest = new RestartGameRequest();
                restartGameRequest.fromStream(rawMessage);

                onRestartGame(player, restartGameRequest);
                break;
            }

            case MessageCode.SELECT_CARDS_RQ: {
                final SelectCardsRequest selectCardsRequest = new SelectCardsRequest();
                selectCardsRequest.fromStream(rawMessage);

                onSelectCards(player, selectCardsRequest);
                break;
            }

            case MessageCode.SELECT_WINNER_RQ: {
                final SelectWinnerRequest selectWinnerRequest = new SelectWinnerRequest();
                selectWinnerRequest.fromStream(rawMessage);

                onSelectWinner(player, selectWinnerRequest);
                break;
            }
        }
    }

    @Override
    protected void onErrorReceived(final ErrorObject errorObject, final Session session) {

    }

    @Override
    protected void closeSession(final Session session) {
        final Player player = (Player)session;

        playerRepository.removePlayer(player);
        gameRepository.unregister(player);

        super.closeSession(session);
    }

    private void onStartGame(final Player player, final StartGameRequest startGameRequest) {
        if(player.getCurrentGame() == null) {
            player.setNickName(startGameRequest.nickName);
            final Game game = gameRepository.register(player);

            if(game != null) {
               newRound(game);
            }
        }
    }

    private void newRound(final Game game) {
        final List<Player> players = game.getPlayers();
        final BlackCard blackCard = blackCardRepository.getCards(game.getUsedBlackCards(), 1).get(0);
        game.addUsedBlackCard(blackCard);

        for(final Player playerGame : players) {
            final List<WhiteCard> whiteCards = whiteCardRepository.get(game.getUsedWhiteCards(), 3 - playerGame.getWhiteCardCount());
            game.addUsedWhiteCards(whiteCards);
            playerGame.addWhiteCard(whiteCards);

            final NewRoundResponse newRoundResponse = new NewRoundResponse();
            newRoundResponse.whiteCardModels = whiteCards.stream()
                    .map(whiteCard -> {
                        final WhiteCardModel whiteCardModel = new WhiteCardModel();
                        whiteCardModel.whiteCardId = whiteCard.getWhiteCardId();

                        return whiteCardModel;
                    }).collect(Collectors.toList());

            final BlackCardModel blackCardModel = new BlackCardModel();
            blackCardModel.blackCardId = blackCard.getBlackCardId();

            newRoundResponse.blackCardModel = blackCardModel;

            playerGame.say(newRoundResponse);
        }

        final Player gameMaster = game.getGameMaster();
        final GameMasterResponse gameMasterResponse = new GameMasterResponse();

        gameMaster.say(gameMasterResponse);
    }

    private void onRestartGame(final Player player, final RestartGameRequest restartGameRequest) {
        final Player activePlayer = playerRepository.getPlayerBySessionId(restartGameRequest.sessionKey);

        if(activePlayer != null) {
            playerRepository.reassignPlayer(activePlayer, player);

            final Game currentGame = activePlayer.getCurrentGame();
            currentGame.startGame(activePlayer);
        } else {
            final FinishedGameResponse finishedGameResponse = new FinishedGameResponse();
            player.say(finishedGameResponse);
        }
    }

    private void onSelectCards(final Player player, final SelectCardsRequest selectCardsRequest) {
        final List<WhiteCard> selectedCards = selectCardsRequest.whiteCardModels.stream()
                .map(whiteCardModel -> whiteCardRepository.get(whiteCardModel.whiteCardId)).collect(Collectors.toList());

        player.selectCards(selectedCards);
    }

    private void onSelectWinner(final Player player, final SelectWinnerRequest selectWinnerRequest) {
        final Game game = player.getCurrentGame();

        if(game.getGameMaster() != player) {
            //TODo
        }

        final List<WhiteCard> winnerCards = selectWinnerRequest.whiteCardModels.stream()
                .map(whiteCardModel -> whiteCardRepository.get(whiteCardModel.whiteCardId)).collect(Collectors.toList());

        game.selectWinnerCards(winnerCards);
        newRound(game);
    }
}
