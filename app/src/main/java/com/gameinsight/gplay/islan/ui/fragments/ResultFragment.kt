package com.gameinsight.gplay.islan.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gameinsight.gplay.islan.R
import com.gameinsight.gplay.islan.databinding.ResultGameFgBinding

class ResultFragment : BaseFragment<ResultGameFgBinding>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewBinding = getVB(inflater, container)
        return viewBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.resBtn?.setOnClickListener {
            findNavController().navigate(R.id.startGameFragment)
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {}
            })
    }

    override fun getVB(inflater: LayoutInflater, container: ViewGroup?) =
        ResultGameFgBinding.inflate(inflater, container, false)

}