package com.nollpointer.dates.ui.practise

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.ui.activity.MainActivity
import com.nollpointer.dates.ui.practiseselect.SetDetailsFragment
import kotlinx.android.synthetic.main.fragment_dates_practise.*

class DatesPractiseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dates_practise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        practiseToolbar.apply {
//            inflateMenu(R.menu.practise_menu)
//            setOnMenuItemClickListener { menuItem ->
//                if (menuItem.itemId == R.id.practise_help) {
//                    val helpDialog = newInstance(R.string.help_practise)
//                    helpDialog.show(activity!!.supportFragmentManager, null)
//                }
//                true
//            }
//            setOnClickListener {scrollToTop() }
//            setNavigationOnClickListener { fragmentManager!!.popBackStack() }
//        }

        practiseCards.setOnClickListener {
            onPractiseClicked(Practise.CARDS)
        }

        practiseVoice.setOnClickListener {
            onPractiseClicked(Practise.VOICE)
        }

        practiseTest.setOnClickListener {
            onPractiseClicked(Practise.TEST)
        }

        practiseTrueFalse.setOnClickListener {
            onPractiseClicked(Practise.TRUE_FALSE)
        }

        practiseLink.setOnClickListener {
            onPractiseClicked(Practise.LINK)
        }

        practiseSort.setOnClickListener {
            onPractiseClicked(Practise.SORT)
        }

        practiseDistribution.setOnClickListener {
            onPractiseClicked(Practise.DISTRIBUTION)
        }

//        practiseViewPager.apply {
//            adapter = PractiseCellAdapter()
//            pageMargin = 32
//        }
//        practiseTabLayout.setupWithViewPager(practiseViewPager, true)

    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity?)!!.showBottomNavigationView()
    }

    private fun onPractiseClicked(practise: Int){
        val mainActivity = activity as MainActivity
        val practiseParcelable = Practise(practise,mainActivity.mode)
        val permissionRecord = ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.RECORD_AUDIO)
        if (practise == Practise.VOICE && permissionRecord != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(mainActivity, arrayOf(Manifest.permission.RECORD_AUDIO), 1)
        else
            mainActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout, SetDetailsFragment.newInstance(practiseParcelable))
                    .addToBackStack(null)
                    .commit()
    }

    fun scrollToTop() {
        practiseScrollView.smoothScrollTo(0,0)
    }

}