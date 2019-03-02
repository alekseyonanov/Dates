package com.nollpointer.dates.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nollpointer.dates.Misc;
import com.nollpointer.dates.R;

import java.util.List;

public class GameCardsAdapter extends RecyclerView.Adapter<GameCardsAdapter.ViewHolder> {
    private GameCardsAdapter.FinalResultListener mListener;

    private boolean isGameMode = false;
    private int rightAnswered = 0;
    private int openedCards = 0;

    private List<Integer> rightAnswers;

    public interface FinalResultListener {
        void onClick(boolean result);
    }

    public void setListener(GameCardsAdapter.FinalResultListener listener) {
        mListener = listener;
    }

    public GameCardsAdapter() {
        rightAnswers = Misc.getRightAnswersList(5);
    }

    @Override
    public GameCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView c = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.game_card, parent, false);
        return new GameCardsAdapter.ViewHolder(c);
    }

    @Override
    public void onBindViewHolder(GameCardsAdapter.ViewHolder holder, final int position) {
        TextView textView = holder.itemView.findViewById(R.id.game_card_text);
        ImageView imageView = holder.itemView.findViewById(R.id.game_card_image);
        if(!isGameMode && position == 4)
            textView.setText("Играть");

        if(isGameMode)
            textView.setText("?");

        if(rightAnswers.contains(position))
            imageView.setImageResource(R.drawable.ic_star);
        else
            imageView.setImageResource(R.drawable.ic_star_empty);
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    public void startGameMode(){
        isGameMode = true;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(final CardView c) {
            super(c);
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(isGameMode){
                        openedCards++;
                        c.findViewById(R.id.game_card_text).setVisibility(View.GONE);
                        c.findViewById(R.id.game_card_image).setVisibility(View.VISIBLE);
                    }

                    if(!isGameMode && getAdapterPosition() == 4)
                        startGameMode();

                }
            });
        }
    }
}
