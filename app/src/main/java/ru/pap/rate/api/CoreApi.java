package ru.pap.rate.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.pap.rate.R;
import ru.pap.rate.RateApplication;
import ru.pap.rate.api.gson.deserializer.QuoteContainerDeserializer;
import ru.pap.rate.api.gson.deserializer.SymbolsContainerDeserializer;
import ru.pap.rate.model.QuotesContainer;
import ru.pap.rate.model.SymbolsContainer;

/**
 * Created by alex on 09.11.16.
 */

public class CoreApi {

    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 60;
    private static final int READ_TIMEOUT = 60;

    private final OkHttpClient mOkHttpClient;

    private Gson mGson;

    private static CoreApi sCoreApi;

    public static CoreApi getInstance(){
        if(sCoreApi == null){
            sCoreApi = new CoreApi();
        }
        return sCoreApi;
    }

    private CoreApi(){
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        mGson = new GsonBuilder()
                .registerTypeAdapter(SymbolsContainer.class, new SymbolsContainerDeserializer())
                .registerTypeAdapter(QuotesContainer.class, new QuoteContainerDeserializer())
                .create();
    }

    public SymbolsApi getSymbolsApi(){
        return getSymbolsRetrofit().create(SymbolsApi.class);
    }

    public QuoteApi getQuoteApi(){
        return getValueRetrofit().create(QuoteApi.class);
    }

    private Retrofit getSymbolsRetrofit(){
        return getRetrofit(RateApplication.getAppContext().getResources().getString(R.string.symbols_api_base_url));
    }

    private Retrofit getValueRetrofit(){
        return getRetrofit(RateApplication.getAppContext().getResources().getString(R.string.symbol_values_api_base_url));
    }

    private Retrofit getRetrofit(String baseUrl){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .client(mOkHttpClient)
                .build();
    }

}
