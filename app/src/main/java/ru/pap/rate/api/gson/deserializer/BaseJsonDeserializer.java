package ru.pap.rate.api.gson.deserializer;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by alex on 10.11.16.
 */

public abstract class BaseJsonDeserializer<T> implements JsonDeserializer<T> {

    protected JsonElement getElementPaths(JsonElement json, String... paths) {
        if (json == null || paths == null || paths.length == 0) {
            return null;
        }
        if(!json.isJsonObject()){
            return null;
        }
        List<String> listPath = new ArrayList<>();
        Collections.addAll(listPath, paths);
        Iterator<String> iterator = listPath.iterator();
        String path;
        while (iterator.hasNext()){
            if(json == null || !json.isJsonObject()){
                break;
            }
            path = iterator.next();
            iterator.remove();
            json = json.getAsJsonObject().get(path);
        }
        if(listPath.isEmpty()){
            return json;
        }
        return null;
    }
}
