package com.zky.basics.common.mvvm

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import com.alibaba.android.arouter.launcher.ARouter
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import com.zky.basics.common.R
import com.zky.basics.common.event.common.BaseActivityEvent
import com.zky.basics.common.manager.ActivityManager.Companion.instance
import com.zky.basics.common.mvvm.view.IBaseView
import com.zky.basics.common.util.NetUtil.checkNetToast
import com.zky.basics.common.view.LoadingInitView
import com.zky.basics.common.view.LoadingTransView
import com.zky.basics.common.view.NetErrorView
import com.zky.basics.common.view.NoDataView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseActivity : RxAppCompatActivity(), IBaseView {
    private var mTxtTitle: TextView? = null
    private var mToolbar: Toolbar? = null
    private var mNetErrorView: NetErrorView? = null
    private var mNoDataView: NoDataView? = null
    private var mLoadingInitView: LoadingInitView? = null
    private var mLoadingTransView: LoadingTransView? = null
    private var mViewStubContent: RelativeLayout? = null
    private var mViewStubToolbar: ViewStub? = null
    private var mViewStubInitLoading: ViewStub? = null
    private var mViewStubTransLoading: ViewStub? = null
    private var mViewStubNoData: ViewStub? = null
    private var mViewStubError: ViewStub? = null
    private var mContentView: ViewGroup? = null
    private lateinit  var context:Context
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        if (isFullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        super.setContentView(R.layout.activity_root1)
        mContentView = findViewById<View>(android.R.id.content) as ViewGroup
        context=this
        initCommonView()
        initContentView()
        ARouter.getInstance().inject(this)
        initView()
        initListener()
        initData()
        EventBus.getDefault().register(this)
        instance?.addActivity(this)
    }

    private fun initCommonView() {
        mViewStubToolbar = findViewById(R.id.view_stub_toolbar)
        mViewStubContent =
            findViewById(R.id.view_stub_content)
        mViewStubInitLoading =
            findViewById(R.id.view_stub_init_loading)
        mViewStubTransLoading =
            findViewById(R.id.view_stub_trans_loading)
        mViewStubError = findViewById(R.id.view_stub_error)
        mViewStubNoData = findViewById(R.id.view_stub_nodata)
        if (enableToolbar()) {
            mViewStubToolbar?.layoutResource = onBindToolbarLayout()
            val view = mViewStubToolbar?.inflate()
            initToolbar(view)
        }
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        if (mViewStubContent != null) {
            initContentView(layoutResID)
        }
    }

    open fun initContentView() {
        initContentView(onBindLayout())
    }

    private fun initContentView(@LayoutRes layoutResID: Int) {
        val view =
            LayoutInflater.from(this).inflate(layoutResID, mViewStubContent, false)
        mViewStubContent?.let {
            it.id = android.R.id.content
            it.removeAllViews()
            it.addView(view)
        }
        mContentView?.id = View.NO_ID
    }

    private fun initToolbar(view: View?) {
        mToolbar = view?.findViewById(R.id.toolbar_root)
        mTxtTitle = view?.findViewById(R.id.toolbar_title)
        if (mToolbar != null) {
            setSupportActionBar(mToolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            mToolbar?.setNavigationOnClickListener { onBackPressed() }
        }
    }

    override fun onTitleChanged(title: CharSequence, color: Int) {
        super.onTitleChanged(title, color)
        if (mTxtTitle != null && !TextUtils.isEmpty(title)) {
            mTxtTitle?.text = title
        }
        //可以再次覆盖设置title
        val tootBarTitle = tootBarTitle
        if (mTxtTitle != null && !TextUtils.isEmpty(tootBarTitle)) {
            mTxtTitle?.text = tootBarTitle
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        instance?.finishActivity(this)
    }

    open fun onBindToolbarLayout(): Int {
        return R.layout.blue_common_toolbar
    }

    abstract fun onBindLayout(): Int
    abstract override fun initView()
    abstract override fun initData()
    override fun initListener() {}
    override fun finishActivity() {
        finish()
    }

    open val tootBarTitle: String
        get() = ""

    open fun enableToolbar(): Boolean {
        return true
    }

    override fun showInitLoadView(show: Boolean) {
        if (mLoadingInitView == null) {
            val view = mViewStubInitLoading!!.inflate()
            mLoadingInitView = view.findViewById(R.id.view_init_loading)
        }
        mLoadingInitView?.visibility = if (show) View.VISIBLE else View.GONE
        mLoadingInitView?.loading(show)
    }

    override fun showNetWorkErrView(show: Boolean) {
        if (mNetErrorView == null) {
            val view = mViewStubError!!.inflate()
            mNetErrorView = view.findViewById(R.id.view_net_error)
            mNetErrorView?.setOnClickListener(View.OnClickListener {
                if (!checkNetToast()) {
                    return@OnClickListener
                }
                showNetWorkErrView(false)
                initData()
            })
        }
        mNetErrorView?.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showNoDataView(show: Boolean) {
        if (mNoDataView == null) {
            val view = mViewStubNoData?.inflate()
            mNoDataView = view?.findViewById(R.id.view_no_data)
        }
        mNoDataView?.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showTransLoadingView(show: Boolean) {
        if (mLoadingTransView == null) {
            val view = mViewStubTransLoading!!.inflate()
            mLoadingTransView = view.findViewById(R.id.view_trans_loading)
        }
        mLoadingTransView!!.visibility = if (show) View.VISIBLE else View.GONE
        mLoadingTransView!!.loading(show)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun <T> onEvent(@Suppress("UNUSED_PARAMETER") event: BaseActivityEvent<T>?) {
    }



    open val isFullScreen: Boolean
        get() = false

    companion object {
        protected val TAG = BaseActivity::class.java.simpleName
    }
}