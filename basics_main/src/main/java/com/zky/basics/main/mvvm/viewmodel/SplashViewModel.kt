package com.zky.basics.main.mvvm.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.tencent.mmkv.MMKV
import com.zky.basics.api.RetrofitManager
import com.zky.basics.api.room.AppDatabase
import com.zky.basics.api.room.bean.TestRoomDb
import com.zky.basics.api.splash.entity.SplashViewBean
import com.zky.basics.api.splash.entity.Userinfo
import com.zky.basics.common.event.SingleLiveEvent
import com.zky.basics.common.mvvm.viewmodel.BaseViewModel
import com.zky.basics.common.util.DisplayUtil.dip2px
import com.zky.basics.common.util.InfoVerify
import com.zky.basics.common.util.MD5
import com.zky.basics.common.util.NetUtil.checkNet
import com.zky.basics.common.util.SPUtils
import com.zky.basics.common.util.ToastUtil.showToast
import com.zky.basics.common.util.security.MMKVUtil
import com.zky.basics.common.util.security.SM3
import com.zky.basics.common.util.spread.decode
import com.zky.basics.common.util.spread.decodeParcelable
import com.zky.basics.common.util.spread.encode
import com.zky.basics.common.util.spread.showToast
import com.zky.basics.common.view.showFullPopupWindow
import com.zky.basics.main.R
import com.zky.basics.main.activity.FrogetActivity
import com.zky.basics.main.activity.RegistActivity
import com.zky.basics.main.mvvm.model.SplashModel
import io.reactivex.rxjava3.disposables.Disposable
import views.ViewOption.OptionsPickerBuilder
import java.lang.ref.WeakReference
import java.util.*

