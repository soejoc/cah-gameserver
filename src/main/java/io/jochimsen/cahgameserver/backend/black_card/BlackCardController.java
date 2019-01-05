package io.jochimsen.cahgameserver.backend.black_card;

import io.jochimsen.cahgameserver.backend.BaseController;
import io.jochimsen.cahgameserver.backend.black_card.response.BlackCardResponse;
import io.jochimsen.cahgameserver.backend.global.response.HashResponse;
import io.reactivex.Single;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlackCardController extends BaseController {
    private static final String NAMESPACE = "blackCard";

    private BlackCardAPI blackCardAPI;

    public BlackCardController() {
        super(NAMESPACE);
    }

    @Override
    protected void initializeApi() {
        blackCardAPI = createAPI(BlackCardAPI.class);
    }

    public Single<HashResponse<List<BlackCardResponse>>> getBlackCards() {
        return blackCardAPI.getBlackCards();
    }
}
