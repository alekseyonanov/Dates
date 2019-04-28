package com.nollpointer.dates.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nollpointer.dates.Date;
import com.nollpointer.dates.R;

import java.util.ArrayList;
import java.util.List;

public class SortCardsAdapter extends RecyclerView.Adapter<SortCardsAdapter.ViewHolder> {
    private SortCardsAdapter.Listener listener;

    private int itemCount = 3;
    private List<Date> dates;

    private ArrayList<Integer> sequence = new ArrayList<>();

    public interface Listener {
        void onClick(int position);
    }

    @Override
    public SortCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_sort, parent, false);
        return new SortCardsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SortCardsAdapter.ViewHolder holder, final int position) {
        View view = holder.itemView;
        TextView mainTextView = view.findViewById(R.id.textMain);
        TextView numberTextView = view.findViewById(R.id.textNumber);
        numberTextView.setText(Integer.toString(position + 1));
        mainTextView.setText(dates.get(position).getEvent());
        sequence.add(position);
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);

        int fromNumber = sequence.get(fromPosition);
        int toNumber = sequence.get(toPosition);

        sequence.set(toPosition,fromNumber);
        sequence.set(fromPosition,toNumber);

        return true;
    }


    public void setListener(SortCardsAdapter.Listener listener) {
        this.listener = listener;
    }

    public void setItemCount(int count) {
        this.itemCount = count;
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
        sequence.clear();
    }

    public List<Integer> getAnswerSequence(){
        return sequence;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(getAdapterPosition());
                }
            });
        }
    }
}