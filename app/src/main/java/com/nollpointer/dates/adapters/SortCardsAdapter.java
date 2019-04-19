package com.nollpointer.dates.adapters;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nollpointer.dates.R;

public class SortCardsAdapter extends RecyclerView.Adapter<SortCardsAdapter.ViewHolder>{
    private SortCardsAdapter.Listener mListener;

    public interface Listener {
        void onClick(int position);
    }

    public void setListener(SortCardsAdapter.Listener listener) {
        mListener = listener;
    }


    public SortCardsAdapter() {

    }

    @Override
    public SortCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView c = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_card, parent, false);
        return new SortCardsAdapter.ViewHolder(c);
    }

    @Override
    public void onBindViewHolder(SortCardsAdapter.ViewHolder holder, final int position) {


    }

    @Override
    public int getItemCount() {
        return 16;
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(CardView c) {
            super(c);
        }
    }
}