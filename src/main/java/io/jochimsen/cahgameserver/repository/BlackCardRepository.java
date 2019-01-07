package io.jochimsen.cahgameserver.repository;

import io.jochimsen.cahgameserver.backend.black_card.BlackCardController;
import io.jochimsen.cahgameserver.backend.black_card.response.BlackCardResponse;
import io.jochimsen.cahgameserver.backend.global.response.HashResponse;
import io.jochimsen.cahgameserver.game.card.BlackCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class BlackCardRepository {
    private final Map<Long, BlackCard> blackCardMap;

    @Autowired
    public BlackCardRepository(final BlackCardController blackCardController) {
        final HashResponse<List<BlackCardResponse>> hashResponse = blackCardController.getBlackCards().blockingGet();
        final List<BlackCardResponse> blackCardsResponse = hashResponse.data;

        blackCardMap = blackCardsResponse.stream()
                .map(blackCardResponse -> new BlackCard(blackCardResponse.blackCardId, blackCardResponse.text, blackCardResponse.blankCount))
                .collect(Collectors.toMap(BlackCard::getBlackCardId, Function.identity()));
    }

    public List<BlackCard> getCards(final List<BlackCard> usedCards, int amount) {
        return blackCardMap.values().stream()
                .filter(blackCard -> !usedCards.contains(blackCard))
                .limit(amount)
                .collect(Collectors.toList());
    }
}
