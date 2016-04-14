package com.hamstersapp;

import android.os.Bundle;
import android.text.TextUtils;

import com.hamstersapp.model.HamsterModel;

/**
 * Created by Костя on 14.04.2016.
 */

public class DetailsActivity extends BaseActivity implements DetailsFragment.DetailsFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_single_fragment);
        addFragment(R.id.container, DetailsFragment.newInstance(getIntent().<HamsterModel>getParcelableExtra("model")));
    }

    @Override
    public void onShare(String title, String imageUrl) {
        if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(imageUrl))
            share(title, imageUrl);
    }

}
