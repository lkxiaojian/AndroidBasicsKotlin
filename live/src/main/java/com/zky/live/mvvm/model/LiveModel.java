package com.zky.live.mvvm.model;

import android.app.Application;

import com.zky.basics.api.RetrofitManager;
import com.zky.basics.api.apiservice.CommonService;
import com.zky.basics.common.mvvm.model.BaseModel;

/**
 * Created by lk
 * Date 2019-11-08
 * Time 10:55
 * Detail:
 */
public class LiveModel extends BaseModel {
    private CommonService mCommonService;


    public LiveModel(Application application) {
        super(application);
        mCommonService = RetrofitManager.Companion.getInstance().getCommonService();

    }
}
