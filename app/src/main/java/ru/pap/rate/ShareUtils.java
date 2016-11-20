package ru.pap.rate;

import android.content.Intent;

/**
 * Created by alex on 13.11.16.
 */

public class ShareUtils {

    public static Intent onCreateShareTextIntent(String text){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        return sendIntent;
    }

}
