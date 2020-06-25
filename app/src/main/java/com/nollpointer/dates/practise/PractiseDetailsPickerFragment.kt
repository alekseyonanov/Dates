package com.nollpointer.dates.practise

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckedTextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appodeal.ads.Appodeal
import com.flurry.android.FlurryAgent
import com.google.android.material.chip.ChipGroup
import com.nollpointer.dates.R
import com.nollpointer.dates.activity.MainActivity
import com.nollpointer.dates.cards.CardsFragment.Companion.newInstance
import com.nollpointer.dates.distribute.DistributeFragment
import com.nollpointer.dates.other.Date
import com.nollpointer.dates.other.Misc.getIntegerListFromString
import com.nollpointer.dates.other.Misc.getStringFromIntegerList
import com.nollpointer.dates.practise.PractiseConstants.CARDS
import com.nollpointer.dates.practise.PractiseConstants.DIFFICULTY_EASY
import com.nollpointer.dates.practise.PractiseConstants.DIFFICULTY_HARD
import com.nollpointer.dates.practise.PractiseConstants.DIFFICULTY_MEDIUM
import com.nollpointer.dates.practise.PractiseConstants.DISTRIBUTE
import com.nollpointer.dates.practise.PractiseConstants.SORT
import com.nollpointer.dates.practise.PractiseConstants.TEST
import com.nollpointer.dates.practise.PractiseConstants.TEST_MODE
import com.nollpointer.dates.practise.PractiseConstants.TRUE_FALSE
import com.nollpointer.dates.practise.PractiseConstants.VOICE
import com.nollpointer.dates.sort.SortFragment
import com.nollpointer.dates.sort.SortFragment.Companion.newInstance
import com.nollpointer.dates.test.TestFragment
import com.nollpointer.dates.truefalse.TrueFalseFragment
import com.nollpointer.dates.voice.VoiceFragment
import java.util.*

