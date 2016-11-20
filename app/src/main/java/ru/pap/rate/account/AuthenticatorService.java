package ru.pap.rate.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import ru.pap.rate.account.Authenticator;

/**
 * Created by alex on 14.11.16.
 */

public class AuthenticatorService extends Service {

    private Authenticator mAuthenticator;
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new Authenticator(this);
    }
    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
