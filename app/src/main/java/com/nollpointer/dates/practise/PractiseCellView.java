package com.nollpointer.dates.practise;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nollpointer.dates.R;

import static com.nollpointer.dates.practise.PractiseConstants.CARDS;
import static com.nollpointer.dates.practise.PractiseConstants.DISTRIBUTE;
import static com.nollpointer.dates.practise.PractiseConstants.SORT;
import static com.nollpointer.dates.practise.PractiseConstants.TEST;
import static com.nollpointer.dates.practise.PractiseConstants.TRUE_FALSE;
import static com.nollpointer.dates.practise.PractiseConstants.VOICE;

public class PractiseCellView extends RecyclerView {

    public interface OnClickListener {
        void onClicked(String practise, int mode);
    }

    public static final int TRAINING_MODE = 0;
    public static final int TEST_MODE = 1;

    private int practiseMode = TRAINING_MODE;

    private OnClickListener listener;

    public PractiseCellView(@NonNull Context context) {
        super(context);
        initializeAdapter();
    }

    public PractiseCellView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeAdapter();
    }

    public PractiseCellView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeAdapter();
    }

    private void initializeAdapter() {
        PractiseCardsAdapter adapter = new PractiseCardsAdapter(getResources().getStringArray(R.array.practise_titles),
                getResources().getStringArray(R.array.practise_description),
                new int[]{R.drawable.ic_cards,R.drawable.ic_cards_with_voice, R.drawable.ic_test, R.drawable.ic_true_false,
                        R.drawable.ic_sort, R.drawable.ic_distribution},
                new int[]{R.drawable.ic_practise_background_cards, R.drawable.ic_practise_background_voice_cards, R.drawable.ic_practise_background_test, R.drawable.ic_practise_background_true_false,
                        R.drawable.ic_practise_background_sort, R.drawable.ic_practise_background_distribution});

        adapter.setListener(new PractiseCardsAdapter.Listener() {
            @Override
            public void onClick(int position) {
                String practise;
                switch (position) {
                    case 0:
                        practise = CARDS;
                        break;
                    case 1:
                        practise = VOICE;
                        break;
                    case 2:
                        practise = TEST;
                        break;
                    case 3:
                        practise = TRUE_FALSE;
                        break;
                    case 4:
                        practise = SORT;
                        break;
                    case 5:
                        practise = DISTRIBUTE;
                        break;
                    default:
                        practise = CARDS;
                }
                listener.onClicked(practise, practiseMode);

            }
        });

        setAdapter(adapter);
        setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setPractiseMode(int mode) {
        practiseMode = mode;

        PractiseCardsAdapter adapter = (PractiseCardsAdapter) getAdapter();
        adapter.setMode(practiseMode);
    }

    public void setMarks(int[] marks) {
        PractiseCardsAdapter adapter = (PractiseCardsAdapter) getAdapter();
        adapter.setMarks(marks);
    }
}
