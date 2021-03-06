package com.zky.basics.common.mvvm

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zky.basics.common.mvvm.viewmodel.BaseViewModel
import com.zky.basics.common.mvvm.viewmodel.BaseViewModel.ParameterField
import com.zky.basics.common.util.log.KLog


abstract class BaseMvvmActivity<V : ViewDataBinding?, VM : BaseViewModel<*>?> :
    BaseActivity() {
    private var mBinding: V? = null
    @JvmField
    protected var mViewModel: VM? = null
    private var viewModelId = 0
    override fun initContentView() {
        initViewDataBinding()
        initBaseViewObservable()
        initViewObservable()
    }

    private fun createViewModel(): VM? {
        val onBindViewModelFactory = onBindViewModelFactory()
        if (onBindViewModelFactory != null) {
            return ViewModelProvider(this, onBindViewModelFactory)[onBindViewModel()]
        }
        return null
    }

    private fun initViewDataBinding() {
        mBinding = DataBindingUtil.setContentView<V>(this, onBindLayout())
        viewModelId = onBindVariableId()
        mViewModel = createViewModel()
        if (mBinding != null && mViewModel != null) {
            mBinding?.setVariable(viewModelId, mViewModel)
            lifecycle.addObserver(mViewModel!!)
        }

    }

    abstract fun onBindViewModel(): Class<VM>
    abstract fun onBindViewModelFactory(): ViewModelProvider.Factory?
    abstract fun initViewObservable()
    abstract fun onBindVariableId(): Int
    protected open fun initBaseViewObservable() {
        mViewModel?.uc()?.showInitLoadViewEvent?.observe(
            this,
            Observer { show: Boolean? ->
                showInitLoadView(
                    show!!
                )
            }
        )
        mViewModel?.uc()?.showTransLoadingViewEvent?.observe(
            this,
            { show: Boolean? ->
                KLog.v("MYTAG", "view postShowTransLoadingViewEvent start...")
                showTransLoadingView(show!!)
            }
        )
        mViewModel?.uc()?.showNoDataViewEvent?.observe(
            this,
            Observer { show: Boolean? ->
                showNoDataView(
                    show!!
                )
            }
        )
        mViewModel?.uc()?.showNetWorkErrViewEvent?.observe(
            this,
            Observer { show: Boolean? ->
                showNetWorkErrView(
                    show!!
                )
            }
        )
        mViewModel?.uc()?.startActivityEvent?.observe(
            this,
            Observer { params: Map<String, Any?>? ->
                val clz =
                    params!![ParameterField.CLASS] as Class<*>?
                val bundle = params[ParameterField.BUNDLE] as Bundle?
                startActivity(clz, bundle)
            }
        )
        mViewModel?.uc()?.finishActivityEvent?.observe(
            this,
            Observer { finish() }
        )
        mViewModel?.uc()?.onBackPressedEvent?.observe(
            this,
            Observer { onBackPressed() }
        )
    }

    open fun startActivity(clz: Class<*>?, bundle: Bundle?) {
        val intent = Intent(this, clz)
        bundle?.let {
            intent.putExtras(it)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding?.unbind()
    }

    override fun initView() {}
    override fun initData() {}
    override fun initListener() {}
}