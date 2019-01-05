package io.jochimsen.cahgameserver.backend.white_card;

import io.jochimsen.cahgameserver.backend.global.response.HashResponse;
import io.jochimsen.cahgameserver.backend.white_card.response.WhiteCardResponse;
import io.reactivex.Single;
import retrofit2.http.GET;

import java.util.List;

interface WhiteCardAPI {
    @GET(".")
    Single<HashResponse<List<WhiteCardResponse>>> getWhiteCards();
}
