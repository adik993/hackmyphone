package com.adrian.hackmyphone.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.adrian.hackmyphone.databinders.InfoItemDataBinder;
import com.adrian.hackmyphone.databinders.ProgressItemDataBinder;
import com.adrian.hackmyphone.databinders.SensorDataBinder;
import com.adrian.hackmyphone.databinders.SimpleCardDataBinder;
import com.adrian.hackmyphone.databinders.SimpleTextDataBinder;
import com.adrian.hackmyphone.items.InfoItem;
import com.adrian.hackmyphone.items.IntentItem;
import com.adrian.hackmyphone.items.ProgressItem;
import com.adrian.hackmyphone.items.Section;
import com.adrian.hackmyphone.items.SensorItem;
import com.adrian.hackmyphone.items.Titled;

import java.util.List;

/**
 * Created by student215 on 2016-03-07.
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected SparseArray<DataBinder> mDataBinders=new SparseArray<>();

    public List<Object> elems;
    OnClickListener listener;

    public MyAdapter(List<Object> elems) {
        this.elems = elems;
        mDataBinders.put(1, new SensorDataBinder(this));
        mDataBinders.put(2, new SimpleTextDataBinder());
        mDataBinders.put(3, new SimpleCardDataBinder());
        mDataBinders.put(4, new InfoItemDataBinder());
        mDataBinders.put(5, new ProgressItemDataBinder());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DataBinder dataBinder = mDataBinders.get(viewType);
        if(dataBinder==null) throw new IllegalStateException("No data binder for view type: "+viewType);
        RecyclerView.ViewHolder viewHolder = dataBinder.onCreateViewHolder(LayoutInflater.from(parent.getContext()), parent);
        viewHolder.itemView.setOnClickListener(v -> {
            int i = viewHolder.getAdapterPosition();
            if (i != -1 && listener != null) listener.onItemClick(i, viewHolder);
        });
        viewHolder.itemView.setOnLongClickListener(v -> {
            int i = viewHolder.getAdapterPosition();
            if(i!=-1 && listener!=null) return listener.onItemLongClick(i, viewHolder);
            else return false;
        });
        return viewHolder;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        DataBinder dataBinder = mDataBinders.get(viewType);
        if(dataBinder==null) throw new IllegalStateException("No data binder for view type: "+viewType);
        dataBinder.onBindViewHolder(elems.get(position), holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        Object o = elems.get(position);
        if(o instanceof SensorItem) return 1;
        else if(o instanceof IntentItem) return 3;
        else if(o instanceof InfoItem) return 4;
        else if(o instanceof Titled) return 2;
        else if(o instanceof ProgressItem) return 5;
        else return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return elems.size();
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }
}
