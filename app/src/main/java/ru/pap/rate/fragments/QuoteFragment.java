package ru.pap.rate.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ru.pap.rate.Config;
import ru.pap.rate.R;
import ru.pap.rate.UserPreferences;
import ru.pap.rate.adapters.BaseAdapter;
import ru.pap.rate.adapters.BaseFooterAdapter;
import ru.pap.rate.adapters.QuoteAdapter;
import ru.pap.rate.db.QuoteContract;
import ru.pap.rate.model.Symbol;
import ru.pap.rate.service.LoadDataService;
import ru.pap.rate.service.QuoteSyncParam;
import ru.pap.rate.service.QuoteSyncParam.StateSync;
import ru.pap.rate.service.LoadDataHelper;
import ru.pap.rate.sync.RateSyncAdapterHelper;
import ru.pap.rate.sync.SyncType;
import ru.pap.rate.widget.SymbolQuoteWidget;

/**
 * A placeholder fragment containing a simple view.
 */
public class QuoteFragment extends ContentFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int QUOTE_LOADER_ID = 10;
    private static final String AUTO_SYNC_SAVE_KEY = "auto_sync_save_key";

    private QuoteAdapter mQuoteAdapter = new QuoteAdapter(null);
    private String mSelectSymbol;
    private boolean mAutoSync;


    public QuoteFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerNotifyObserver(QuoteContract.buildBaseUri());

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mAutoSync = savedInstanceState.getBoolean(AUTO_SYNC_SAVE_KEY, false);
        }
        UserPreferences preferences = UserPreferences.getPreferences();
        onSelectedSymbol(preferences.getCurrentName(), preferences.getCurrentSymbol(), false);
        onAutoSync();
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == QUOTE_LOADER_ID) {
            return new CursorLoader(getContext(), QuoteContract.buildBaseUri(), null,
                    "symbol_symbol = ?", new String[]{mSelectSymbol}, "date desc");
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mQuoteAdapter.swapCursor(data);
        onRefreshReset();
    }

    private void onRefreshReset() {
        mQuoteAdapter.setAdapterState(BaseFooterAdapter.AdapterState.VIEW);
        setSwipeEnabled(true);
        setSwipeVisible(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mQuoteAdapter.swapCursor(null);
    }

    @Override
    public void loadNewData(int limit, boolean refresh) {



        if (!refresh) {
            mQuoteAdapter.setAdapterState(BaseFooterAdapter.AdapterState.LOAD_FOOTER);
        } else {
            mQuoteAdapter.setAdapterState(BaseFooterAdapter.AdapterState.VIEW);
            setSwipeVisible(true);
        }

        if(TextUtils.isEmpty(mSelectSymbol)){
            onRefreshReset();
            return;
        }

        QuoteSyncParam quoteSyncParam = new QuoteSyncParam(mSelectSymbol);
        quoteSyncParam.setStateSync(refresh ? StateSync.SYNC : StateSync.DOWN);
        Bundle bundle = new Bundle();
        bundle.putSerializable(LoadDataHelper.QUERY_PARAMS, quoteSyncParam);
        Intent intent = new Intent(getActivity(), LoadDataService.class);
        intent.putExtra(LoadDataService.DATA_PARAM, bundle);
        getActivity().startService(intent);

    }

    @Override
    public boolean isMoreAvailable() {
        return true;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        onTitleUpdate(UserPreferences.getPreferences().getCurrentName());
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_symbols:
                openSymbolChooser();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }
        if (requestCode == SymbolFragment.REQUEST_CODE) {
            Symbol symbol = (Symbol) data.getSerializableExtra(SymbolFragment.RESULT_PARAM);
            if (symbol == null) {
                return;
            }
            onSaveSelectedSymbol(symbol);
        }

    }

    private void onSaveSelectedSymbol(Symbol symbol) {
        UserPreferences.getPreferences().onSaveCurrentSymbol(symbol);
        onSelectedSymbol(symbol.getName(), symbol.getSymbol(), true);
        onSyncNewDate();
    }

    private void onTitleUpdate(String name) {
        if (TextUtils.isEmpty(name)) {
            getActivity().setTitle(getString(R.string.no_selected_symbol));
            return;
        }
        getActivity().setTitle(name);
    }

    private void onSelectedSymbol(String name, String symbol, boolean restart) {
        onTitleUpdate(name);
        if (TextUtils.isEmpty(symbol)) {
            setSwipeEnabled(false);
            mQuoteAdapter.swapCursor(null);
            return;
        }

        getActivity().setTitle(name);
        mSelectSymbol = symbol;

        setSwipeVisible(true);
        if (restart && getLoaderManager().getLoader(QUOTE_LOADER_ID) != null) {
            getLoaderManager().restartLoader(QUOTE_LOADER_ID, null, this);
        } else {
            getLoaderManager().initLoader(QUOTE_LOADER_ID, null, this);
        }
    }

    private void openSymbolChooser() {

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentByTag(SymbolFragment.TAG);
        if (fragment != null) {
            fm.popBackStack();
        } else {
            fragment = new SymbolFragment();

            fragment.setTargetFragment(QuoteFragment.this, SymbolFragment.REQUEST_CODE);
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_top, 0, 0, R.anim.slide_out_top)
                    .add(R.id.content, fragment, SymbolFragment.TAG)
                    .addToBackStack("")
                    .commit();

        }
    }

    private void onAutoSync(){
        if(mAutoSync && RateSyncAdapterHelper.isSyncable(getContext())){
            return;
        }
        onSyncNewDate();
        getContext().sendBroadcast(SymbolQuoteWidget.getIntentUpdate(getContext()));
        //RateSyncAdapterHelper.requestManual(getContext(), SyncType.QUOTE);
        //setSwipeVisible(true);
        mAutoSync = true;
    }

    private void onSyncNewDate() {
        if(TextUtils.isEmpty(mSelectSymbol)){
            onRefreshReset();
            return;
        }
        loadNewData(Config.DEFAULT_LOAD_ELEMENT_LIMIT, true);
    }

    @Override
    public BaseAdapter getAdapter() {
        return mQuoteAdapter;
    }

    @Override
    public int getLimit() {
        return Config.DEFAULT_LOAD_ELEMENT_LIMIT;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterNotifyObserver(QuoteContract.buildBaseUri());
    }

    @Override
    protected void onSyncError(Uri uri) {
        super.onSyncError(uri);
        Toast.makeText(getContext(), R.string.error_load, Toast.LENGTH_SHORT).show();
        onRefreshReset();
    }

    @Override
    protected void onSyncComplete(Uri uri) {
        super.onSyncComplete(uri);
        RateSyncAdapterHelper.onValidSync(getContext());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AUTO_SYNC_SAVE_KEY, mAutoSync);
    }
}
