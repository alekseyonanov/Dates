package com.nollpointer.dates;

import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.TreeMap;

public class DatesCardsAdapter extends RecyclerView.Adapter<DatesCardsAdapter.ViewHolder>{
    private Cursor crs;
    private int DATE = 0, DATE_WITH_MARGIN = 1;
    private int mode;
    private TreeMap<Integer,String> top_texts = new TreeMap<>();
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView mCardView;
        ViewHolder(CardView c){
            super(c);
            mCardView = c;
        }
    }
    public DatesCardsAdapter(Cursor crs,int mode,String[] text_tops){
        this.crs = crs;
        this.mode = mode;
        fill_top_texts(text_tops);
    }

    @Override
    public DatesCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView c;
        if(viewType == DATE)
            c = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.dates_cards,parent,false);
        else
            c = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.dates_cards_top_text,parent,false);
        return new ViewHolder(c);
    }

    @Override
    public void onBindViewHolder(DatesCardsAdapter.ViewHolder holder, final int position) {
            if (crs.moveToPosition(position)) {
                CardView cardView = holder.mCardView;
                TextView textView = cardView.findViewById(R.id.date_number);
                textView.setText(crs.getString(0));
                textView = cardView.findViewById(R.id.date_event);
                textView.setText(crs.getString(1));
                if(top_texts.containsKey(position)){
                    textView = cardView.findViewById(R.id.date_top_text);
                    textView.setText(top_texts.get(Integer.valueOf(position)));
                }
            }
    }


    private void fill_top_texts(String[] texts){
        if(mode == 0){
            int[] positions = {0,21,41,76,107,147,195,242,284,334};
            for(int i=0;i<positions.length;i++){
                top_texts.put(positions[i],texts[i]);
            }
        }else{
            int[] positions = {0,48};
            for(int i=0;i<positions.length;i++){
                top_texts.put(positions[i],texts[i]);
            }
        }
    }



    @Override
    public int getItemCount() {
        return crs.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        if(top_texts.containsKey(position))
            return DATE_WITH_MARGIN;
        else
            return DATE;
    }
}
