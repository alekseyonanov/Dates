package com.nollpointer.dates.ui.practise

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentDatesPractiseBinding
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.ui.activity.MainActivity
import com.nollpointer.dates.ui.practiseselect.SetDetailsFragment

/**
 * Экран со списком практики для дат
 *
 * @author Onanov Aleksey (@onanov)
 */
class DatesPractiseFragment : Fragment() {

    private var _binding: FragmentDatesPractiseBinding? = null
    private val binding: FragmentDatesPractiseBinding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentDatesPractiseBinding.inflate(inflater, container, false)

        binding.cards.setOnClickListener {
            onPractiseClicked(Practise.CARDS)
        }

        binding.voice.setOnClickListener {
            onPractiseClicked(Practise.VOICE)
        }

        binding.test.setOnClickListener {
            onPractiseClicked(Practise.TEST)
        }

        binding.trueFalse.setOnClickListener {
            onPractiseClicked(Practise.TRUE_FALSE)
        }

        binding.link.setOnClickListener {
            onPractiseClicked(Practise.LINK)
        }

        binding.sort.setOnClickListener {
            onPractiseClicked(Practise.SORT)
        }

        binding.distribution.setOnClickListener {
            onPractiseClicked(Practise.DISTRIBUTION)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Разрешение получено", Toast.LENGTH_SHORT).show()
                onPractiseClicked(Practise.VOICE)
            }
        }
    }

    private fun onPractiseClicked(practise: Int) {
        val mainActivity = activity as MainActivity
        val practiseParcelable = Practise(practise, mainActivity.mode)
        val permissionRecord = ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.RECORD_AUDIO)
        //TODO: переделать этот момент
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
        binding.scrollView.smoothScrollTo(0, 0)
    }

}