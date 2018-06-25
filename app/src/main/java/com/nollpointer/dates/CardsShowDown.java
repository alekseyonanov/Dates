package com.nollpointer.dates;


import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.shape.NoShape;

import static com.nollpointer.dates.PractiseConstants.BOUND;
import static com.nollpointer.dates.PractiseConstants.POSITION;
import static com.nollpointer.dates.PractiseConstants.TYPE;

public class CardsShowDown extends Fragment {
    private Cursor crs;
    private int[] position, bound;
    private int type,mPick;

    private int getPick() {
        return mPick;
    }

    private void setPick(int pick) {
        mPick = pick;
    }


    public static CardsShowDown newInstance(ArrayList<Integer> arrayList,int picked_pos,int mode,boolean infinitive){
        CardsShowDown cards = new CardsShowDown();
        int[] position,bound;
        if(mode == MainActivity.FULL_DATES_MODE) {
            if (arrayList.contains(10))
                for (int i = 0; i < 10; i++)
                    arrayList.add(i, i);
            arrayList.remove(Integer.valueOf(10));
            position = new int[arrayList.size()];
            bound = new int[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                switch (arrayList.get(i)) {
                    case 0:
                        position[i] = 0;
                        bound[i] = 21;
                        break;
                    case 1:
                        position[i] = 21;
                        bound[i] = 20;
                        break;
                    case 2:
                        position[i] = 41;
                        bound[i] = 35;
                        break;
                    case 3:
                        position[i] = 76;
                        bound[i] = 31;
                        break;
                    case 4:
                        position[i] = 107;
                        bound[i] = 40;
                        break;
                    case 5:
                        position[i] = 147;
                        bound[i] = 48;
                        break;
                    case 6:
                        position[i] = 195;
                        bound[i] = 48;
                        break;
                    case 7:
                        position[i] = 242;
                        bound[i] = 42;
                        break;
                    case 8:
                        position[i] = 284;
                        bound[i] = 50;
                        break;
                    case 9:
                        position[i] = 334;
                        bound[i] = 50;
                        break;
                }
            }
        }else{
            if (arrayList.contains(2))
                for (int i = 0; i < 2; i++)
                    arrayList.add(i, i);
            arrayList.remove(Integer.valueOf(2));
            position = new int[arrayList.size()];
            bound = new int[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                switch (arrayList.get(i)) {
                    case 0:
                        position[i] = 0;
                        bound[i] = 48;
                        break;
                    case 1:
                        position[i] = 48;
                        bound[i] = 47;
                        break;
                }
            }
        }

        Bundle bundle = new Bundle();
        bundle.putIntArray(POSITION,position);
        bundle.putIntArray(BOUND,bound);
        bundle.putInt(TYPE,picked_pos);
        cards.setArguments(bundle);
        return cards;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cards_show_down, container, false);
        MainActivity mAc = (MainActivity) getActivity();
        mAc.getSupportActionBar().hide();
        final TextView textView = view.findViewById(R.id.date_cards_text);
        mAc.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        crs = mAc.getCursor();
        Bundle saved = getArguments();
        type = saved.getInt(TYPE);
        bound = saved.getIntArray(BOUND);
        position = saved.getIntArray(POSITION);
        setRandom();
        textView.setText(crs.getString(getPick()));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,45);
        view.findViewById(R.id.cards_next_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,45);
                setRandom();
                textView.setText(crs.getString(mPick));
            }
        });
        view.findViewById(R.id.cards_description_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(crs.getString(0) + "\n" + crs.getString(1));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,40);
            }
        });
        if(mAc.isFirstTime(MainActivity.CARDS))
            new MaterialShowcaseView.Builder(mAc)
                    .setTarget(view)
                    .setDelay(200)
                    .setContentText(R.string.tutorial_cards)
                    .setDismissText(R.string.got_it)
                    .setDismissOnTouch(true)
                    .setDismissTextColor(Color.GREEN)
                    .setMaskColour(getResources().getColor(R.color.colorMask))
                    .setShape(new NoShape())
                    .show();
        return view;
    }

    public void setRandom(){
        Random rnd = new Random();
        int x = rnd.nextInt(position.length);
        crs.moveToPosition(position[x] + rnd.nextInt(bound[x]));
        switch (type){
            case 0:
                setPick(0);
                break;
            case 1:
                setPick(1);
                break;
            default:
                setPick(rnd.nextInt(10) > 4 ? 1 : 0);
        }
    }
}
