package com.nollpointer.dates;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class TestMenuCardsAdapter extends RecyclerView.Adapter<TestMenuCardsAdapter.ViewHolder> {
    private String[] title_texts;
    private String[] subtitle_texts;
    private int[] imageIds;
    private Listener mListener;
    private int DIVIDER = 0, CONTENT = 1;

    public static interface Listener{
        void onClick(int position);
    }

    public void setListner(Listener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView mCardView;
        ViewHolder(CardView c){
            super(c);
            mCardView = c;
        }

    }
    public TestMenuCardsAdapter(String[] title,String[] subtitle,int[] i){
        title_texts = title;
        subtitle_texts = subtitle;
        imageIds = i;
    }

    @Override
    public TestMenuCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView c;
        if(viewType == CONTENT)
            c = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.test_menu_cards,parent,false);
        else
            c = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.divider_layout,parent,false);
        return new ViewHolder(c);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(getItemViewType(position) == CONTENT) {
            CardView cardView = holder.mCardView;
            TextView textView = cardView.findViewById(R.id.info_text_title);
            textView.setText(title_texts[position]);
            textView = cardView.findViewById(R.id.info_text_subtitle);
            textView.setText(subtitle_texts[position]);
            ImageView imageView = cardView.findViewById(R.id.info_image);
            imageView.setImageResource(imageIds[position]);
            imageView.setContentDescription(title_texts[position]);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null)
                        mListener.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return title_texts.length;
    }

    @Override
    public int getItemViewType(int position) {
        if(title_texts[position].equals("DIVIDER"))
            return DIVIDER;
        else return CONTENT;
    }
}
