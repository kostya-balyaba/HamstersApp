package com.hamstersapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hamstersapp.adapter.HamstersAdapter;
import com.hamstersapp.api2.ApiFactory;
import com.hamstersapp.model.HamsterModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Костя on 13.04.2016.
 */

public class MainFragment extends Fragment implements Callback<List<HamsterModel>> {

    @Bind(R.id.progress_bar_container)
    protected RelativeLayout mProgressBarContainer;
    @Bind(R.id.content_container)
    protected LinearLayout mContentContainer;
    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;
    @Bind(R.id.list)
    protected RecyclerView mList;
    private List<HamsterModel> mData;
    private HamstersAdapter mAdapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        inflater.inflate(R.menu.menu_about, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about: {
                ((MainActivity) getActivity()).showFragmentDialog(new AboutDialog());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).initToolbar(mToolbar);
        showLoading();
        executeRequest();
    }

    private void fillUI() {
        if (mAdapter == null) {
            mAdapter = new HamstersAdapter(mData, getActivity());
            mList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mList.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        showContent();
    }

    private void showLoading() {
        mProgressBarContainer.setVisibility(View.VISIBLE);
        mContentContainer.setVisibility(View.GONE);
    }

    private void showContent() {
        mProgressBarContainer.setVisibility(View.GONE);
        mContentContainer.setVisibility(View.VISIBLE);
    }

    private void executeRequest() {
        ApiFactory.getHamstersService().getHamsters().enqueue(this);
    }

    @Override
    public void onResponse(Call<List<HamsterModel>> call, retrofit2.Response<List<HamsterModel>> response) {
        if (response != null && response.body() != null && !response.body().isEmpty()) {
            mData = response.body();
            fillUI();
        }
    }

    @Override
    public void onFailure(Call<List<HamsterModel>> call, Throwable t) {

    }


}
