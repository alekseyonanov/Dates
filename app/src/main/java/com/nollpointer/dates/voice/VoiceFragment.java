package com.nollpointer.dates.voice;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.appodeal.ads.Appodeal;
import com.google.android.material.chip.Chip;
import com.nollpointer.dates.other.Date;
import com.nollpointer.dates.R;
import com.nollpointer.dates.practise.PractiseResult;
import com.nollpointer.dates.practise.PractiseResultFragment;
import com.nollpointer.dates.other.PractiseHelpDialog;
import com.nollpointer.dates.other.PractiseSettingsDialog;
import com.nollpointer.dates.test.TestFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.nollpointer.dates.practise.PractiseConstants.DATES;
import static com.nollpointer.dates.practise.PractiseConstants.DIFFICULTY;
import static com.nollpointer.dates.practise.PractiseConstants.TEST_MODE;
import static com.nollpointer.dates.practise.PractiseConstants.TYPE;
import static com.nollpointer.dates.practise.PractiseConstants.VOICE;


public class VoiceFragment extends Fragment implements RecognitionListener {

    private ImageView resultImage;

    private AudioManager audioManager;

    private TextView questionTextView;
    private TextView recognizedTextView;

    private Chip questionNumberChip;
    private Chip rightAnswersChip;
    private Chip wrongAnswersChip;

    private VisualizerView visualizerView;

    private Date currentDate;

    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;

    private List<Date> dates;
    private int difficulty;
    private boolean isTestMode;
    private int type;

    private boolean isLocked = false;

    private ArrayList<PractiseResult> practiseResults = new ArrayList<>();

    private int delay = 900;

    private int originalVolumeLevel;

    private String savedRecognizedText = "";

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (isLocked)
                return;

            lockAnswerButtons();

            int rightAnswersCount = Integer.parseInt(rightAnswersChip.getText().toString());
            int wrongAnswersCount = Integer.parseInt(wrongAnswersChip.getText().toString());

            String recognizedText = recognizedTextView.getText().toString();
            boolean isCorrect = check(recognizedText);

            if (isCorrect) {
                showCorrectImage();
                rightAnswersCount++;
            } else {
                showMistakeImage();
                wrongAnswersCount++;
            }

            if(isTestMode){
                PractiseResult practiseResult = new PractiseResult(questionTextView.getText().toString(),isCorrect);
                practiseResults.add(practiseResult);
            }

