package com.hamstersapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hamstersapp.adapter.HamstersAdapter;
import com.hamstersapp.api.HamstersApiFactory;
import com.hamstersapp.data.DataBaseReadLoader;
import com.hamstersapp.data.DataBaseWriteLoader;
import com.hamstersapp.model.HamsterModel;
import com.hamstersapp.utils.WowRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by CisDevelopment
 *
 * @author Kostya Balyaba
 *         on 13.04.2016.
 */

public class MainFragment extends Fragment implements Callback<List<HamsterModel>>,
        SearchView.OnQueryTextListener,
        HamstersAdapter.HamsterAdapterCallback,
        WowRecyclerView.ItemClickSupport.OnItemClickListener {

    @Bind(R.id.progress_bar_container)
    protected RelativeLayout mProgressBarContainer;
    @Bind(R.id.content_container)
    protected LinearLayout mContentContainer;
    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;
    @Bind(R.id.list)
    protected RecyclerView mList;
    @Bind(R.id.refresh_layout)
    protected SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.error_container)
    protected RelativeLayout mErrorContainer;
    @Bind(R.id.error_title)
    protected TextView mErrorTitle;
    private List<HamsterModel> mData;
    private HamstersAdapter mAdapter;

    private final int DATABASE_LOADER_ID = 0;
    private final int NETWORK_LOADER_ID = 1;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (mData == null)
            mData = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, null);
        if (rootView != null)
            ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint(getString(R.string.search));
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about: {
                ((MainActivity) getActivity()).showFragmentDialog(AboutDialog.newInstance());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((BaseActivity) getActivity()).initToolbar(mToolbar, false);
        showLoading();
        loadFromDataBase();
        executeRequest();
    }

    private void fillUI() {
        if (mAdapter == null) {
            mAdapter = new HamstersAdapter(mData, getActivity(), this);
            mAdapter.setHasStableIds(true);
            mList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mList.setAdapter(mAdapter);
            WowRecyclerView
                    .ItemClickSupport
                    .addTo(mList)
                    .setOnItemClickListener(this);
            mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    executeRequest();
                }
            });
        } else {
            mAdapter.notifyDataSetChanged();
        }
        showContent();
    }

    private void showLoading() {
        mProgressBarContainer.setVisibility(View.VISIBLE);
        mContentContainer.setVisibility(View.GONE);
        mErrorContainer.setVisibility(View.GONE);
    }

    private void showContent() {
        mErrorContainer.setVisibility(View.GONE);
        mProgressBarContainer.setVisibility(View.GONE);
        mContentContainer.setVisibility(View.VISIBLE);
    }

    private void showError(String message) {
        mErrorTitle.setText(message);
        mProgressBarContainer.setVisibility(View.GONE);
        mContentContainer.setVisibility(View.GONE);
        mErrorContainer.setVisibility(View.VISIBLE);
    }

    private void loadFromDataBase() {
        if (getLoaderManager().getLoader(DATABASE_LOADER_ID) == null)
            getLoaderManager().initLoader(DATABASE_LOADER_ID, null, mReadFromDataBaseCallback).forceLoad();
        else
            getLoaderManager().getLoader(DATABASE_LOADER_ID).forceLoad();
    }

    private void executeRequest() {
        HamstersApiFactory.getHamstersService().getHamsters().enqueue(this);
    }

    private void putInDataBase() {
        if (getLoaderManager().getLoader(NETWORK_LOADER_ID) == null)
            getLoaderManager().initLoader(NETWORK_LOADER_ID, null, mPutInDataBaseCallback).forceLoad();
        else
            getLoaderManager().getLoader(NETWORK_LOADER_ID).forceLoad();
    }

    @Override
    public void onResponse(Call<List<HamsterModel>> call, retrofit2.Response<List<HamsterModel>> response) {
        if (!isAdded())
            return;
        if (response != null && response.body() != null && !response.body().isEmpty()) {
            if (!mData.isEmpty())
                mData.clear();
            mData.addAll(response.body());
            putInDataBase();
        } else {
            if (mData.isEmpty())
                showError(getString(R.string.error));
            else
                showToast(getString(R.string.error));
        }
        if (mRefreshLayout.isRefreshing())
            mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailure(Call<List<HamsterModel>> call, Throwable t) {
        if (!isAdded())
            return;
        if (mData.isEmpty())
            showError(getString(R.string.connection_error));
        else
            showToast(getString(R.string.connection_error));

        if (mRefreshLayout.isRefreshing())
            mRefreshLayout.setRefreshing(false);
    }

    private LoaderManager.LoaderCallbacks<List<HamsterModel>> mReadFromDataBaseCallback = new LoaderManager.LoaderCallbacks<List<HamsterModel>>() {
        @Override
        public Loader<List<HamsterModel>> onCreateLoader(int id, Bundle args) {
            return new DataBaseReadLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<List<HamsterModel>> loader, List<HamsterModel> data) {
            if (!isAdded())
                return;
            if (data != null && !data.isEmpty()) {
                mData.clear();
                mData.addAll(data);
                fillUI();
            }
        }

        @Override
        public void onLoaderReset(Loader<List<HamsterModel>> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks mPutInDataBaseCallback = new LoaderManager.LoaderCallbacks() {
        @Override
        public Loader onCreateLoader(int id, Bundle args) {
            return new DataBaseWriteLoader(getActivity(), mData);
        }

        @Override
        public void onLoadFinished(Loader loader, Object data) {
            if (!isAdded())
                return;
            /*To get sorted data */
            loadFromDataBase();
        }

        @Override
        public void onLoaderReset(Loader loader) {

        }
    };

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        if (mData.get(position) != null) {
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra("model", mData.get(position));
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLoaderManager().destroyLoader(DATABASE_LOADER_ID);
        getLoaderManager().destroyLoader(NETWORK_LOADER_ID);
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (mAdapter != null) {
            mAdapter.getFilter().filter(newText);
        }
        return true;
    }

    @Override
    public void showFilterEmptyResult() {
        showError(getString(R.string.empty_error));
    }

    @Override
    public void showFilterResult() {
        showContent();
    }

}
