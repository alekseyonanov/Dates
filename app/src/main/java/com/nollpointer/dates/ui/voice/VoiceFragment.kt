package com.nollpointer.dates.ui.voice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appodeal.ads.Appodeal
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentVoiceBinding
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.model.PractiseResult
import com.nollpointer.dates.ui.view.BaseFragment
import java.util.*

/**
 * @author Onanov Aleksey (@onanov)
 */
class VoiceFragment : BaseFragment(), RecognitionListener {

    private lateinit var audioManager: AudioManager

    private lateinit var currentDate: Date
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent
    private lateinit var dates: List<Date>

    private var isTestMode = false
    private var type = 0
    private var isLocked = false
    private val practiseResults = ArrayList<PractiseResult>()
    private var originalVolumeLevel = 0
    private var savedRecognizedText = ""

    private var isCancelled = true

    private var _binding: FragmentVoiceBinding? = null
    private val binding: FragmentVoiceBinding
        get() = _binding!!

    private val listener = View.OnClickListener {
        if (isLocked) return@OnClickListener
        lockAnswerButtons()
        var rightAnswersCount = binding.rightAnswers.text.toString().toInt()
        var wrongAnswersCount = binding.wrongAnswers.text.toString().toInt()
        val recognizedText = binding.recognizedText.text.toString()
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
        //TODO if (rightAnswersCount + wrongAnswersCount == 20 && isTestMode) fragmentManager!!.beginTransaction().replace(R.id.frameLayout, newInstance(VOICE, practiseResults, arguments!!)).commit()
        binding.question.text = currentDate.event + "\n" + currentDate.date
        if (currentDate.containsMonth) binding.question.text = currentDate.event + "\n" + currentDate.date + ", " + currentDate.month
        binding.rightAnswers.text = rightAnswersCount.toString()
        binding.wrongAnswers.text = wrongAnswersCount.toString()
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
                              savedInstanceState: Bundle?): View {

        //Appodeal.setBannerViewId(R.id.appodealBannerView_voice)

        _binding = FragmentVoiceBinding.inflate(inflater, container, false)

        binding.arrowBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.settings.setOnClickListener {

        }

        binding.recognitionButton.apply {
            setImageResource(R.drawable.ic_voice_gray)
            setOnClickListener { speechRecognizer.startListening(recognizerIntent) }
        }
        binding.check.setOnClickListener(listener)

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context,
                ComponentName.unflattenFromString("com.google.android.googlequicksearchbox/com.google.android.voicesearch.serviceapi.GoogleRecognitionService"))
        recognizerIntent = createIntentForRecognizer()
        speechRecognizer.setRecognitionListener(this)

        //generateAndSetInfo()
        audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        isCancelled = false
        originalVolumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE)
        speechRecognizer.startListening(recognizerIntent)
    }

    override fun onPause() {
        super.onPause()
        isCancelled = true
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolumeLevel, 0)
        speechRecognizer.cancel()
    }

    override fun onStop() {
        super.onStop()
        Appodeal.hide(requireActivity(), Appodeal.BANNER_VIEW)
    }

    override fun onStart() {
        super.onStart()
        Appodeal.show(requireActivity(), Appodeal.BANNER_VIEW)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getStatusBarColorRes() = R.color.colorBackground

    override fun isStatusBarLight() = true

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
        binding.result.setImageResource(R.drawable.ic_correct)
        binding.result.visibility = View.VISIBLE
    }

    private fun showMistakeImage() {
        binding.result.setImageResource(R.drawable.ic_mistake)
        binding.result.visibility = View.VISIBLE
    }

    private fun hideResultImage() {
        binding.result.visibility = View.INVISIBLE
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
        binding.recognizedText.text = ""
        savedRecognizedText = ""
        binding.question.text = date!!.event
        //if(date.containsMonth())
//questionTextView.setText(date.getEvent() + "\n" + date.getDate() + ", " + date.getMonth());
        val rightAnswersCount = binding.rightAnswers.text.toString().toInt()
        val wrongAnswersCount = binding.wrongAnswers.text.toString().toInt()
        binding.questionNumber.text = (rightAnswersCount + wrongAnswersCount + 1).toString()
    }

    private fun generateDate(): Date {
        val random = Random(System.currentTimeMillis())
        return dates[random.nextInt(dates.size)]
    }

    private fun createIntentForRecognizer(): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, requireContext().packageName)
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
        val recognizedText = binding.recognizedText.text.toString()
        if (recognizedText.contains("проверить") || recognizedText.contains("проверь") || recognizedText.contains("проверьте") || recognizedText.contains("проверка")) listener.onClick(null)
    }

    override fun onReadyForSpeech(bundle: Bundle) {}
    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(v: Float) {
        if (!isCancelled) {
            binding.visualizer.setAmplitude(v)
        }
    }

    override fun onBufferReceived(bytes: ByteArray) {}
    override fun onEndOfSpeech() {}
    override fun onError(i: Int) {
        if (!isCancelled) {
            speechRecognizer.startListening(recognizerIntent)
        }
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
            binding.recognizedText.text = "$savedRecognizedText $text"
            checkSpeech()
        }
    }

    override fun onEvent(i: Int, bundle: Bundle) {}

    companion object {
        private const val VOICE = "Voice"

        @JvmStatic
        fun newInstance(practise: Practise) =
                VoiceFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(VOICE, practise)
                    }
                }
    }
}