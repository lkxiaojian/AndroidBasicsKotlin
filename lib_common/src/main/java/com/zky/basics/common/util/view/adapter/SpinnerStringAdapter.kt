package com.zky.basics.common.util.view.adapter

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.TextView
import com.zky.basics.common.R
import me.jessyan.autosize.utils.AutoSizeUtils.dp2px

/**
 * Created by lk
 * Date 2019-11-14
 * Time 14:35
 * Detail:
 */
class SpinnerStringAdapter(context: Context):AbsListBaseAdapter<String>(context) {
    private var dropTextSize: Int = 0
    private var viewTextSize:Int = 0
    @ColorRes
    private var dropTextColor: Int = 0
    @ColorRes
    private var viewTextColor: Int = 0
    @DrawableRes
    private var dropBgColor: Int = 0
    @DrawableRes
    private var viewBgColor: Int = 0
    private var gravity: Int = 0

    init {
        dropTextSize = 12
        viewTextSize = 12
        dropTextColor = R.color.color_4a4a4a
        viewTextColor = R.color.color_4a4a4a
        gravity = Gravity.CENTER

    }

    fun setDropAndViewTextSize(dropTextSize: Int, viewTextSize: Int) {
        this.dropTextSize = dropTextSize
        this.viewTextSize = viewTextSize
    }

    fun setDropAndViewTextColor(@ColorRes dropTextColor: Int, @ColorRes viewTextColor: Int) {
        this.dropTextColor = dropTextColor
        this.viewTextColor = viewTextColor
    }

    fun setDropAndViewBg(@DrawableRes dropBgColor: Int, @DrawableRes viewBgColor: Int) {
        this.dropBgColor = dropBgColor
        this.viewBgColor = viewBgColor
    }

    fun setGravity(gravity: Int) {
        this.gravity = gravity
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertViewTmp = convertView
        //修改Spinner展开后的字体颜色
        if (convertView == null) {
            convertViewTmp = inflater.inflate(R.layout.cus_simple_spinner_item, parent, false)
        }

        //此处text1是Spinner默认的用来显示文字的TextView
        val tv = convertViewTmp?.findViewById<View>(android.R.id.text1) as TextView
        tv.setPadding(
            dp2px(context, 10f),
            dp2px(context, 5f),
            dp2px(context, 10f),
            dp2px(context, 5f)
        )
        tv.text = getItem(position)
        tv.textSize = dropTextSize.toFloat()
        tv.gravity = gravity
//        tv.setTextColor(context.resources.getColor(dropTextColor))

        tv.setTextColor(ContextCompat.getColor(context,dropTextColor))
        tv.setBackgroundResource(dropBgColor)

        tv.layoutParams =
            AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT)
        return convertView!!

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertViewTmp = convertView
        // 修改Spinner选择后结果的字体颜色
        if (convertView == null) {
            convertViewTmp = inflater.inflate(android.R.layout.simple_spinner_item, parent, false)
        }

        //此处text1是Spinner默认的用来显示文字的TextView
        val tv = convertView!!.findViewById<View>(android.R.id.text1) as TextView
        tv.setPadding(
            dp2px(context, 10f),
            dp2px(context, 10f),
            dp2px(context, 10f),
            dp2px(context, 10f)
        )
        tv.text = getItem(position)
        tv.textSize = viewTextSize.toFloat()
        tv.gravity = gravity
//        tv.setTextColor(context.resources.getColor(viewTextColor))
        tv.setTextColor(ContextCompat.getColor(context,viewTextColor))

        tv.setBackgroundResource(viewBgColor)
        tv.layoutParams =
            AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT)
        return convertViewTmp!!
    }
}