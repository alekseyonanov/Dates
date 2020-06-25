package com.nollpointer.dates.voice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.appodeal.ads.Appodeal
import com.google.android.material.chip.Chip
import com.nollpointer.dates.R
import com.nollpointer.dates.other.Date
import com.nollpointer.dates.other.PractiseHelpDialog
import com.nollpointer.dates.other.PractiseSettingsDialog
import com.nollpointer.dates.practise.PractiseConstants.DATES
import com.nollpointer.dates.practise.PractiseConstants.DIFFICULTY
import com.nollpointer.dates.practise.PractiseConstants.TEST_MODE
import com.nollpointer.dates.practise.PractiseConstants.TYPE
import com.nollpointer.dates.practise.PractiseConstants.VOICE
import com.nollpointer.dates.practise.PractiseResult
import com.nollpointer.dates.practise.PractiseResultFragment.Companion.newInstance
import java.util.*

class VoiceFragment : Fragment(), RecognitionListener {

    private lateinit var resultImage: ImageView
    private lateinit var audioManager: AudioManager
    private lateinit var questionTextView: TextView
    private lateinit var recognizedTextView: TextView
    private lateinit var questionNumberChip: Chip
    private lateinit var rightAnswersChip: Chip
    private lateinit var wrongAnswersChip: Chip
    private lateinit var visualizerView: VisualizerView
    private lateinit var currentDate: Date
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent
    private lateinit var dates: List<Date>

    private var difficulty = 0
    private var isTestMode = false
    private var type = 0
    private var isLocked = false
    private val practiseResults = ArrayList<PractiseResult>()
    private var delay = 900
    private var originalVolumeLevel = 0
    private var savedRecognizedText = ""

