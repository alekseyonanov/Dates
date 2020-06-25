package com.nollpointer.dates.terms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import com.nollpointer.dates.activity.MainActivity

class TermsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.fragment_terms, container, false)

        val mainActivity = activity as MainActivity?
        val recyclerView: RecyclerView = mainView.findViewById(R.id.terms_recycler_view)
        val toolbar: Toolbar = mainView.findViewById(R.id.terms_toolbar)
        toolbar.title = "Термины"
        toolbar.inflateMenu(R.menu.dates_menu)

        val adapter = TermsCardsAdapter(mainActivity!!.terms)
        val linearLayout = LinearLayoutManager(mainActivity)
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context,
                linearLayout.orientation)
        recyclerView.layoutManager = linearLayout
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.adapter = adapter

/*
        recyclerView.setOnScrollListener(new RecyclerView . OnScrollListener () {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == SCROLL_STATE_DRAGGING) //TODO Optimize
                    ((InputMethodManager) getContext ().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
            }

        });*/
        return mainView
    }
}