package com.nollpointer.dates.ui.dates

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.speech.RecognizerIntent
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentDatesBinding
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.other.CustomItemDecoration
import com.nollpointer.dates.other.Keyboard
import com.nollpointer.dates.other.Loader
import com.nollpointer.dates.other.SimpleTextWatcher
import com.nollpointer.dates.ui.activity.MainActivity
import com.nollpointer.dates.ui.activity.MainActivity.Companion.EASY_DATES_MODE
import com.nollpointer.dates.ui.view.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class DatesFragment : BaseFragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var adapter: DatesAdapter

    private lateinit var dates: List<Date>

    private val viewModel by viewModels<DatesViewModel>()

    private var _binding: FragmentDatesBinding? = null
    private val binding: FragmentDatesBinding
        get() = _binding!!

    @Inject
    lateinit var loader: Loader

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentDatesBinding.inflate(inflater, container, false)

        mainActivity = activity as MainActivity
        binding.toolbar.apply {
            setTitle(R.string.title_dates)
            setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.dates_app_bar_search) showSearch()
                true
            }
            setOnClickListener {
                scrollToTop()
            }
        }

        dates = mainActivity.dates

        initializeSearchView()

        adapter = DatesAdapter(resources, dates, mainActivity.mode).apply {
            onDateClickListener = {
                Keyboard.hide(binding.cardSearch)
                viewModel.onDateClicked(it)
            }
            when (loader.datesViewType) {
                0 -> {
                    itemLayoutId = R.layout.item_dates
                    itemWithTitleLayoutId = R.layout.item_dates_top_text
                }
                else -> {
                    itemLayoutId = R.layout.item_dates_2
                    itemWithTitleLayoutId = R.layout.item_dates_2_top_text
                }
            }
        }

        binding.recyclerView.apply {
            val dividerItemDecoration = CustomItemDecoration(requireContext(),
                    DividerItemDecoration.VERTICAL,
                    resources.getIntArray(when (mainActivity.mode) {
                        EASY_DATES_MODE -> R.array.dates_easy_positions
                        else -> R.array.dates_full_positions
                    }))
            dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider) as Drawable)
            addItemDecoration(dividerItemDecoration)
            layoutManager = LinearLayoutManager(this.context)
            this.adapter = this@DatesFragment.adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) //TODO Optimize
                        Keyboard.hide(binding.cardSearch)
                }
            })
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mainActivity.showBottomNavigationView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RECOGNIZER_REQUEST_CODE
                && data != null
                && data.extras!!.containsKey(RecognizerIntent.EXTRA_RESULTS)) {
            val text = data.extras!!.getStringArrayList(RecognizerIntent.EXTRA_RESULTS)
            binding.searchText.setText(text!![0], TextView.BufferType.EDITABLE)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun getStatusBarColorRes() = R.color.colorPrimary

    override fun isStatusBarLight() = false

    private fun initializeSearchView() {
        binding.searchArrowBack.apply {
            setImageResource(R.drawable.ic_arrow_back_black)
            setOnClickListener { hideSearch() }
        }
        binding.searchMultiButton.apply {
            setImageResource(R.drawable.ic_voice)
            setOnClickListener {
                if (binding.searchText.text.isEmpty()) {
                    startVoiceSearch()
                } else {
                    binding.searchText.setText("", TextView.BufferType.EDITABLE)
                }
            }
        }

        binding.searchText.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.searchMultiButton.setImageResource(
                        if (count == 0) {
                            R.drawable.ic_voice
                        } else {
                            R.drawable.ic_clear_black
                        }
                )
                search(s.toString())
            }

        })
        binding.searchText.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP &&
                    (keyCode == KeyEvent.KEYCODE_ENTER ||
                            keyCode == KeyEvent.KEYCODE_SEARCH)) {
                (requireContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(binding.cardSearch.windowToken, 0)
                return@OnKeyListener true
            }
            if (event.action == KeyEvent.ACTION_UP &&
                    keyCode == KeyEvent.KEYCODE_BACK &&
                    binding.cardSearch.visibility == View.VISIBLE) {
                hideSearch()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun showSearch() {
        binding.appBar.setExpanded(true)
        binding.recyclerView.isNestedScrollingEnabled = false
        binding.cardSearch.visibility = View.VISIBLE
        adapter.isSearchMode = true
        adapter.notifyItemChanged(0)
        Keyboard.show(binding.cardSearch)
        Handler().postDelayed({
            binding.searchText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0))
            binding.searchText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0))
        }, 200)
    }

    private fun hideSearch() {
        binding.recyclerView.isNestedScrollingEnabled = true
        binding.cardSearch.visibility = View.GONE
        binding.searchText.setText("", TextView.BufferType.EDITABLE)
        adapter.isSearchMode = false
        adapter.notifyItemChanged(0)
        Keyboard.hide(binding.cardSearch)
    }

    private fun search(query: String) {
        adapter.items = if (query.isEmpty()) {
            dates
        } else {
            dates.filter { date -> date.contains(query) }
        }

        adapter.notifyDataSetChanged()
    }

    private fun startVoiceSearch() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, requireContext().packageName)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, java.lang.Long.valueOf(5000))
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, java.lang.Long.valueOf(5000))
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }

        startActivityForResult(intent, RECOGNIZER_REQUEST_CODE)
    }

    fun scrollToTop() {
        binding.appBar.setExpanded(true)
        binding.recyclerView.smoothScrollToPosition(0)
    }

    companion object {
        private const val RECOGNIZER_REQUEST_CODE = 1417

        @JvmStatic
        fun newInstance() = DatesFragment()
    }
}