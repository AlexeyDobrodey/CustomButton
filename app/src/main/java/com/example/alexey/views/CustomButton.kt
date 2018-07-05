package com.example.alexey.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.alexey.customviewbutton.R


class CustomButton : CustomView {

    private var mCornerRadius = .0f

    private var mText: String? = null
    private var mTextSize = 0
    private var mMinTextSize = 0

    private var mWidthText = 0f
    private var mHeightText = 0f

    private lateinit var mPaint: Paint
    private lateinit var mTextPaint: TextPaint

    private var rectF = RectF()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun initAttributes(attrs: AttributeSet?) {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

        if (attrs == null) return

        val ta = context.obtainStyledAttributes(attrs, R.styleable.CustomButton)

        val bgColor = ta.getColor(R.styleable.CustomButton_cbBGColor, Color.GRAY)
        val textFontColor = ta.getColor(R.styleable.CustomButton_cbTextColor, Color.BLACK)

        mCornerRadius = ta.getDimension(R.styleable.CustomButton_cbCornerRadius, 0f)

        mText = ta.getString(R.styleable.CustomButton_cbText)

        mTextSize = ta.getDimensionPixelSize(R.styleable.CustomButton_cbTextSize, resources.getDimensionPixelSize(R.dimen.default_text_size))
        mMinTextSize = ta.getDimensionPixelSize(R.styleable.CustomButton_cbMinTextSize, resources.getDimensionPixelSize(R.dimen.min_default_text_size))

        mPaint.color = bgColor
        mTextPaint.color = textFontColor
        mTextPaint.textSize = mTextSize.toFloat()

        ta.recycle()
    }

    fun setTextData(text: String, minTextSize: Int, textSize: Int) {
        mText = text

        mMinTextSize = (minTextSize * resources.displayMetrics.density).toInt()
        mTextSize = (textSize * resources.displayMetrics.density).toInt()
        mTextPaint.textSize = mTextSize.toFloat()

        invalidate()
        requestLayout()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        calculateTextSize()

        val desiredWidth = mWidthText + paddingStart + paddingEnd
        val desiredHeight = mHeightText + paddingTop + paddingBottom

        val width = reconSize(desiredWidth.toInt(), widthMeasureSpec)
        val height = reconSize(desiredHeight.toInt(), heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (mWidthText > width) {
            runBinarySearchOptimalTextSize(width)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectF.set(0f, 0f, w.toFloat(), h.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(rectF, mCornerRadius, mCornerRadius, mPaint)

        if (mText.isNullOrEmpty()) return

        var bufferText = mText
        var bufferTextWidth = mWidthText

        if (mTextSize == mMinTextSize) {
            bufferText = TextUtils.ellipsize(mText, mTextPaint, width - 15f, TextUtils.TruncateAt.END).toString()
            bufferTextWidth = width.toFloat()
        }

        val posXText = (width - bufferTextWidth) / 2
        val posYText = (height + mHeightText) / 2
        canvas.drawText(bufferText, posXText, posYText, mTextPaint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean =
            when (event?.action) {
                null -> super.onTouchEvent(event)

                MotionEvent.ACTION_DOWN -> {
                    alpha = 0.8f
                    true
                }

                MotionEvent.ACTION_UP -> {
                    alpha = 1f
                    true
                }

                else -> super.onTouchEvent(event)
            }

    private fun calculateTextSize() {
        if (mText.isNullOrEmpty()) return

        val textBounds = Rect()
        mTextPaint.getTextBounds(mText, 0, mText!!.length, textBounds)
        mWidthText = textBounds.width().toFloat()
        mHeightText = textBounds.height().toFloat()
    }

    private fun runBinarySearchOptimalTextSize(widthView: Int) {
        val optimalMinWidthText = widthView - resources.getDimension(R.dimen.default_padding_button)

        var left = mMinTextSize
        var right = mTextSize

        while (true) {
            val mid = left + (right - left) / 2
            val widthText = getWidthTextByFontSize(mid)

            if ((widthText >= optimalMinWidthText && widthText <= widthView) || mid == mMinTextSize) {
                mTextSize = mid
                calculateTextSize()
                return
            }

            if (widthText > widthView) {
                right = mid
            } else {
                left = mid
            }
        }
    }

    private fun getWidthTextByFontSize(textSize: Int): Float {
        mTextPaint.textSize = textSize.toFloat()
        return mTextPaint.measureText(mText)
    }
}
