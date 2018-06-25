package com.nollpointer.dates;


import android.app.Fragment;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;


import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.shape.NoShape;

import static com.nollpointer.dates.MainActivity.SORT;
import static com.nollpointer.dates.MainActivity.SORT_CHECK;


public class SortFragment extends Fragment {
    private Cursor crs;
    private TextView RightAnswers, WrongAnswers;
    private TextView[] Numbers = new TextView[3];
    private TextView[] Texts = new TextView[3];
    private CardView[] cards = new CardView[3];
    private TextView instructions;
    private int RightSequence = -1;
    private int Answer = 123;
    private int CheckModeRightSequence = -1;
    private int right_answers_count=0,wrong_answers_count=0,best_result = 0;
    private int[] position, bound;
    private String[] questions = new String[3];
    private TreeMap<Integer,Integer> tree = new TreeMap<>();
    private Handler mHandler;
    private Runnable post;
    private ArrayList<String> dates = new ArrayList<>();
    private Button check_button;
    private boolean isInfinitive;
    private boolean isResultScreenOn = false;
    private boolean checkMode = false;
    private GridLayout view;
    private boolean isFirstTimeCheck = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT == 19)
            view = (GridLayout) inflater.inflate(R.layout.fragment_sort_low_api, container, false);
        else
            view =(GridLayout) inflater.inflate(R.layout.fragment_sort, container, false);
        final MainActivity ctx = (MainActivity) getActivity();
        ctx.getSupportActionBar().hide();
        crs = ctx.getCursor();
        RightAnswers = view.findViewById(R.id.right_answers);
        WrongAnswers = view.findViewById(R.id.wrong_answers);
        instructions = view.findViewById(R.id.instruction_sort);
        RightAnswers.setText("0");
        WrongAnswers.setText("0");
        cards[0] = view.findViewById(R.id.cardView1);
        cards[1] = view.findViewById(R.id.cardView2);
        cards[2] = view.findViewById(R.id.cardView3);
        int color = Color.WHITE;
        cards[0].setBackgroundColor(color);
        cards[1].setBackgroundColor(color);
        cards[2].setBackgroundColor(color);
        Numbers[0] = view.findViewById(R.id.button_cardview_1);
        Numbers[1] = view.findViewById(R.id.button_cardview_2);
        Numbers[2] = view.findViewById(R.id.button_cardview_3);
        Texts[0] = view.findViewById(R.id.textview_cardview_1);
        Texts[1] = view.findViewById(R.id.textview_cardview_2);
        Texts[2] = view.findViewById(R.id.textview_cardview_3);
        tree.put(R.id.cardView1,1);
        tree.put(R.id.cardView2,2);
        tree.put(R.id.cardView3,3);
