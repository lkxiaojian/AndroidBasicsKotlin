package com.zky.live.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by lk
 * Date 2019-11-08
 * Time 14:45
 * Detail:
 */
public class TestFragement extends Fragment {


    public static TestFragement getInstance(){
        return new TestFragement();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
