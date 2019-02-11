package io.jochimsen.cahgameserver.backend.api;

import io.jochimsen.cahgameserver.backend.response.HashResponse;
import io.jochimsen.cahgameserver.backend.response.WhiteCardResponse;
import io.reactivex.Single;
import retrofit2.http.GET;

import java.util.List;

public interface WhiteCardApi {
    @GET("whiteCard/")
    Single<HashResponse<List<WhiteCardResponse>>> getWhiteCards();
}
