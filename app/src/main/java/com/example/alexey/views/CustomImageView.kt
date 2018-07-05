package com.example.alexey.views

import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import com.example.alexey.customviewbutton.R
import com.example.alexey.views.CustomImageView.CropShape.*

class CustomImageView : CustomView {


    enum class CropShape {
        CIRCLE, STAR, EIGHT, NOTHING
    }

    companion object {
        const val NOT_USED = -1
    }

    private var idCropShape: Int = 0
    lateinit var cropShape: CropShape
    private var bgImage: Bitmap? = null
    private lateinit var paint: Paint
    private lateinit var path: Path

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun initAttributes(attrs: AttributeSet?) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView)


        idCropShape = typeArray.getInt(R.styleable.CustomImageView_civCropShape, NOT_USED)
        setCropShape(idCropShape)

        val imageResource = typeArray.getResourceId(R.styleable.CustomImageView_civSrc, NOT_USED)

        bgImage = BitmapFactory.decodeResource(resources, imageResource)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        path = Path()

        typeArray.recycle()
    }

    fun changeCrop() {

        idCropShape++
        if (idCropShape > 2) idCropShape = 0

        setCropShape(idCropShape)
        invalidate()
    }

    private fun setCropShape(idCropShape: Int) {
        when (idCropShape) {
            0 -> {
                cropShape = CIRCLE
            }

            1 -> {
                cropShape = STAR
            }

            2 -> {
                cropShape = EIGHT
            }

            else -> cropShape = NOTHING
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val desireWidth = if (bgImage == null) 0 else bgImage!!.width
        val desireHeight = if (bgImage == null) 0 else bgImage!!.height

        val w = reconSize(desireWidth, widthMeasureSpec)
        val h = reconSize(desireHeight, heightMeasureSpec)

        setMeasuredDimension(w, h)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (bgImage == null) return

        bgImage = Bitmap.createScaledBitmap(bgImage, w, h, true)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null || bgImage == null) return


        path.reset()
        when (cropShape) {
            CIRCLE -> {
                cropCircle(width.toFloat(), height.toFloat())
            }

            STAR -> {
                cropStar(width.toFloat(), height.toFloat())
            }

            EIGHT -> {
                cropEight(width.toFloat(), height.toFloat())
            }
            NOTHING -> TODO()
        }

        if (cropShape != NOTHING) canvas.clipPath(path)

        canvas.drawBitmap(bgImage, 0f, 0f, paint)
    }

    private fun cropCircle(width: Float, height: Float) {
        val radius = Math.min(width / 2, height / 2)
        path.addCircle(width / 2, height / 2, radius, Path.Direction.CW)
    }

    private fun cropStar(width: Float, height: Float) {
        val pointOne = PointF(width / 2, 0f)
        val pointTwo = PointF(width, height)
        val pointThree = PointF(0f, height / 2)
        val pointFour = PointF(width, height / 2)
        val pointFive = PointF(0f, height)

        path.moveTo(pointOne.x, pointOne.y)
        path.lineTo(pointTwo.x, pointTwo.y)
        path.lineTo(pointThree.x, pointThree.y)
        path.lineTo(pointFour.x, pointFour.y)
        path.lineTo(pointFive.x, pointFive.y)
        path.close()
    }

    private fun cropEight(width: Float, height: Float) {
        path.moveTo(width / 2, height / 2)

        path.cubicTo(width, 0f, 0f, 0f, width / 2, height / 2)

        path.moveTo(width / 2, height / 2)

        path.cubicTo(0f, height, width, height, width / 2, height / 2)
    }

}