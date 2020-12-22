package com.zky.basics.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.Paint.Join
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Pair
import android.widget.TextView
import com.zky.basics.common.R
import java.util.*

/**
 * Description: <描边效果的TextView><br></br>
</描边效果的TextView> */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
@SuppressLint("AppCompatCustomView")
class MagicTextView : TextView {
    private var outerShadows: ArrayList<Shadow>? = null
    private var innerShadows: ArrayList<Shadow>? = null
    private var canvasStore: WeakHashMap<String, Pair<Canvas, Bitmap?>>? = null
    private var tempCanvas: Canvas? = null
    private var tempBitmap: Bitmap? = null
    private var foregroundDrawable: Drawable? = null
    private var strokeWidth = 0f
    private var strokeColor: Int? = null
    private var strokeJoin: Join? = null
    private var strokeMiter = 0f
    private lateinit var lockedCompoundPadding: IntArray
    private var frozen = false

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs)
    }

    fun init(attrs: AttributeSet?) {
        outerShadows = ArrayList()
        innerShadows = ArrayList()
        if (canvasStore == null) {
            canvasStore = WeakHashMap()
        }
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.MagicTextView)
            val typefaceName = a.getString(R.styleable.MagicTextView_typeface)
            if (typefaceName != null) {
                val tf = Typeface.createFromAsset(
                    context.assets,
                    String.format("fonts/%s.ttf", typefaceName)
                )
                typeface = tf
            }
            if (a.hasValue(R.styleable.MagicTextView_foreground)) {
                val foreground = a.getDrawable(R.styleable.MagicTextView_foreground)
                if (foreground != null) {
                    setForegroundDrawable(foreground)
                } else {
                    this.setTextColor(a.getColor(R.styleable.MagicTextView_foreground, -0x1000000))
                }
            }
            if (a.hasValue(R.styleable.MagicTextView_backgroundcolor)) {
                val background = a.getDrawable(R.styleable.MagicTextView_backgroundcolor)
                if (background != null) {
                    setBackgroundDrawable(background)
                } else {
                    setBackgroundColor(
                        a.getColor(R.styleable.MagicTextView_backgroundcolor, -0x1000000)
                    )
                }
            }
            if (a.hasValue(R.styleable.MagicTextView_innerShadowColor)) {
                addInnerShadow(
                    a.getDimensionPixelSize(R.styleable.MagicTextView_innerShadowRadius, 0)
                        .toFloat(),
                    a.getDimensionPixelOffset(R.styleable.MagicTextView_innerShadowDx, 0).toFloat(),
                    a.getDimensionPixelOffset(R.styleable.MagicTextView_innerShadowDy, 0).toFloat(),
                    a.getColor(R.styleable.MagicTextView_innerShadowColor, -0x1000000)
                )
            }
            if (a.hasValue(R.styleable.MagicTextView_outerShadowColor)) {
                addOuterShadow(
                    a.getDimensionPixelSize(R.styleable.MagicTextView_outerShadowRadius, 0)
                        .toFloat(),
                    a.getDimensionPixelOffset(R.styleable.MagicTextView_outerShadowDx, 0).toFloat(),
                    a.getDimensionPixelOffset(R.styleable.MagicTextView_outerShadowDy, 0).toFloat(),
                    a.getColor(R.styleable.MagicTextView_outerShadowColor, -0x1000000)
                )
            }
            if (a.hasValue(R.styleable.MagicTextView_strokeColor)) {
                val strokeWidth =
                    a.getDimensionPixelSize(R.styleable.MagicTextView_strokeWidth, 1).toFloat()
                val strokeColor = a.getColor(R.styleable.MagicTextView_strokeColor, -0x1000000)
                val strokeMiter =
                    a.getDimensionPixelSize(R.styleable.MagicTextView_strokeMiter, 10).toFloat()
                var strokeJoin: Join? = null
                when (a.getInt(R.styleable.MagicTextView_strokeJoinStyle, 0)) {
                    0 -> strokeJoin = Join.MITER
                    1 -> strokeJoin = Join.BEVEL
                    2 -> strokeJoin = Join.ROUND
                }
                this.setStroke(strokeWidth, strokeColor, strokeJoin, strokeMiter)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
            && (innerShadows!!.size > 0 || foregroundDrawable != null)
        ) {
            setLayerType(LAYER_TYPE_SOFTWARE, null)
        }
    }

    fun setStroke(width: Float, color: Int, join: Join?, miter: Float) {
        strokeWidth = width
        strokeColor = color
        strokeJoin = join
        strokeMiter = miter
    }

    fun setStroke(width: Float, color: Int) {
        setStroke(width, color, Join.MITER, 10f)
    }

    fun addOuterShadow(_r: Float, dx: Float, dy: Float, color: Int) {
        var r = _r
        if (r == 0f) {
            r = 0.0001f
        }
        outerShadows!!.add(Shadow(r, dx, dy, color))
    }

    fun addInnerShadow(_r: Float, dx: Float, dy: Float, color: Int) {
        var r = _r
        if (r == 0f) {
            r = 0.0001f
        }
        innerShadows!!.add(Shadow(r, dx, dy, color))
    }

    fun clearInnerShadows() {
        innerShadows!!.clear()
    }

    fun clearOuterShadows() {
        outerShadows!!.clear()
    }

    fun setForegroundDrawable(d: Drawable?) {
        foregroundDrawable = d
    }

    override fun getForeground(): Drawable {
        return if (foregroundDrawable == null) foregroundDrawable!! else ColorDrawable(this.currentTextColor)
    }

    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        freeze()
        val restoreBackground = this.background
        val restoreDrawables = this.compoundDrawables
        val restoreColor = this.currentTextColor
        setCompoundDrawables(null, null, null, null)
        for (shadow in outerShadows!!) {
            setShadowLayer(shadow.r, shadow.dx, shadow.dy, shadow.color)
            super.onDraw(canvas)
        }
        setShadowLayer(0f, 0f, 0f, 0)
        this.setTextColor(restoreColor)
        if (foregroundDrawable != null && foregroundDrawable is BitmapDrawable) {
            generateTempCanvas()
            super.onDraw(tempCanvas)
            val paint = (foregroundDrawable as BitmapDrawable?)!!.paint
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
            foregroundDrawable?.bounds = canvas.clipBounds
            foregroundDrawable?.draw(tempCanvas)
            canvas.drawBitmap(tempBitmap, 0f, 0f, null)
            tempCanvas!!.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        }
        if (strokeColor != null) {
            val paint = this.paint
            paint.style = Paint.Style.STROKE
            paint.strokeJoin = strokeJoin
            paint.strokeMiter = strokeMiter
            this.setTextColor(strokeColor!!)
            paint.strokeWidth = strokeWidth
            super.onDraw(canvas)
            paint.style = Paint.Style.FILL
            this.setTextColor(restoreColor)
        }
        if (innerShadows!!.size > 0) {
            generateTempCanvas()
            val paint = this.paint
            for (shadow in innerShadows!!) {
                this.setTextColor(shadow.color)
                super.onDraw(tempCanvas)
                this.setTextColor(-0x1000000)
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
                paint.maskFilter = BlurMaskFilter(shadow.r, BlurMaskFilter.Blur.NORMAL)
                tempCanvas?.save()
                tempCanvas?.translate(shadow.dx, shadow.dy)
                super.onDraw(tempCanvas)
                tempCanvas?.restore()
                canvas.drawBitmap(tempBitmap, 0f, 0f, null)
                tempCanvas!!.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                paint.xfermode = null
                paint.maskFilter = null
                this.setTextColor(restoreColor)
                setShadowLayer(0f, 0f, 0f, 0)
            }
        }
        this.setCompoundDrawablesWithIntrinsicBounds(
            restoreDrawables[0], restoreDrawables[1],
            restoreDrawables[2], restoreDrawables[3]
        )
        setBackgroundDrawable(restoreBackground)
        this.setTextColor(restoreColor)
        unfreeze()
    }

    private fun generateTempCanvas() {
        val key = String.format("%dx%d", width, height)
        val stored = canvasStore!![key]
        if (stored != null) {
            tempCanvas = stored.first
            tempBitmap = stored.second
        } else {
            tempCanvas = Canvas()
            tempBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            tempCanvas!!.setBitmap(tempBitmap)
            canvasStore!![key] = Pair(tempCanvas, tempBitmap)
        }
    }

    // Keep these things locked while onDraw in processing
    fun freeze() {
        lockedCompoundPadding = intArrayOf(
            compoundPaddingLeft, compoundPaddingRight,
            compoundPaddingTop, compoundPaddingBottom
        )
        frozen = true
    }

    fun unfreeze() {
        frozen = false
    }

    override fun requestLayout() {
        if (!frozen) super.requestLayout()
    }

    override fun postInvalidate() {
        if (!frozen) super.postInvalidate()
    }

    override fun postInvalidate(left: Int, top: Int, right: Int, bottom: Int) {
        if (!frozen) super.postInvalidate(left, top, right, bottom)
    }

    override fun invalidate() {
        if (!frozen) super.invalidate()
    }

    override fun invalidate(rect: Rect) {
        if (!frozen) super.invalidate(rect)
    }

    override fun invalidate(l: Int, t: Int, r: Int, b: Int) {
        if (!frozen) super.invalidate(l, t, r, b)
    }

    override fun getCompoundPaddingLeft(): Int {
        return if (!frozen) super.getCompoundPaddingLeft() else lockedCompoundPadding[0]
    }

    override fun getCompoundPaddingRight(): Int {
        return if (!frozen) super.getCompoundPaddingRight() else lockedCompoundPadding[1]
    }

    override fun getCompoundPaddingTop(): Int {
        return if (!frozen) super.getCompoundPaddingTop() else lockedCompoundPadding[2]
    }

    override fun getCompoundPaddingBottom(): Int {
        return if (!frozen) super.getCompoundPaddingBottom() else lockedCompoundPadding[3]
    }

    class Shadow(var r: Float, var dx: Float, var dy: Float, var color: Int)
}