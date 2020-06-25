package com.nollpointer.dates.practise

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.nollpointer.dates.R
import com.nollpointer.dates.activity.MainActivity
import com.nollpointer.dates.other.PractiseHelpDialog.Companion.newInstance
import com.nollpointer.dates.practise.PractiseConstants.VOICE

class PractiseFragment : Fragment(), PractiseCellView.OnClickListener {

    private lateinit var tabLayout: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.fragment_practise, container, false)
        val toolbar: Toolbar = mainView.findViewById(R.id.practise_toolbar)
        toolbar.inflateMenu(R.menu.practise_menu)
        toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.practise_help) {
                val helpDialog = newInstance(R.string.help_practise)
                helpDialog.show(activity!!.supportFragmentManager, null)
            }
            true
        }
        toolbar.setNavigationOnClickListener { fragmentManager!!.popBackStack() }
        val viewPager: ViewPager = mainView.findViewById(R.id.practise_view_pager)
        viewPager.adapter = PractiseCellAdapter()
        tabLayout = mainView.findViewById(R.id.practise_tabs)
        tabLayout.setupWithViewPager(viewPager, true)
        return mainView
    }

    private val lastMarks: IntArray
        get() {
            val markTitles = resources.getStringArray(R.array.practise_marks_titles)
            val marks = IntArray(markTitles.size)
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            for (i in marks.indices) marks[i] = preferences.getInt(markTitles[i], -1)
            return marks
        }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity?)!!.showBottomNavigationView()
    }

    override fun onClicked(practise: String?, mode: Int) {
        val mainActivity = activity as MainActivity?
        val permissionRecord = ContextCompat.checkSelfPermission(mainActivity!!, Manifest.permission.RECORD_AUDIO)
        if (practise == VOICE && permissionRecord != PackageManager.PERMISSION_GRANTED) ActivityCompat.requestPermissions(mainActivity, arrayOf(Manifest.permission.RECORD_AUDIO), 1) else mainActivity.supportFragmentManager.beginTransaction().replace(R.id.frameLayout, PractiseDetailsPickerFragment.newInstance(practise as String, mode == 1, mainActivity.mode)).addToBackStack(null).commit()
    }

    inner class PractiseCellAdapter : PagerAdapter() {
        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val recyclerView = PractiseCellView(context!!)
            recyclerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            recyclerView.setListener(this@PractiseFragment)
            //if(position == 1)
            recyclerView.setPractiseMode(position)
            if (position == 1) recyclerView.setMarks(lastMarks)
            collection.addView(recyclerView)
            return recyclerView
        }

        override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
            collection.removeView(view as View)
        }

        override fun getCount(): Int {
            return 2
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return if (position == 0)
                "Тренировка"
            else
                "Тестирование"
        }
    }

    companion object {
        private const val TAG = "PractiseFragment"

    }
}