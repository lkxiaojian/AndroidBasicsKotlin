package com.zky.basics.main.mvvm.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bigkoo.pickerview.view.OptionsPickerView;
import com.zky.basics.api.RetrofitManager;
import com.zky.basics.api.dto.RespDTO;
import com.zky.basics.api.http.ExceptionHandler;
import com.zky.basics.api.splash.entity.ImageUrl;
import com.zky.basics.api.splash.entity.RegionOrSchoolBean;
import com.zky.basics.api.splash.entity.Userinfo;
import com.zky.basics.common.event.SingleLiveEvent;
import com.zky.basics.common.mvvm.viewmodel.BaseViewModel;
import com.zky.basics.common.util.DisplayUtil;
import com.zky.basics.common.util.InfoVerify;
import com.zky.basics.common.util.MD5UtlisKt;
import com.zky.basics.common.util.NetUtil;
import com.zky.basics.common.util.SPUtils;
import com.zky.basics.common.util.ToastUtil;
import com.zky.basics.common.util.reflec.ReflecInstaceJava;
import com.zky.basics.common.view.PopupWindowUtilKt;
import com.zky.basics.main.R;
import com.zky.basics.main.activity.FrogetActivity;
import com.zky.basics.main.activity.RegistActivity;
import com.zky.basics.api.splash.entity.SplashViewBean;
import com.zky.basics.main.mvvm.model.SplashModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import views.ViewOption.OptionsPickerBuilder;


public class SplashViewModel extends BaseViewModel<SplashModel> {
    public static String TAG = SplashViewModel.class.getSimpleName();
    private SingleLiveEvent<String> mVoidSingleLiveEvent;
    private OptionsPickerBuilder pickerBuilder;
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> paw = new ObservableField<>();

    public ObservableField<Boolean> rgProvinceV = new ObservableField<>();
    public ObservableField<Boolean> rgCityV = new ObservableField<>();
    public ObservableField<Boolean> rgTwonV = new ObservableField<>();
    public ObservableField<Boolean> rgSchoolV = new ObservableField<>();
    public ObservableField<SplashViewBean> data = new ObservableField<>();


    private Timer timer;
    private TimerTask timerTask;
    private View rgitstView;
    private SplashViewBean splashViewBean;

    private String provinceCode = "";
    private String cityCode = "";
    private String twonCode = "";
    private String schoolCode = "";
    private int provinceIndexl = 0;
    private String token;
    private Disposable subscribe;

    private OptionsPickerView<Object> pickerView;
    //账号级别，可选值【0-中央、2-省（自治区）、3-市（自治州）、4-县（区）、5-学校】
    private List<Object> levelList = new ArrayList<Object>() {{
        this.add("中央");
        this.add("省（自治区)");
        this.add("市（自治州)");
        this.add("县（区)");
        this.add("学校");
    }};

    private int time = 60;