class SplashViewModel(application: Application, model: SplashModel) :
    BaseViewModel<SplashModel>(application, model) {
    private var mVoidSingleLiveEvent: SingleLiveEvent<String>? = null
    private var pickerBuilder: OptionsPickerBuilder? = null

    var name = ObservableField<String>()

    var paw = ObservableField<String>()

    var rgProvinceV = ObservableField<Boolean>()

    var rgCityV = ObservableField<Boolean>()

    var rgTwonV = ObservableField<Boolean>()

    var rgSchoolV = ObservableField<Boolean>()

    var data = ObservableField<SplashViewBean>()
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private var rgitstView: View? = null
    private var splashViewBean: SplashViewBean? = null
    private var provinceCode: String? = ""
    private var cityCode: String? = ""
    private var twonCode: String? = ""
    private var schoolCode: String? = ""
    private var provinceIndexl = 0
    private var token: String? = null
    private var subscribe: Disposable? = null
    private var pickerView: OptionsPickerView<Any?>? = null
    private var handler = WeakReference(CustomHandle()).get()

    //账号级别，可选值【0-中央、2-省（自治区）、3-市（自治州）、4-县（区）、5-学校】
    private val levelList: List<Any?> =
        object : ArrayList<Any?>() {
            init {
                this.add("中央")
                this.add("省（自治区)")
                this.add("市（自治州)")
                this.add("县（区)")
                this.add("学校")
            }
        }
    private var time = 60
    private fun initPicker(application: Context) {

        pickerBuilder = OptionsPickerBuilder(application)
            .setCancelText("取消")
            .setCancelColor(ContextCompat.getColor(application, R.color.color_b0b0b0))
            .setSubCalSize(16)
            .setSubmitColor(ContextCompat.getColor(application, R.color.color_4a90e2))
            .setSubmitText("确定")
            .setContentTextSize(20) //滚轮文字大小
            .setTextColorCenter(ContextCompat.getColor(application, R.color.color_333)) //设置选中文本的颜色值
            .setLineSpacingMultiplier(2f) //行间距
            .setDividerColor(ContextCompat.getColor(application, R.color.color_f5f5f5)) //设置分割线的颜色
        pickerView = pickerBuilder!!.build()
    }

    private fun login() {
        val sName = name.get()
        val sTmpPaw = paw.get()

        if (sName!!.isEmpty()) {
            showToast("用户名为空")
            return
        }
        if (InfoVerify.isEmpty(sTmpPaw)) {
            showToast("密码为空")
            return
        }
        val sPaw = SM3.encrypt(sTmpPaw!!)
        //离线登入
        if (!checkNet()) {
            val userinfo = decodeParcelable<Userinfo>("user")
            if (userinfo?.accountLevel == 5) {
                val phone = userinfo.phone
                val pwd = userinfo.password
                if (phone != name.get() || pwd != paw.get()) {
                    R.string.acountorpaw.showToast()
                    return
                }
                getmVoidSingleLiveEvent().value = "noNet"
                mVoidSingleLiveEvent!!.call()
            } else {
                R.string.net_error.showToast()
            }
            return
        }
        getmVoidSingleLiveEvent().value = "loadShow"
        getmVoidSingleLiveEvent().call()


        launchUI({

            val loginDTORespDTO = mModel.login(sName, sPaw)
            loginDTORespDTO?.let { it ->
                it.token?.let {
                    RetrofitManager.TOKEN = it
                }

                //存储 user 对象
                loginDTORespDTO.encode("user")
                getmVoidSingleLiveEvent().value = "login"
                getmVoidSingleLiveEvent().call()
            }

        }, object : NetError {
            override fun getError(e: Exception) {
                getmVoidSingleLiveEvent().value = "miss"
                getmVoidSingleLiveEvent().call()
            }

        })
    }

    fun startClick(view: View) {
        val i = view.id //账号注册
        //忘记密码
        if (i == R.id.register) {
            view.context.startActivity(Intent(view.context, RegistActivity::class.java))
        } else if (i == R.id.forgetPassword) {
            view.context.startActivity(Intent(view.context, FrogetActivity::class.java))
        } else if (i == R.id.forget) { //重置密码
            if (InfoVerify.isEmpty(data.get()!!.rgPhone)) {
                showToast("请填写正确的手机号")
                return
            }
            if (InfoVerify.isEmpty(data.get()!!.rgImageCode)) {
                showToast("请填写验证码")
                return
            }
            if (InfoVerify.isEmpty(data.get()!!.rgPaw)) {
                showToast("账号密码为空")
                return
            }
            if (!InfoVerify.isPwd(data.get()!!.rgPaw)) {
                showToast("请输入6-20位字母和数字组合，必须同时含有字母和数字")
                return
            }
            if (data.get()!!.rgPaw != data.get()!!.rgqrPaw) {
                showToast("密码和确认密码不一致")
                return
            }
            foggerPas()
        } else if (i == R.id.register_get_message || i == R.id.forget_get_message) {
            rgitstView = view
            if (!InfoVerify.isPhone(data.get()!!.rgPhone)) {
                showToast("请填写正确的手机号")
                return
            }
            if (data.get()!!.rgImageCode == null || data.get()!!.rgImageCode!!.isEmpty()) {
                showToast("请填写验证码")
                return
            }
            if (token!!.isEmpty()) {
                showToast("验证码失效")
                captcha()
                return
            }
            var type = "regist"
            if (view.id == R.id.forget_get_message) {
                type = "forget"
            }
            sendSms(type)
        } else if (i == R.id.register_mark) {
            val contentView = LayoutInflater.from(view.context)
                .inflate(R.layout.popup_tip, null)
            val params = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            params.topMargin = location[1]
            params.leftMargin = location[0] + view.width / 2 - dip2px(22f)
            contentView.layoutParams = params
            showFullPopupWindow(
                contentView,
                view,
                R.color.color_99ffffff
            )
        } else if (i == R.id.register_account_level) { //账号级别
            initPicker(view.context)
            pickerView!!.setPicker(levelList)
            pickerView!!.setSelectOptions(data.get()!!.levelIndel)
            pickerView!!.show()
            pickerBuilder!!.setOnOptionsSelectListener { options1: Int, _: Int, _: Int, _: View? ->
                if (options1 != data.get()!!.levelIndel) { //清空省市县的选择
                    data.get()!!.rgProvince = "省"
                    data.get()!!.rgTwon = "县"
                    data.get()!!.rgCity = "市"
                    data.get()!!.rgSchool = "学校"
                    provinceCode = ""
                    cityCode = ""
                    twonCode = ""
                    schoolCode = ""
                    provinceIndexl = 0
                    data.get()?.writeSchool = false
                    data.get()?.writeTwon = false
                    data.get()?.writeCity = false
                    data.get()?.writeProvince = false
                }
                data.get()?.levelIndel = options1
                data.get()?.rgLevel = levelList[options1].toString()
                data.get()?.writeLevel = true
                when (options1) {
                    0 -> {
                        rgProvinceV.set(false)
                        rgCityV.set(false)
                        rgTwonV.set(false)
                        rgSchoolV.set(false)
                    }
                    1 -> {
                        rgProvinceV.set(true)
                        rgCityV.set(false)
                        rgTwonV.set(false)
                        rgSchoolV.set(false)
                    }
                    2 -> {
                        rgProvinceV.set(true)
                        rgCityV.set(true)
                        rgTwonV.set(false)
                        rgSchoolV.set(false)
                    }
                    3 -> {
                        rgProvinceV.set(true)
                        rgCityV.set(true)
                        rgTwonV.set(true)
                        rgSchoolV.set(false)
                    }
                    else -> {
                        rgProvinceV.set(true)
                        rgCityV.set(true)
                        rgTwonV.set(true)
                        rgSchoolV.set(true)
                    }
                }
            }
        } else if (i == R.id.register_get_image || i == R.id.forget_get_image) {
            captcha()
        } else if (i == R.id.register_province) {
            initPicker(view.context)
            getRegionOrSchool("2", "", "0")
        } else if (i == R.id.register_city) {
            initPicker(view.context)
            getRegionOrSchool("3", provinceCode, "1")
        } else if (i == R.id.register_town) {
            initPicker(view.context)
            getRegionOrSchool("4", cityCode, "2")
        } else if (i == R.id.register_school) {
            initPicker(view.context)
            getRegionOrSchool("", twonCode, "3")
        } else if (i == R.id.start_register) {
            if (InfoVerify.isEmpty(data.get()!!.rgName)) {
                showToast("用户名为空")
                return
            }
            if (!InfoVerify.isChinese(data.get()!!.rgName)) {
                showToast("姓名只能是中文")
                return
            }
            if (!InfoVerify.isPhone(data.get()!!.rgPhone)) {
                showToast("请填写正确的手机号")
                return
            }
            if (InfoVerify.isEmpty(data.get()!!.rgCode)) {
                showToast("短信验证码为空")
                return
            }
            val rgLevel = data.get()!!.rgLevel
            if (InfoVerify.isEmpty(rgLevel) || "账号级别" == rgLevel) {
                showToast("账号级别为空")
                return
            }
            if (InfoVerify.isEmpty(data.get()!!.rgPaw)) {
                showToast("账号密码为空")
                return
            }
            if (!InfoVerify.isPwd(data.get()!!.rgPaw)) {
                showToast("请输入6-20位字母和数字组合，必须同时含有字母和数字")
                return
            }
            if (data.get()!!.rgPaw != data.get()!!.rgqrPaw) {
                showToast("密码和确认密码不一致")
                return
            }
            //账号级别，可选值【0-中央、2-省（自治区）、3-市（自治州）、4-县（区）、5-学校】
            val numLevel: String
            when (rgLevel) {
                "中央" -> {
                    numLevel = "0"
                    startRigst(
                        data.get()?.rgName,
                        data.get()?.rgPaw,
                        numLevel,
                        "",
                        "",
                        "",
                        "",
                        data.get()?.rgCode
                    )
                }
                "省（自治区)" -> {
                    numLevel = "2"
                    if (InfoVerify.isEmpty(data.get()!!.rgProvince) || "省" == data.get()!!.rgProvince) {
                        showToast("省（自治区)为空")
                        return
                    }
                    startRigst(
                        data.get()?.rgName,
                        data.get()?.rgPaw,
                        numLevel,
                        provinceCode,
                        "",
                        "",
                        "",
                        data.get()?.rgCode
                    )
                }
                "市（自治州)" -> {
                    numLevel = "3"
                    if (InfoVerify.isEmpty(data.get()!!.rgProvince) || "省" == data.get()!!.rgProvince) {
                        showToast("省（自治区)为空")
                        return
                    }
                    if (InfoVerify.isEmpty(data.get()!!.rgCity) || "市" == data.get()!!.rgCity) {
                        showToast("市（自治州)为空")
                        return
                    }
                    startRigst(
                        data.get()?.rgName,
                        data.get()?.rgPaw,
                        numLevel,
                        provinceCode,
                        cityCode,
                        "",
                        "",
                        data.get()?.rgCode
                    )
                }
                "县（区)" -> {
                    numLevel = "4"
                    if (InfoVerify.isEmpty(data.get()!!.rgProvince) || "省" == data.get()!!.rgProvince) {
                        showToast("省（自治区)为空")
                        return
                    }
                    if (InfoVerify.isEmpty(data.get()!!.rgCity) || "市" == data.get()!!.rgCity) {
                        showToast("市（自治州)为空")
                        return
                    }
                    if (InfoVerify.isEmpty(data.get()!!.rgTwon) || "县" == data.get()!!.rgTwon) {
                        showToast("县（区)为空")
                        return
                    }
                    startRigst(
                        data.get()!!.rgName,
                        data.get()!!.rgPaw,
                        numLevel,
                        provinceCode,
                        cityCode,
                        twonCode,
                        "",
                        data.get()!!.rgCode
                    )
                }
                else -> {
                    numLevel = "5"
                    if (InfoVerify.isEmpty(data.get()!!.rgProvince) || "省" == data.get()!!.rgProvince) {
                        showToast("省（自治区)为空")
                        return
                    }
                    if (InfoVerify.isEmpty(data.get()!!.rgCity) || "市" == data.get()!!.rgCity) {
                        showToast("市（自治州)为空")
                        return
                    }
                    if (InfoVerify.isEmpty(data.get()!!.rgTwon) || "县" == data.get()!!.rgTwon) {
                        showToast("县（区)为空")
                        return
                    }
                    if (InfoVerify.isEmpty(data.get()!!.rgSchool) || "学校" == data.get()!!.rgSchool) {
                        showToast("学校为空")
                        return
                    }
                    startRigst(
                        data.get()!!.name,
                        data.get()!!.rgPaw,
                        numLevel,
                        provinceCode,
                        cityCode,
                        twonCode,
                        schoolCode,
                        data.get()!!.rgCode
                    )
                }
            }
        } else if (i == R.id.login) {
            login()
        }
    }

    /**
     * 忘记密码
     */
    private fun foggerPas() {
        launchUI({
            val updateUserPassword = mModel.updateUserPassword(
                "forget",
                data.get()!!.rgPhone,
                "",
                MD5(data.get()!!.rgPaw!!),
                data.get()!!.rgCode
            )
            showToast(updateUserPassword.toString())
            getmVoidSingleLiveEvent().call()
        })
    }

    fun getmVoidSingleLiveEvent(): SingleLiveEvent<String> {
        return createLiveData(mVoidSingleLiveEvent).also {
            mVoidSingleLiveEvent = it
        }
    }

    private fun startTimer() {
        rgitstView!!.isEnabled = false
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                handler?.sendEmptyMessage(1)
            }
        }
        timer?.schedule(timerTask, 0, 1000)
    }

    fun captcha() {
        launchUI({
            //数据库使用 用例
//          var  testRoomDbDao =
//                AppDatabase.getDatabase(getApplication())?.testRoomDbDao()!!
//            val testRoomDb = TestRoomDb(2231, "name", 3, "1", "3")
//            val list= arrayListOf<TestRoomDb>()
//            list.add(testRoomDb)
//            testRoomDbDao.insertOrUpdate(list)
//            val users = testRoomDbDao.users()

            val captcha = mModel.captcha()
            captcha?.let {
                data.get()?.rgImageUrl =
                    it.getBitmap()
                token = it.token
            }

        })
    }


    /**
     * @param regLevel 区划级别
     * @param regCode  区划编号
     * @param type     省 0 市 1  县 2 学校 3
     */
    private fun getRegionOrSchool(
        regLevel: String,
        regCode: String?,
        type: String
    ) {
        if ("1" == type && regCode!!.isEmpty()) {
            "省还未选择!!!".showToast()
            return
        }
        if ("2" == type && (regLevel.isEmpty() || regCode!!.isEmpty())) {
            if (regLevel.isEmpty() || regCode!!.isEmpty()) {
                "省市未选择!!!".showToast()
                return
            }
        }
        if ("3" == type && regCode!!.isEmpty()) {
            "省市县有未选择!!!".showToast()
            return
        }

        launchUI({

            val result = mModel.getRegionOrSchool(regLevel, regCode)
            if (result == null || result.isEmpty()) {
                return@launchUI
            }
            val dalist: MutableList<Any?> =
                ArrayList()
            for ((_, name1, SCHOOL_NAME) in result) {
                if ("3" == type) {
                    dalist.add(SCHOOL_NAME)
                } else {
                    dalist.add(name1)
                }
            }
            if ("0" == type) {
                pickerView?.setSelectOptions(provinceIndexl)
            }
            pickerView?.setPicker(dalist)
            pickerView?.show()
            pickerBuilder?.setOnOptionsSelectListener { options1: Int, _: Int, _: Int, _: View? ->
                when (type) {
                    "0" -> {
                        data.get()?.let {
                            it.rgProvince = dalist[options1].toString()
                            it.rgTwon = "县"
                            it.rgCity = "市"
                            it.rgSchool = "学校"
                            it.writeProvince = true
                            it.writeCity = false
                            it.writeTwon = false
                            it.writeSchool = false
                        }

                        provinceIndexl = options1
                        provinceCode = result[options1].code
                        cityCode = ""
                        twonCode = ""
                        schoolCode = ""
                    }
                    "1" -> {
                        data.get()?.let {
                            it.rgCity = dalist[options1].toString()
                            it.rgTwon = "县"
                            it.rgSchool = "学校"
                            it.writeProvince = true
                            it.writeCity = true
                            it.writeTwon = false
                            it.writeSchool = false
                        }
                        cityCode = result[options1].code
                        twonCode = ""
                        schoolCode = ""
                    }
                    "2" -> {
                        data.get()?.let {
                            it.rgTwon = dalist[options1].toString()
                            it.rgSchool = "学校"
                            it.writeProvince = true
                            it.writeCity = true
                            it.writeTwon = true
                            it.writeSchool = false
                        }
                        twonCode = result[options1].code
                        schoolCode = ""
                    }
                    "3" -> {
                        data.get()?.let {
                            it.rgSchool = dalist[options1].toString()
                            it.writeProvince = true
                            it.writeCity = true
                            it.writeTwon = true
                            it.writeSchool = true
                        }
                        schoolCode = result[options1].SCHOOL_ID
                    }
                }
            }
        })
    }

    /**
     * 发送验证吗
     */
    private fun sendSms(type: String) {

        launchUI({
            mModel.sendSms(
                token,
                data.get()!!.rgImageCode,
                data.get()!!.rgPhone,
                type
            )
            startTimer()
            showToast("发送成功")
        }, object : NetError {
            override fun getError(e: Exception) {
                resume()
            }

        })
    }

    private fun startRigst(
        userName: String?,
        password: String?,
        accountLevel: String,
        province: String?,
        city: String?,
        county: String?,
        college: String?,
        smsCode: String?
    ) {
        val passwordMD = MD5(password!!)
        val phone = data.get()!!.rgPhone
        launchUI({
            mModel.regist(
                userName,
                passwordMD,
                accountLevel,
                province,
                city,
                county,
                college, smsCode, phone
            )
            R.string.register_success.showToast()
            data.get()?.rgPhone?.let { SPUtils.put(getApplication(), "phone", it) }
            getmVoidSingleLiveEvent().call()
        }, object : NetError {
            override fun getError(e: Exception) {
                R.string.register_fail.showToast()
                getmVoidSingleLiveEvent().call()
            }

        })

    }


    @SuppressLint("HandlerLeak")
    inner class CustomHandle : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg?.what == 1) {
                time -= 1
                data.get()?.timeMeesage = time.toString() + "秒"
                if (time == 1) {
                    resume()
                }
            }
        }

    }

    private fun resume() {
        if (timer != null) {
            timer?.cancel()
            timerTask?.cancel()
        }
        data.get()?.timeMeesage = "获取"
        data.set(splashViewBean)
        time = 60
        rgitstView?.isEnabled = true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (subscribe != null) {
            subscribe!!.dispose()
        }
        if (handler != null) {
            handler?.removeMessages(1)
            handler = null
        }
        if (timer != null) {
            timer?.cancel()
            timerTask?.cancel()
            timerTask = null
            timer = null
        }
    }

    companion object {
        var TAG = SplashViewModel::class.java.simpleName
    }

    init {
        val phone = SPUtils.get(getApplication(), "phone", "")
        val pwd =
            SPUtils.get(getApplication(), phone.toString(), "")
        name.set(phone.toString())
        paw.set(pwd.toString())
        //        getCaptcha();
        try {
            splashViewBean = SplashViewBean()
            splashViewBean?.let {
                it.timeMeesage = "获取"
                it.rgLevel = "账号级别"
                it.rgProvince = "省"
                it.rgTwon = "县"
                it.rgCity = "市"
                it.rgSchool = "学校"
                it.levelIndel = -1
                it.rgErrorImageUrl = R.drawable.image_code_error
            }
            data.set(splashViewBean)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}