//        setQuestions();
        cards[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isResultScreenOn) {
                    int numb = tree.get(view.getId());
                    numb %= 3;
                    numb++;
                    Answer = numb * 100 + Answer % 100;
                    Numbers[0].setText(Integer.toString(numb));
                    tree.put(view.getId(), numb);
                    if(checkMode)
                        OnGoingCheck();
                }

            }
        });
        cards[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isResultScreenOn) {
                    int numb = tree.get(view.getId());
                    numb %= 3;
                    numb++;
                    Answer = (Answer / 100) * 100 + numb * 10 + Answer % 10;
                    Numbers[1].setText(Integer.toString(numb));
                    tree.put(view.getId(), numb);
                    if(checkMode)
                        OnGoingCheck();
                }else{
                    resetInfo();
                }

            }
        });
        cards[2].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!isResultScreenOn) {
                    int numb = tree.get(view.getId());
                    numb %= 3;
                    numb++;
                    Answer = (Answer / 10) * 10 + numb;
                    Numbers[2].setText(Integer.toString(numb));
                    tree.put(view.getId(), numb);
                    if(checkMode)
                        OnGoingCheck();
                }else
                    ctx.getFragmentManager().popBackStack();

            }
        });
        check_button = view.findViewById(R.id.sort_check);
        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkMode) {
                    refreshPage();
                    Answer = 123;
                }
                else
                    Check();
                checkMode = !checkMode;
                check_button.setText(getString(checkMode ? R.string.next_sort : R.string.check_button));
            }
        });

        Appodeal.setBannerViewId(R.id.appodealBannerView_sort);
        Appodeal.show(ctx,Appodeal.BANNER_VIEW);
        mHandler = new Handler();
        post = new Runnable() {
            @Override
            public void run() {
                int color = getResources().getColor(android.R.color.white);
                for(CardView c:cards)
                    c.setBackgroundColor(color);
                check_button.setClickable(true);
                if(!isInfinitive && wrong_answers_count+right_answers_count==20)
                    setResultScreen();
                else
                    setQuestions();
            }
        };
        setSequence();
        setQuestionInfo();
        setQuestions();
        isFirstTimeCheck = ctx.isFirstTime(SORT_CHECK);
        if(ctx.isFirstTime(SORT))
            new MaterialShowcaseView.Builder(ctx)
                    .setTarget(view)
                    .setDelay(200)
                    .setContentText(R.string.tutorial_sort)
                    .setDismissText(R.string.got_it)
                    .setDismissOnTouch(true)
                    .setDismissTextColor(Color.GREEN)
                    .setMaskColour(getResources().getColor(R.color.colorMask))
                    .setShape(new NoShape())
                    .show();
        return view;
    }

    private void refreshPage(){
        int color = getResources().getColor(android.R.color.white);
        for(CardView c:cards)
            c.setBackgroundColor(color);
        check_button.setClickable(true);
        if(!isInfinitive && wrong_answers_count+right_answers_count==20)
            setResultScreen();
        else
            setQuestions();
    }

    private void setQuestionInfo(){
        Random rand = new Random();
        int[] numbs = new int[3];
        int start_pos = rand.nextInt(position.length);
        String date;
        boolean bln;
        for(int i=0;i<3;i++){
            bln = false;
            numbs[i] = position[start_pos] + rand.nextInt(bound[start_pos]);
            crs.moveToPosition(numbs[i]);
            date = crs.getString(0).trim();
            for(int j=0;j<i;j++){
                if(numbs[i] == numbs[j]){
                    i--;
                    bln = true;
                    break;
                }
            }
            if(!bln && (dates.contains(date) || date.contains("-") || date.contains("–"))){
                i--;
                bln = true;
            }
            if(bln)
                continue;
            dates.add(date);
            questions[i] = crs.getString(1).trim();
        }
        dates.clear();
        for(int i=0;i<3;i++){
            for(int j=i;j<3;j++){
                if(numbs[j]<numbs[i]){
                    String d = questions[i];
                    int k = numbs[i];
                    numbs[i]=numbs[j];
                    questions[i]=questions[j];
                    numbs[j] = k;
                    questions[j] = d;
                }
            }
        }
        //Log.wtf("AFTER_SORT",numbs[0] +"\t" + numbs[1] + "\t" + numbs[2]);
    }

    private void setQuestions(){
        for (int i=0;i<Numbers.length;i++) {
            Numbers[i].setText(Integer.toString(i+1));
        }
        int z = RightSequence/100 -1;
        Texts[0].setText(questions[z]);
        z = (RightSequence/10)%10 -1;
        Texts[1].setText(questions[z]);
        z = RightSequence%10 -1;
        Texts[2].setText(questions[z]);
    }

    private void setSequence(){
        Random rnd = new Random();
        switch (rnd.nextInt(6)){
            case 0:
                RightSequence = 123;
                break;
            case 1:
                RightSequence = 132;
                break;
            case 2:
                RightSequence = 213;
                break;
            case 3:
                RightSequence = 231;
                break;
            case 4:
                RightSequence = 312;
                break;
            case 5:
                RightSequence = 321;
                break;
        }
    }

    private void OnGoingCheck(){
        setColoredCards(CheckModeRightSequence);
    }

    private void setColoredCards(int RightSequence){
        boolean[] ar = new boolean[3];
        ar[0] = RightSequence/100 == Answer/100;
        ar[1] = (RightSequence/10)%10 == (Answer/10)%10;
        ar[2] = RightSequence%10 == Answer%10;
        int colorR = getResources().getColor(android.R.color.holo_green_dark);
        int colorW = getResources().getColor(android.R.color.holo_red_light);
        for(int i =0;i<3;i++){
            if(ar[i])
                cards[i].setBackgroundColor(colorR);
            else
                cards[i].setBackgroundColor(colorW);
        }
    }

    private void Check(){
        if(Answer == RightSequence) {
            right_answers_count++;
            RightAnswers.setText(Integer.toString(right_answers_count));
            int color = getResources().getColor(android.R.color.holo_green_dark);
            for(CardView c:cards)
                c.setBackgroundColor(color);
        }else{
            wrong_answers_count++;
            WrongAnswers.setText(Integer.toString(wrong_answers_count));
            setColoredCards(RightSequence);
        }
        CheckModeRightSequence = RightSequence;
        if(isFirstTimeCheck) {
            new MaterialShowcaseView.Builder(getActivity())
                    .setTarget(view)
                    .setDelay(150)
                    .setContentText(R.string.tutorial_sort_check)
                    .setDismissText(R.string.got_it)
                    .setDismissOnTouch(true)
                    .setDismissTextColor(Color.GREEN)
                    .setMaskColour(getResources().getColor(R.color.colorMask))
                    .setShape(new NoShape())
                    .show();
            isFirstTimeCheck = false;
        }
        setSequence();
        setQuestionInfo();
    }

    public SortFragment setCenturies(ArrayList<Integer> arrayList, int type, int mode,boolean infinitive) {
        isInfinitive = infinitive;
        if(mode ==0) {
            if (arrayList.contains(10))
                for (int i = 0; i < 10; i++)
                    arrayList.add(i, i);
            arrayList.remove(Integer.valueOf(10));
            //Log.wtf("ASDASDA",arrayList.size() + "");
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
        return this;
    }

    private void setResultScreen(){
        isResultScreenOn = true;
        check_button.setVisibility(View.INVISIBLE);
        check_button.setClickable(false);
        Texts[1].setText(R.string.reset_button);
        Texts[2].setText(R.string.exit_button);
        cards[1].setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        cards[2].setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        int cur_best = best_result;
        if(right_answers_count > best_result) {
            best_result = right_answers_count ;
        }
        String mark;
        int color;
        if(right_answers_count < 5) {
            mark = getString(R.string.mark_2);
            color = getResources().getColor(android.R.color.holo_red_light);
        }else if (right_answers_count < 10) {
            mark = getString(R.string.mark_3);
            color = getResources().getColor(android.R.color.holo_red_light);
        }else if (right_answers_count < 15) {
            mark = getString(R.string.mark_4);
            color = getResources().getColor(android.R.color.holo_green_light);
        }else if (right_answers_count < 20) {
            mark = getString(R.string.mark_5);
            color = getResources().getColor(android.R.color.holo_green_light);
        }else if (right_answers_count==20) {
            mark = getString(R.string.mark_20);
            color = getResources().getColor(android.R.color.holo_green_light);
        }else {
            mark = "WOW";
            color = getResources().getColor(android.R.color.holo_green_light);
        }
        Numbers[0].setText(Integer.toString(right_answers_count));
        Numbers[1].setText(">");
        Numbers[2].setText("<");
        Texts[0].setText(mark);
        Texts[0].setTextColor(color);
        if(cur_best == 0)
            instructions.setText(getString(R.string.current_result));
        else
            instructions.setText(getString(R.string.best_result) + cur_best + "\n\n" + getString(R.string.current_result));

    }

    private void resetInfo(){
        if(Appodeal.isLoaded(Appodeal.INTERSTITIAL))
            Appodeal.show(getActivity(),Appodeal.INTERSTITIAL);
        wrong_answers_count=right_answers_count=0;
        WrongAnswers.setText(Integer.toString(wrong_answers_count));
        RightAnswers.setText(Integer.toString(right_answers_count));
        isResultScreenOn = false;
        check_button.setVisibility(View.VISIBLE);
        check_button.setClickable(true);
        ColorStateList cols = Texts[1].getTextColors();
        Texts[0].setTextColor(cols);
        cards[1].setBackgroundColor(getResources().getColor(android.R.color.white));
        cards[2].setBackgroundColor(getResources().getColor(android.R.color.white));
        instructions.setText(getString(R.string.sotr_text));
        setQuestions();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            mHandler.removeCallbacks(post);
        }catch (Exception e){}
    }

}