            if(rightAnswersCount + wrongAnswersCount == 20 && isTestMode)
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, PractiseResultFragment.newInstance(VOICE,practiseResults,getArguments())).commit();


            rightAnswersChip.setText(Integer.toString(rightAnswersCount));
            wrongAnswersChip.setText(Integer.toString(wrongAnswersCount));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    generateAndSetInfo();
                }
            }, delay);
        }
    };

    public static VoiceFragment newInstance(ArrayList<Date> dates, int type, int difficulty, boolean testMode) {
        VoiceFragment fragment = new VoiceFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TEST_MODE, testMode);
        bundle.putInt(TYPE, type);
        bundle.putInt(DIFFICULTY, difficulty);
        bundle.putParcelableArrayList(DATES, dates);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_voice, container, false);

        Bundle arguments = getArguments();

        type = arguments.getInt(TYPE);
        dates = arguments.getParcelableArrayList(DATES);
        difficulty = arguments.getInt(DIFFICULTY);
        isTestMode = arguments.getBoolean(TEST_MODE);

        delay = getDelay();

        initializeViews(mainView);

        initializeRecognizer();
        generateAndSetInfo();

        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

        return mainView;
    }

    private void initializeViews(View mainView) {
        Appodeal.setBannerViewId(R.id.appodealBannerView_voice);

        ImageButton backButton = mainView.findViewById(R.id.voice_back_button);
        ImageButton settingsButton = mainView.findViewById(R.id.voice_settings_button);
        ImageButton helpButton = mainView.findViewById(R.id.voice_help_button);
        ImageView voiceRecognitionButton = mainView.findViewById(R.id.voiceRecognitionButton);

        backButton.setImageResource(R.drawable.ic_arrow_back_white);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        settingsButton.setImageResource(R.drawable.ic_settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PractiseSettingsDialog settingsDialog = PractiseSettingsDialog.newInstance(delay);
                settingsDialog.setListener(new PractiseSettingsDialog.Listener() {
                    @Override
                    public void onDelayPicked(int delay) {
                        setDelay(delay);
                    }
                });
                settingsDialog.show(getActivity().getSupportFragmentManager(), null);
            }
        });

        helpButton.setImageResource(R.drawable.ic_help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PractiseHelpDialog helpDialog = new PractiseHelpDialog();
                helpDialog.show(getActivity().getSupportFragmentManager(), null);
            }
        });

        voiceRecognitionButton.setImageResource(R.drawable.ic_voice_gray);
        voiceRecognitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechRecognizer.startListening(recognizerIntent);
            }
        });

        Button checkButton = mainView.findViewById(R.id.voice_check);
        checkButton.setOnClickListener(listener);


        questionTextView = mainView.findViewById(R.id.voiceQuestion);
        recognizedTextView = mainView.findViewById(R.id.voiceRecognizedText);

        questionNumberChip = mainView.findViewById(R.id.voiceQuestionNumber);
        rightAnswersChip = mainView.findViewById(R.id.voiceRightAnswers);
        wrongAnswersChip = mainView.findViewById(R.id.voiceWrongAnswers);

        resultImage = mainView.findViewById(R.id.voice_result_image);

        visualizerView = mainView.findViewById(R.id.voiceVisualizer);

    }

    private boolean check(String text){
        boolean isCorrect;
        if(currentDate.isContinuous()){
            String[] years = currentDate.getDate().split("–");

            isCorrect = text.contains(years[0]) && text.contains(years[1]);

        }else{
            isCorrect = text.contains(currentDate.getDate());
        }

        if(currentDate.containsMonth()){
            isCorrect &= text.contains(currentDate.getMonth());
        }




        return isCorrect;
    }

    private void showCorrectImage() {
        resultImage.setImageResource(R.drawable.ic_correct);
        resultImage.setVisibility(View.VISIBLE);
    }

    private void showMistakeImage() {
        resultImage.setImageResource(R.drawable.ic_mistake);
        resultImage.setVisibility(View.VISIBLE);
    }

    private void hideResultImage() {
        resultImage.setVisibility(View.INVISIBLE);
    }

    private void lockAnswerButtons() {
        isLocked = true;
    }

    private void unlockAnswerButtons() {
        isLocked = false;
    }

    private void generateAndSetInfo() {

        currentDate = generateDate();

        hideResultImage();

        setInfo(currentDate);
        unlockAnswerButtons();

    }

    private void setInfo(Date date) {
        recognizedTextView.setText("");
        savedRecognizedText = "";
        questionTextView.setText(date.getEvent());

        //if(date.containsMonth())
            //questionTextView.setText(date.getEvent() + "\n" + date.getDate() + ", " + date.getMonth());


        int rightAnswersCount = Integer.parseInt(rightAnswersChip.getText().toString());
        int wrongAnswersCount = Integer.parseInt(wrongAnswersChip.getText().toString());
        questionNumberChip.setText("#" + (rightAnswersCount + wrongAnswersCount + 1));

    }

    private Date generateDate() {
        Random random = new Random(System.currentTimeMillis());

        return dates.get(random.nextInt(dates.size()));
    }

    private int getDelay(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        int delay = preferences.getInt("voice delay", 900);

        return delay;

    }

    private void setDelay(int delay){
        this.delay = delay;

        SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();

        preferences.putInt("voice delay", delay);

        preferences.apply();
    }

    private void initializeRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext(),
                ComponentName.unflattenFromString("com.google.android.googlequicksearchbox/com.google.android.voicesearch.serviceapi.GoogleRecognitionService"));

        recognizerIntent = createIntentForRecognizer();
        speechRecognizer.setRecognitionListener(this);
    }

    private Intent createIntentForRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getContext().getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        // Выставление минимального времени для распознавателя
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, Long.valueOf(5000));
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, Long.valueOf(5000));

        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ru-RU");

        return intent;
    }

    private void checkSpeech(){

        if (isLocked)
            return;

        String recognizedText = recognizedTextView.getText().toString();

        if(recognizedText.contains("проверить") || recognizedText.contains("проверь") || recognizedText.contains("проверьте") || recognizedText.contains("проверка"))
            listener.onClick(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        originalVolumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

        speechRecognizer.startListening(recognizerIntent);
    }

    @Override
    public void onPause() {
        super.onPause();
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolumeLevel, 0);

        speechRecognizer.cancel();
    }

    @Override
    public void onStop() {
        super.onStop();
        Appodeal.hide(getActivity(), Appodeal.BANNER_VIEW);
    }

    @Override
    public void onStart() {
        super.onStart();
        Appodeal.show(getActivity(), Appodeal.BANNER_VIEW);
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {
        visualizerView.setAmplitude(v);
    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int i) {

        speechRecognizer.startListening(recognizerIntent);
    }

    @Override
    public void onResults(Bundle bundle) {

        if (bundle.containsKey(SpeechRecognizer.RESULTS_RECOGNITION)) {
            ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String text = data.get(0);

            savedRecognizedText += " " + text;
            checkSpeech();
        }

        speechRecognizer.startListening(recognizerIntent);

    }

    @Override
    public void onPartialResults(Bundle bundle) {
        if (bundle.containsKey(SpeechRecognizer.RESULTS_RECOGNITION)) {
            ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String text = data.get(0);

            recognizedTextView.setText(savedRecognizedText + " " + text);
            checkSpeech();
        }
    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

}
