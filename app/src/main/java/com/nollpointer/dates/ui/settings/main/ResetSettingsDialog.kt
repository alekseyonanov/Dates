package com.nollpointer.dates.ui.settings.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nollpointer.dates.databinding.DialogResetSettingsBinding
import com.nollpointer.dates.other.Loader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Диалоговое окно, сбрасывающее выбор настроек
 *
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class ResetSettingsDialog : BottomSheetDialogFragment() {

    private var _binding: DialogResetSettingsBinding? = null
    private val binding: DialogResetSettingsBinding
        get() = _binding!!

    @Inject
    lateinit var loader: Loader

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogResetSettingsBinding.inflate(inflater, container, false)

        binding.reset.setOnClickListener {
            loader.clear()
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = ResetSettingsDialog()
    }
}