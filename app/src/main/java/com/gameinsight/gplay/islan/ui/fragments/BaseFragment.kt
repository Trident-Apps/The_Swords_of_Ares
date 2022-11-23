package com.gameinsight.gplay.islan.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    protected var mViewBinding: VB? = null
    protected val viewBinding get() = mViewBinding

    abstract fun getVB(inflater: LayoutInflater, container: ViewGroup?): VB
    override fun onDestroyView() {
        super.onDestroyView()
        mViewBinding = null
    }
}