package com.nollpointer.dates.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nollpointer.dates.R;

public class DistributionCardsAdapter extends RecyclerView.Adapter<DistributionCardsAdapter.ViewHolder>{
    private DistributionCardsAdapter.Listener mListener;

    public interface Listener {
        void onClick(int position);
    }

    public void setListener(DistributionCardsAdapter.Listener listener) {
        mListener = listener;
    }


    public DistributionCardsAdapter() {

    }

    @Override
    public DistributionCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_distribution, parent, false);
        return new DistributionCardsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DistributionCardsAdapter.ViewHolder holder, final int position) {



    }

    @Override
    public int getItemCount() {
        return 16;
    }


    public void onItemDismiss(int position) {
        //mItems.remove(position);
        notifyItemRemoved(position);
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View view) {
            super(view);
        }
    }
}
