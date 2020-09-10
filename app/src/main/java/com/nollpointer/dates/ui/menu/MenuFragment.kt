package com.nollpointer.dates.ui.menu

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.nollpointer.dates.R
import com.nollpointer.dates.ui.activity.MainActivity
import com.nollpointer.dates.ui.game.GameFragment
import com.nollpointer.dates.ui.settings.main.SettingsFragment
import kotlinx.android.synthetic.main.fragment_menu.*

/**
 * @author Onanov Aleksey (@onanov)
 */
class MenuFragment : Fragment() {

    private var currentMode = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        telegramMessage.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://t.me/alekseyonanov"))
            startActivity(intent)
        }
        twitterMessage.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/alekseyonanov"))
            startActivity(intent)
        }
        mailMessage.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("dates@onanov.ru"))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.message_developer_title))
            startActivity(intent)
        }
        vkMessage.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/onanov"))
            startActivity(intent)
        }
        instagramMessage.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/alekseyonanov"))
            startActivity(intent)
        }
        onanovRu.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://onanov.ru/"))
            startActivity(intent)
        }
        gameStart.setOnClickListener {
            fragmentManager!!.beginTransaction().replace(R.id.frameLayout, GameFragment()).addToBackStack(null).commit()
        }

        currentMode = (activity as MainActivity?)!!.mode

        menuViewPager.apply {
            adapter = ModeAdapter(context, R.array.mode_count, R.array.mode_titles)
            addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
                override fun onPageSelected(i: Int) {
                    if (i == currentMode) menuSelectCurrentMode.visibility = View.GONE else menuSelectCurrentMode.visibility = View.VISIBLE
                }

                override fun onPageScrollStateChanged(i: Int) {}
            })
            currentItem = currentMode
        }
        menuTabDots.setupWithViewPager(menuViewPager, true)
        menuSelectCurrentMode.setOnClickListener { v ->
            val mainActivity = activity as MainActivity?
            currentMode = menuViewPager.currentItem
            mainActivity!!.updateMode(currentMode)
            Log.e("TAG", "onClick: $currentMode")
            SaveCurrentMode(activity, currentMode).execute()
            v.visibility = View.GONE
        }

        menuToolbar.apply {
            inflateMenu(R.menu.menu_menu)
            setOnMenuItemClickListener {
                fragmentManager?.beginTransaction()?.addToBackStack(null)?.replace(R.id.frameLayout, SettingsFragment.newInstance())?.commit()
                true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity?)!!.showBottomNavigationView()
    }

    private inner class ModeAdapter(private val context: Context?, modeCountResourceId: Int, modeTitleResourceId: Int) : PagerAdapter() {
        private val modeCountArray: Array<String>
        private val modeTitle: Array<String>

        init {
            val resources = context!!.resources
            modeCountArray = resources.getStringArray(modeCountResourceId)
            modeTitle = resources.getStringArray(modeTitleResourceId)
        }

        override fun instantiateItem(collection: ViewGroup, position: Int): Any { //CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
            val inflater = LayoutInflater.from(context)
            val layout = inflater.inflate(R.layout.item_menu_mode, collection, false) as ViewGroup
            collection.addView(layout)
            val modeTitleTextView = layout.findViewById<TextView>(R.id.mode_title)
            val modeDatesCountTextView = layout.findViewById<TextView>(R.id.mode_dates_count)
            modeTitleTextView.text = modeTitle[position]
            modeDatesCountTextView.text = modeCountArray[position]
            return layout
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
    }

    class SaveCurrentMode internal constructor(var context: Context?, var mode: Int) : AsyncTask<Void, Void?, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putInt("mode", mode)
            Log.e("asd", "doInBackground: written")
            editor.apply()
            return null
        }
    }
}