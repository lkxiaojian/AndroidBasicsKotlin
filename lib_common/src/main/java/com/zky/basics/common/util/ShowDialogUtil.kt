package com.zky.basics.common.util

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.TextView
import com.zky.basics.common.R
import com.zky.basics.common.util.view.CustomDialog


//显示dialog
//只有一个确定按钮
/**
 * @param context
 * @param titleText 标题
 * @param firstText 第一内容
 * @param contentText 第二内容
 * @param btnCancelText 关闭按钮
 * @param btnSureText 确定按钮
 */

fun showCustomDialog(
    context: Context,
    titleText: String,
    firstText: String,
    contentText: String,
    btnCancelText: String,
    btnSureText: String
): CustomDialog {
    val customDialog = if (btnCancelText.isEmpty()) {
        CustomDialog(context, R.layout.dialog_layout)
    } else {
        CustomDialog(context, R.layout.dialog_layout_two)
    }
    customDialog.show()

    customDialog.findViewById<TextView>(R.id.dialog_title).text = titleText
    customDialog.findViewById<TextView>(R.id.dialog_first).text = firstText
    val sureView = customDialog.findViewById<TextView>(R.id.dialog_sure)
    sureView.text = btnSureText
    sureView.setOnClickListener(customDialog)
    val contentView = customDialog.findViewById<TextView>(R.id.dialog_content)
    if (contentText.isEmpty()) {
        contentView.visibility = View.GONE
    } else {
        contentView.visibility = View.VISIBLE
        contentView.movementMethod = ScrollingMovementMethod.getInstance()
        contentView.text = contentText
    }
    if (!btnCancelText.isEmpty()) {
        val cancelView = customDialog.findViewById<TextView>(R.id.dialog_cancel)
        cancelView.text = btnCancelText
        cancelView.setOnClickListener(customDialog)
    }

    return customDialog
}


fun onShowDialogLoading(context: Context): CustomDialog {
    val dialog = CustomDialog(context, R.layout.dialog_layout_progress)

//    dialog.show()
//    val tv = dialog.findViewById<TextView>(R.id.dialog_content_pro)
//    tv.text = message
    return dialog
}

fun dialogSetText(dialog: CustomDialog, message: String) {

    dialog.show()
    val tv = dialog.findViewById<TextView>(R.id.dialog_content_pro)
    tv.text = message

}
