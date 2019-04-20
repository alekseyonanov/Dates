package com.nollpointer.dates.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nollpointer.dates.R;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PractiseCardsAdapter extends RecyclerView.Adapter<PractiseCardsAdapter.ViewHolder> {
    private String[] title_texts;
    private String[] subtitle_texts;
    private int[] imageIds;
    private int[] backgrounds;
    private Listener mListener;

    private int currentMode = 0;

    public interface Listener {
        void onClick(int position);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public PractiseCardsAdapter(String[] title, String[] subtitle, int[] images, int[] backgrounds) {
        title_texts = title;
        subtitle_texts = subtitle;
        this.imageIds = images;
        this.backgrounds = backgrounds;
    }

    public void setMode(int practiseMode) {
        currentMode = practiseMode;
    }

    @Override
    public PractiseCardsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.practise_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        View cardView = holder.itemView;

        TextView titleTextView = cardView.findViewById(R.id.info_text_title);
        TextView subTitleTextView = cardView.findViewById(R.id.info_text_subtitle);
        TextView markTextView = cardView.findViewById(R.id.info_text_mark);
        ImageView imageView = cardView.findViewById(R.id.info_image);

        titleTextView.setText(title_texts[position]);
        subTitleTextView.setText(subtitle_texts[position]);
        imageView.setImageResource(imageIds[position]);
        imageView.setBackgroundResource(backgrounds[position]);
        imageView.setContentDescription(title_texts[position]);

        if (currentMode == 0) {

        } else {
            markTextView.setVisibility(View.VISIBLE);
            markTextView.setTextColor(getMarkColor());
            markTextView.setText(getMark());
        }
    }

    @Override
    public int getItemCount() {
        return imageIds.length;
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
