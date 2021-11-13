package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import java.util.*
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var downloadingRectangle = Rect()
    private var downloadingCircle = RectF()
    private var downloadProgress = 0
    private var downloadingState = 0

    private val valueAnimator = ValueAnimator()

    private val paint = Paint().apply {
        isAntiAlias = true
        textAlignment = TEXT_ALIGNMENT_CENTER
        textSize = resources.getDimension(R.dimen.standard_text_size)
    }

    // Reference variables
    private lateinit var buttonText: String
    private var buttonOriginalColor = ContextCompat.getColor(context, R.color.colorSecondary)
    private var buttonDownloadingColor = ContextCompat.getColor(context, R.color.colorSecondaryDark)
    private var circleColor = ContextCompat.getColor(context, R.color.colorPrimaryLight)

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Complete) { _, _, new ->
        when (new) {
            ButtonState.Clicked -> {
                isEnabled = false

                // Start download
                defineButtonState(ButtonState.Downloading)
            }
            ButtonState.Downloading -> {
                // TODO set button text
                startAnimation()
            }
            ButtonState.Complete -> {
                stopAnimation()
                isEnabled = true
                // TODO reset button title to "download"
            }
        }
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonOriginalColor = getColor(R.styleable.LoadingButton_buttonOriginalColor, 0)
            buttonDownloadingColor = getColor(R.styleable.LoadingButton_buttonDownloadingColor, 0)
            circleColor = getColor(R.styleable.LoadingButton_circleColor, 0)
            buttonText = getString(R.styleable.LoadingButton_buttonText).toString()
        }
        buttonState = ButtonState.Complete
    }

    private fun defineButtonState(buttonState: ButtonState) {
        this.buttonState = buttonState
    }

    private fun startAnimation() {
        // TODO setup button animation
    }

    private fun stopAnimation() {
        defineButtonState(ButtonState.Complete)
        valueAnimator.cancel()
        downloadProgress = 0
        downloadingState = 0
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        with(canvas) {

            val textToBeDrawn = context.getString(buttonState.getTextId()).toUpperCase(Locale.ROOT)
            var textToBeDrawnOffSet = 0

            paint.getTextBounds(textToBeDrawn, 0, textToBeDrawn.length, downloadingRectangle)
            val textToBeDrawnX = width / 2f - downloadingRectangle.width() / 2f
            val textToBeDrawnY =
                height / 2f + downloadingRectangle.height() / 2f - downloadingRectangle.bottom

            paint.color = Color.WHITE
            drawText(textToBeDrawn, textToBeDrawnX - textToBeDrawnOffSet, textToBeDrawnY, paint)

            when (buttonState) {
                ButtonState.Complete -> {
                    // Restore saved canvas
                    restore()
                    save()

                    // Draw elements on screen
                    drawColor(buttonOriginalColor)
                    drawText(
                        context.getString(R.string.button_download),
                        width.toFloat() / 2,
                        (height.toFloat()) + (paint.textSize / 2),
                        paint
                    )
                }

                ButtonState.Downloading -> {
                    with(canvas) {
                        // Draw download progress rectangle bg color
                        paint.color = buttonDownloadingColor

                        if (downloadingState == 0) {
                            downloadingRectangle.set(0, 0, width * downloadProgress / 360, height)
                        } else {
                            downloadingRectangle.set(
                                width * downloadProgress / 360,
                                0,
                                width,
                                height
                            )
                        }
                        drawRect(downloadingRectangle, paint)

                        // Draw download progress circle
                        paint.style = Paint.Style.FILL
                        paint.color = circleColor

                        val circleInitialX = width / 2f + downloadingRectangle.width() / 2f
                        val circleInitialY = height / 2f - 20
                        downloadingCircle.set(
                            circleInitialX,
                            circleInitialY,
                            circleInitialX + 40,
                            circleInitialY + 40
                        )

                        if (downloadingState == 0) {
                            drawArc(downloadingCircle, 0f, downloadProgress.toFloat(), true, paint)
                        } else {
                            drawArc(
                                downloadingCircle,
                                downloadProgress.toFloat(),
                                360f - downloadProgress.toFloat(),
                                true,
                                paint
                            )
                        }
                        textToBeDrawnOffSet = 35
                    }
                }
                else -> return
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minW: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minW, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
}