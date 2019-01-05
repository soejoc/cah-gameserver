package io.jochimsen.cahgameserver.backend.black_card;

import java.util.List;

import io.jochimsen.cahgameserver.backend.black_card.response.BlackCardResponse;
import io.jochimsen.cahgameserver.backend.global.response.HashResponse;
import io.reactivex.Single;
import retrofit2.http.GET;

public interface BlackCardAPI {
    @GET(".")
    Single<HashResponse<List<BlackCardResponse>>> getBlackCards();
}
