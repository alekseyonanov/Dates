package com.nollpointer.dates.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nollpointer.dates.Date;
import com.nollpointer.dates.MainActivity;
import com.nollpointer.dates.R;
import com.nollpointer.dates.constants.DatesCategoryConstants;

import java.util.List;
import java.util.TreeMap;

public class DatesCardsAdapter extends RecyclerView.Adapter<DatesCardsAdapter.ViewHolder> {

    private List<Date> dates;

    private static final int DATE = 0;
    private static final int DATE_WITH_MARGIN = 1;
    public static final int DEFAULT_TEXT_SIZE = 14;
    private int mode;
    private TreeMap<Integer, String> main_top_texts = new TreeMap<>();
    private TreeMap<Integer, String> add_top_texts = new TreeMap<>();
    private int fontSize = 14;
    private Listener listener;
    private boolean searchMode = false;

    private int dateCardId;
    private int dateCardTopTextId;


    public interface Listener {
        void onItemClick(Date clickedDate);
    }

    public DatesCardsAdapter(List<Date> dates, int mode, String[] main_text_tops, String[] additional_text_tops) {
        this.dates = dates;
        this.mode = mode;
        fillTopTexts(main_text_tops, additional_text_tops);
    }

    public void setViewIds(int dateCardId, int dateCardTopTextId) {
        this.dateCardId = dateCardId;
        this.dateCardTopTextId = dateCardTopTextId;
    }

    public int getFontSize() {
        return fontSize;
    }

    @Override
    public DatesCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case DATE:
                view = LayoutInflater.from(parent.getContext()).inflate(dateCardId, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(dateCardTopTextId, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DatesCardsAdapter.ViewHolder holder, final int position) {
        Date date = dates.get(position);
        final View cardView = holder.itemView;
        TextView textView = cardView.findViewById(R.id.text1);
        textView.setText(date.getDate());
        textView = cardView.findViewById(R.id.text2);
        textView.setText(date.getEvent());
        textView = cardView.findViewById(R.id.text3);

        if (date.containsMonth())
            textView.setText(date.getMonth());
        else
            textView.setText("");

        if (getItemViewType(position) == DATE_WITH_MARGIN) {
            textView = cardView.findViewById(R.id.textTitle);
            textView.setText(main_top_texts.get(Integer.valueOf(position)));
            //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(dates.get(position));
            }
        });
    }

    private void fillTopTexts(String[] texts, String[] add_texts) {
        int[] positions_main = {0, 21, 41, 76, 107, 147, 195, 242, 284, 334};
        int[] positions_easy = {0, 48};
        for (int i = 0; i < positions_main.length; i++)
            main_top_texts.put(positions_main[i], texts[i]);
        for (int i = 0; i < positions_easy.length; i++)
            add_top_texts.put(positions_easy[i], add_texts[i]);
        if (mode == MainActivity.EASY_DATES_MODE)
            change_top_texts();
    }

    public void change_top_texts() {
        TreeMap<Integer, String> tree = main_top_texts;
        main_top_texts = add_top_texts;
        add_top_texts = tree;
    }

    public void startSearchMode(){
        searchMode = true;
        for(Integer position: main_top_texts.keySet())
            notifyItemChanged(position);
        for(Integer position: add_top_texts.keySet())
            notifyItemChanged(position);
    }

    public void stopSearchMode(List<Date> dates){
        searchMode = false;
        this.dates = dates;
        notifyDataSetChanged();
    }

    public void refresh(List<Date> dates) {
        change_top_texts();
        this.dates = dates;
        //refreshMarginDates();
        notifyDataSetChanged();
    }

    public void refresh(List<Date> dates, int category) {

        searchMode = category != DatesCategoryConstants.ALL;
        this.dates = dates;

        refreshMarginDates();

        notifyDataSetChanged();
    }

    private void refreshMarginDates() {
        for (int position : main_top_texts.keySet()) {
            notifyItemChanged(position);
        }
    }


    public boolean changeFontSize(int m) {
        fontSize += m;
        notifyDataSetChanged();
        return isFontSizeChanged();
    }

    private boolean isFontSizeChanged() {
        return fontSize == DEFAULT_TEXT_SIZE;
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (!searchMode && main_top_texts.containsKey(position))
            return DATE_WITH_MARGIN;
        else
            return DATE;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View c) {
            super(c);
        }
    }
}
