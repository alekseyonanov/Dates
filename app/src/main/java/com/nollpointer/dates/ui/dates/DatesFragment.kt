package com.nollpointer.dates.ui.dates

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.other.CustomItemDecoration
import com.nollpointer.dates.other.Keyboard
import com.nollpointer.dates.other.Loader
import com.nollpointer.dates.ui.activity.MainActivity
import com.nollpointer.dates.ui.activity.MainActivity.Companion.EASY_DATES_MODE
import com.nollpointer.dates.ui.details.DatesDetailsFragment
import com.nollpointer.dates.ui.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_dates.*

/**
 * @author Onanov Aleksey (@onanov)
 */
class DatesFragment : BaseFragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var adapter: DatesAdapter

    //private TabLayout tabLayout;
    private lateinit var dates: List<Date>

    //private AppBarLayout appBarLayout;
    private var isEditTextEmpty = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        datesToolbar.apply {
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

        ///tabLayout = view.findViewById(R.id.id_tabs);
        //appBarLayout = view.findViewById(R.id.id_appbar);
        //tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int position = tab.getPosition();
//                if (tab.getPosition() != ALL) {
//                    ArrayList<Date> list = new ArrayList<>();
//                    for (Date date : dates) {
//                        if (date.getType() == position)
//                            list.add(date);
//                    }
//                    adapter.refresh(list, position);
//                } else
//                    adapter.refresh(dates, ALL);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                goToStartPosition();
//            }
//        });
        initializeSearchView()

        adapter = DatesAdapter(resources, dates, mainActivity.mode).apply {
            onDateClickListener = {
                Keyboard.hide(datesCardSearch)
                fragmentManager?.beginTransaction()?.addToBackStack(null)?.replace(R.id.frameLayout, DatesDetailsFragment.newInstance(it))?.commit()
            }
            when (Loader.getDatesViewType(context as Context)) {
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

        datesRecyclerView.apply {
            val dividerItemDecoration = CustomItemDecoration(datesRecyclerView.context,
                    DividerItemDecoration.VERTICAL,
                    resources.getIntArray(when (mainActivity.mode) {
                        EASY_DATES_MODE -> R.array.dates_easy_positions
                        else -> R.array.dates_full_positions
                    }))
            dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider) as Drawable)
            layoutManager = LinearLayoutManager(this.context)
            addItemDecoration(dividerItemDecoration)
            this.adapter = this@DatesFragment.adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) //TODO Optimize
                        Keyboard.hide(datesCardSearch)
                }
            })
        }
    }

    override fun getStatusBarColorRes() = R.color.colorPrimary

    override fun isStatusBarLight() = false

    override fun onStart() {
        super.onStart()
        mainActivity.showBottomNavigationView()
    }

    private fun initializeSearchView() {
        datesSearchBack.apply {
            setImageResource(R.drawable.ic_arrow_back_black)
            setOnClickListener { hideSearch() }
        }
        datesSearchMultiButton.apply {
            setImageResource(R.drawable.ic_voice)
            setOnClickListener { if (isEditTextEmpty) startVoiceSearch() else datesSearchEditText.setText("", TextView.BufferType.EDITABLE) }
        }

        datesSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                isEditTextEmpty = count == 0
                if (isEditTextEmpty) datesSearchMultiButton.setImageResource(R.drawable.ic_voice) else datesSearchMultiButton.setImageResource(R.drawable.ic_clear_black)
                search(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })
        datesSearchEditText.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP &&
                    (keyCode == KeyEvent.KEYCODE_ENTER ||
                            keyCode == KeyEvent.KEYCODE_SEARCH)) {
                (context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(datesCardSearch.windowToken, 0)
                return@OnKeyListener true
            }
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK && datesCardSearch.visibility == View.VISIBLE) {
                hideSearch()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun showSearch() {
        datesAppbar.setExpanded(true)
        datesRecyclerView.isNestedScrollingEnabled = false
        datesCardSearch.visibility = View.VISIBLE
        adapter.isSearchMode = true
        adapter.notifyItemChanged(0)
        Keyboard.show(datesCardSearch)
        Handler().postDelayed({
            datesSearchEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0))
            datesSearchEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0))
        }, 200)
    }

    private fun hideSearch() {
        datesRecyclerView.isNestedScrollingEnabled = true
        datesCardSearch.visibility = View.GONE
        datesSearchEditText.setText("", TextView.BufferType.EDITABLE)
        adapter.isSearchMode = false
        adapter.notifyItemChanged(0)
        Keyboard.hide(datesCardSearch)
    }

    fun search(query: String) {
        adapter.items = if (query.isEmpty())
            dates
        else
            dates.filter { date -> date.contains(query) }

        adapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RECOGNIZER_REQUEST_CODE) if (data != null && data.extras!!.containsKey(RecognizerIntent.EXTRA_RESULTS)) {
            val text = data.extras!!.getStringArrayList(RecognizerIntent.EXTRA_RESULTS)
            datesSearchEditText.setText(text!![0], TextView.BufferType.EDITABLE)
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

    fun scrollToTop() {
        datesAppbar.setExpanded(true)
        datesRecyclerView.smoothScrollToPosition(0)
    }



    companion object {
        private const val RECOGNIZER_REQUEST_CODE = 1417

        fun newInstance() = DatesFragment()
    }
}