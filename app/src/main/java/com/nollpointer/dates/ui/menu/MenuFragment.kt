package com.nollpointer.dates.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentMenuBinding
import com.nollpointer.dates.databinding.ItemMenuModeBinding
import com.nollpointer.dates.model.MenuPage
import com.nollpointer.dates.ui.activity.MainActivity
import com.nollpointer.dates.ui.view.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class MenuFragment : BaseFragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding: FragmentMenuBinding
        get() = _binding!!

    override val statusBarColorRes = R.color.colorPrimary

    override val isStatusBarLight = false

    private val viewModel by viewModels<MenuViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)

        binding.toolbar.apply {
            inflateMenu(R.menu.menu_menu)
            setOnMenuItemClickListener {
                viewModel.onSettingsClicked()
                true
            }
        }

        binding.telegram.setOnClickListener {
            viewModel.onTelegramClicked()
        }
        binding.twitter.setOnClickListener {
            viewModel.onTwitterClicked()
        }
        binding.mail.setOnClickListener {
            viewModel.onMailClicked()
        }
        binding.vk.setOnClickListener {
            viewModel.onVkClicked()
        }
        binding.instagram.setOnClickListener {
            viewModel.onInstagramClicked()
        }
        binding.onanov.setOnClickListener {
            viewModel.onOnanovClicked()
        }
        binding.game.setOnClickListener {
            viewModel.onGameClicked()
        }

        binding.viewPager.apply {
            val items = resources.getStringArray(R.array.mode_titles)
                    .zip(resources.getStringArray(R.array.mode_count)) { title, count ->
                        MenuPage(title, count)
                    }
            adapter = ModeAdapter(items)
            addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(item: Int) {
                    viewModel.onPageSelected(item)
                }
            })

            binding.dots.setupWithViewPager(this, true)
        }
        binding.modeSelect.setOnClickListener {
            viewModel.onModeSelectClicked(binding.viewPager.currentItem)
        }

        viewModel.apply {
            modeLiveData.observe({ lifecycle }, ::setMode)
            modeSelectVisibilityLiveData.observe({ lifecycle }, ::showModeSelect)
            start()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).showBottomNavigationView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showModeSelect(isShown: Boolean) {
        binding.modeSelect.visibility = if (isShown) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun setMode(mode: Int) {
        binding.viewPager.currentItem = mode
    }

    private inner class ModeAdapter(private val items: List<MenuPage>) : PagerAdapter() {

        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val binding = ItemMenuModeBinding.inflate(layoutInflater)
            binding.title.text = items[position].title
            binding.count.text = items[position].count
            collection.addView(binding.root)
            return binding.root
        }

        override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
            collection.removeView(view as View)
        }

        override fun getCount() = items.size

        override fun isViewFromObject(view: View, other: Any) = view === other
    }

}