package com.zky.basics.common.provider

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * Created by lk
 * Date 2019-11-08
 * Time 14:23
 * Detail:
 */
interface IMapProvider : IProvider {
    val mapFragment: Fragment?
}