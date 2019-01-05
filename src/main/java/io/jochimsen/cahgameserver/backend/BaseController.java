package io.jochimsen.cahgameserver.backend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.PostConstruct;

public abstract class BaseController {

    @Autowired
    @Qualifier("baseUrl")
    private String baseUrl;

    private final String namespace;

    public BaseController(final String namespace) {
        this.namespace = namespace;
    }

    @PostConstruct
    protected abstract void initializeApi();

    protected <T> T createAPI(final Class<T> service) {
        final Gson gson = new GsonBuilder()
                .create();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl + namespace + "/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(service);
    }
}
