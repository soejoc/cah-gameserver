package io.jochimsen.cahgameserver.config;

import io.jochimsen.cahgameserver.backend.api.BlackCardApi;
import io.jochimsen.cahgameserver.backend.api.WhiteCardApi;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

@Configuration
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class ApiConfig {
    private final Retrofit retrofit;

    @Bean
    public WhiteCardApi whiteCardAPI() {
        return retrofit.create(WhiteCardApi.class);
    }

    @Bean
    public BlackCardApi blackCardAPI() {
        return retrofit.create(BlackCardApi.class);
    }
}
