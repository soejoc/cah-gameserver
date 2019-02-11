package io.jochimsen.cahgameserver.backend.api;

import java.util.List;

import io.jochimsen.cahgameserver.backend.response.BlackCardResponse;
import io.jochimsen.cahgameserver.backend.response.HashResponse;
import io.reactivex.Single;
import retrofit2.http.GET;

public interface BlackCardApi {
    @GET("blackCard/")
    Single<HashResponse<List<BlackCardResponse>>> getBlackCards();
}
