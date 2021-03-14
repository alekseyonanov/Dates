package com.nollpointer.dates.ui.terms

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentTermsBinding
import com.nollpointer.dates.model.Term
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.CustomItemDecoration
import com.nollpointer.dates.other.Keyboard
import com.nollpointer.dates.other.Loader
import com.nollpointer.dates.ui.activity.MainActivity
import com.nollpointer.dates.ui.view.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class TermsFragment : BaseFragment() {

    private lateinit var terms: List<Term>
    lateinit var adapter: TermsAdapter

    private var isEditTextEmpty = true

    private var _binding: FragmentTermsBinding? = null
    private val binding: FragmentTermsBinding
        get() = _binding!!

    override val statusBarColorRes = R.color.colorPrimary

    override val isStatusBarLight = false

    @Inject
    lateinit var loader: Loader

    @Inject
    lateinit var navigator: AppNavigator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentTermsBinding.inflate(inflater, container, false)

        val mainActivity = activity as MainActivity
        terms = mainActivity.terms

        binding.toolbar.apply {
            setOnClickListener {
                binding.recyclerView.smoothScrollToPosition(0)
            }
            setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.terms_search)
                    showSearch()
                true
            }
        }

        adapter = TermsAdapter(resources, terms).apply {
            onTermClickListener = {
                Keyboard.hide(binding.cardSearch)
                navigator.navigateToTermsDetails(it)
            }
            when (loader.termsViewType) {
                0 -> {
                    itemLayoutId = R.layout.item_term
                    itemWithTitleLayoutId = R.layout.item_term_top_text
                }
                else -> {
                    itemLayoutId = R.layout.item_term_2
                    itemWithTitleLayoutId = R.layout.item_term_2_top_text
                }
            }
        }

        binding.recyclerView.apply {
            this.adapter = this@TermsFragment.adapter

            val linearLayout = LinearLayoutManager(mainActivity)
            val dividerItemDecoration = CustomItemDecoration(this.context,
                    linearLayout.orientation, resources.getIntArray(R.array.terms_positions))
            dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider) as Drawable)
            layoutManager = linearLayout
            addItemDecoration(dividerItemDecoration)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) //TODO Optimize
                        Keyboard.hide(binding.cardSearch)
                }
            })
        }

        binding.arrowBack.apply {
            setImageResource(R.drawable.ic_arrow_back_black)
            setOnClickListener { hideSearch() }
        }
        binding.searchMultiButton.apply {
            setImageResource(R.drawable.ic_voice)
            setOnClickListener {
                if (isEditTextEmpty)
                    startVoiceSearch()
                else
                    binding.searchText.setText("", TextView.BufferType.EDITABLE)
            }
        }
        binding.searchText.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    isEditTextEmpty = count == 0
                    if (isEditTextEmpty)
                        binding.searchMultiButton.setImageResource(R.drawable.ic_voice)
                    else
                        binding.searchMultiButton.setImageResource(R.drawable.ic_clear_black)
                    search(s.toString())
                }

                override fun afterTextChanged(s: Editable) {}
            })
            setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_UP &&
                        (keyCode == KeyEvent.KEYCODE_ENTER ||
                                keyCode == KeyEvent.KEYCODE_SEARCH)) {
                    Keyboard.hide(binding.cardSearch)
                    return@OnKeyListener true
                }
                if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK && binding.cardSearch.visibility == View.VISIBLE) {
                    hideSearch()
                    return@OnKeyListener true
                }
                false
            })
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RECOGNIZER_REQUEST_CODE) if (data != null && data.extras!!.containsKey(RecognizerIntent.EXTRA_RESULTS)) {
            val text = data.extras!!.getStringArrayList(RecognizerIntent.EXTRA_RESULTS)
            binding.searchText.setText(text!![0], TextView.BufferType.EDITABLE)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun search(query: String) {
        adapter.items = if (query.isEmpty())
            terms
        else {
            terms.filter { term -> term.contains(query) }
        }
        adapter.notifyDataSetChanged()
    }

    private fun showSearch() {
        binding.appBar.setExpanded(true)
        binding.recyclerView.isNestedScrollingEnabled = false
        binding.cardSearch.visibility = View.VISIBLE
        Keyboard.show(binding.cardSearch)
        Handler().postDelayed({
            binding.searchText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0))
            binding.searchText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0))
        }, 200)

        adapter.isSearchMode = true
        adapter.notifyItemChanged(0)
    }

    private fun hideSearch() {
        binding.recyclerView.isNestedScrollingEnabled = true
        binding.cardSearch.visibility = View.GONE
        binding.searchText.setText("", TextView.BufferType.EDITABLE)
        adapter.items = terms
        adapter.isSearchMode = false
        adapter.notifyItemChanged(0)
        Keyboard.hide(binding.cardSearch)
    }

    fun scrollToTop() {
        binding.appBar.setExpanded(true)
        binding.recyclerView.smoothScrollToPosition(0)
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

    companion object {
        private const val RECOGNIZER_REQUEST_CODE = 1417
    }
}