package com.zky.basics.common.mvvm.view


interface IBaseView {
    fun initView()
    fun initListener()
    fun initData()
    fun finishActivity()
    fun showInitLoadView(show: Boolean)
    fun showNoDataView(show: Boolean)
    fun showTransLoadingView(show: Boolean)
    fun showNetWorkErrView(show: Boolean)

}