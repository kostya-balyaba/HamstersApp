package com.hamstersapp.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hamstersapp.R;

import java.util.ArrayList;
import java.util.List;


public class WowRecyclerView extends RecyclerView {

    private final List<OnScrollListener> onScrollListeners = new ArrayList<>();
    private static final long HEADER_ID = -23;
    private EndlessScrollListener endlessScrollListener;
    private AdapterWrapper adapterWrapper;
    private View progressView;
    private View headerView;
    private boolean refreshing;
    private int threshold = 1;

    public WowRecyclerView(Context context) {
        this(context, null);
    }

    public WowRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WowRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        super.setOnScrollListener(new OnScrollListenerImpl());
    }

    @Override
    public void setAdapter(Adapter adapter) {
        //noinspection unchecked
        adapterWrapper = new AdapterWrapper(adapter);
        super.setAdapter(adapterWrapper);
    }

    @Override
    public Adapter getAdapter() {
        return adapterWrapper.getAdapter();
    }

    /**
     * Use {@link #addOnScrollListener(OnScrollListener)} and
     * {@link #removeOnScrollListener(OnScrollListener)} methods instead. Calling this method will
     * cause {@link UnsupportedOperationException}.
     */
    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        throw new UnsupportedOperationException("use addOnScrollListener(OnScrollListener) and " +
                "removeOnScrollListener(OnScrollListener) instead");
    }

    /**
     * @param layout instances of {@link LinearLayoutManager} only
     */
    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (layout instanceof LinearLayoutManager) {
            super.setLayoutManager(layout);
        } else {
            throw new IllegalArgumentException(
                    "layout manager must be an instance of LinearLayoutManager");
        }
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return (LinearLayoutManager) super.getLayoutManager();
    }

    /**
     * Adds {@link RecyclerView.OnScrollListener} to use with this view.
     *
     * @param listener listener to add
     */
    public void addOnScrollListener(OnScrollListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener is null");
        }
        onScrollListeners.add(listener);
    }

    /**
     * Removes {@link RecyclerView.OnScrollListener} to use with this view.
     *
     * @param listener listener to remove
     */
    public void removeOnScrollListener(OnScrollListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener is null");
        }
        onScrollListeners.remove(listener);
    }

    /**
     * Sets {@link WowRecyclerView.Pager} to use with the view.
     *
     * @param pager pager to set or {@code null} to clear current pager
     */
    public void setPager(Pager pager) {
        if (pager != null) {
            endlessScrollListener = new EndlessScrollListener(pager);
            endlessScrollListener.setThreshold(threshold);
            addOnScrollListener(endlessScrollListener);
        } else if (endlessScrollListener != null) {
            removeOnScrollListener(endlessScrollListener);
            endlessScrollListener = null;
        }
    }

    /**
     * Sets threshold to use. Only positive numbers are allowed. This value is used to determine if
     * loading should start when user scrolls the view down. Default value is 1.
     *
     * @param threshold positive number
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
        if (endlessScrollListener != null) {
            endlessScrollListener.setThreshold(threshold);
        }
    }

    /**
     * Sets progress view to show on the bottom of the list when loading starts.
     *
     * @param layoutResId layout resource ID
     */
    public void setProgressView(int layoutResId) {
        setProgressView(LayoutInflater
                .from(getContext())
                .inflate(layoutResId, this, false));
    }

    /**
     * Sets progress view to show on the bottom of the list when loading starts.
     *
     * @param view the view
     */
    public void setProgressView(View view) {
        progressView = view;
    }

    /**
     * If async operationId completed you may want to call this method to hide progress view.
     *
     * @param refreshing {@code true} if list is currently refreshing, {@code false} otherwise
     */
    public void setRefreshing(boolean refreshing) {
        if (this.refreshing == refreshing) {
            return;
        }
        this.refreshing = refreshing;
        this.adapterWrapper.notifyDataSetChanged();
    }

    public View getHeaderView() {
        return headerView;
    }

    public void addHeaderView(View headerView) {
        this.headerView = headerView;
        if (adapterWrapper != null)
            adapterWrapper.notifyDataSetChanged();
    }

    public void removeHeaderView() {
        this.headerView = null;
    }

    private final class OnScrollListenerImpl extends OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            for (OnScrollListener listener : onScrollListeners) {
                listener.onScrolled(recyclerView, dx, dy);
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            for (OnScrollListener listener : onScrollListeners) {
                listener.onScrollStateChanged(recyclerView, newState);
            }
        }
    }

    private final class EndlessScrollListener extends OnScrollListener {

        private final Pager pager;

        private int threshold = 1;

        public EndlessScrollListener(Pager pager) {
            if (pager == null) {
                throw new NullPointerException("pager is null");
            }
            this.pager = pager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int lastVisibleItemPosition = getLayoutManager()
                    .findLastVisibleItemPosition();
            int lastItemPosition = getAdapter().getItemCount();

            if (!refreshing && pager.shouldLoad() && lastItemPosition - lastVisibleItemPosition <= threshold) {
                setRefreshing(true);
                pager.loadNextPage();
            }
        }

        public void setThreshold(int threshold) {
            if (threshold <= 0) {
                throw new IllegalArgumentException("illegal threshold: " + threshold);
            }
            this.threshold = threshold;
        }
    }

    private final class AdapterWrapper extends Adapter<ViewHolder> {

        private static final int HEADER_VIEW_TYPE = -2;
        private static final int PROGRESS_VIEW_TYPE = -1;

        private final Adapter<ViewHolder> adapter;

        private ProgressViewHolder progressViewHolder;
        private HeaderViewHolder headerViewHolder;

        public AdapterWrapper(Adapter<ViewHolder> adapter) {
            if (adapter == null) {
                throw new NullPointerException("adapter is null");
            }
            this.adapter = adapter;
            setHasStableIds(adapter.hasStableIds());
        }

        private int getHeaderOffset() {
            return headerView != null ? 1 : 0;
        }

        @Override
        public int getItemCount() {
            return adapter.getItemCount()
                    + (refreshing && progressView != null ? 1 : 0)
                    + getHeaderOffset();
        }

        @Override
        public long getItemId(int position) {
            if (position == 0 && headerView != null) {
                return HEADER_ID;
            }
            return position == (adapter.getItemCount() + getHeaderOffset()) ? NO_ID
                    : adapter.getItemId(position - getHeaderOffset());
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 && headerView != null) {
                return HEADER_VIEW_TYPE;
            }
            return refreshing & position == (adapter.getItemCount() + getHeaderOffset())
                    ? PROGRESS_VIEW_TYPE :
                    adapter.getItemViewType(position - getHeaderOffset());
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if ((headerView == null || position != 0) && (position < (adapter.getItemCount() + getHeaderOffset()))) {
                adapter.onBindViewHolder(holder, position - getHeaderOffset());
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == HEADER_VIEW_TYPE) {
                return headerViewHolder = new HeaderViewHolder();
            }
            return viewType == PROGRESS_VIEW_TYPE ? progressViewHolder = new ProgressViewHolder() :
                    adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            super.onDetachedFromRecyclerView(recyclerView);
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public boolean onFailedToRecycleView(ViewHolder holder) {
            return holder == headerViewHolder
                    || holder == progressViewHolder
                    || adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            if (holder == progressViewHolder || holder == headerViewHolder) {
                return;
            }
            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            if (holder == progressViewHolder || holder == headerViewHolder) {
                return;
            }
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            if (holder == progressViewHolder || holder == headerViewHolder) {
                return;
            }
            adapter.onViewRecycled(holder);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            super.registerAdapterDataObserver(observer);
            adapter.registerAdapterDataObserver(observer);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            super.unregisterAdapterDataObserver(observer);
            adapter.unregisterAdapterDataObserver(observer);
        }

        public Adapter<ViewHolder> getAdapter() {
            return adapter;
        }

        private final class ProgressViewHolder extends ViewHolder {
            public ProgressViewHolder() {
                super(progressView);
            }
        }

        private final class HeaderViewHolder extends ViewHolder {
            public HeaderViewHolder() {
                super(headerView);
            }
        }
    }

    /**
     * Pager interface.
     */
    public interface Pager {
        /**
         * @return {@code true} if pager should load new page
         */
        boolean shouldLoad();

        /**
         * Starts loading operationId.
         */
        void loadNextPage();
    }

    public static class ItemClickSupport {
        private final RecyclerView mRecyclerView;
        private OnItemClickListener mOnItemClickListener;
        private OnItemLongClickListener mOnItemLongClickListener;
        private OnClickListener mOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int offset = 0;
                    if (mRecyclerView instanceof WowRecyclerView) {
                        offset = ((WowRecyclerView) mRecyclerView).getHeaderView() != null ? 1 : 0;
                    }
                    RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                    mOnItemClickListener.onItemClicked(mRecyclerView, holder.getAdapterPosition() - offset, v);
                }
            }
        };
        private OnLongClickListener mOnLongClickListener = new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    int offset = 0;
                    if (mRecyclerView instanceof WowRecyclerView) {
                        offset = ((WowRecyclerView) mRecyclerView).getHeaderView() != null ? 1 : 0;
                    }
                    RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                    return mOnItemLongClickListener.onItemLongClicked(mRecyclerView, holder.getAdapterPosition() - offset, v);
                }
                return false;
            }
        };
        private RecyclerView.OnChildAttachStateChangeListener mAttachListener
                = new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                if (mRecyclerView != null && mRecyclerView.getChildItemId(view) != NO_ID
                        && mRecyclerView.getChildItemId(view) != HEADER_ID) {
                    if (mOnItemClickListener != null) {
                        view.setOnClickListener(mOnClickListener);
                    }
                    if (mOnItemLongClickListener != null) {
                        view.setOnLongClickListener(mOnLongClickListener);
                    }
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {

            }
        };

        private ItemClickSupport(RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
            mRecyclerView.setTag(R.id.item_click_support, this);
            mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener);
        }

        public static ItemClickSupport addTo(RecyclerView view) {
            ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
            if (support == null) {
                support = new ItemClickSupport(view);
            }
            return support;
        }

        public static ItemClickSupport removeFrom(RecyclerView view) {
            ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
            if (support != null) {
                support.detach(view);
            }
            return support;
        }

        public ItemClickSupport setOnItemClickListener(OnItemClickListener listener) {
            mOnItemClickListener = listener;
            return this;
        }

        public ItemClickSupport setOnItemLongClickListener(OnItemLongClickListener listener) {
            mOnItemLongClickListener = listener;
            return this;
        }

        private void detach(RecyclerView view) {
            view.removeOnChildAttachStateChangeListener(mAttachListener);
            view.setTag(R.id.item_click_support, null);
        }

        public interface OnItemClickListener {

            void onItemClicked(RecyclerView recyclerView, int position, View v);
        }

        public interface OnItemLongClickListener {

            boolean onItemLongClicked(RecyclerView recyclerView, int position, View v);
        }
    }
}