open class PractiseDetailsPickerFragment : Fragment() {
    private lateinit var typeAdapter: PractiseDetailsPickerAdapter
    private lateinit var centuryAdapter: PractiseDetailsPickerAdapter
    private lateinit var difficultyChipGroup: ChipGroup
    private var mode = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.fragment_practise_details_picker, container, false)
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val bundle = arguments
        val saveCurrentState = preferences.getBoolean("save_current_state", true)
        mode = bundle!!.getInt(MainActivity.MODE, MainActivity.FULL_DATES_MODE)
        val practise = bundle.getString(PRACTISE)
        val toolbar: Toolbar = mainView.findViewById(R.id.practise_details_picker_toolbar)
        toolbar.inflateMenu(R.menu.practise_details_picker_menu)
        toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.practise_details_picker_random) setRandomValues(isLockedType(practise))
            true
        }
        toolbar.setNavigationOnClickListener { fragmentManager!!.popBackStack() }
        val practiseButton = mainView.findViewById<Button>(R.id.practise_details_picker_button)
        practiseButton.setOnClickListener { startPractise() }
        val resources = resources
        val centuryTitle = mainView.findViewById<CheckedTextView>(R.id.centuryTitle)
        centuryTitle.setOnClickListener { v ->
            val view = v as CheckedTextView
            if (view.isChecked) {
                if (centuryAdapter.getCenturies() != generateFullCenturiesList(mode)) view.isChecked = false
            } else {
                view.isChecked = true
                centuryAdapter.setCenturies(generateFullCenturiesList(mode))
                centuryAdapter.notifyDataSetChanged()
            }
        }
        val typeRecyclerView: RecyclerView = mainView.findViewById(R.id.typeRecyclerView)
        typeAdapter = PractiseDetailsPickerAdapter(resources.getTextArray(R.array.pick_type),
                PractiseDetailsPickerAdapter.TYPE)
        typeRecyclerView.adapter = typeAdapter
        typeAdapter.setTitleCheckedTextView(centuryTitle)
        typeRecyclerView.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        val centuryRecyclerView: RecyclerView = mainView.findViewById(R.id.centuriesRecyclerView)
        centuryAdapter = PractiseDetailsPickerAdapter(
                if (mode == MainActivity.FULL_DATES_MODE) resources.getTextArray(R.array.centuries) else resources.getTextArray(R.array.centuries_easy),
                PractiseDetailsPickerAdapter.CENTURY)
        centuryAdapter.setTitleCheckedTextView(centuryTitle)
        centuryRecyclerView.adapter = centuryAdapter
        centuryRecyclerView.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        difficultyChipGroup = mainView.findViewById(R.id.difficulty_chip_group)
        difficultyChipGroup.check(R.id.difficulty_chip_easy)
        difficultyChipGroup.setOnCheckedChangeListener(object : ChipGroup.OnCheckedChangeListener {
            var previousSelection = R.id.difficulty_chip_easy
            override fun onCheckedChanged(chipGroup: ChipGroup, id: Int) {
                if (id == View.NO_ID) chipGroup.check(previousSelection) else previousSelection = id
            }
        })
        //TODO добавить выбор сразу всех веков по кнопке
        if (saveCurrentState) {
            val type = preferences.getInt("practise_type", 0)
            val list: ArrayList<Int> = if (mode == MainActivity.FULL_DATES_MODE) getIntegerListFromString(preferences.getString("practise_centuries_full", null)) else getIntegerListFromString(preferences.getString("practise_centuries_easy", null))
            if (isLockedType(practise)) setLockedType(practise) else typeAdapter.type = type
            centuryAdapter.setCenturies(list)
            if (list.size == 10 && mode == MainActivity.FULL_DATES_MODE || mode == MainActivity.EASY_DATES_MODE && list.size == 2) centuryTitle.isChecked = true
        } else {
            if (isLockedType(practise)) setLockedType(practise)
        }
        return mainView
    }

    private fun generateFullCenturiesList(mode: Int): ArrayList<Int> {
        val list = ArrayList<Int>()
        val max = if (mode == MainActivity.FULL_DATES_MODE) 10 else 2
        for (i in 0 until max) {
            list.add(i)
        }
        return list
    }

    private fun setLockedType(practise: String?) {
        when (practise) {
            CARDS -> typeAdapter.type = 1
            VOICE -> typeAdapter.type = 0
            TRUE_FALSE -> typeAdapter.type = 2
            SORT -> typeAdapter.type = 1
            DISTRIBUTE -> typeAdapter.type = 1
        }
        typeAdapter.setLocked()
    }

    private fun isLockedType(practise: String?): Boolean {
        return practise == CARDS || practise == TRUE_FALSE || practise == SORT || practise == DISTRIBUTE || practise == VOICE
    }

    private fun startPractise() {
        val type = typeAdapter.type
        val centuries = centuryAdapter.getCenturies()
        SaveCurrentState(context, type, centuries, mode).execute()
        if (centuries.isEmpty())
            return
        val dates = getListForPractise(centuries)
        val practise = arguments!!.getString(PRACTISE)
        val isTestMode = arguments!!.getBoolean(TEST_MODE)
        val fragment: Fragment
        var event: String
        when (practise) {
            CARDS -> {
                fragment = newInstance(dates, type)
                event = "Cards"
            }
            VOICE -> {
                fragment = VoiceFragment.newInstance(dates, type, difficulty, isTestMode)
                event = "Voice"
            }
            TEST -> {
                fragment = TestFragment.newInstance(dates, type, difficulty, isTestMode)
                event = "Test"
            }
            TRUE_FALSE -> {
                fragment = TrueFalseFragment.newInstance(dates, difficulty, isTestMode)
                event = "TrueFalse"
            }
            SORT -> {
                fragment = SortFragment.newInstance(dates, difficulty, isTestMode)
                event = "Sort"
            }
            DISTRIBUTE -> {
                fragment = DistributeFragment()
                event = "Distribute"
            }
            else -> {
                fragment = newInstance(dates, type)
                event = "Cards"
            }
        }
        if (isTestMode) {
            showAds()
            event += "_Test"
        }
        FlurryAgent.logEvent(event)
        fragmentManager!!.beginTransaction().replace(R.id.frameLayout, fragment).commit()
    }

    private fun showAds() {
        if (Appodeal.isLoaded(Appodeal.INTERSTITIAL)) Appodeal.show(activity!!, Appodeal.INTERSTITIAL)
    }

    private val difficulty: Int = when (difficultyChipGroup.checkedChipId) {
        R.id.difficulty_chip_easy -> DIFFICULTY_EASY
        R.id.difficulty_chip_medium -> DIFFICULTY_MEDIUM
        R.id.difficulty_chip_hard -> DIFFICULTY_HARD
        else -> DIFFICULTY_EASY
    }

    private fun setRandomValues(isTypeLocked: Boolean) {
        centuryAdapter.makeRandomValues()
        setRandomDifficulty()
        if (!isTypeLocked) typeAdapter.makeRandomValues()
    }

    private fun setRandomDifficulty() {
        val random = Random(System.currentTimeMillis())
        difficultyChipGroup.check(difficultyChipGroup.getChildAt(random.nextInt(3)).id)
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity?)!!.hideBottomNavigationView()
    }

    private fun getListForPractise(centuriesList: List<Int>): ArrayList<Date> {
        val mainActivity = activity as MainActivity
        val dates: ArrayList<Date> = mainActivity.dates
        val practiseList = ArrayList<Date>()
        if (mode == MainActivity.FULL_DATES_MODE && centuriesList.size == 10 || mode == MainActivity.EASY_DATES_MODE && centuriesList.size == 2) // Если выбраны все даты
            return dates
        var pair: Pair<Int?, Int?>
        Collections.sort(centuriesList)
        for (number in centuriesList) {
            pair = getDatesRange(number, mode)
            practiseList.addAll(dates.subList(pair.first!!, pair.second!!))
        }
        return practiseList
    }

    private fun getDatesRange(pickedCentury: Int, mode: Int): Pair<Int?, Int?> {
        var start = 0
        var end = 0
        if (mode == MainActivity.FULL_DATES_MODE) {
            when (pickedCentury) {
                0 -> {
                    start = 0
                    end = start + 21
                }
                1 -> {
                    start = 21
                    end = start + 20
                }
                2 -> {
                    start = 41
                    end = start + 44
                }
                3 -> {
                    start = 85
                    end = start + 33
                }
                4 -> {
                    start = 118
                    end = start + 42
                }
                5 -> {
                    start = 160
                    end = start + 49
                }
                6 -> {
                    start = 209
                    end = start + 47
                }
                7 -> {
                    start = 256
                    end = start + 42
                }
                8 -> {
                    start = 298
                    end = start + 50
                }
                9 -> {
                    start = 348
                    end = start + 58
                }
            }
        } else {
            when (pickedCentury) {
                0 -> {
                    start = 0
                    end = start + 48
                }
                1 -> {
                    start = 48
                    end = start + 47
                }
            }
        }
        return Pair(start, end)
    }

    protected class SaveCurrentState : AsyncTask<Void, Void?, Void> {
        private var type = -1
        private var centuries: List<Int?>
        private var context: Context?
        private var mode = 0

        internal constructor(context: Context?, type: Int, centuries: List<Int?>, mode: Int) {
            this.context = context
            this.type = type
            this.centuries = centuries
            this.mode = mode
        }

        internal constructor(context: Context?, centuries: List<Int?>) {
            this.context = context
            this.centuries = centuries
        }

        override fun doInBackground(vararg voids: Void): Void? {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val numberString = getStringFromIntegerList(centuries)
            if (type != -1) editor.putInt("practise_type", type)
            if (mode == MainActivity.FULL_DATES_MODE) editor.putString("practise_centuries_full", numberString) else editor.putString("practise_centuries_easy", numberString)
            editor.apply()
            return null
        }


    }

    companion object {
        private const val PRACTISE = "practise"
        private const val TAG = "PractiseDetailsPicker"
        fun newInstance(practise: String, practiseMode: Boolean, mode: Int): PractiseDetailsPickerFragment {
            val practiseDetailsPickerFragment = PractiseDetailsPickerFragment()
            val bundle = Bundle()
            bundle.putString(PRACTISE, practise)
            bundle.putInt(MainActivity.MODE, mode)
            bundle.putBoolean(TEST_MODE, practiseMode)
            practiseDetailsPickerFragment.arguments = bundle
            return practiseDetailsPickerFragment
        }
    }
}