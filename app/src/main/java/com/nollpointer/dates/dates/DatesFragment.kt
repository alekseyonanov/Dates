package com.nollpointer.dates.dates

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.preference.PreferenceManager
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import com.nollpointer.dates.activity.MainActivity
import com.nollpointer.dates.dates.DatesInfoDialog.Companion.newInstance
import com.nollpointer.dates.other.Date
import com.nollpointer.dates.other.StartPosition
import java.util.*

class DatesFragment : Fragment(), StartPosition, DatesCardsAdapter.Listener {
    private lateinit var mainActivity: MainActivity
    private lateinit var adapter: DatesCardsAdapter
    //private TabLayout tabLayout;
    private lateinit var recycler: RecyclerView
    private lateinit var dates: List<Date>
    private lateinit var searchContainer: CardView
    private lateinit var searchEditText: EditText
    //private AppBarLayout appBarLayout;
    private lateinit var toolbar: Toolbar
    private  var isEditTextEmpty = true
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dates, container, false)
        recycler = view.findViewById(R.id.dates_recycler_view)
        mainActivity = activity as MainActivity
        ///tabLayout = view.findViewById(R.id.id_tabs);
        toolbar = view.findViewById(R.id.dates_toolbar)
        searchContainer = view.findViewById(R.id.dates_card_search)
        //appBarLayout = view.findViewById(R.id.id_appbar);
        dates = mainActivity.dates
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
        initializeSearchView(view)
        initializeMenu()
        mainActivity.showBottomNavigationView()
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val dateCardValue = preferences.getString("dates_card_type", "0")
        val resources = resources
        adapter = DatesCardsAdapter(dates, mainActivity.mode, resources.getStringArray(R.array.centuries), resources.getStringArray(R.array.centuries_easy))
        if (dateCardValue == "0") adapter.setViewIds(R.layout.card_dates, R.layout.card_dates_top_text) else adapter.setViewIds(R.layout.card_dates_2, R.layout.card_dates_2_top_text)
        adapter.setListener(this)
        val linearLayout = LinearLayoutManager(mainActivity)
        val dividerItemDecoration = DividerItemDecoration(recycler.context,
                linearLayout.orientation)
        recycler.layoutManager = linearLayout
        recycler.addItemDecoration(dividerItemDecoration)
        recycler.adapter = adapter
        recycler.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) //TODO Optimize
                    (context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(recyclerView.windowToken, 0)
            }
        })
        return view
    }

    private fun initializeMenu() {
        toolbar.inflateMenu(R.menu.dates_menu)
        toolbar.setTitle(R.string.title_dates)
        toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.dates_app_bar_search) showSearch()
            true
        }
        toolbar.setOnClickListener {
            goToStartPosition()
            //appBarLayout.setExpanded(true);
        }
    }

    private fun initializeSearchView(mainView: View) {
        searchEditText = mainView.findViewById(R.id.dates_edit_text_search)
        val backButton = mainView.findViewById<ImageView>(R.id.dates_image_search_back)
        val multiButton = mainView.findViewById<ImageView>(R.id.dates_search_multi_button)
        backButton.setImageResource(R.drawable.ic_arrow_back_black)
        multiButton.setImageResource(R.drawable.ic_voice)
        backButton.setOnClickListener { hideSearch() }
        multiButton.setOnClickListener { if (isEditTextEmpty) startVoiceSearch() else searchEditText.setText("", TextView.BufferType.EDITABLE) }
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                isEditTextEmpty = count == 0
                if (isEditTextEmpty) multiButton.setImageResource(R.drawable.ic_voice) else multiButton.setImageResource(R.drawable.ic_clear_black)
                search(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })
        searchEditText.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP &&
                    (keyCode == KeyEvent.KEYCODE_ENTER ||
                            keyCode == KeyEvent.KEYCODE_SEARCH)) {
                (context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(searchContainer.windowToken, 0)
                return@OnKeyListener true
            }
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK && searchContainer.visibility == View.VISIBLE) {
                hideSearch()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun showSearch() { //appBarLayout.setExpanded(false);
        recycler.isNestedScrollingEnabled = false
        searchContainer.visibility = View.VISIBLE
        adapter.startSearchMode()
        (context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        Handler().postDelayed({
            searchEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0))
            searchEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0))
        }, 200)
    }

    private fun hideSearch() { //appBarLayout.setExpanded(true);
        recycler.isNestedScrollingEnabled = true
        searchContainer.visibility = View.GONE
        searchEditText.setText("", TextView.BufferType.EDITABLE)
        adapter.stopSearchMode(dates)
        (context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(searchContainer.windowToken, 0)
        //mainActivity.showBottomNavigationView();
    }

    fun search(query: String) {
        val list = ArrayList<Date>()
        for (date in dates) {
            if (date.contains(query)) list.add(date)
        }
        adapter.setDates(list)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RECOGNIZER_REQUEST_CODE) if (data != null && data.extras!!.containsKey(RecognizerIntent.EXTRA_RESULTS)) {
            val text = data.extras!!.getStringArrayList(RecognizerIntent.EXTRA_RESULTS)
            searchEditText.setText(text!![0], TextView.BufferType.EDITABLE)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun startVoiceSearch() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context!!.packageName)
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, java.lang.Long.valueOf(5000))
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, java.lang.Long.valueOf(5000))
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        startActivityForResult(intent, RECOGNIZER_REQUEST_CODE)
    }

    override fun onItemClick(clickedDate: Date?) {
        val more = newInstance(R.string.gdpr_agree)
        more.show(mainActivity.supportFragmentManager, null)
    }

    //    public void refresh() {
//        this.dates = mainActivity.getDates();
//
//        final int selectedTab = tabLayout.getSelectedTabPosition();
//        if (selectedTab == ALL)
//            adapter.refresh(dates);
//        else {
//            adapter.change_top_texts();
//            ArrayList<Date> list = new ArrayList<>();
//            for (Date date : dates) {
//                if (date.getType() == selectedTab)
//                    list.add(date);
//            }
//            adapter.refresh(list, selectedTab);
//        }
//    }
    override fun goToStartPosition() {
        recycler.smoothScrollToPosition(0)
    }

    companion object {
        private const val RECOGNIZER_REQUEST_CODE = 1417
    }
}