package com.hamstersapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hamstersapp.model.HamsterModel;
import com.hamstersapp.utils.PlaceHolders;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CisDevelopment
 * @author Kostya Balyaba
 * on 14.04.2016.
 */
public class DetailsFragment extends Fragment {

    private HamsterModel mModel;

    @Bind(R.id.header_view_layout)
    protected RelativeLayout mHeaderViewLayout;
    @Bind(R.id.header_view)
    protected ImageView mHeaderImage;
    @Bind(R.id.title_text_view)
    protected TextView mTitleView;
    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;
    @Bind(R.id.description_text_view)
    protected TextView mDescriptionView;

    private DetailsFragmentListener mListener;
    private final HeaderTarget mHeaderTarget = new HeaderTarget();


    public interface DetailsFragmentListener {
        void onShare(String title, String imageUrl);
    }

    public static DetailsFragment newInstance(HamsterModel model) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable("model", model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (mModel == null && getArguments() != null && getArguments().containsKey("model"))
            mModel = getArguments().getParcelable("model");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DetailsFragmentListener)
            mListener = (DetailsFragmentListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, null);
        if (rootView != null)
            ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (getActivity() != null)
                    getActivity().finish();
                break;
            }
            case R.id.share: {
                if (mListener != null)
                    mListener.onShare(mModel.getTitle(), mModel.getImage());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((BaseActivity) getActivity()).initToolbar(mToolbar, true);
        fillUI();
    }

    private void fillUI() {
        if (mModel != null) {
            buildHeaderView();

            mTitleView.setText(!TextUtils.isEmpty(mModel.getTitle()) ? mModel.getTitle() : "");
            mDescriptionView.setText(!TextUtils.isEmpty(mModel.getDescription()) ? mModel.getDescription() : "");

        }
    }

    private void buildHeaderView() {
        mHeaderViewLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int width = ((BaseActivity) getActivity()).getScreenWidth();
        int height = mHeaderViewLayout.getMeasuredHeight();
        Drawable placeHolder = PlaceHolders.rect(width, height, R.color.colorPrimary);

        if (!TextUtils.isEmpty(mModel.getImage()))
            Picasso.with(getActivity())
                    .load(mModel.getImage())
                    .resize(width, height)
                    .placeholder(placeHolder)
                    .error(placeHolder)
                    .centerCrop()
                    .into(mHeaderTarget);
    }

    private class HeaderTarget implements Target {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
            if (getActivity() != null && !getActivity().isFinishing()) {
                if (mHeaderViewLayout != null && mHeaderImage != null) {
                    mHeaderViewLayout.setVisibility(View.VISIBLE);
                    mHeaderImage.setImageBitmap(bitmap);
                }
                mTitleView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_fading));
            }
        }

        @Override
        public void onBitmapFailed(Drawable drawable) {
            if (getActivity() != null && !getActivity().isFinishing()) {
                if (mHeaderViewLayout != null && mHeaderImage != null) {
                    mHeaderViewLayout.setVisibility(View.GONE);
                }
                mTitleView.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
            }
        }

        @Override
        public void onPrepareLoad(Drawable drawable) {
            if (getActivity() != null && !getActivity().isFinishing()) {
                if (mHeaderViewLayout != null && mHeaderImage != null) {
                    mHeaderViewLayout.setVisibility(View.GONE);
                }
                mTitleView.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
            }
        }
    }


}
