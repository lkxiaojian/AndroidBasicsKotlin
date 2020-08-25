package com.zky.basics.common.util.view

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import com.zky.basics.common.R
import com.zky.basics.common.util.view.adapter.SpinnerStringAdapter


@Suppress("DEPRECATION")
fun showListPopupWindow(
    view: View,
    values: List<String>,
    @Suppress("UNUSED_PARAMETER") width: Int,
    onItemClickListener: AdapterView.OnItemClickListener
): PopupWindow {
    val context = view.context
    val popupWindow = PopupWindow(context)
    val maxAvailableHeight = popupWindow.getMaxAvailableHeight(view)
    popupWindow.width =ViewGroup.LayoutParams.WRAP_CONTENT/2
    popupWindow.height = maxAvailableHeight + view.height
    val relativeLayout = LayoutInflater.from(context).inflate(R.layout.item_popup_window, null)
    val layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    val layoutParams1 = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    relativeLayout.layoutParams = layoutParams
    val listView = relativeLayout.findViewById<ListView>(R.id.listView)
    listView.layoutParams = layoutParams1
    val stringAdapter = SpinnerStringAdapter(context)
    stringAdapter.addAll(values)
    listView.adapter = stringAdapter
    listView.onItemClickListener = AdapterView.OnItemClickListener { parent, viewp, position, id ->
        onItemClickListener.onItemClick(parent, viewp, position, id)
        popupWindow.dismiss()
    }
    listView.isFastScrollAlwaysVisible = false
    popupWindow.isFocusable = true
    popupWindow.contentView = relativeLayout
    popupWindow.isOutsideTouchable = true
//    popupWindow.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.transparent)))
    popupWindow.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context,R.color.transparent)))

    popupWindow.showAsDropDown(view, 0, -view.height)
    return popupWindow
}

@Suppress("DEPRECATION")
fun showFullPopupWindow(contentView: View, anchorView: View, @ColorRes color: Int): PopupWindow {
    val context = anchorView.context
    val popupWindow = PopupWindow(context)
    popupWindow.width = ViewGroup.LayoutParams.MATCH_PARENT
    popupWindow.height = ViewGroup.LayoutParams.MATCH_PARENT
    val relativeLayout = RelativeLayout(context)
    val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    relativeLayout.layoutParams = layoutParams
    relativeLayout.setOnClickListener {
        popupWindow.dismiss()
    }
//    relativeLayout.setBackgroundColor(context.resources.getColor(color))

    relativeLayout.setBackgroundColor(ContextCompat.getColor(context,color))
    relativeLayout.addView(contentView)
    popupWindow.contentView = relativeLayout
    popupWindow.isOutsideTouchable = true
    popupWindow.setBackgroundDrawable(BitmapDrawable())
    popupWindow.showAsDropDown(anchorView)
    return popupWindow
}


//fun showWriteLiveMessagePopupWindow(context: Activity): PopupWindow {
//    val popupWindow = PopupWindow(context)
//    val contentView = LayoutInflater.from(context).inflate(R.layout.message_popup_window, null)
//    popupWindow.contentView = contentView
//    popupWindow.width = ViewGroup.LayoutParams.WRAP_CONTENT
//    popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
//    popupWindow.isFocusable = true
//    popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0)
//
//
//    val cancle = contentView.findViewById<TextView>(R.id.tv_cancle)
//    val sure = contentView.findViewById<TextView>(R.id.tv_sure)
//    val liveMessage = contentView.findViewById<EditText>(R.id.et_live_message)
//    val liveTitle = contentView.findViewById<TextView>(R.id.et_live_title)
//    cancle.setOnClickListener {
//        popupWindow.dismiss()
//    }
//
//    sure.setOnClickListener {
//        if (liveTitle.text.toString().isEmpty()) {
//            Toast.makeText(context, "房间的标题为空，请填写！！！", Toast.LENGTH_SHORT).show()
//
//            return@setOnClickListener
//        }
////        if(liveMessage.text.toString().isEmpty()){
////            Toast.makeText(context,"房间的描述为空，请填写！！！",Toast.LENGTH_SHORT).show()
////
////            return@setOnClickListener
////        }
//
//
//        EventBus.getDefault().post(StartLive(liveTitle.text.toString(), liveMessage.text.toString()))
//        popupWindow.dismiss()
//
//    }
//
//    return popupWindow
//}


//fun showCenterListPopupWindow(
//    view: View,
//    values: List<String>,
//    width: Int,
//    onItemClickListener: AdapterView.OnItemClickListener
//): PopupWindow {
//    var list = values as MutableList
//
//    val context = view.context
//    val popupWindow = PopupWindow(context)
//    popupWindow.width = ViewGroup.LayoutParams.MATCH_PARENT
//    popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
//    val relativeLayout = LayoutInflater.from(context).inflate(R.layout.item_bottom_popup_window, null)
//    val layoutParams = RelativeLayout.LayoutParams(popupWindow.width, ViewGroup.LayoutParams.WRAP_CONTENT)
//    val layoutParams1 = RelativeLayout.LayoutParams(popupWindow.width, ViewGroup.LayoutParams.WRAP_CONTENT)
//    relativeLayout.layoutParams = layoutParams
//    val listView = relativeLayout.findViewById<ListView>(R.id.listView)
//    listView.layoutParams = layoutParams1
//
//    val stringAdapter = SpinnerStringAdapter(context)
//    list.add(0,"")
//    stringAdapter.addAll(list)
//    listView.adapter = stringAdapter
//    listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
//        onItemClickListener.onItemClick(parent, view, position, id)
//        popupWindow.dismiss()
//    }
//    listView.isFastScrollAlwaysVisible = false
//    popupWindow.isFocusable = true
//    popupWindow.contentView = relativeLayout
//    popupWindow.isOutsideTouchable = true
//    popupWindow.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.transparent)))
//    popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0)
//    return popupWindow
//}
