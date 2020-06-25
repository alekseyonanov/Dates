package com.nollpointer.dates.menu

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
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.tabs.TabLayout
import com.nollpointer.dates.R
import com.nollpointer.dates.activity.MainActivity
import com.nollpointer.dates.activity.SettingsActivity
import com.nollpointer.dates.game.GameFragment
import com.nollpointer.dates.other.OnDatesLoadListener

class MenuFragment : Fragment() {

    private lateinit var modeViewPager: ViewPager
    private var currentMode = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        val mainView = inflater.inflate(R.layout.fragment_menu, container, false)
        val telegramMessage = mainView.findViewById<TextView>(R.id.telegram_message)
        val twitterMessage = mainView.findViewById<TextView>(R.id.twitter_message)
        val mailMessage = mainView.findViewById<TextView>(R.id.mail_message)
        val gameStart = mainView.findViewById<TextView>(R.id.game_start)
        val toolbar: Toolbar = mainView.findViewById(R.id.menu_toolbar)
        telegramMessage.setText(R.string.telegram_label)
        telegramMessage.setOnClickListener {
            //var intent: Intent
            /*try { // get the Telegram app if possible
                context!!.packageManager.getPackageInfo("org.ic_telegram.messenger", 0)
                intent = Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=nollpointer"))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            } catch (e: Exception) { // no Telegram app, revert to browser*/
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://t.me/alekseyonanov"))
            //}
            startActivity(intent)
        }
        twitterMessage.setText(R.string.twitter_label)
        twitterMessage.setOnClickListener {
            var intent: Intent
            try { // get the Twitter app if possible
                context!!.packageManager.getPackageInfo("com.ic_twitter.android", 0)
                intent = Intent(Intent.ACTION_VIEW, Uri.parse("ic_twitter://user?screen_name=alekseyonanov"))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            } catch (e: Exception) { // no Twitter app, revert to browser
                intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ic_twitter.com/alekseyonanov"))
            }
            startActivity(intent)
        }
        mailMessage.setText(R.string.mail_label)
        mailMessage.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("dates@onanov.ru"))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.message_developer_title))
            startActivity(intent)
        }
        gameStart.setText(R.string.game_label)
        gameStart.setOnClickListener {
            fragmentManager!!.beginTransaction().replace(R.id.frameLayout, GameFragment()).addToBackStack(null).commit()
        }
        toolbar.inflateMenu(R.menu.menu_menu)
        toolbar.setOnMenuItemClickListener {
            val intent = Intent(context, SettingsActivity::class.java)
            startActivity(intent)
            true
        }
        currentMode = (activity as MainActivity?)!!.mode
        modeViewPager = mainView.findViewById(R.id.menu_view_pager)
        val selectCurrentModeButton = mainView.findViewById<Button>(R.id.menu_select_current_mode)
        val tabLayout = mainView.findViewById<View>(R.id.menu_tab_dots) as TabLayout
        tabLayout.setupWithViewPager(modeViewPager, true)
        modeViewPager.adapter = ModeAdapter(context, R.array.mode_count, R.array.mode_titles)
        modeViewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
            override fun onPageSelected(i: Int) {
                if (i == currentMode) selectCurrentModeButton.visibility = View.GONE else selectCurrentModeButton.visibility = View.VISIBLE
            }

            override fun onPageScrollStateChanged(i: Int) {}
        })
        modeViewPager.currentItem = currentMode
        selectCurrentModeButton.setOnClickListener { v ->
            val mainActivity = activity as MainActivity?
            currentMode = modeViewPager.currentItem
            mainActivity!!.updateMode(currentMode, object : OnDatesLoadListener {
                override fun onLoadStart() {
                    Log.e("TAG", "onLoadStart: " + System.currentTimeMillis())
                }

                override fun onLoadEnd() {
                    Log.e("TAG", "onLoadEnd: " + System.currentTimeMillis())
                }
            })
            Log.e("TAG", "onClick: $currentMode")
            SaveCurrentMode(activity, currentMode).execute()
            v.visibility = View.GONE
        }
        return mainView
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
            val layout = inflater.inflate(R.layout.card_menu_mode, collection, false) as ViewGroup
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

    protected class SaveCurrentMode internal constructor(var context: Context?, var mode: Int) : AsyncTask<Void, Void?, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putInt("mode", mode)
            Log.e("asd", "doInBackground: written")
            editor.apply()
            return null
        }

    }
}