    public SplashViewModel(@NonNull Application application, SplashModel model) {
        super(application, model);
        Object phone = SPUtils.get(getApplication(), "phone", "");
        Object pwd = SPUtils.get(getApplication(), phone.toString(), "");
        name.set(phone.toString());
        paw.set(pwd.toString());

//        getCaptcha();
        try {
            splashViewBean = ReflecInstaceJava.instaceOf(splashViewBean.getClass());
            splashViewBean.setTimeMeesage("获取");
            splashViewBean.setRgLevel("账号级别");
            splashViewBean.setRgProvince("省");
            splashViewBean.setRgTwon("县");
            splashViewBean.setRgCity("市");
            splashViewBean.setRgSchool("学校");
            splashViewBean.setLevelIndel(-1);
            splashViewBean.setRgErrorImageUrl(R.drawable.image_code_error);
            data.set(splashViewBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPicker(Context application) {
//        if (pickerBuilder != null) {
//            return;
//        }
        pickerBuilder = new OptionsPickerBuilder(application)
                .setCancelText("取消")
                .setCancelColor(application.getResources().getColor(R.color.color_b0b0b0))
                .setSubCalSize(16)
                .setSubmitColor(application.getResources().getColor(R.color.color_4a90e2))
                .setSubmitText("确定")
                .setContentTextSize(20)//滚轮文字大小
                .setTextColorCenter(application.getResources().getColor(R.color.color_333))//设置选中文本的颜色值
                .setLineSpacingMultiplier(2f)//行间距
                .setDividerColor(application.getResources().getColor(R.color.color_f5f5f5));//设置分割线的颜色
        pickerView = pickerBuilder.build();
    }

    public void login() {
        String sName = name.get();
        String sTmpPaw = paw.get();

        if (sName.isEmpty()) {
            ToastUtil.showToast("用户名为空");
            return;
        }
        if (InfoVerify.isEmpty(sTmpPaw)) {
            ToastUtil.showToast("密码为空");
            return;
        }
        String sPaw = MD5UtlisKt.MD5(sTmpPaw);
        if (sPaw.isEmpty()) {
            ToastUtil.showToast("密码为空");
            return;
        }
        //离线登入
        if (!NetUtil.checkNet()) {

            int accountLevel = (int) SPUtils.get(getApplication(), name.get() + "accountLevel", -1);
            if (accountLevel == 5) {
                Object phone = SPUtils.get(getApplication(), "phone", "");
                Object pwd = SPUtils.get(getApplication(), phone.toString(), "");
                if (!phone.toString().equals(name.get()) || !pwd.toString().equals(paw.get())) {
                    ToastUtil.showToast("账号密码错误");
                    return;
                }
                getmVoidSingleLiveEvent().setValue("noNet");
                mVoidSingleLiveEvent.call();

            } else {
                ToastUtil.showToast("网络未连接，请检查网络");
            }
            return;
        }

        getmVoidSingleLiveEvent().setValue("loadShow");
        getmVoidSingleLiveEvent().call();
        mModel.login(sName, sPaw).subscribe(new Observer<RespDTO<Userinfo>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override//Bearer
            public void onNext(RespDTO<Userinfo> loginDTORespDTO) {
                if (loginDTORespDTO.code == ExceptionHandler.APP_ERROR.SUCC) {
                    if (loginDTORespDTO.code == 200) {

                        RetrofitManager.getInstance().TOKEN = loginDTORespDTO.data.getToken();
                        SPUtils.put(getApplication(), "phone", name.get());
                        SPUtils.put(getApplication(), name.get(), paw.get());
                        SPUtils.put(getApplication(), "headImg", loginDTORespDTO.data.getHeadImg() == null ? "" : loginDTORespDTO.data.getHeadImg());
                        SPUtils.put(getApplication(), "userName", loginDTORespDTO.data.getUserName() == null ? "" : loginDTORespDTO.data.getUserName());
                        SPUtils.put(getApplication(), "code", loginDTORespDTO.data.getCode() == null ? "" : loginDTORespDTO.data.getCode());
                        SPUtils.put(getApplication(), name.get() + "accountLevel", loginDTORespDTO.data.getAccountLevel());
                        SPUtils.put(getApplication(), "province", loginDTORespDTO.data.getProvince() == null ? "" : loginDTORespDTO.data.getProvince());
                        SPUtils.put(getApplication(), "city", loginDTORespDTO.data.getCity() == null ? "" : loginDTORespDTO.data.getCity());
                        SPUtils.put(getApplication(), "county", loginDTORespDTO.data.getCounty() == null ? "" : loginDTORespDTO.data.getCounty());
                        SPUtils.put(getApplication(), "provinceName", loginDTORespDTO.data.getProvinceName() == null ? "" : loginDTORespDTO.data.getProvinceName());
                        SPUtils.put(getApplication(), "cityName", loginDTORespDTO.data.getCityName() == null ? "" : loginDTORespDTO.data.getCityName());
                        SPUtils.put(getApplication(), "countyName", loginDTORespDTO.data.getCountyName() == null ? "" : loginDTORespDTO.data.getCountyName());
                        SPUtils.put(getApplication(), "schoolName", loginDTORespDTO.data.getSchoolName() == null ? "" : loginDTORespDTO.data.getSchoolName());
                        SPUtils.put(getApplication(), "college", loginDTORespDTO.data.getCollege() == null ? "" : loginDTORespDTO.data.getCollege());


                        getmVoidSingleLiveEvent().setValue("login");
                        getmVoidSingleLiveEvent().call();
                    }
//                    else {
//                        ToastUtil.showToast(loginDTORespDTO.msg);
//                    }

                } else {
                    Log.v(TAG, "error:" + loginDTORespDTO.msg);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "error:" + e.getMessage());

            }

            @Override
            public void onComplete() {
                getmVoidSingleLiveEvent().setValue("miss");
                getmVoidSingleLiveEvent().call();
            }
        });
    }


    public void startClick(View view) {
        int i = view.getId();//账号注册
//忘记密码
        if (i == R.id.register) {
            view.getContext().startActivity(new Intent(view.getContext(), RegistActivity.class));
        } else if (i == R.id.forgetPassword) {
            view.getContext().startActivity(new Intent(view.getContext(), FrogetActivity.class));
        } else if (i == R.id.forget) {//重置密码

            if (InfoVerify.isEmpty(data.get().getRgPhone())) {
                ToastUtil.showToast("请填写正确的手机号");
                return;

            }

            if (InfoVerify.isEmpty(data.get().getRgImageCode())) {
                ToastUtil.showToast("请填写验证码");
                return;
            }
            if (InfoVerify.isEmpty(data.get().getRgPaw())) {
                ToastUtil.showToast("账号密码为空");
                return;
            }
            if (!InfoVerify.isPwd(data.get().getRgPaw())) {
                ToastUtil.showToast("请输入6-20位字母和数字组合，必须同时含有字母和数字");
                return;

            }
            if (!data.get().getRgPaw().equals(data.get().getRgqrPaw())) {
                ToastUtil.showToast("密码和确认密码不一致");
                return;
            }

            foggerPas();
        } else if (i == R.id.register_get_message || i == R.id.forget_get_message) {
            this.rgitstView = view;
            if (!InfoVerify.isPhone(data.get().getRgPhone())) {
                ToastUtil.showToast("请填写正确的手机号");
                return;
            }

            if (data.get().getRgImageCode() == null || data.get().getRgImageCode().isEmpty()) {
                ToastUtil.showToast("请填写验证码");
                return;
            }
            if (token.isEmpty()) {
                ToastUtil.showToast("验证码失效");
                getCaptcha();
                return;
            }
            String type = "regist";
            if (view.getId() == R.id.forget_get_message) {
                type = "forget";
            }
            sendSms(type);

        } else if (i == R.id.register_mark) {
            View contentView = LayoutInflater.from(view.getContext()).inflate(R.layout.popup_tip, null);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            params.topMargin = location[1];
            params.leftMargin = location[0] + view.getWidth() / 2 - DisplayUtil.dip2px(22f);
            contentView.setLayoutParams(params);
            PopupWindowUtilKt.showFullPopupWindow(
                    contentView,
                    view,
                    R.color.color_99ffffff);
        } else if (i == R.id.register_account_level) {//账号级别
            initPicker(view.getContext());
            pickerView.setPicker(levelList);
            pickerView.setSelectOptions(data.get().getLevelIndel());
            pickerView.show();
            pickerBuilder.setOnOptionsSelectListener((options1, options2, options3, v) ->
                    {
                        if (options1 != data.get().getLevelIndel()) {
                            //清空省市县的选择
                            data.get().setRgProvince("省");
                            data.get().setRgTwon("县");
                            data.get().setRgCity("市");
                            data.get().setRgSchool("学校");
                            provinceCode = "";
                            cityCode = "";
                            twonCode = "";
                            schoolCode = "";
                            provinceIndexl = 0;
                            data.get().setWriteSchool(false);
                            data.get().setWriteTwon(false);
                            data.get().setWriteCity(false);
                            data.get().setWriteProvince(false);
                        }


                        data.get().setLevelIndel(options1);
                        data.get().setRgLevel(levelList.get(options1).toString());
                        data.get().setWriteLevel(true);
                        if (options1 == 0) {
                            rgProvinceV.set(false);
                            rgCityV.set(false);
                            rgTwonV.set(false);
                            rgSchoolV.set(false);
                        } else if (options1 == 1) {
                            rgProvinceV.set(true);
                            rgCityV.set(false);
                            rgTwonV.set(false);
                            rgSchoolV.set(false);
                        } else if (options1 == 2) {

                            rgProvinceV.set(true);
                            rgCityV.set(true);
                            rgTwonV.set(false);
                            rgSchoolV.set(false);
                        } else if (options1 == 3) {
                            rgProvinceV.set(true);
                            rgCityV.set(true);
                            rgTwonV.set(true);
                            rgSchoolV.set(false);
                        } else {
                            rgProvinceV.set(true);
                            rgCityV.set(true);
                            rgTwonV.set(true);
                            rgSchoolV.set(true);
                        }


                    }

            );
        } else if (i == R.id.register_get_image) {
            getCaptcha();
        } else if (i == R.id.register_province) {
            initPicker(view.getContext());
            getRegionOrSchool("2", "", "0");
        } else if (i == R.id.register_city) {
            initPicker(view.getContext());
            getRegionOrSchool("3", provinceCode, "1");
        } else if (i == R.id.register_town) {
            initPicker(view.getContext());
            getRegionOrSchool("4", cityCode, "2");
        } else if (i == R.id.register_school) {
            initPicker(view.getContext());
            getRegionOrSchool("", twonCode, "3");
        } else if (i == R.id.start_register) {
            if (InfoVerify.isEmpty(data.get().getRgName())) {
                ToastUtil.showToast("用户名为空");
                return;
            }

            if (!InfoVerify.isChinese(data.get().getRgName())) {
                ToastUtil.showToast("姓名只能是中文");
                return;
            }

            if (!InfoVerify.isPhone(data.get().getRgPhone())) {
                ToastUtil.showToast("请填写正确的手机号");
                return;
            }

            if (InfoVerify.isEmpty(data.get().getRgCode())) {
                ToastUtil.showToast("短信验证码为空");
                return;
            }
//            splashViewBean.setRgLevel();
//            splashViewBean.setRgProvince("省");
//            splashViewBean.setRgTwon("县");
//            splashViewBean.setRgCity("市");
//            splashViewBean.setRgSchool("学校");

            String rgLevel = data.get().getRgLevel();
            if (InfoVerify.isEmpty(rgLevel) || "账号级别".equals(rgLevel)) {
                ToastUtil.showToast("账号级别为空");
                return;
            }
            if (InfoVerify.isEmpty(data.get().getRgPaw())) {
                ToastUtil.showToast("账号密码为空");
                return;
            }
            if (!InfoVerify.isPwd(data.get().getRgPaw())) {
                ToastUtil.showToast("请输入6-20位字母和数字组合，必须同时含有字母和数字");
                return;

            }
            if (!data.get().getRgPaw().equals(data.get().getRgqrPaw())) {
                ToastUtil.showToast("密码和确认密码不一致");
                return;
            }

            //账号级别，可选值【0-中央、2-省（自治区）、3-市（自治州）、4-县（区）、5-学校】

            String numLevel;
            if ("中央".equals(rgLevel)) {
                numLevel = "0";
                startRigst(data.get().getRgName(), data.get().getRgPaw(), numLevel, "", "", "", "", data.get().getRgCode());
            } else if ("省（自治区)".equals(rgLevel)) {
                numLevel = "2";

                if (InfoVerify.isEmpty(data.get().getRgProvince()) || "省".equals(data.get().getRgProvince())) {
                    ToastUtil.showToast("省（自治区)为空");
                    return;
                }

                startRigst(data.get().getRgName(), data.get().getRgPaw(), numLevel, provinceCode, "", "", "", data.get().getRgCode());

            } else if ("市（自治州)".equals(rgLevel)) {
                numLevel = "3";
                if (InfoVerify.isEmpty(data.get().getRgProvince()) || "省".equals(data.get().getRgProvince())) {
                    ToastUtil.showToast("省（自治区)为空");
                    return;
                }
                if (InfoVerify.isEmpty(data.get().getRgCity()) || "市".equals(data.get().getRgCity())) {
                    ToastUtil.showToast("市（自治州)为空");
                    return;
                }
                startRigst(data.get().getRgName(), data.get().getRgPaw(), numLevel, provinceCode, cityCode, "", "", data.get().getRgCode());


            } else if ("县（区)".equals(rgLevel)) {
                numLevel = "4";
                if (InfoVerify.isEmpty(data.get().getRgProvince()) || "省".equals(data.get().getRgProvince())) {
                    ToastUtil.showToast("省（自治区)为空");
                    return;
                }
                if (InfoVerify.isEmpty(data.get().getRgCity()) || "市".equals(data.get().getRgCity())) {
                    ToastUtil.showToast("市（自治州)为空");
                    return;
                }
                if (InfoVerify.isEmpty(data.get().getRgTwon()) || "县".equals(data.get().getRgTwon())) {
                    ToastUtil.showToast("县（区)为空");
                    return;
                }
                startRigst(data.get().getRgName(), data.get().getRgPaw(), numLevel, provinceCode, cityCode, twonCode, "", data.get().getRgCode());


            } else {
                numLevel = "5";
                if (InfoVerify.isEmpty(data.get().getRgProvince()) || "省".equals(data.get().getRgProvince())) {
                    ToastUtil.showToast("省（自治区)为空");
                    return;
                }
                if (InfoVerify.isEmpty(data.get().getRgCity()) || "市".equals(data.get().getRgCity())) {
                    ToastUtil.showToast("市（自治州)为空");
                    return;
                }
                if (InfoVerify.isEmpty(data.get().getRgTwon()) || "县".equals(data.get().getRgTwon())) {
                    ToastUtil.showToast("县（区)为空");
                    return;
                }
                if (InfoVerify.isEmpty(data.get().getRgSchool()) || "学校".equals(data.get().getRgSchool())) {
                    ToastUtil.showToast("学校为空");
                    return;
                }
                startRigst(data.get().getName(), data.get().getRgPaw(), numLevel, provinceCode, cityCode, twonCode, schoolCode, data.get().getRgCode());

            }
        } else if (i == R.id.login) {
            login();
        }

    }

    /**
     * 忘记密码
     */
    private void foggerPas() {
        mModel.updateUserPassword("forget", data.get().getRgPhone(), "", MD5UtlisKt.MD5(data.get().getRgPaw()), data.get().getRgCode()).subscribe(new Observer<RespDTO>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(RespDTO respDTO) {
                if (respDTO.code == 200) {
                    ToastUtil.showToast(respDTO.msg);
                    getmVoidSingleLiveEvent().call();
                }

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }


    public SingleLiveEvent<String> getmVoidSingleLiveEvent() {
        return mVoidSingleLiveEvent = createLiveData(mVoidSingleLiveEvent);
    }


    private void startTimer() {
        rgitstView.setEnabled(false);
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }


    public void getCaptcha() {
        mModel.getCaptcha().doOnSubscribe(this).subscribe(new Observer<RespDTO<ImageUrl>>() {

            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(RespDTO<ImageUrl> imageUrlRespDTO) {
                data.get().setRgImageUrl(imageUrlRespDTO.data.getBitmap());
                token = imageUrlRespDTO.data.getToken();
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });

    }

    /**
     * @param regLevel 区划级别
     * @param regCode  区划编号
     * @param type     省 0 市 1  县 2 学校 3
     */


    private void getRegionOrSchool(String regLevel, String regCode, String type) {
        if ("1".equals(type) && regCode.isEmpty()) {
            ToastUtil.showToast("省还未选择!!!");
            return;
        }

        if ("2".equals(type) && (regLevel.isEmpty() || regCode.isEmpty())) {
            if ((regLevel.isEmpty() || regCode.isEmpty())) {
                ToastUtil.showToast("省市未选择!!!");
                return;
            }
        }

        if ("3".equals(type) && regCode.isEmpty()) {
            ToastUtil.showToast("省市县有未选择!!!");
            return;
        }
        subscribe = mModel.getRegionOrSchool(regLevel, regCode).subscribe(listRespDTO -> {
            List<RegionOrSchoolBean> result = listRespDTO.data;
            if (result == null || result.size() == 0) {
                return;
            }

            List<Object> dalist = new ArrayList<>();
            for (RegionOrSchoolBean bean : result) {
                if ("3".equals(type)) {
                    dalist.add(bean.getSCHOOL_NAME());
                } else {
                    dalist.add(bean.getName());
                }

            }

            if ("0".equals(type)) {
                pickerView.setSelectOptions(provinceIndexl);
            }
            pickerView.setPicker(dalist);
            pickerView.show();

            pickerBuilder.setOnOptionsSelectListener((options1, options2, options3, v) ->
                    {

                        if ("0".equals(type)) {
                            data.get().setRgProvince(dalist.get(options1).toString());
                            data.get().setRgTwon("县");
                            data.get().setRgCity("市");
                            data.get().setRgSchool("学校");
                            provinceIndexl = options1;
                            provinceCode = result.get(options1).getCode();
                            data.get().setWriteProvince(true);
                            data.get().setWriteCity(false);
                            data.get().setWriteTwon(false);
                            data.get().setWriteSchool(false);
                            cityCode = "";
                            twonCode = "";
                            schoolCode = "";

                        } else if ("1".equals(type)) {
                            data.get().setRgCity(dalist.get(options1).toString());
                            data.get().setRgTwon("县");
                            data.get().setRgSchool("学校");
                            cityCode = result.get(options1).getCode();

                            twonCode = "";
                            schoolCode = "";
                            data.get().setWriteProvince(true);
                            data.get().setWriteCity(true);
                            data.get().setWriteTwon(false);
                            data.get().setWriteSchool(false);

                        } else if ("2".equals(type)) {
                            data.get().setRgTwon(dalist.get(options1).toString());
                            data.get().setRgSchool("学校");
                            twonCode = result.get(options1).getCode();
                            schoolCode = "";
                            data.get().setWriteProvince(true);
                            data.get().setWriteCity(true);
                            data.get().setWriteTwon(true);
                            data.get().setWriteSchool(false);
                        } else if ("3".equals(type)) {
                            data.get().setRgSchool(dalist.get(options1).toString());
                            schoolCode = result.get(options1).getSCHOOL_ID();
                            data.get().setWriteProvince(true);
                            data.get().setWriteCity(true);
                            data.get().setWriteTwon(true);
                            data.get().setWriteSchool(true);
                        }
                    }
            );


        });

    }

    /**
     * 发送验证吗
     */

    private void sendSms(String type) {
        mModel.sendSms(token, data.get().getRgImageCode(), data.get().getRgPhone(), type).doOnSubscribe(this).subscribe(new Observer<RespDTO>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(RespDTO respDTO) {
                if (respDTO.code == 200) {
                    startTimer();
                    ToastUtil.showToast("发送成功");
                } else {
                    resume();

                }

            }

            @Override
            public void onError(Throwable e) {
                resume();

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void startRigst(String userName,
                            String password,
                            String accountLevel,
                            String province,
                            String city,
                            String county,
                            String college,
                            String smsCode) {
        password = MD5UtlisKt.MD5(password);
        String phone = data.get().getRgPhone();

        mModel.regist(userName,
                password,
                accountLevel,
                province,
                city,
                county,
                college, smsCode, phone).subscribe(new Observer<RespDTO>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(RespDTO respDTO) {
                if (respDTO.code == 200) {
                    ToastUtil.showToast("注册成功");
                    SPUtils.put(getApplication(), "phone", data.get().getRgPhone());
                    getmVoidSingleLiveEvent().call();
                }

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                time -= 1;
                data.get().setTimeMeesage(time + "秒");
                if (time == 1) {
                    resume();
                }

            }
        }
    };

    private void resume() {
        if (timer != null) {
            timer.cancel();
            timerTask.cancel();
        }
        data.get().setTimeMeesage("获取");
        data.set(splashViewBean);
        time = 60;
        rgitstView.setEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscribe != null) {
            subscribe.dispose();
        }
        if (handler != null) {
            handler.removeMessages(1);
            handler = null;
        }
        if (timer != null) {
            timer.cancel();
            timerTask.cancel();
            timerTask = null;
            timer = null;
        }


    }
}