package com.zky.basics.main.mvvm.model;

import android.app.Application;

import com.zky.basics.api.RetrofitManager;
import com.zky.basics.api.apiservice.CommonService;
import com.zky.basics.api.dto.RespDTO;
import com.zky.basics.api.http.RxAdapter;
import com.zky.basics.api.splash.entity.ImageUrl;
import com.zky.basics.api.splash.entity.RegionOrSchoolBean;
import com.zky.basics.api.splash.entity.Userinfo;
import com.zky.basics.api.splash.service.SplashService;
import com.zky.basics.common.mvvm.model.BaseModel;

import java.util.List;

import io.reactivex.Observable;


public class SplashModel extends BaseModel {
    private CommonService mCommonService;
    private SplashService splashService;

    public SplashModel(Application application) {
        super(application);
        mCommonService = RetrofitManager.getInstance().getCommonService();
        splashService = RetrofitManager.getInstance().getSplashService();
    }


    public Observable<RespDTO<Userinfo>> login(String username, String password) {
        return mCommonService.login(username, password)
                .compose(RxAdapter.schedulersTransformer())
                .compose(RxAdapter.exceptionTransformer());
    }

    public Observable<RespDTO<ImageUrl>> getCaptcha() {
        return splashService.getCaptcha()
                .compose(RxAdapter.schedulersTransformer())
                .compose(RxAdapter.exceptionTransformer());
    }


    public Observable<RespDTO<List<RegionOrSchoolBean>>> getRegionOrSchool(String regLevel, String regCode) {
        return mCommonService.getRegionOrSchool(regLevel, regCode)
                .compose(RxAdapter.schedulersTransformer())
                .compose(RxAdapter.exceptionTransformer());
    }


    public Observable<RespDTO> sendSms(String token, String code, String phone, String type) {
        Observable compose = splashService.sendSms(token, code, phone, type)
                .compose(RxAdapter.schedulersTransformer())
                .compose(RxAdapter.exceptionTransformer());
        return compose;
    }

    public Observable<RespDTO> regist(String userName,
                                      String password,
                                      String accountLevel,
                                      String province,
                                      String city,
                                      String county,
                                      String college,
                                      String smsCode,
                                      String phone) {
        Observable compose = splashService.regist(userName, password, accountLevel, province, city, county, college, smsCode, phone)
                .compose(RxAdapter.schedulersTransformer())
                .compose(RxAdapter.exceptionTransformer());
        return compose;
    }


    public Observable<RespDTO> updateUserPassword(String oprationType, String phone, String oldPassword, String password, String smsCode) {
        return mCommonService.updateUserPassword(oprationType, phone, oldPassword, password, smsCode)
                .compose(RxAdapter.schedulersTransformer())
                .compose(RxAdapter.exceptionTransformer());
    }


}