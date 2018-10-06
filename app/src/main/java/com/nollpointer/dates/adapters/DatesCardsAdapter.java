package com.nollpointer.dates.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nollpointer.dates.Date;
import com.nollpointer.dates.constants.DatesCategoryConstants;
import com.nollpointer.dates.MainActivity;
import com.nollpointer.dates.R;

import java.util.List;
import java.util.TreeMap;

public class DatesCardsAdapter extends RecyclerView.Adapter<DatesCardsAdapter.ViewHolder>{

    private List<Date> dates;

    private static final int DATE = 0;
    private static final int DATE_WITH_MARGIN = 1;
    public static final int DEFAULT_TEXT_SIZE = 14;
    private int mode;
    private TreeMap<Integer,String> main_top_texts = new TreeMap<>();
    private TreeMap<Integer,String> add_top_texts = new TreeMap<>();
    private int fontSize = 14;
    private Listener listener;
    private boolean isCategoryShow = false;


    public interface Listener{
        void onItemClick(Date clickedDate);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView mCardView;
        ViewHolder(CardView c){
            super(c);
            mCardView = c;
        }
    }

    public DatesCardsAdapter(List<Date> dates, int mode, String[] main_text_tops, String[] additional_text_tops){
        this.dates = dates;
        this.mode = mode;
        isCategoryShow = false;
        fill_top_texts(main_text_tops,additional_text_tops);
    }

    public DatesCardsAdapter(List<Date> dates,int mode,String[] main_text_tops,String[] additional_text_tops,int fons_size){
        this(dates,mode,main_text_tops,additional_text_tops);
        this.fontSize = fons_size;
        fill_top_texts(main_text_tops,additional_text_tops);
    }

    public int getFontSize(){
        return fontSize;
    }

    @Override
    public DatesCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView c;
        if(isCategoryShow)
            c = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.dates_cards,parent,false);
        else
            switch (viewType) {
                case DATE:
                    c = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.dates_cards,parent,false);
                    break;
                default:
                    c = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.dates_cards_top_text,parent,false);
            }
        return new ViewHolder(c);
    }

    @Override
    public void onBindViewHolder(DatesCardsAdapter.ViewHolder holder,final int position) {
        Date date = dates.get(position);
        final CardView cardView = holder.mCardView;
        TextView textView = cardView.findViewById(R.id.date_number);
        textView.setText(date.getDate());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize + 4);
        textView = cardView.findViewById(R.id.date_event);
        textView.setText(date.getEvent());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        if (getItemViewType(position) == DATE_WITH_MARGIN && !isCategoryShow) {
            textView = cardView.findViewById(R.id.date_top_text);
            textView.setText(main_top_texts.get(Integer.valueOf(position)));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        }
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(dates.get(position));
            }
        });
    }

    private void fill_top_texts(String[] texts,String[] add_texts){
            int[] positions_main = {0,21,41,76,107,147,195,242,284,334};
            int[] positions_easy = {0,48};
            for(int i=0;i<positions_main.length;i++)
                main_top_texts.put(positions_main[i],texts[i]);
            for(int i=0;i<positions_easy.length;i++)
                add_top_texts.put(positions_easy[i],add_texts[i]);
            if(mode==MainActivity.EASY_DATES_MODE)
                change_top_texts();
    }

    public void change_top_texts(){
        TreeMap<Integer,String> tree = main_top_texts;
        main_top_texts = add_top_texts;
        add_top_texts = tree;
    }

    public void refresh(List<Date> dates){
        change_top_texts();
        this.dates = dates;
        //refreshMarginDates();
        notifyDataSetChanged();
    }

    public void refresh(List<Date> dates, int category){

        isCategoryShow =  category != DatesCategoryConstants.ALL;
        this.dates = dates;

        refreshMarginDates();

        notifyDataSetChanged();
    }

    private void refreshMarginDates(){
        for(int position : main_top_texts.keySet()){
            notifyItemChanged(position);
        }
    }


    public boolean changeFontSize(int m){
        fontSize += m;
        notifyDataSetChanged();
        return isFontSizeChanged();
    }

    private boolean isFontSizeChanged(){
        return fontSize == DEFAULT_TEXT_SIZE;
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if(isFontSizeChanged()) {
            CardView cardView = holder.mCardView;
            //TextView text = cardView.
        }

    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        //CardView cardView = holder.mCardView;
    }


    @Override
    public int getItemCount() {
        return dates.size();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if(isCategoryShow)
            return DATE;
        if(main_top_texts.containsKey(position))
            return DATE_WITH_MARGIN;
        else
            return DATE;
    }
}
