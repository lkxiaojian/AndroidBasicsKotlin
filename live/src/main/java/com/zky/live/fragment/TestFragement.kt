package com.zky.live.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * Created by lk
 * Date 2019-11-08
 * Time 14:45
 * Detail:
 */
class TestFragement : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        val instance: TestFragement
            get() = TestFragement()
    }
}