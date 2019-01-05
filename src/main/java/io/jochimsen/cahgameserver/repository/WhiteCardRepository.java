package io.jochimsen.cahgameserver.repository;

import io.jochimsen.cahgameserver.backend.global.response.HashResponse;
import io.jochimsen.cahgameserver.backend.white_card.WhiteCardController;
import io.jochimsen.cahgameserver.backend.white_card.response.WhiteCardResponse;
import io.jochimsen.cahgameserver.game.card.WhiteCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class WhiteCardRepository {
    private final Map<Long, WhiteCard> whiteCardMap;

    @Autowired
    public WhiteCardRepository(final WhiteCardController whiteCardController) {
        final HashResponse<List<WhiteCardResponse>> hashResponse = whiteCardController.getWhiteCards().blockingGet();
        final List<WhiteCardResponse> whiteCardsResponse = hashResponse.data;

        whiteCardMap = whiteCardsResponse.stream()
                .map(whiteCardResponse -> new WhiteCard(whiteCardResponse.whiteCardId, whiteCardResponse.text))
                .collect(Collectors.toMap(WhiteCard::getWhiteCardId, Function.identity()));
    }
}
