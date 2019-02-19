package com.nollpointer.dates.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nollpointer.dates.R;

public class GameCardsAdapter extends RecyclerView.Adapter<GameCardsAdapter.ViewHolder> {
    private PractiseCardsAdapter.Listener mListener;
    //private static final int DIVIDER = 0, CONTENT = 1;

    public interface Listener {
        void onClick(int position);
    }

    public void setListener(PractiseCardsAdapter.Listener listener) {
        mListener = listener;
    }


    public GameCardsAdapter() {

    }

    @Override
    public GameCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView c;
        //if (viewType == CONTENT)
        c = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.game_card, parent, false);
        //else
        //    c = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.divider_layout, parent, false);
        return new GameCardsAdapter.ViewHolder(c);
    }

    @Override
    public void onBindViewHolder(GameCardsAdapter.ViewHolder holder, final int position) {
//        CardView cardView = holder.mCardView;
//        TextView textView = cardView.findViewById(R.id.info_text_title);
//        textView.setText(title_texts[position]);
//        textView = cardView.findViewById(R.id.info_text_subtitle);
//        textView.setText(subtitle_texts[position]);
//        ImageView imageView = cardView.findViewById(R.id.info_image);
//        imageView.setImageResource(imageIds[position]);
//        imageView.setBackgroundResource(backgrounds[position]);
//        imageView.setContentDescription(title_texts[position]);
    }

    @Override
    public int getItemCount() {
        return 9;
    }

//    @Override
//    public int getItemViewType(int position) {
////        if (title_texts[position].equals("DIVIDER"))
////            return DIVIDER;
////        else
//            return CONTENT;
//    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;

        ViewHolder(CardView c) {
            super(c);
            mCardView = c;
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null)
                        mListener.onClick(getAdapterPosition());
                }
            });
        }
    }

}
