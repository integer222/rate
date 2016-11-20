package ru.pap.rate.api.gson.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import android.net.Uri;

import java.lang.reflect.Type;

import ru.pap.rate.model.Quote;
import ru.pap.rate.model.QuotesContainer;


/**
 * Created by alex on 10.11.16.
 */

public class QuoteContainerDeserializer extends BaseJsonDeserializer<QuotesContainer> {
    @Override
    public QuotesContainer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        QuotesContainer quotesContainer = new QuotesContainer();

        JsonElement element = getElementPaths(json, "query", "results", "quote");
        if (element == null) {
            return quotesContainer;
        }
        if (element.isJsonArray()) {
            for (JsonElement item : element.getAsJsonArray()) {
                quotesContainer.getQuotes().add(onQuoteParse(item, context));
            }
        }else if (element.isJsonObject()){
            quotesContainer.getQuotes().add(onQuoteParse(element, context));
        }
        return quotesContainer;
    }

    private Quote onQuoteParse(JsonElement item, JsonDeserializationContext context){
        Quote quote = context.deserialize(item, Quote.class);
        quote.setSymbol(Uri.decode(quote.getSymbol()));
        return quote;
    }
}
