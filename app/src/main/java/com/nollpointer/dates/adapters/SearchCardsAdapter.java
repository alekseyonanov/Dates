package com.nollpointer.dates.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nollpointer.dates.Date;
import com.nollpointer.dates.R;

import java.util.List;

public class SearchCardsAdapter extends RecyclerView.Adapter<SearchCardsAdapter.ViewHolder> {

    private List<Date> dates;

    private SearchCardsAdapter.Listener listener;

    public SearchCardsAdapter(List<Date> dates){
        this.dates = dates;
    }

    @Override
    public SearchCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView c = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.dates_cards, parent, false);
        return new SearchCardsAdapter.ViewHolder(c);
    }

    @Override
    public void onBindViewHolder(SearchCardsAdapter.ViewHolder holder, final int position) {
        Date date = dates.get(position);
        final CardView cardView = holder.mCardView;
        TextView textView = cardView.findViewById(R.id.date_number);
        textView.setText(date.getDate());
        textView = cardView.findViewById(R.id.date_event);
        textView.setText(date.getEvent());
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public void refreshList(List<Date> dates){
        this.dates = dates;
        notifyDataSetChanged();
    }

    public void setListener(SearchCardsAdapter.Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onItemClick(Date clickedDate);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;

        ViewHolder(CardView c) {
            super(c);
            mCardView = c;
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(dates.get(getAdapterPosition()));
                }
            });
        }
    }
}