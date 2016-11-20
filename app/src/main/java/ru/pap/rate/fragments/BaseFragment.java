package ru.pap.rate.fragments;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import ru.pap.rate.service.LoadDataHelper;

/**
 * Created by alex on 09.11.16.
 */

public abstract class BaseFragment extends Fragment {


    private Map<String, WeakReference<ContentObserver>> mObserveMap = new HashMap<>();

    public void registerNotifyObserver(Uri uri) {
        if (uri != null) {
            unregisterNotifyObserver(uri);
            ContentObserver actionObserver = new ContentObserver(new Handler()) {
                @Override
                public void onChange(boolean selfChange, Uri uri) {
                    super.onChange(selfChange, uri);
                    handleSyncAction(uri);
                }
            };

            getContext().getContentResolver().registerContentObserver(uri, true, actionObserver);
            mObserveMap.put(uri.toString(), new WeakReference<ContentObserver>(actionObserver));
        }
    }

    public void unregisterNotifyObserver(Uri uri) {
        if (uri != null) {
            WeakReference<ContentObserver> weakObserver = mObserveMap.remove(uri.toString());
            if (weakObserver != null) {
                ContentObserver observer = weakObserver.get();
                if (observer != null) {
                    getContext().getContentResolver().unregisterContentObserver(observer);
                }
            }
        }
    }

    private void handleSyncAction(Uri uri) {
        if (uri == null) {
            return;
        }
        String action = uri.getQueryParameter(LoadDataHelper.KEY_ACTION);
        if (LoadDataHelper.ACTION_ERROR.equals(action)) {
            onSyncError(uri);
        } else {
            onSyncComplete(uri);
        }

    }

    protected void onSyncError(Uri uri) {

    }

    protected void onSyncComplete(Uri uri) {

    }

}
