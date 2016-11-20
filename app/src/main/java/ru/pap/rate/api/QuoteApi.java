package ru.pap.rate.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.pap.rate.api.queries.SymbolValuesQuery;
import ru.pap.rate.model.QuotesContainer;

/**
 * Created by alex on 09.11.16.
 */

public interface QuoteApi {

    @GET("v1/public/yql?format=json&env=store://datatables.org/alltableswithkeys")
    Call<QuotesContainer> getSymbolValues(@Query(encoded = true, value = "q") SymbolValuesQuery query);

}
