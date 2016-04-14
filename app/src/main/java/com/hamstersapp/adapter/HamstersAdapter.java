package com.hamstersapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hamstersapp.R;
import com.hamstersapp.model.HamsterModel;
import com.hamstersapp.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Костя on 14.04.2016.
 */

public class HamstersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HamsterModel> mData;
    private Context mContext;

    public HamstersAdapter(List<HamsterModel> data, Context context) {
        this.mData = data;
        this.mContext = context;
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
        protected ImageView image;
        @Bind(R.id.hamster_title)
        protected TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(HamsterModel model) {
            if (model != null) {
                title.setText(!TextUtils.isEmpty(model.getTitle()) ? model.getTitle() : "");
                if (!TextUtils.isEmpty(model.getImage()))
                    Picasso.with(mContext)
                            .load(model.getImage())
                            .transform(new CircleTransform())
                            .into(image);

            }
        }
    }


}
