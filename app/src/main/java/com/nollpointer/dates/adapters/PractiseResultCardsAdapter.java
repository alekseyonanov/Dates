package com.nollpointer.dates.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nollpointer.dates.R;

import java.util.Random;

public class PractiseResultCardsAdapter extends RecyclerView.Adapter<PractiseResultCardsAdapter.ViewHolder> {
    private PractiseCardsAdapter.Listener mListener;

    private int currentMode = 0;

    public interface Listener {
        void onClick(int position);
    }

    public void setListener(PractiseCardsAdapter.Listener listener) {
        mListener = listener;
    }

    public void setMode(int practiseMode) {
        currentMode = practiseMode;
    }

    @Override
    public PractiseResultCardsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_result, parent, false);
        return new PractiseResultCardsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PractiseResultCardsAdapter.ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    private int getMarkColor() {
        int colors[] = {0xFFB71C1C,0xFFFFEB3B, 0xFF43a047};
        //int colors[] = {Color.RED,Color.YELLOW, Color.GREEN};
        Random random = new Random();

        return colors[random.nextInt(3)];
    }

    private String getMark() {
        Random random = new Random();
        double number = random.nextInt(20)*5./20;

        return String.format("%.1f",number);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null)
                        mListener.onClick(getAdapterPosition());
                }
            });
        }
    }

}
