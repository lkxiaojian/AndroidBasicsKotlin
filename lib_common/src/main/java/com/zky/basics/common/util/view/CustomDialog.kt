package com.zky.basics.common.util.view

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.KeyEvent
import android.view.View
import com.zky.basics.common.R



class CustomDialog(context: Context, @LayoutRes private val layoutResID: Int) : Dialog(context, R.style.CustomDialog),
    View.OnClickListener, DialogInterface.OnDismissListener {

    private var listener: OnItemClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        window?.setWindowAnimations(R.style.bottom_menu_animation) // 添加动画效果
        setContentView(layoutResID)
        // 宽度全屏
        //        WindowManager windowManager = window.getWindowManager();
        //        Display display = windowManager.getDefaultDisplay();
        //        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //        lp.width = display.getWidth() * 14 / 25; // 设置dialog宽度为屏幕的56%
        //        getWindow().setAttributes(lp);
        // 点击Dialog外部消失
        setCanceledOnTouchOutside(false)
        setOnDismissListener(this)
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (isDismiss) {
            return
        }
        listener?.onDismiss()
    }

    interface OnItemClickListener {
        fun onSure()
        fun onDismiss()
    }

    open class SimpleClickListener : OnItemClickListener {
        override fun onDismiss() {}

        override fun onSure() {}
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    private var isDismiss: Boolean = false

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return if (event.keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN && event.repeatCount == 0) {
            true
        } else super.dispatchKeyEvent(event)
    }

    override fun show() {
        isDismiss = false
        super.show()
    }

    override fun onClick(view: View) {
        isDismiss = true
        dismiss()
        when (view.tag?.toString()) {
            "0" -> listener?.onDismiss()
            "1" -> listener?.onSure()
        }
    }
}