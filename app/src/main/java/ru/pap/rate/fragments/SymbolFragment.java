package ru.pap.rate.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ru.pap.rate.R;
import ru.pap.rate.adapters.BaseAdapter;
import ru.pap.rate.adapters.BaseFooterAdapter;
import ru.pap.rate.adapters.IHolderClick;
import ru.pap.rate.adapters.SymbolAdapter;
import ru.pap.rate.db.SymbolContract;
import ru.pap.rate.model.Symbol;
import ru.pap.rate.service.LoadDataService;
import ru.pap.rate.service.SymbolSyncParam;
import ru.pap.rate.service.LoadDataHelper;

/**
 * Created by alex on 12.11.16.
 */

public class SymbolFragment extends ContentFragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        SearchView.OnQueryTextListener, IHolderClick {

    public static final String TAG = "SymbolFragment";
    private static final String SEARCH_SAVE_KEY = "search";
    public static final int REQUEST_CODE = 123;
    public static final String RESULT_PARAM = "symbol_result";

    private static final int SYMBOL_LOADER_ID = 20;
    private SymbolAdapter mSymbolAdapter = new SymbolAdapter(null, this);
    private SearchView mSearchView;
    private String mSearchString;

    public SymbolFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerNotifyObserver(SymbolContract.buildBaseUri());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(SYMBOL_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == SYMBOL_LOADER_ID) {
            mSymbolAdapter.setAdapterState(BaseFooterAdapter.AdapterState.LOAD_FOOTER);
            return new CursorLoader(getContext(), SymbolContract.buildBaseUri(), null,
                    getSelection(), getSelectionArgs(), "name");
        }
        return null;
    }

    private String getSelection() {
        if (TextUtils.isEmpty(mSearchString)) {
            return null;
        }
        return SymbolContract.Symbol.NAME + " LIKE ?";
    }

    private String[] getSelectionArgs() {
        if (TextUtils.isEmpty(mSearchString)) {
            return null;
        }
        return new String[]{"%" + mSearchString + "%"};
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mSymbolAdapter.swapCursor(data);
        mSymbolAdapter.setAdapterState(BaseFooterAdapter.AdapterState.VIEW);
        setSwipeVisible(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSymbolAdapter.swapCursor(null);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        if (!TextUtils.isEmpty(mSearchString)) {
            searchMenuItem.expandActionView();
            mSearchView.setQuery(mSearchString, true);
            mSearchView.clearFocus();
        }
        mSearchView.setOnQueryTextListener(this);
        getActivity().setTitle(getString(R.string.list_symbol));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mSearchString = mSearchView.getQuery().toString();
        outState.putString(SEARCH_SAVE_KEY, mSearchString);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mSearchString = savedInstanceState.getString(SEARCH_SAVE_KEY);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mSearchString = newText;
        getLoaderManager().restartLoader(SYMBOL_LOADER_ID, null, this);
        return true;
    }

    @Override
    public void onItemClick(int position, View view) {
        Symbol symbol = mSymbolAdapter.getItem(position);
        if (symbol != null) {
            onSendResult(symbol);
        }
    }

    private void onSendResult(Symbol symbol) {
        Fragment targetFragment = getTargetFragment();
        if (targetFragment == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(RESULT_PARAM, symbol);
        targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        getFragmentManager().popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterNotifyObserver(SymbolContract.buildBaseUri());
    }

    @Override
    protected void onSyncError(Uri uri) {
        super.onSyncError(uri);
        Toast.makeText(getContext(), R.string.error_load, Toast.LENGTH_SHORT).show();
    }

    @Override
    BaseAdapter getAdapter() {
        return mSymbolAdapter;
    }

    @Override
    int getLimit() {
        return 0;
    }

    @Override
    protected void onSyncComplete(Uri uri) {
        super.onSyncComplete(uri);
    }

    @Override
    public void loadNewData(int limit, boolean refresh) {
        if(!refresh){
            return;
        }
        SymbolSyncParam symbolQueryParam = new SymbolSyncParam();
        symbolQueryParam.setRefresh(true);
        Bundle bundle = new Bundle();
        bundle.putSerializable(LoadDataHelper.QUERY_PARAMS, symbolQueryParam);
        Intent intent = new Intent(getContext(), LoadDataService.class);
        intent.putExtra(LoadDataService.DATA_PARAM, bundle);
        getContext().startService(intent);
    }

    @Override
    public boolean isMoreAvailable() {
        return false;
    }
}
