package com.nollpointer.dates.practise;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nollpointer.dates.R;

import java.util.List;

public class PractiseResultCardsAdapter extends RecyclerView.Adapter<PractiseResultCardsAdapter.ViewHolder> {
    private PractiseCardsAdapter.Listener mListener;

    private List<PractiseResult> practiseResults;

    public PractiseResultCardsAdapter(List<PractiseResult> practiseResults) {
        this.practiseResults = practiseResults;
    }

    public interface Listener {
        void onClick(int position);
    }

    public void setListener(PractiseCardsAdapter.Listener listener) {
        mListener = listener;
    }

    @Override
    public PractiseResultCardsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_result, parent, false);
        return new PractiseResultCardsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PractiseResultCardsAdapter.ViewHolder holder, final int position) {
        TextView numberTextView = holder.itemView.findViewById(R.id.result_card_number);
        TextView questionTextView = holder.itemView.findViewById(R.id.result_card_main_text);
        ImageView imageView = holder.itemView.findViewById(R.id.result_card_image_view);

        numberTextView.setText(Integer.toString(position + 1));
        questionTextView.setText(practiseResults.get(position).getQuestion());

        if(practiseResults.get(position).isCorrect())
            imageView.setImageResource(R.drawable.ic_correct);
        else
            imageView.setImageResource(R.drawable.ic_mistake);
    }

    @Override
    public int getItemCount() {
        return practiseResults.size();
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
