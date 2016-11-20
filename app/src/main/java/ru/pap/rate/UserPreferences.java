package ru.pap.rate;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ru.pap.rate.model.Symbol;

/**
 * Created by alex on 13.11.16.
 */

public class UserPreferences {

    private static UserPreferences sUserPreferences;

    private SharedPreferences mSharedPreferences;

    private UserPreferences(SharedPreferences sharedPreferences){
        mSharedPreferences = sharedPreferences;
    }

    public static UserPreferences getPreferences(){
        if(sUserPreferences == null){
            Context context = RateApplication.getAppContext();
            sUserPreferences = new UserPreferences(PreferenceManager.getDefaultSharedPreferences(context));
        }
        return sUserPreferences;
    }

    public String getCurrentSymbol(){
        return mSharedPreferences.getString("symbol_id", null);
    }

    public String getCurrentName(){
        return mSharedPreferences.getString("symbol_name", null);
    }

    public void onSaveCurrentSymbol(Symbol symbol){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("symbol_id", symbol.getSymbol());
        editor.putString("symbol_name", symbol.getName());
        editor.apply();
    }

    public void clear(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }





}
