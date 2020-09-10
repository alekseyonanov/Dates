package com.nollpointer.dates.ui.terms

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Term
import com.nollpointer.dates.other.CustomItemDecoration
import com.nollpointer.dates.other.Keyboard
import com.nollpointer.dates.other.Loader
import com.nollpointer.dates.ui.activity.MainActivity
import com.nollpointer.dates.ui.details.TermsDetailsFragment
import kotlinx.android.synthetic.main.fragment_terms.*

class TermsFragment : Fragment() {

    lateinit var terms: ArrayList<Term>
    lateinit var adapter: TermsAdapter

    private var isEditTextEmpty = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_terms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        terms = mainActivity.terms

        termsToolbar.apply {
            setOnClickListener {
                termsRecyclerView.smoothScrollToPosition(0)
            }
            setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.terms_search)
                    showSearch()
                true
            }
        }

        adapter = TermsAdapter(resources, terms).apply {
            onTermClickListener = {
                Keyboard.hide(termsCardSearch)
                fragmentManager?.beginTransaction()?.addToBackStack(null)?.replace(R.id.frameLayout, TermsDetailsFragment.newInstance(it))?.commit()
            }
            when (Loader.getTermsViewType(context as Context)) {
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

        termsRecyclerView.apply {
            this.adapter = this@TermsFragment.adapter

            val linearLayout = LinearLayoutManager(mainActivity)
            val dividerItemDecoration = CustomItemDecoration(this.context,
                    linearLayout.orientation, resources.getIntArray(R.array.terms_positions))
            dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider) as Drawable)
            layoutManager = linearLayout
            addItemDecoration(dividerItemDecoration)
        }

        termsImageSearchBack.apply {
            setImageResource(R.drawable.ic_arrow_back_black)
            setOnClickListener { hideSearch() }
        }
        termsSearchMultiButton.apply {
            setImageResource(R.drawable.ic_voice)
            setOnClickListener {
                if (isEditTextEmpty)
                    startVoiceSearch()
                else
                    termsEditTextSearch.setText("", TextView.BufferType.EDITABLE)
            }
        }
        termsEditTextSearch.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    isEditTextEmpty = count == 0
                    if (isEditTextEmpty)
                        termsSearchMultiButton.setImageResource(R.drawable.ic_voice)
                    else
                        termsSearchMultiButton.setImageResource(R.drawable.ic_clear_black)
                    search(s.toString())
                }

                override fun afterTextChanged(s: Editable) {}
            })
            setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_UP &&
                        (keyCode == KeyEvent.KEYCODE_ENTER ||
                                keyCode == KeyEvent.KEYCODE_SEARCH)) {
                    (context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(termsCardSearch.windowToken, 0)
                    return@OnKeyListener true
                }
                if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK && termsCardSearch.visibility == View.VISIBLE) {
                    hideSearch()
                    return@OnKeyListener true
                }
                false
            })
        }
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
        termsAppbar.setExpanded(true)
        termsRecyclerView.isNestedScrollingEnabled = false
        termsCardSearch.visibility = View.VISIBLE
        (context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        Handler().postDelayed({
            termsEditTextSearch.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0))
            termsEditTextSearch.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0))
        }, 200)

        adapter.isSearchMode = true
        adapter.notifyItemChanged(0)
    }

    private fun hideSearch() {
        termsRecyclerView.isNestedScrollingEnabled = true
        termsCardSearch.visibility = View.GONE
        termsEditTextSearch.setText("", TextView.BufferType.EDITABLE)
        adapter.items = terms
        adapter.isSearchMode = false
        adapter.notifyItemChanged(0)
        (context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(termsCardSearch.windowToken, 0)
    }

    fun scrollToTop() {
        termsAppbar.setExpanded(true)
        termsRecyclerView.smoothScrollToPosition(0)
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).showBottomNavigationView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RECOGNIZER_REQUEST_CODE) if (data != null && data.extras!!.containsKey(RecognizerIntent.EXTRA_RESULTS)) {
            val text = data.extras!!.getStringArrayList(RecognizerIntent.EXTRA_RESULTS)
            termsEditTextSearch.setText(text!![0], TextView.BufferType.EDITABLE)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun startVoiceSearch() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context!!.packageName)
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