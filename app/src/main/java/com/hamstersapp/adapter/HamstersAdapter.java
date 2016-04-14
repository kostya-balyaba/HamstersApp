package com.hamstersapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.hamstersapp.BaseActivity;
import com.hamstersapp.R;
import com.hamstersapp.model.HamsterModel;
import com.hamstersapp.utils.CircleTransform;
import com.hamstersapp.utils.PlaceHolders;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Костя on 14.04.2016.
 */

public class HamstersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {


    private Context mContext;

    /*Contains the list of PhotoPlaceModel that represent the data of this Adapter.*/
    private List<HamsterModel> mData;

    // A copy of the original mPhotoPlaces array, initialized from and then used instead as soon as
    // the mFilter PhotoPlacesFilter is used. mPhotoPlaces will then only contain the filtered values.
    private ArrayList<HamsterModel> mOriginalValues;
    private final Object mLock = new Object();
    private HamsterAdapterCallback mCallback;
    private HamstersFilter mFilter;
    private int size;
    protected Drawable mPlaceholder;


    public interface HamsterAdapterCallback {
        void showFilterEmptyResult();

        void showFilterResult();
    }


    public HamstersAdapter(List<HamsterModel> data, Context context, HamsterAdapterCallback callback) {
        this.mData = data;
        this.mContext = context;
        this.mCallback = callback;
        size = (int) ((BaseActivity) context).px2dp(100);
        mPlaceholder = PlaceHolders.round(size / 2, ContextCompat.getColor(context, android.R.color.white));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_hamster, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bind(mData.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.hamster_picture)
        protected ImageView imageView;
        @Bind(R.id.hamster_title)
        protected TextView titleView;
        @Bind(R.id.description_text_view)
        protected TextView descriptionView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(HamsterModel model) {
            if (model != null) {
                titleView.setText(!TextUtils.isEmpty(model.getTitle()) ? model.getTitle() : "");
                descriptionView.setText(!TextUtils.isEmpty(model.getDescription()) ? model.getDescription() : "");
                if (!TextUtils.isEmpty(model.getImage()))
                    Picasso.with(mContext)
                            .load(model.getImage())
                            .placeholder(mPlaceholder)
                            .error(mPlaceholder)
                            .resize(size, size)
                            .transform(new CircleTransform())
                            .centerCrop()
                            .into(imageView);
                else
                    imageView.setImageDrawable(mPlaceholder);
            }
        }
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null)
            mFilter = new HamstersFilter();
        return mFilter;
    }

    class HamstersFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(mData);
                }
            }

            if (constraint == null || constraint.length() == 0) {
                ArrayList<HamsterModel> list;
                synchronized (mLock) {
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = constraint.toString().toLowerCase();

                ArrayList<HamsterModel> values;
                synchronized (mLock) {
                    values = new ArrayList<>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<HamsterModel> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final HamsterModel value = values.get(i);
                    final String valueText = value.getTitle().toLowerCase();

                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (String word : words) {

                            if (word.startsWith(prefixString) || word.contains(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                if (mData.size() != 0)
                    mData.clear();
                mData.addAll((ArrayList) results.values);
                notifyDataSetChanged();
                mCallback.showFilterResult();
            } else {
                mCallback.showFilterEmptyResult();
            }
        }
    }

}
