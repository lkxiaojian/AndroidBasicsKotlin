package com.zky.basics.common.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import com.zky.basics.common.R
import com.zky.basics.common.event.common.BaseFragmentEvent
import com.zky.basics.common.mvvm.view.IBaseView
import com.zky.basics.common.util.NetUtil.checkNetToast
import com.zky.basics.common.util.log.KLog
import com.zky.basics.common.view.LoadingInitView
import com.zky.basics.common.view.LoadingTransView
import com.zky.basics.common.view.NetErrorView
import com.zky.basics.common.view.NoDataView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


@Suppress("DEPRECATION")
abstract class BaseFragment : Fragment(), IBaseView {
    protected var mActivity: RxAppCompatActivity? = null
    private var mView: View? = null
    private var mTxtTitle: TextView? = null
    private var mToolbar: Toolbar? = null
    private var mNetErrorView: NetErrorView? = null
    private var mNoDataView: NoDataView? = null
    private var mLoadingInitView: LoadingInitView? = null
    private var mLoadingTransView: LoadingTransView? = null
    private var mViewStubToolbar: ViewStub? = null
    private var mViewStubContent: RelativeLayout? = null
    private var mViewStubInitLoading: ViewStub? = null
    private var mViewStubTransLoading: ViewStub? = null
    private var mViewStubNoData: ViewStub? = null
    private var mViewStubError: ViewStub? = null
    private var isViewCreated = false
    private var isViewVisable = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as RxAppCompatActivity?
        ARouter.getInstance().inject(this)
        EventBus.getDefault().register(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_root1, container, false)
        initCommonView(mView)
        initView(mView)
        initListener()
        return mView
    }

    open fun initCommonView(view: View?) {
        mViewStubToolbar = view?.findViewById(R.id.view_stub_toolbar)
        mViewStubContent =
            view?.findViewById(R.id.view_stub_content)
        mViewStubInitLoading =
            view?.findViewById(R.id.view_stub_init_loading)
        mViewStubTransLoading =
            view?.findViewById(R.id.view_stub_trans_loading)
        mViewStubNoData = view?.findViewById(R.id.view_stub_nodata)
        mViewStubError = view?.findViewById(R.id.view_stub_error)
        if (enableToolbar()) {
            mViewStubToolbar?.layoutResource = onBindToolbarLayout()
            val viewTooBbar = mViewStubToolbar?.inflate()
            initTooBar(viewTooBbar)
        }
        initConentView(mViewStubContent)
    }

    open fun initConentView(root: ViewGroup?) {
        LayoutInflater.from(mActivity).inflate(onBindLayout(), root, true)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        //如果启用了懒加载就进行懒加载，否则就进行预加载
        if (enableLazyData()) {
            lazyLoad()
        } else {
            initData()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isViewVisable = isVisibleToUser
        //如果启用了懒加载就进行懒加载，
        if (enableLazyData() && isViewVisable) {
            lazyLoad()
        }
    }

    private fun lazyLoad() { //这里进行双重标记判断,必须确保onCreateView加载完毕且页面可见,才加载数据
        KLog.v("MYTAG", "lazyLoad start...")
        KLog.v("MYTAG", "isViewCreated:$isViewCreated")
        KLog.v("MYTAG", "isViewVisable$isViewVisable")
        if (isViewCreated && isViewVisable) {
            initData()
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false
            isViewVisable = false
        }
    }

    //默认不启用懒加载
    fun enableLazyData(): Boolean {
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun initTooBar(view: View?) {
        mToolbar = view?.findViewById(R.id.toolbar_root)
        mTxtTitle = view?.findViewById(R.id.toolbar_title)
        mToolbar.let {
            mActivity?.setSupportActionBar(mToolbar)
            mActivity?.supportActionBar?.setDisplayShowTitleEnabled(false)
            mToolbar?.setNavigationOnClickListener { mActivity?.onBackPressed() }
        }
        mTxtTitle?.text = getToolbarTitle()

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun <T> onEvent(@Suppress("UNUSED_PARAMETER")event: BaseFragmentEvent<T>?) {

    }

    abstract fun onBindLayout(): Int
    abstract fun initView(view: View?)
    override fun initView() {}
    abstract override fun initData()


    open fun getToolbarTitle(): String {
        return ""
    }

    override fun initListener() {}
    override fun finishActivity() {
        mActivity?.finish()
    }

    open fun enableToolbar(): Boolean {
        return false
    }

    fun onBindToolbarLayout()=R.layout.common_toolbar

    override fun showInitLoadView(show: Boolean) {
        if (mLoadingInitView == null) {
            val view = mViewStubInitLoading?.inflate()
            mLoadingInitView = view?.findViewById(R.id.view_init_loading)
        }
        mLoadingInitView?.visibility = if (show) View.VISIBLE else View.GONE
        mLoadingInitView?.loading(show)
    }

    override fun showNetWorkErrView(show: Boolean) {
        if (mNetErrorView == null) {
            val view = mViewStubError?.inflate()
            mNetErrorView = view?.findViewById(R.id.view_net_error)
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

    fun showNoDataView(show: Boolean, resid: Int) {
        showNoDataView(show)
        if (show) {
            mNoDataView?.setNoDataView(resid)
        }
    }

    override fun showTransLoadingView(show: Boolean) {
        if (mLoadingTransView == null) {
            val view = mViewStubTransLoading?.inflate()
            mLoadingTransView = view?.findViewById(R.id.view_trans_loading)
        }
        mLoadingTransView?.visibility = if (show) View.VISIBLE else View.GONE
        mLoadingTransView?.loading(show)
    }

    companion object {
        protected val TAG = BaseFragment::class.java.simpleName
    }
}