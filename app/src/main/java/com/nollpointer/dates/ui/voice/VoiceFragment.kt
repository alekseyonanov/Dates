package com.nollpointer.dates.ui.voice

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
import androidx.fragment.app.Fragment
import com.appodeal.ads.Appodeal
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.model.PractiseResult
import com.nollpointer.dates.ui.dialog.PractiseHelpDialog
import com.nollpointer.dates.ui.dialog.PractiseSettingsDialog
import com.nollpointer.dates.ui.practiseresult.PractiseResultFragment.Companion.newInstance
import kotlinx.android.synthetic.main.fragment_voice.*
import java.util.*

class VoiceFragment : Fragment(), RecognitionListener {

    private lateinit var audioManager: AudioManager

    private lateinit var currentDate: Date
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent
    private lateinit var dates: List<Date>

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
        var rightAnswersCount = voiceRightAnswersChip.text.toString().toInt()
        var wrongAnswersCount = voiceWrongAnswersChip.text.toString().toInt()
        val recognizedText = voiceRecognizedText.text.toString()
        val isCorrect = check(recognizedText)
        if (isCorrect) {
            showCorrectImage()
            rightAnswersCount++
        } else {
            showMistakeImage()
            wrongAnswersCount++
        }
//        if (isTestMode) {
//            val practiseResult = PractiseResult(voiceQuestion.text.toString(), isCorrect)
//            practiseResults.add(practiseResult)
//        }
        if (rightAnswersCount + wrongAnswersCount == 20 && isTestMode) fragmentManager!!.beginTransaction().replace(R.id.frameLayout, newInstance(VOICE, practiseResults, arguments!!)).commit()
        voiceQuestion.text = currentDate.event + "\n" + currentDate.date
        if (currentDate.containsMonth) voiceQuestion.text = currentDate.event + "\n" + currentDate.date + ", " + currentDate.month
        voiceRightAnswersChip.text = rightAnswersCount.toString()
        voiceWrongAnswersChip.text = wrongAnswersCount.toString()
        Handler().postDelayed({ generateAndSetInfo() }, delay.toLong())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val practise = it.getParcelable<Practise>(VOICE) as Practise
            isTestMode = practise.isTestMode
            dates = practise.dates
            type = practise.type
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_voice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Appodeal.setBannerViewId(R.id.appodealBannerView_voice)

        delay = getDelay()

        voiceBack.apply{
            setImageResource(R.drawable.ic_arrow_back_white)
            setOnClickListener { fragmentManager!!.popBackStack() }
        }
        voiceSettings.apply {
            setImageResource(R.drawable.ic_settings)
            setOnClickListener {
                val settingsDialog = PractiseSettingsDialog.newInstance(delay)
                settingsDialog.setListener(object : PractiseSettingsDialog.Listener {
                    override fun onDelayPicked(delay: Int) {
                        setDelay(delay)
                    }
                })
                settingsDialog.show(activity!!.supportFragmentManager, null)
            }
        }
        voiceHelp.apply {
            setImageResource(R.drawable.ic_help)
            setOnClickListener {
                val helpDialog = PractiseHelpDialog.newInstance(R.string.help_voice)
                helpDialog.show(activity!!.supportFragmentManager, null)
            }
        }
        voiceRecognitionButton.apply{
            setImageResource(R.drawable.ic_voice_gray)
            setOnClickListener { speechRecognizer.startListening(recognizerIntent) }
        }
        voiceCheck.setOnClickListener(listener)

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context,
                ComponentName.unflattenFromString("com.google.android.googlequicksearchbox/com.google.android.voicesearch.serviceapi.GoogleRecognitionService"))
        recognizerIntent = createIntentForRecognizer()
        speechRecognizer.setRecognitionListener(this)

        //generateAndSetInfo()
        audioManager = context!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
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

    private fun check(text: String): Boolean {
        var isCorrect: Boolean
        isCorrect = if (currentDate.isContinuous) {
            val years = currentDate.date.split("–").toTypedArray()
            text.contains(years[0]) && text.contains(years[1])
        } else {
            text.contains(currentDate.date)
        }
        if (currentDate.containsMonth) {
            isCorrect = isCorrect and text.contains(currentDate.month)
        }
        return isCorrect
    }

    private fun showCorrectImage() {
        voiceResult.setImageResource(R.drawable.ic_correct)
        voiceResult.visibility = View.VISIBLE
    }

    private fun showMistakeImage() {
        voiceResult.setImageResource(R.drawable.ic_mistake)
        voiceResult.visibility = View.VISIBLE
    }

    private fun hideResultImage() {
        voiceResult.visibility = View.INVISIBLE
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
        voiceRecognizedText.text = ""
        savedRecognizedText = ""
        voiceQuestion.text = date!!.event
        //if(date.containsMonth())
//questionTextView.setText(date.getEvent() + "\n" + date.getDate() + ", " + date.getMonth());
        val rightAnswersCount = voiceRightAnswersChip.text.toString().toInt()
        val wrongAnswersCount = voiceWrongAnswersChip.text.toString().toInt()
        voiceQuestionNumberChip.text = "#" + (rightAnswersCount + wrongAnswersCount + 1)
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

    private fun createIntentForRecognizer(): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context!!.packageName)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            // Выставление минимального времени для распознавателя
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, java.lang.Long.valueOf(5000))
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, java.lang.Long.valueOf(5000))
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ru-RU")
        }
    }

    private fun checkSpeech() {
        if (isLocked) return
        val recognizedText = voiceRecognizedText.text.toString()
        if (recognizedText.contains("проверить") || recognizedText.contains("проверь") || recognizedText.contains("проверьте") || recognizedText.contains("проверка")) listener.onClick(null)
    }

    override fun onReadyForSpeech(bundle: Bundle) {}
    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(v: Float) {
        voiceVisualizer.setAmplitude(v)
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
            voiceRecognizedText.text = "$savedRecognizedText $text"
            checkSpeech()
        }
    }

    override fun onEvent(i: Int, bundle: Bundle) {}

    companion object {

        private const val VOICE = "Voice"

        fun newInstance(practise: Practise) =
                VoiceFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(VOICE, practise)
                    }
                }
    }
}