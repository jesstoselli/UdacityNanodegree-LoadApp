package com.udacity

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.text.StaticLayout
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.math.abs
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var textWidth = 0f

    private var frame = Rect()

//    private var horizontalProgress = RectF() // former BOUNDS
//    private var valueAnimator = ValueAnimator()

    // Original color for background
    private var buttonOriginalColor = 0
    private val buttonOriginalPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("", Typeface.BOLD)
    }

    // Downloading progress color
    private var buttonDownloadingColor = 0
    private val buttonDownloadingPaint = Paint().apply {
        style = Paint.Style.FILL
    }

    // Text
    private var buttonText: String = resources.getString(R.string.button_download)
    private val buttonTextPaint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        textSize = 20f * resources.displayMetrics.scaledDensity
    }
    private var textHeight = 0f
    private var textOffset = 0f

    // Animation
//    private var downloadProgress = 0f
//        set(value) {
//            field = value
//            invalidate()
//        }
//    private var arcProgress = 0f
//        set(value) {
//            field = value
//            invalidate()
//        }

    // Circle
//    private var circleColor = 0
//    private val circlePaint = Paint()
//    private var arcDiameter: Float = 0f
//    private var arcMargin: Float = 0f

    // Processing different states of the button
    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Complete) { _, _, new ->
        when (new) {
            ButtonState.Downloading -> {
//                buttonText = resources.getString(R.string.button_downloading)
//                valueAnimator = ValueAnimator.ofFloat(0.0F, 1.0F).apply {
//                    duration = 1000L
//
//                    addUpdateListener {
//                        downloadProgress = animatedValue as Float * measuredWidth.toFloat()
//                        arcProgress = animatedValue as Float * 360F
//                    }
//                    start()
//                }
            }
            ButtonState.Complete -> {
//                buttonText = resources.getString(R.string.button_download)
//                valueAnimator.end()
            }
            ButtonState.Clicked -> {/* Does nothing */
            }
        }
    }

    // First time setup for the view
    init {
        isClickable = true

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonOriginalColor = getColor(R.styleable.LoadingButton_buttonOriginalColor, 0)
            buttonDownloadingColor = getColor(R.styleable.LoadingButton_buttonDownloadingColor, 0)
//            circleColor = getColor(R.styleable.LoadingButton_circleColor, 0)
        }

        buttonOriginalPaint.color = buttonOriginalColor
        buttonDownloadingPaint.color = buttonDownloadingColor
//        circlePaint.color = circleColor
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // First you gotta paint your canvas, in this case, the whole button frame
        canvas.drawColor(buttonOriginalColor)

        // Then draw text in original state
//        buttonTextPaint.getTextBounds(buttonText, 0, buttonText.length, frame)
        canvas.drawText(
            buttonText,
            width.toFloat() / 2,
            ((height / 2) - ((buttonTextPaint.descent() + buttonTextPaint.ascent()) / 2)),
            buttonTextPaint
        )


//        if (buttonState == ButtonState.Downloading) {
//            buttonText = resources.getString(R.string.button_downloading)
//            canvas.drawText(
//                buttonText,
//                (widthSize / 2).toFloat(),
//                (heightSize / 2).toFloat(),
//                buttonTextPaint
//            )
//        }

        // Button's background
//        canvas.drawRect(0f,0f, widthSize.toFloat(), heightSize.toFloat(), buttonOriginalPaint)

//        if (buttonState == ButtonState.Downloading){
        // Draw the horizontal progress over original button
//            canvas.drawRect(0f, 0f, downloadProgress, heightSize.toFloat(), buttonDownloadingPaint)

        // Draw oval within the given angles
//            canvas.drawArc(
//                widthSize - arcDiameter - arcMargin,
//                arcMargin,
//                widthSize - arcMargin,
//                arcMargin + arcDiameter,
//                0F,
//                arcProgress,
//                true,
//                arcPaint
//            )

//        }

        // Draw text
//        canvas.drawText(buttonText, horizontalProgress.centerX(), horizontalProgress.centerY() + textOffset, buttonTextPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h

//        textHeight = (buttonTextPaint.descent().toDouble() - buttonTextPaint.ascent()).toFloat()
//        textOffset = (textHeight / 2) - buttonTextPaint.descent()
//
//        arcDiameter = heightSize * 0.6F
//        arcMargin = heightSize * 0.1F
//
//        horizontalProgress.right = widthSize.toFloat()
//        horizontalProgress.bottom = heightSize.toFloat()

        setMeasuredDimension(w, h)
    }

    fun downloadComplete() {
//        stopAnimation()
        defineButtonState(ButtonState.Complete)
    }

//    private fun stopAnimation() {
//        defineButtonState(ButtonState.Complete)
//        valueAnimator.cancel()
//        downloadProgress = 0
//        downloadingState = 0
//    }

    // Changing state of LoadingButton
    fun defineButtonState(buttonState: ButtonState) {
        this.buttonState = buttonState
    }

//    fun drawTextCentred(canvas: Canvas, paint: Paint, text: String, cx: Float, cy: Float) {
//        paint.getTextBounds(text, 0, text.length, textBounds)
//        canvas.drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint)
//    }

    private fun getTextCenterToDraw(text: String, region: RectF, paint: Paint): PointF {
        val textBounds = Rect()
        paint.getTextBounds(text, 0, text.length, textBounds)
        val x = region.centerX() - textBounds.width() * 0.4f
        val y = region.centerY() + textBounds.height() * 0.4f
        return PointF(x, y)
    }
}