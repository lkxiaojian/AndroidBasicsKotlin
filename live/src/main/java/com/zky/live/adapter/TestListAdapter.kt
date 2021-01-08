package com.zky.live.adapter

import android.content.Context
import androidx.databinding.ObservableArrayList
import com.zky.basics.common.adapter.BaseBindAdapter
import com.zky.basics.common.util.log.KLog
import com.zky.live.R
import com.zky.live.databinding.TestListBinding

/**
 *create_time : 21-1-7 上午10:57
 *author: lk
 *description： TestListAdapter
 */
class TestListAdapter(context: Context, items: ObservableArrayList<String>?) :
    BaseBindAdapter<String, TestListBinding>(context, items) {
    override fun getLayoutItemId(viewType: Int) = R.layout.test_list

    override fun onBindItem(binding: TestListBinding?, item: String, position: Int) {
        binding?.data = item
        binding?.clClick?.setOnClickListener {
            mItemClickListener?.onItemClick(item ,position)
        }
    }


}