    private val listener = View.OnClickListener {
        if (isLocked) return@OnClickListener
        lockAnswerButtons()
        var rightAnswersCount = rightAnswersChip.text.toString().toInt()
        var wrongAnswersCount = wrongAnswersChip.text.toString().toInt()
        val recognizedText = recognizedTextView.text.toString()
        val isCorrect = check(recognizedText)
        if (isCorrect) {
            showCorrectImage()
            rightAnswersCount++
        } else {
            showMistakeImage()
            wrongAnswersCount++
        }
        if (isTestMode) {
            val practiseResult = PractiseResult(questionTextView.text.toString(), isCorrect)
            practiseResults.add(practiseResult)
        }
        if (rightAnswersCount + wrongAnswersCount == 20 && isTestMode) fragmentManager!!.beginTransaction().replace(R.id.frameLayout, newInstance(VOICE, practiseResults, arguments!!)).commit()
        questionTextView.text = currentDate.event + "\n" + currentDate.date
        if (currentDate.containsMonth()) questionTextView.text = currentDate.event + "\n" + currentDate.date + ", " + currentDate.month
        rightAnswersChip.text = rightAnswersCount.toString()
        wrongAnswersChip.text = wrongAnswersCount.toString()
        Handler().postDelayed({ generateAndSetInfo() }, delay.toLong())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.fragment_voice, container, false)
        val arguments = arguments
        type = arguments!!.getInt(TYPE)
        val preDates: ArrayList<Date?>? = arguments.getParcelableArrayList(DATES)
        val dates = ArrayList<Date>()
        preDates!!.forEach {
            dates.add(it as Date)
        }
        difficulty = arguments.getInt(DIFFICULTY)
        isTestMode = arguments.getBoolean(TEST_MODE)
        delay = getDelay()
        initializeViews(mainView)
        initializeRecognizer()
        generateAndSetInfo()
        audioManager = context!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return mainView
    }

    private fun initializeViews(mainView: View) {
        Appodeal.setBannerViewId(R.id.appodealBannerView_voice)
        val backButton = mainView.findViewById<ImageButton>(R.id.voice_back_button)
        val settingsButton = mainView.findViewById<ImageButton>(R.id.voice_settings_button)
        val helpButton = mainView.findViewById<ImageButton>(R.id.voice_help_button)
        val voiceRecognitionButton = mainView.findViewById<ImageView>(R.id.voiceRecognitionButton)
        backButton.setImageResource(R.drawable.ic_arrow_back_white)
        backButton.setOnClickListener { fragmentManager!!.popBackStack() }
        settingsButton.setImageResource(R.drawable.ic_settings)
        settingsButton.setOnClickListener {
            val settingsDialog = PractiseSettingsDialog.newInstance(delay)
            settingsDialog.setListener(object : PractiseSettingsDialog.Listener {
                override fun onDelayPicked(delay: Int) {
                    setDelay(delay)
                }
            })
            settingsDialog.show(activity!!.supportFragmentManager, null)
        }
        helpButton.setImageResource(R.drawable.ic_help)
        helpButton.setOnClickListener {
            val helpDialog = PractiseHelpDialog.newInstance(R.string.help_voice)
            helpDialog.show(activity!!.supportFragmentManager, null)
        }
        voiceRecognitionButton.setImageResource(R.drawable.ic_voice_gray)
        voiceRecognitionButton.setOnClickListener { speechRecognizer.startListening(recognizerIntent) }
        val checkButton = mainView.findViewById<Button>(R.id.voice_check)
        checkButton.setOnClickListener(listener)
        questionTextView = mainView.findViewById(R.id.voiceQuestion)
        recognizedTextView = mainView.findViewById(R.id.voiceRecognizedText)
        questionNumberChip = mainView.findViewById(R.id.voiceQuestionNumber)
        rightAnswersChip = mainView.findViewById(R.id.voiceRightAnswers)
        wrongAnswersChip = mainView.findViewById(R.id.voiceWrongAnswers)
        resultImage = mainView.findViewById(R.id.voice_result_image)
        visualizerView = mainView.findViewById(R.id.voiceVisualizer)
    }

    private fun check(text: String): Boolean {
        var isCorrect: Boolean
        isCorrect = if (currentDate.isContinuous) {
            val years = currentDate.date.split("–").toTypedArray()
            text.contains(years[0]) && text.contains(years[1])
        } else {
            text.contains(currentDate.date)
        }
        if (currentDate.containsMonth()) {
            isCorrect = isCorrect and text.contains(currentDate.month as String)
        }
        return isCorrect
    }

    private fun showCorrectImage() {
        resultImage.setImageResource(R.drawable.ic_correct)
        resultImage.visibility = View.VISIBLE
    }

    private fun showMistakeImage() {
        resultImage.setImageResource(R.drawable.ic_mistake)
        resultImage.visibility = View.VISIBLE
    }

    private fun hideResultImage() {
        resultImage.visibility = View.INVISIBLE
    }

    private fun lockAnswerButtons() {
        isLocked = true
    }

    private fun unlockAnswerButtons() {
        isLocked = false
    }

    private fun generateAndSetInfo() {
        currentDate = generateDate()
        hideResultImage()
        setInfo(currentDate)
        unlockAnswerButtons()
    }

    private fun setInfo(date: Date?) {
        recognizedTextView.text = ""
        savedRecognizedText = ""
        questionTextView.text = date!!.event
        //if(date.containsMonth())
//questionTextView.setText(date.getEvent() + "\n" + date.getDate() + ", " + date.getMonth());
        val rightAnswersCount = rightAnswersChip.text.toString().toInt()
        val wrongAnswersCount = wrongAnswersChip.text.toString().toInt()
        questionNumberChip.text = "#" + (rightAnswersCount + wrongAnswersCount + 1)
    }

    private fun generateDate(): Date {
        val random = Random(System.currentTimeMillis())
        return dates[random.nextInt(dates.size)]
    }

    private fun getDelay(): Int {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getInt("voice delay", 900)
    }

    private fun setDelay(delay: Int) {
        this.delay = delay
        val preferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
        preferences.putInt("voice delay", delay)
        preferences.apply()
    }

    private fun initializeRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context,
                ComponentName.unflattenFromString("com.google.android.googlequicksearchbox/com.google.android.voicesearch.serviceapi.GoogleRecognitionService"))
        recognizerIntent = createIntentForRecognizer()
        speechRecognizer.setRecognitionListener(this)
    }

    private fun createIntentForRecognizer(): Intent {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context!!.packageName)
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        // Выставление минимального времени для распознавателя
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, java.lang.Long.valueOf(5000))
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, java.lang.Long.valueOf(5000))
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ru-RU")
        return intent
    }

    private fun checkSpeech() {
        if (isLocked) return
        val recognizedText = recognizedTextView.text.toString()
        if (recognizedText.contains("проверить") || recognizedText.contains("проверь") || recognizedText.contains("проверьте") || recognizedText.contains("проверка")) listener.onClick(null)
    }

    override fun onResume() {
        super.onResume()
        originalVolumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE)
        speechRecognizer.startListening(recognizerIntent)
    }

    override fun onPause() {
        super.onPause()
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolumeLevel, 0)
        speechRecognizer.cancel()
    }

    override fun onStop() {
        super.onStop()
        Appodeal.hide(activity!!, Appodeal.BANNER_VIEW)
    }

    override fun onStart() {
        super.onStart()
        Appodeal.show(activity!!, Appodeal.BANNER_VIEW)
    }

    override fun onReadyForSpeech(bundle: Bundle) {}
    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(v: Float) {
        visualizerView.setAmplitude(v)
    }

    override fun onBufferReceived(bytes: ByteArray) {}
    override fun onEndOfSpeech() {}
    override fun onError(i: Int) {
        speechRecognizer.startListening(recognizerIntent)
    }

    override fun onResults(bundle: Bundle) {
        if (bundle.containsKey(SpeechRecognizer.RESULTS_RECOGNITION)) {
            val data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val text = data!![0]
            savedRecognizedText += " $text"
            checkSpeech()
        }
        speechRecognizer.startListening(recognizerIntent)
    }

    override fun onPartialResults(bundle: Bundle) {
        if (bundle.containsKey(SpeechRecognizer.RESULTS_RECOGNITION)) {
            val data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val text = data!![0]
            recognizedTextView.text = "$savedRecognizedText $text"
            checkSpeech()
        }
    }

    override fun onEvent(i: Int, bundle: Bundle) {}

    companion object {
        fun newInstance(dates: ArrayList<Date>, type: Int, difficulty: Int, testMode: Boolean): VoiceFragment {
            val fragment = VoiceFragment()
            val bundle = Bundle()
            bundle.putBoolean(TEST_MODE, testMode)
            bundle.putInt(TYPE, type)
            bundle.putInt(DIFFICULTY, difficulty)
            bundle.putParcelableArrayList(DATES, dates)
            fragment.arguments = bundle
            return fragment
        }
    }
}