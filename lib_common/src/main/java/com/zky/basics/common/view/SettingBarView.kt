package com.zky.basics.common.view

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.zky.basics.common.R
import com.zky.basics.common.binding.command.BindingCommand

class SettingBarView(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    private val imgLeftIcon: ImageView?
    private val txtSetTitle: TextView
    private val txtSetContent: EditText?
    private val imgRightIcon: ImageView
    private val layoutSettingBar: RelativeLayout
    private var mOnClickSettingBarViewListener: OnClickSettingBarViewListener? = null
    private var mOnClickRightImgListener: OnClickRightImgListener? = null
    private var isEdit = false //是否需要编辑
    private var mOnViewChangeListener: OnViewChangeListener? = null

    interface OnClickSettingBarViewListener {
        fun onClick()
    }

    interface OnClickRightImgListener {
        fun onClick()
    }

    fun setOnClickRightImgListener(onClickRightImgListener: OnClickRightImgListener?) {
        mOnClickRightImgListener = onClickRightImgListener
    }

    fun setOnClickSettingBarViewListener(onClickSettingBarViewListener: OnClickSettingBarViewListener?) {
        mOnClickSettingBarViewListener = onClickSettingBarViewListener
    }

    var content: String?
        get() = txtSetContent?.text?.toString()
        set(value) {
            if (txtSetContent != null && !TextUtils.isEmpty(value)) {
                txtSetContent.setText(value)
            }
        }

    private interface OnViewChangeListener {
        fun onChange()
    }

    private fun setViewChangeListener(listener: OnViewChangeListener?) {
        mOnViewChangeListener = listener
    }

    fun setEnableEditContext(b: Boolean) {
        isEdit = b
        if (txtSetContent != null) {
            txtSetContent.isEnabled = b
        }
    }

    fun showImgLeftIcon(show: Boolean) {
        if (imgLeftIcon != null) {
            imgLeftIcon.visibility = if (show) VISIBLE else GONE
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return !isEdit
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return layoutSettingBar.onTouchEvent(event)
    }

    companion object {
        @BindingAdapter(value = ["content"], requireAll = false)
        fun setContent(view: SettingBarView, value: String) {
            if (!TextUtils.isEmpty(view.content) && view.content == value) {
                return
            }
            if (view.txtSetContent != null && !TextUtils.isEmpty(value)) {
                view.txtSetContent.setText(value)
            }
        }

        @InverseBindingAdapter(attribute = "content", event = "contentAttrChanged")
        fun getContent(view: SettingBarView): String? {
            return view.content
        }

        @BindingAdapter(value = ["contentAttrChanged"], requireAll = false)
        fun setDisplayAttrChanged(
            view: SettingBarView,
            inverseBindingListener: InverseBindingListener?
        ) {
            Log.d("MYTAG", "setDisplayAttrChanged")
            if (inverseBindingListener == null) {
                view.setViewChangeListener(null)
                Log.d("MYTAG", "setViewChangeListener(null)")
            } else {
                view.setViewChangeListener(object : OnViewChangeListener {
                    override fun onChange() {
                        Log.d("MYTAG", "setDisplayAttrChanged -> onChange")
                        inverseBindingListener.onChange()
                    }
                })
            }
        }

        @BindingAdapter(value = ["onClickSettingBarView"], requireAll = false)
        fun onClickSettingBarView(view: SettingBarView, bindingCommand: BindingCommand<*>?) {
            view.layoutSettingBar.setOnClickListener { bindingCommand?.execute() }
        }
    }

    init {
        inflate(context, R.layout.view_setting_bar, this)
        layoutSettingBar = findViewById(R.id.layout_setting_bar)
        imgLeftIcon = findViewById(R.id.img_left_icon)
        txtSetTitle = findViewById(R.id.txt_set_title)
        txtSetContent = findViewById(R.id.txt_set_content)
        imgRightIcon = findViewById(R.id.img_right_icon)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingBarView)
        val isVisableLeftIcon =
            typedArray.getBoolean(R.styleable.SettingBarView_set_left_icon_visable, false)
        val isVisableRightIcon =
            typedArray.getBoolean(R.styleable.SettingBarView_set_right_icon_visable, false)
        val title = typedArray.getString(R.styleable.SettingBarView_set_title)
        val hint = typedArray.getString(R.styleable.SettingBarView_set_content_hint)
        val content = typedArray.getString(R.styleable.SettingBarView_set_content)
        val rightIcon = typedArray.getResourceId(R.styleable.SettingBarView_set_right_icon, 0)
        val leftIcon = typedArray.getResourceId(R.styleable.SettingBarView_set_left_icon, 0)
        imgLeftIcon.visibility = if (isVisableLeftIcon) VISIBLE else GONE
        txtSetTitle.text = title
        txtSetContent.hint = hint
        txtSetContent.setText(content)
        imgRightIcon.visibility = if (isVisableRightIcon) VISIBLE else GONE
        if (leftIcon > 0) {
            imgLeftIcon.setImageResource(leftIcon)
        }
        if (rightIcon > 0) {
            imgRightIcon.setImageResource(rightIcon)
        }
        imgRightIcon.setOnClickListener {
            if (mOnClickRightImgListener != null) {
                mOnClickRightImgListener!!.onClick()
            }
        }
        layoutSettingBar.setOnClickListener {
            if (mOnClickSettingBarViewListener != null) {
                mOnClickSettingBarViewListener!!.onClick()
            }
        }
        txtSetContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.v("MYTAG", "onTextChanged start....")
                if (mOnViewChangeListener != null) {
                    mOnViewChangeListener!!.onChange()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
}