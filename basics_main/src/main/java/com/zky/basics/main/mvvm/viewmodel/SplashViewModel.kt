package com.zky.basics.main.mvvm.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.ObservableField
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.zky.basics.api.RetrofitManager
import com.zky.basics.api.dto.RespDTO
import com.zky.basics.api.splash.entity.SplashViewBean
import com.zky.basics.common.event.SingleLiveEvent
import com.zky.basics.common.mvvm.viewmodel.BaseViewModel
import com.zky.basics.common.util.DisplayUtil.dip2px
import com.zky.basics.common.util.InfoVerify
import com.zky.basics.common.util.MD5
import com.zky.basics.common.util.NetUtil.checkNet
import com.zky.basics.common.util.SPUtils
import com.zky.basics.common.util.ToastUtil.showToast
import com.zky.basics.common.view.showFullPopupWindow
import com.zky.basics.main.R
import com.zky.basics.main.activity.FrogetActivity
import com.zky.basics.main.activity.RegistActivity
import com.zky.basics.main.mvvm.model.SplashModel
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import views.ViewOption.OptionsPickerBuilder
import java.util.*

class SplashViewModel(application: Application, model: SplashModel?) :
    BaseViewModel<SplashModel?>(application, model) {
    private var mVoidSingleLiveEvent: SingleLiveEvent<String>? = null
    private var pickerBuilder: OptionsPickerBuilder? = null
    @JvmField
    var name = ObservableField<String>()
    @JvmField
    var paw = ObservableField<String>()
    @JvmField
    var rgProvinceV = ObservableField<Boolean>()
    @JvmField
    var rgCityV = ObservableField<Boolean>()
    @JvmField
    var rgTwonV = ObservableField<Boolean>()
    @JvmField
    var rgSchoolV = ObservableField<Boolean>()
    @JvmField
    var data = ObservableField<SplashViewBean?>()
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
    private fun initPicker(application: Context) { //        if (pickerBuilder != null) {
//            return;
//        }
        pickerBuilder = OptionsPickerBuilder(application)
            .setCancelText("取消")
            .setCancelColor(application.resources.getColor(R.color.color_b0b0b0))
            .setSubCalSize(16)
            .setSubmitColor(application.resources.getColor(R.color.color_4a90e2))
            .setSubmitText("确定")
            .setContentTextSize(20) //滚轮文字大小
            .setTextColorCenter(application.resources.getColor(R.color.color_333)) //设置选中文本的颜色值
            .setLineSpacingMultiplier(2f) //行间距
            .setDividerColor(application.resources.getColor(R.color.color_f5f5f5)) //设置分割线的颜色
        pickerView = pickerBuilder!!.build()
    }

    fun login() {
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
        val sPaw = MD5(sTmpPaw!!)
        if (sPaw.isEmpty()) {
            showToast("密码为空")
            return
        }
        //离线登入
        if (!checkNet()) {
            val accountLevel = SPUtils.get(
                getApplication(),
                name.get().toString() + "accountLevel",
                -1
            ) as Int
            if (accountLevel == 5) {
                val phone =
                    SPUtils.get(getApplication(), "phone", "")
                val pwd =
                    SPUtils.get(getApplication(), phone.toString(), "")
                if (phone.toString() != name.get() || pwd.toString() != paw.get()) {
                    showToast("账号密码错误")
                    return
                }
                getmVoidSingleLiveEvent().value = "noNet"
                mVoidSingleLiveEvent!!.call()
            } else {
                showToast("网络未连接，请检查网络")
            }
            return
        }
        getmVoidSingleLiveEvent().value = "loadShow"
        getmVoidSingleLiveEvent().call()


        launchUI({

            val loginDTORespDTO = mModel!!.login(sName, sPaw)
            if (loginDTORespDTO.code == 200) {
                RetrofitManager.TOKEN = loginDTORespDTO.data.token
                SPUtils.put(
                    getApplication(),
                    "phone",
                    name.get()
                )
                SPUtils.put(
                    getApplication(),
                    name.get(),
                    paw.get()
                )
                SPUtils.put(
                    getApplication(),
                    "headImg",
                    if (loginDTORespDTO.data.headImg == null) "" else loginDTORespDTO.data.headImg
                )
                SPUtils.put(
                    getApplication(),
                    "userName",
                    if (loginDTORespDTO.data.userName == null) "" else loginDTORespDTO.data.userName
                )
                SPUtils.put(
                    getApplication(),
                    "code",
                    if (loginDTORespDTO.data.code == null) "" else loginDTORespDTO.data.code
                )
                SPUtils.put(
                    getApplication(),
                    name.get().toString() + "accountLevel",
                    loginDTORespDTO.data.accountLevel
                )
                SPUtils.put(
                    getApplication(),
                    "province",
                    if (loginDTORespDTO.data.province == null) "" else loginDTORespDTO.data.province
                )
                SPUtils.put(
                    getApplication(),
                    "city",
                    if (loginDTORespDTO.data.city == null) "" else loginDTORespDTO.data.city
                )
                SPUtils.put(
                    getApplication(),
                    "county",
                    if (loginDTORespDTO.data.county == null) "" else loginDTORespDTO.data.county
                )
                SPUtils.put(
                    getApplication(),
                    "provinceName",
                    if (loginDTORespDTO.data.provinceName == null) "" else loginDTORespDTO.data.provinceName
                )
                SPUtils.put(
                    getApplication(),
                    "cityName",
                    if (loginDTORespDTO.data.cityName == null) "" else loginDTORespDTO.data.cityName
                )
                SPUtils.put(
                    getApplication(),
                    "countyName",
                    if (loginDTORespDTO.data.countyName == null) "" else loginDTORespDTO.data.countyName
                )
                SPUtils.put(
                    getApplication(),
                    "schoolName",
                    if (loginDTORespDTO.data.schoolName == null) "" else loginDTORespDTO.data.schoolName
                )
                SPUtils.put(
                    getApplication(),
                    "college",
                    if (loginDTORespDTO.data.college == null) "" else loginDTORespDTO.data.college
                )
                getmVoidSingleLiveEvent().value = "login"
                getmVoidSingleLiveEvent().call()
            }

        }, object : NetError {
            override fun getError(e: Exception) {
                getmVoidSingleLiveEvent().value = "miss"
                getmVoidSingleLiveEvent().call()
            }

        })

//        mModel!!.login(sName, sPaw)
//            .subscribe(object : Observer<RespDTO<Userinfo>> {
//                override fun onSubscribe(d: Disposable) {}
//                //Bearer
//                override fun onNext(loginDTORespDTO: RespDTO<Userinfo>) {
//                    if (loginDTORespDTO.code == ExceptionHandler.APP_ERROR.SUCC) {
//                        if (loginDTORespDTO.code == 200) {
//                            RetrofitManager.TOKEN = loginDTORespDTO.data.token
//                            SPUtils.put(
//                                getApplication(),
//                                "phone",
//                                name.get()
//                            )
//                            SPUtils.put(
//                                getApplication(),
//                                name.get(),
//                                paw.get()
//                            )
//                            SPUtils.put(
//                                getApplication(),
//                                "headImg",
//                                if (loginDTORespDTO.data.headImg == null) "" else loginDTORespDTO.data.headImg
//                            )
//                            SPUtils.put(
//                                getApplication(),
//                                "userName",
//                                if (loginDTORespDTO.data.userName == null) "" else loginDTORespDTO.data.userName
//                            )
//                            SPUtils.put(
//                                getApplication(),
//                                "code",
//                                if (loginDTORespDTO.data.code == null) "" else loginDTORespDTO.data.code
//                            )
//                            SPUtils.put(
//                                getApplication(),
//                                name.get().toString() + "accountLevel",
//                                loginDTORespDTO.data.accountLevel
//                            )
//                            SPUtils.put(
//                                getApplication(),
//                                "province",
//                                if (loginDTORespDTO.data.province == null) "" else loginDTORespDTO.data.province
//                            )
//                            SPUtils.put(
//                                getApplication(),
//                                "city",
//                                if (loginDTORespDTO.data.city == null) "" else loginDTORespDTO.data.city
//                            )
//                            SPUtils.put(
//                                getApplication(),
//                                "county",
//                                if (loginDTORespDTO.data.county == null) "" else loginDTORespDTO.data.county
//                            )
//                            SPUtils.put(
//                                getApplication(),
//                                "provinceName",
//                                if (loginDTORespDTO.data.provinceName == null) "" else loginDTORespDTO.data.provinceName
//                            )
//                            SPUtils.put(
//                                getApplication(),
//                                "cityName",
//                                if (loginDTORespDTO.data.cityName == null) "" else loginDTORespDTO.data.cityName
//                            )
//                            SPUtils.put(
//                                getApplication(),
//                                "countyName",
//                                if (loginDTORespDTO.data.countyName == null) "" else loginDTORespDTO.data.countyName
//                            )
//                            SPUtils.put(
//                                getApplication(),
//                                "schoolName",
//                                if (loginDTORespDTO.data.schoolName == null) "" else loginDTORespDTO.data.schoolName
//                            )
//                            SPUtils.put(
//                                getApplication(),
//                                "college",
//                                if (loginDTORespDTO.data.college == null) "" else loginDTORespDTO.data.college
//                            )
//                            getmVoidSingleLiveEvent().value = "login"
//                            getmVoidSingleLiveEvent().call()
//                        }
//                        //                    else {
////                        ToastUtil.showToast(loginDTORespDTO.msg);
////                    }
//                    } else {
//                        Log.v(
//                            TAG,
//                            "error:" + loginDTORespDTO.msg
//                        )
//                    }
//                }
//
//                override fun onError(e: Throwable) {
//                    Log.e(TAG, "error:" + e.message)
//                }
//
//                override fun onComplete() {
//                    getmVoidSingleLiveEvent().value = "miss"
//                    getmVoidSingleLiveEvent().call()
//                }
//            })
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
            pickerBuilder!!.setOnOptionsSelectListener(
                OnOptionsSelectListener { options1: Int, options2: Int, options3: Int, v: View? ->
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
                        data.get()!!.writeSchool = false
                        data.get()!!.writeTwon = false
                        data.get()!!.writeCity = false
                        data.get()!!.writeProvince = false
                    }
                    data.get()!!.levelIndel = options1
                    data.get()!!.rgLevel = levelList[options1].toString()
                    data.get()!!.writeLevel = true
                    if (options1 == 0) {
                        rgProvinceV.set(false)
                        rgCityV.set(false)
                        rgTwonV.set(false)
                        rgSchoolV.set(false)
                    } else if (options1 == 1) {
                        rgProvinceV.set(true)
                        rgCityV.set(false)
                        rgTwonV.set(false)
                        rgSchoolV.set(false)
                    } else if (options1 == 2) {
                        rgProvinceV.set(true)
                        rgCityV.set(true)
                        rgTwonV.set(false)
                        rgSchoolV.set(false)
                    } else if (options1 == 3) {
                        rgProvinceV.set(true)
                        rgCityV.set(true)
                        rgTwonV.set(true)
                        rgSchoolV.set(false)
                    } else {
                        rgProvinceV.set(true)
                        rgCityV.set(true)
                        rgTwonV.set(true)
                        rgSchoolV.set(true)
                    }
                }
            )
        } else if (i == R.id.register_get_image) {
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
            //            splashViewBean.setRgLevel();
//            splashViewBean.setRgProvince("省");
//            splashViewBean.setRgTwon("县");
//            splashViewBean.setRgCity("市");
//            splashViewBean.setRgSchool("学校");
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
                        data.get()!!.rgName,
                        data.get()!!.rgPaw,
                        numLevel,
                        "",
                        "",
                        "",
                        "",
                        data.get()!!.rgCode
                    )
                }
                "省（自治区)" -> {
                    numLevel = "2"
                    if (InfoVerify.isEmpty(data.get()!!.rgProvince) || "省" == data.get()!!.rgProvince) {
                        showToast("省（自治区)为空")
                        return
                    }
                    startRigst(
                        data.get()!!.rgName,
                        data.get()!!.rgPaw,
                        numLevel,
                        provinceCode,
                        "",
                        "",
                        "",
                        data.get()!!.rgCode
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
                        data.get()!!.rgName,
                        data.get()!!.rgPaw,
                        numLevel,
                        provinceCode,
                        cityCode,
                        "",
                        "",
                        data.get()!!.rgCode
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
        mModel!!.updateUserPassword(
            "forget",
            data.get()!!.rgPhone,
            "",
            MD5(data.get()!!.rgPaw!!),
            data.get()!!.rgCode
        )!!.subscribe(object : Observer<RespDTO<*>> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(respDTO: RespDTO<*>) {
                if (respDTO.code == 200) {
                    showToast(respDTO.msg)
                    getmVoidSingleLiveEvent().call()
                }
            }

            override fun onError(e: Throwable) {}
            override fun onComplete() {}
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
                handler!!.sendEmptyMessage(1)
            }
        }
        timer!!.schedule(timerTask, 0, 1000)
    }
