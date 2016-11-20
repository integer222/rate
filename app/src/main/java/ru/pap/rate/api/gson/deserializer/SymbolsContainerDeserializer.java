package ru.pap.rate.api.gson.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ru.pap.rate.model.Symbol;
import ru.pap.rate.model.SymbolsContainer;

/**
 * Created by alex on 10.11.16.
 */

public class SymbolsContainerDeserializer extends BaseJsonDeserializer<SymbolsContainer> {
    @Override
    public SymbolsContainer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        SymbolsContainer symbolsContainer = new SymbolsContainer();

        JsonElement element = getElementPaths(json, "list", "resources");
        if (element != null && element.isJsonArray()) {
            for (JsonElement item : element.getAsJsonArray()) {
                item = getElementPaths(item, "resource", "fields");
                if (item == null || !item.isJsonObject()) {
                    continue;
                }
                symbolsContainer.getSymbols().add((Symbol) context.deserialize(item, Symbol.class));
            }
        }
        return symbolsContainer;
    }
}
