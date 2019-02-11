package io.jochimsen.cahgameserver.repository;

import io.jochimsen.cahgameserver.backend.api.BlackCardApi;
import io.jochimsen.cahgameserver.backend.response.BlackCardResponse;
import io.jochimsen.cahgameserver.backend.response.HashResponse;
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
    public BlackCardRepository(final BlackCardApi blackCardApi) {
        final HashResponse<List<BlackCardResponse>> hashResponse = blackCardApi.getBlackCards().blockingGet();
        final List<BlackCardResponse> blackCardsResponse = hashResponse.getData();

        blackCardMap = blackCardsResponse.stream()
                .map(blackCardResponse -> new BlackCard(blackCardResponse.getBlackCardId(), blackCardResponse.getText(), blackCardResponse.getBlankCount()))
                .collect(Collectors.toMap(BlackCard::getBlackCardId, Function.identity()));
    }
}
