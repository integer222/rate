package ru.pap.rate.api;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.pap.rate.model.SymbolsContainer;

/**
 * Created by alex on 09.11.16.
 */

public interface SymbolsApi {

    @GET("webservice/v1/symbols/allcurrencies/quote?format=json")
    Call<SymbolsContainer> getSymbols();

}
