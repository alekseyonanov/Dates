package com.nollpointer.dates.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.nollpointer.dates.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PractiseDetailsPickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    CharSequence[] titles;
    private int mode;
    private int type = 0;
    private ArrayList<Integer> centuries = new ArrayList<>();
    private CheckedTextView previousPickedType;

    public static final int CENTURY = 1;
    public static final int TYPE = 0;

    public PractiseDetailsPickerAdapter(CharSequence[] positions, int mode) {
        this.titles = positions;
        this.mode = mode;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (mode == TYPE)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_singlechoice, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_multichoice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        CheckedTextView view = holder.itemView.findViewById(android.R.id.text1);
        view.setText(titles[position]);
        if (mode == TYPE) {
            if (position == type) {
                view.setChecked(true);
                previousPickedType = view;
            } else
                view.setChecked(false);
        } else {
            if (centuries.contains(position))
                view.setChecked(true);
            else
                view.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public int getPickedType() {
        return type;
    }

    public List<Integer> getPickedCenturies() {
        return centuries;
    }

    public void makeRandomValues() {
        Random random = new Random(System.currentTimeMillis());
        type = random.nextInt(3);
        int size = random.nextInt(titles.length);
        centuries.clear();
        if (size == titles.length - 1)
            for (int i = 0; i < titles.length; i++) {
                centuries.add(i);
            }
        else
            for (int i = 0; i < size; i++)
                centuries.add(random.nextInt(titles.length - 1));
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(final View view) {
            super(view);
            View textView = view.findViewById(android.R.id.text1);
            if (mode == TYPE)
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CheckedTextView view = (CheckedTextView) v;
                        if (!view.isChecked()) {
                            view.setChecked(true);
                            previousPickedType.setChecked(false);
                            previousPickedType = view;
                            type = getAdapterPosition();
                        }
                    }
                });
            else
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckedTextView view = (CheckedTextView) v;
                        if (view.isChecked()) {
                            view.setChecked(false);
                            centuries.remove(Integer.valueOf(getAdapterPosition()));
                        } else {
                            view.setChecked(true);
                            centuries.add(getAdapterPosition());
                        }
                    }
                });
        }
    }
}
