package site.chniccs.basefrm.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import site.chniccs.basefrm.holder.base.BaseRCHolder;
import site.chniccs.basefrm.listener.ItemClickListener;
import site.chniccs.basefrm.listener.ItemLongClickListener;


public abstract class BaseRCAdapter<T extends List<?>>
        extends RecyclerView.Adapter<BaseRCHolder> {
    protected T mData;
    private ItemClickListener myItemClickListener;
    private ItemLongClickListener myItemLongClickListener;

    public void addData(T paramT) {
    }

    public abstract BaseRCHolder getItemHolder(ViewGroup paramViewGroup, int paramInt);

    public void onBindViewHolder(BaseRCHolder paramBaseRCHolder, int paramInt) {
        setItemData(paramBaseRCHolder, paramInt);
        paramBaseRCHolder.bindViewHolder(paramInt);

    }

    public BaseRCHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        BaseRCHolder localBaseRCHolder = getItemHolder(paramViewGroup, paramInt);
        if ((this.myItemClickListener != null) && (localBaseRCHolder != null)) {
            localBaseRCHolder.setOnClickListener(this.myItemClickListener);
        }
        if ((this.myItemLongClickListener != null) && (localBaseRCHolder != null)) {
            localBaseRCHolder.setOnLongClickListener(this.myItemLongClickListener);
        }
        return localBaseRCHolder;
    }

    public void setData(T paramT) {
        this.mData = paramT;
    }

    protected void setItemData(BaseRCHolder paramBaseRCHolder, int paramInt) {
        paramBaseRCHolder.setData(mData.get(paramInt));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setOnItemClickListener(ItemClickListener paramItemClickListener) {
        this.myItemClickListener = paramItemClickListener;
    }

    public void setOnItemLongClickListener(ItemLongClickListener paramItemLongClickListener) {
        this.myItemLongClickListener = paramItemLongClickListener;
    }
}