//


    fun captcha() {
        launchUI({
            val captcha = mModel!!.captcha()
            data.get()!!.rgImageUrl =
                captcha.data?.getBitmap()
            token = captcha.data?.token
        }, null)

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
            showToast("省还未选择!!!")
            return
        }
        if ("2" == type && (regLevel.isEmpty() || regCode!!.isEmpty())) {
            if (regLevel.isEmpty() || regCode!!.isEmpty()) {
                showToast("省市未选择!!!")
                return
            }
        }
        if ("3" == type && regCode!!.isEmpty()) {
            showToast("省市县有未选择!!!")
            return
        }

        launchUI({
            val re = mModel!!.getRegionOrSchool(regLevel, regCode)
            val result = re.data
            if (result == null || result.size == 0) {
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
                pickerView!!.setSelectOptions(provinceIndexl)
            }
            pickerView!!.setPicker(dalist)
            pickerView!!.show()
            pickerBuilder!!.setOnOptionsSelectListener(
                OnOptionsSelectListener { options1: Int, options2: Int, options3: Int, v: View? ->
                    when (type) {
                        "0" -> {
                            data.get()!!.rgProvince = dalist[options1].toString()
                            data.get()!!.rgTwon = "县"
                            data.get()!!.rgCity = "市"
                            data.get()!!.rgSchool = "学校"
                            provinceIndexl = options1
                            provinceCode = result[options1].code
                            data.get()!!.writeProvince = true
                            data.get()!!.writeCity = false
                            data.get()!!.writeTwon = false
                            data.get()!!.writeSchool = false
                            cityCode = ""
                            twonCode = ""
                            schoolCode = ""
                        }
                        "1" -> {
                            data.get()!!.rgCity = dalist[options1].toString()
                            data.get()!!.rgTwon = "县"
                            data.get()!!.rgSchool = "学校"
                            cityCode = result[options1].code
                            twonCode = ""
                            schoolCode = ""
                            data.get()!!.writeProvince = true
                            data.get()!!.writeCity = true
                            data.get()!!.writeTwon = false
                            data.get()!!.writeSchool = false
                        }
                        "2" -> {
                            data.get()!!.rgTwon = dalist[options1].toString()
                            data.get()!!.rgSchool = "学校"
                            twonCode = result[options1].code
                            schoolCode = ""
                            data.get()!!.writeProvince = true
                            data.get()!!.writeCity = true
                            data.get()!!.writeTwon = true
                            data.get()!!.writeSchool = false
                        }
                        "3" -> {
                            data.get()!!.rgSchool = dalist[options1].toString()
                            schoolCode = result[options1].SCHOOL_ID
                            data.get()!!.writeProvince = true
                            data.get()!!.writeCity = true
                            data.get()!!.writeTwon = true
                            data.get()!!.writeSchool = true
                        }
                    }
                }
            )
        }, null)
    }

    /**
     * 发送验证吗
     */
    private fun sendSms(type: String) {

        launchUI({
            val sendSms = mModel!!.sendSms(
                token,
                data.get()!!.rgImageCode,
                data.get()!!.rgPhone,
                type
            )

            if (sendSms.code == 200) {
                startTimer()
                showToast("发送成功")
            } else {
                resume()
            }
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
        var password = password
        password = MD5(password!!)
        val phone = data.get()!!.rgPhone
        launchUI({
            val respDTO = mModel!!.regist(
                userName,
                password,
                accountLevel,
                province,
                city,
                county,
                college, smsCode, phone
            )
            if (respDTO.code == 200) {
                showToast("注册成功")
                SPUtils.put(
                    getApplication(),
                    "phone",
                    data.get()!!.rgPhone
                )
                getmVoidSingleLiveEvent().call()
            }
        })

    }

    private var handler: Handler? = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1) {
                time -= 1
                data.get()!!.timeMeesage = time.toString() + "秒"
                if (time == 1) {
                    resume()
                }
            }
        }
    }

    private fun resume() {
        if (timer != null) {
            timer!!.cancel()
            timerTask!!.cancel()
        }
        data.get()!!.timeMeesage = "获取"
        data.set(splashViewBean)
        time = 60
        rgitstView!!.isEnabled = true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (subscribe != null) {
            subscribe!!.dispose()
        }
        if (handler != null) {
            handler!!.removeMessages(1)
            handler = null
        }
        if (timer != null) {
            timer!!.cancel()
            timerTask!!.cancel()
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