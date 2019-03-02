package com.nollpointer.dates.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nollpointer.dates.R;

public class StatisticsCardsAdapter extends RecyclerView.Adapter<StatisticsCardsAdapter.ViewHolder>{
    private StatisticsCardsAdapter.Listener mListener;

    public interface Listener {
        void onClick(int position);
    }

    public void setListener(StatisticsCardsAdapter.Listener listener) {
        mListener = listener;
    }


    public StatisticsCardsAdapter() {

    }

    @Override
    public StatisticsCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView c = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_card, parent, false);
        return new StatisticsCardsAdapter.ViewHolder(c);
    }

    @Override
    public void onBindViewHolder(StatisticsCardsAdapter.ViewHolder holder, final int position) {


    }

    @Override
    public int getItemCount() {
        return 16;
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
//        if (fromPosition < toPosition) {
//            for (int i = fromPosition; i < toPosition; i++) {
//                Collections.swap(mItems, i, i + 1);
//            }
//        } else {
//            for (int i = fromPosition; i > toPosition; i--) {
//                Collections.swap(mItems, i, i - 1);
//            }
//        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(CardView c) {
            super(c);
//            c.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mListener != null)
//                        mListener.onClick(getAdapterPosition());
//                }
//            });
        }
    }
}