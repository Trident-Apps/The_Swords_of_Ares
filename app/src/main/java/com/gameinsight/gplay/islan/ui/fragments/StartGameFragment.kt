package com.gameinsight.gplay.islan.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.gameinsight.gplay.islan.R
import com.gameinsight.gplay.islan.databinding.StartGameFgBinding

class StartGameFragment : BaseFragment<StartGameFgBinding>() {
    lateinit var iM: ImageView
    lateinit var iM2: ImageView
    private var totalScore = 0
    private var fFlag = false
    private var sFlag = false
    private val drawablesList = listOf(
        R.drawable.e,
        R.drawable.q,
        R.drawable.r,
        R.drawable.w
    )

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
        changeIvPosition(drawablesList)
        viewBinding?.let { binding ->
            listOf(
                binding.iv,
                binding.iv2,
                binding.iv3,
                binding.iv4
            ).forEach { iV ->
                iV.setOnClickListener { view ->
                    view as ImageView
                    iM = view
                    fFlag = true
                    setIvi(view)
                }
            }
            listOf(
                binding.iv5,
                binding.iv6,
                binding.iv7,
                binding.iv8
            ).forEach { iV ->
                iV.setOnClickListener { image ->
                    image as ImageView
                    iM2 = image
                    sFlag = true
                    setIvi(image)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {}
            })
    }

    private fun setIvi(iV: ImageView) {
        iV.animate().apply {
            duration = 1000
            rotationXBy(1080F)
        }.withEndAction {
            when (iV) {
                viewBinding?.iv -> {
                    iV.setImageResource(drawablesList[1])
                    iV.tag = drawablesList[1]
                }
                viewBinding?.iv2 -> {
                    iV.setImageResource(drawablesList[0])
                    iV.tag = drawablesList[0]
                }
                viewBinding?.iv3 -> {
                    iV.setImageResource(drawablesList[3])
                    iV.tag = drawablesList[3]
                }
                viewBinding?.iv4 -> {
                    iV.setImageResource(drawablesList[2])
                    iV.tag = drawablesList[2]
                }
                viewBinding?.iv5 -> {
                    iV.setImageResource(drawablesList[3])
                    iV.tag = drawablesList[3]
                }
                viewBinding?.iv6 -> {
                    iV.setImageResource(drawablesList[1])
                    iV.tag = drawablesList[1]
                }
                viewBinding?.iv7 -> {
                    iV.setImageResource(drawablesList[2])
                    iV.tag = drawablesList[2]
                }
                viewBinding?.iv8 -> {
                    iV.setImageResource(drawablesList[0])
                    iV.tag = drawablesList[0]
                }
            }
            if (fFlag && sFlag) {
                compareIv(iM, iM2)
            }
        }
    }

    private fun compareIv(iv1: ImageView, iv2: ImageView) {
        if (iv1.tag == iv2.tag) {
            iv1.visibility = View.INVISIBLE
            iv2.visibility = View.INVISIBLE
            sFlag = false
            fFlag = false
            totalScore++
        } else {
            iv1.animate().apply {
                duration = 1000L
                rotationXBy(1080F)
            }.withEndAction {
                iv1.setImageResource(R.drawable.init)
                fFlag = false
            }
            iv2.animate().apply {
                duration = 1000L
                rotationXBy(1080F)
            }.withEndAction {
                iv2.setImageResource(R.drawable.init)
                sFlag = false
            }
        }
        if (totalScore == 4) {
            findNavController().navigate(R.id.resultFragment)
        }
    }

    private fun changeIvPosition(list: List<Int>) {
        list.shuffled()
    }

    override fun getVB(inflater: LayoutInflater, container: ViewGroup?) =
        StartGameFgBinding.inflate(
            inflater, container, false
        )
}