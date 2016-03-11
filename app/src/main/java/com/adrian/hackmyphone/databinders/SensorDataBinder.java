package com.adrian.hackmyphone.databinders;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.adrian.hackmyphone.R;
import com.adrian.hackmyphone.adapter.DataBinder;
import com.adrian.hackmyphone.adapter.MyAdapter;
import com.adrian.hackmyphone.items.SensorItem;
import com.adrian.hackmyphone.viewholders.ExpandableViewHolder;

/**
 * Created by adrian on 3/10/16.
 */
public class SensorDataBinder implements DataBinder<SensorItem,ExpandableViewHolder> {

    MyAdapter mAdapter;

    public SensorDataBinder(MyAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public ExpandableViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        ExpandableViewHolder holder = new ExpandableViewHolder(inflater.inflate(R.layout.expandable_item, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(SensorItem elem, ExpandableViewHolder holder, int position) {
        holder.text.setText(elem.getSensor().getName());
        holder.expandable.setExpanded(false, elem.isRegistered());
        if(elem.isRegistered()) {
            float[] values = elem.getValues();
            StringBuilder builder=new StringBuilder();
            for(int i=0; i<values.length; i++) {
                if(i!=values.length-1) builder.append(String.format("%.4f",values[i])+", ");
                else builder.append(values[i]);
            }
            holder.values.setText("Values: " + builder.toString());
        } else {
            holder.values.setText("");
        }
    }
}
