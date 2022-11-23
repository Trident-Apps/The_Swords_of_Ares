package com.gameinsight.gplay.islan.ui.activities

import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.gameinsight.gplay.islan.ui.viewmodel.AresViewModel

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    lateinit var viewModel: AresViewModel
    var mViewBinding: VB? = null
    val viewBinding get() = mViewBinding
    abstract fun initViewBinding(): VB
    abstract fun naviGateTo(link: String, adb: String)

    override fun onDestroy() {
        super.onDestroy()
        mViewBinding = null
    }
}