package com.zky.basics.common.mvvm


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zky.basics.common.mvvm.viewmodel.BaseViewModel
import com.zky.basics.common.mvvm.viewmodel.BaseViewModel.ParameterField
import com.zky.basics.common.util.log.KLog

abstract class BaseMvvmFragment<V : ViewDataBinding?, VM : BaseViewModel<*>?> :
    BaseFragment() {
    protected var mBinding: V? = null
    protected var mViewModel: VM? = null
    private var viewModelId = 0
    override fun initConentView(root: ViewGroup?) {
        mBinding =
            DataBindingUtil.inflate<V>(LayoutInflater.from(mActivity), onBindLayout(), root, true)
        initViewModel()
        initBaseViewObservable()
        initViewObservable()
    }

    private fun initViewModel() {
        mViewModel = createViewModel()
        viewModelId = onBindVariableId()
        if (mBinding != null && mViewModel != null) {
            mBinding?.setVariable(viewModelId, mViewModel)
            lifecycle.addObserver(mViewModel!!)
        }
    }

    private fun createViewModel(): VM? {

//        ViewModelProviders.of(this, onBindViewModelFactory())[onBindViewModel()!!]
        val onBindViewModelFactory = onBindViewModelFactory()
        if (onBindViewModelFactory != null) {
            return ViewModelProvider(this, onBindViewModelFactory)[onBindViewModel()!!]
        }
        return null
    }

    abstract fun onBindViewModel(): Class<VM>?
    abstract fun onBindViewModelFactory(): ViewModelProvider.Factory?
    abstract fun initViewObservable()
    abstract fun onBindVariableId(): Int
    protected open fun initBaseViewObservable() {
        mViewModel?.uc()?.showInitLoadViewEvent
            ?.observe(this, Observer { show -> showInitLoadView(show!!) })
        mViewModel?.uc()?.showTransLoadingViewEvent
            ?.observe(this, Observer { show ->
                KLog.v("MYTAG", "view postShowTransLoadingViewEvent start...")
                showTransLoadingView(show!!)
            })
        mViewModel?.uc()?.showNoDataViewEvent
            ?.observe(this, Observer { show -> showNoDataView(show!!) })
        mViewModel?.uc()?.showNetWorkErrViewEvent
            ?.observe(this, Observer { show -> showNetWorkErrView(show!!) })
        mViewModel?.uc()?.startActivityEvent?.observe(
            this,
            Observer { params ->
                val clz =
                    params?.get(ParameterField.CLASS) as Class<*>?
                val bundle = params?.get(ParameterField.BUNDLE) as Bundle?
                startActivity(clz, bundle)
            })
        mViewModel?.uc()?.finishActivityEvent
            ?.observe(this, Observer {
                mActivity?.finish()
            })
        mViewModel?.uc()?.onBackPressedEvent
            ?.observe(this, Observer {
                mActivity?.onBackPressed()
            })
    }

    open fun startActivity(clz: Class<*>?, bundle: Bundle?) {
        val intent = Intent(mActivity, clz)
        bundle?.let {
            intent.putExtras(it)
        }
        startActivity(intent)
    }
}