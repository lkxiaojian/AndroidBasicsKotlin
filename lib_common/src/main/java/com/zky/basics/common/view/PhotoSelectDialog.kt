package com.zky.basics.common.view
//
//import android.R
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//import com.zky.basics.common.util.DisplayUtil.dip2px
//import com.zky.basics.common.util.MultiMediaUtil
//import com.zky.basics.common.util.MultiMediaUtil.getPhotoPath
//import com.zky.basics.common.util.MultiMediaUtil.pohotoSelect
//import com.zky.basics.common.util.MultiMediaUtil.takePhoto
//import me.nereo.multi_image_selector.MultiImageSelectorActivity
//
//class PhotoSelectDialog : BottomSheetDialogFragment(), View.OnClickListener {
//    private var mOnClickLisener: OnPhotoClickLisener? = null
//    private var mPhotoPath: String? = null
//    private var count = 1 //图片选择的数量
//    fun setOnClickLisener(onPhotoClickLisener: OnPhotoClickLisener?) {
//        mOnClickLisener = onPhotoClickLisener
//    }
//
//    interface OnPhotoClickLisener {
//        fun onTakePhototClick(path: String?)
//        fun onSelectPhotoClick(list: List<String?>?)
//    }
//
//    override fun onStart() {
//        super.onStart()
//        dialog!!.window.setLayout(
//            resources.displayMetrics.widthPixels - dip2px(16f) * 2,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        dialog!!.window.findViewById<View>(com.zky.basics.common.R.id.design_bottom_sheet)
//            .setBackgroundResource(
//                R.color.transparent
//            )
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view: View =
//            inflater.inflate(com.zky.basics.common.R.layout.fragment_photo_select, container, false)
//        val btnSelectPhoto =
//            view.findViewById<View>(com.zky.basics.common.R.id.btn_select_photo) as Button
//        val btnTakephoto =
//            view.findViewById<View>(com.zky.basics.common.R.id.btn_take_photo) as Button
//        val btnCancel = view.findViewById<View>(com.zky.basics.common.R.id.btn_cancel) as Button
//        btnSelectPhoto.setOnClickListener(this)
//        btnTakephoto.setOnClickListener(this)
//        btnCancel.setOnClickListener(this)
//        return view
//    }
//
//    override fun onClick(v: View) {
//        val i = v.id
//        when (i) {
//            com.zky.basics.common.R.id.btn_take_photo -> {
//                mPhotoPath = getPhotoPath(activity)
//                takePhoto(this, mPhotoPath!!, MultiMediaUtil.TAKE_PHONE)
//            }
//            com.zky.basics.common.R.id.btn_select_photo -> {
//                if (count < 1) {
//                    count = 1
//                }
//                pohotoSelect(this, count, MultiMediaUtil.SELECT_IMAGE)
//            }
//            com.zky.basics.common.R.id.btn_cancel -> {
//                dismiss()
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        when (requestCode) {
//            MultiMediaUtil.SELECT_IMAGE -> if (resultCode == Activity.RESULT_OK) {
//                val path: List<String?> =
//                    data!!.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT)
//                if (mOnClickLisener != null) {
//                    mOnClickLisener!!.onSelectPhotoClick(path)
//                }
//                dismiss()
//            }
//            MultiMediaUtil.TAKE_PHONE -> {
//                Log.v(TAG, "img path:$mPhotoPath")
//                if (mOnClickLisener != null) {
//                    mOnClickLisener!!.onTakePhototClick(mPhotoPath)
//                }
//                dismiss()
//            }
//        }
//    }
//
//    fun setCount(count: Int) {
//        this.count = count
//    }
//
//    companion object {
//        val TAG = PhotoSelectDialog::class.java.simpleName
//        fun newInstance(): PhotoSelectDialog {
//            return PhotoSelectDialog()
//        }
//    }
//}