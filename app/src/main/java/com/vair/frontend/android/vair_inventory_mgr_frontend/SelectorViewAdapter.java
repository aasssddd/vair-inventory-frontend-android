package com.vair.frontend.android.vair_inventory_mgr_frontend;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by vair on 2016/1/15.
 */
public class SelectorViewAdapter extends RecyclerView.Adapter<SelectorViewAdapter.ViewHolder>{

    private String[] dataSet;

    public SelectorViewAdapter(String[] dataset) {
        this.dataSet = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.selector_card_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(dataSet[position]);
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.info_text)
        TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
