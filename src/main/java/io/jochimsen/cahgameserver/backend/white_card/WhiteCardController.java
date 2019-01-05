package io.jochimsen.cahgameserver.backend.white_card;

import io.jochimsen.cahgameserver.backend.BaseController;
import io.jochimsen.cahgameserver.backend.global.response.HashResponse;
import io.jochimsen.cahgameserver.backend.white_card.response.WhiteCardResponse;
import io.reactivex.Single;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WhiteCardController extends BaseController {
    private static final String NAMESPACE = "whiteCard";

    private WhiteCardAPI whiteCardAPI;

    public WhiteCardController() {
        super(NAMESPACE);
    }

    @Override
    protected void initializeApi() {
        whiteCardAPI = createAPI(WhiteCardAPI.class);
    }

    public Single<HashResponse<List<WhiteCardResponse>>> getWhiteCards() {
        return whiteCardAPI.getWhiteCards();
    }
}
