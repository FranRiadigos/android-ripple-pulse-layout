package com.kuassivi.component

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout


class RipplePulseRelativeLayout : RelativeLayout {

    /**
     * Constant values
     */
    companion object {
        private const val STROKE: Int = 1
        private const val FILL: Int = 0
        private const val ANGLE_360:Float = 360F
    }

    /**
     * Object that paints either an arc with a stroke or a circle with a fill color
     */
    private lateinit var ripplePaint: Paint

    /**
     * The bounds of the Paint object
     */
    private lateinit var rippleBounds: RectF

    /**
     * Animator set to apply and handle the different effects
     */
    private lateinit var animatorSet: AnimatorSet


    private var radius: Float = 0F
    private var fraction: Float = 0F

    /**
     * The ripple Color
     */
    private var _rippleColor: Int = 0
    var rippleColor: Int
        get() = _rippleColor
        set(value) {
            _rippleColor = value
            invalidatePaint()
        }

    /**
     * The pulse Type
     */
    private var _pulseType: Int = STROKE
    var pulseType: Int
        get() = _pulseType
        set(value) {
            _pulseType = value
            invalidatePaint()
        }

    /**
     * The ripple Stroke Width
     */
    private var _rippleStrokeWidth: Float = 0F
    var rippleStrokeWidth: Float
        get() = _rippleStrokeWidth
        set(value) {
            _rippleStrokeWidth = value
            invalidatePaint()
        }

    /**
     * The pulse animation Duration
     */
    private var _pulseDuration: Long = 0
    var pulseDuration: Long
        get() = _pulseDuration
        set(value) {
            _pulseDuration = value
           //startAnimator()
        }

    /**
     * The ripple Start Radius
     */
    private var _rippleStartRadius: Float = 0F
    var rippleStartRadius: Float
        get() = _rippleStartRadius
        set(value) {
            _rippleStartRadius = value
            //startAnimator()
        }

    /**
     * The ripple End Radius
     */
    private var _rippleEndRadius: Float = 100F
    var rippleEndRadius: Float
        get() = _rippleEndRadius
        set(value) {
            _rippleEndRadius = value
            //startAnimator()
        }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        attrs?.let { initAttributes(attrs, defStyle) }
    }

    init {
        _rippleColor = ContextCompat.getColor(context, R.color.com_chattylabs_component_color_green)
        _pulseDuration = resources.getInteger(android.R.integer.config_longAnimTime).toLong() * 10
        _rippleStrokeWidth = resources.getDimension(R.dimen.com_chattylabs_component_dimen_stroke)
        // This option is needed to get onDraw called
        // otherwise we would need to re-measure the bounds of this View
        setWillNotDraw(false)
    }

    private fun initAttributes(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.RipplePulseRelativeLayout, defStyle, 0)

        if (a.hasValue(R.styleable.RipplePulseRelativeLayout_rippleColor))
            _rippleColor = a.getColor(
                    R.styleable.RipplePulseRelativeLayout_rippleColor,
                    rippleColor)
        if (a.hasValue(R.styleable.RipplePulseRelativeLayout_pulseType))
            _pulseType = a.getInt(
                    R.styleable.RipplePulseRelativeLayout_pulseType,
                    pulseType)
        if (a.hasValue(R.styleable.RipplePulseRelativeLayout_pulseDuration))
            _pulseDuration = a.getInteger(
                    R.styleable.RipplePulseRelativeLayout_pulseDuration,
                    pulseDuration.toInt()).toLong()
        if (a.hasValue(R.styleable.RipplePulseRelativeLayout_rippleStrokeWidth))
            _rippleStrokeWidth = a.getDimension(
                    R.styleable.RipplePulseRelativeLayout_rippleStrokeWidth,
                    rippleStrokeWidth)
        if (a.hasValue(R.styleable.RipplePulseRelativeLayout_rippleStartRadius))
            _rippleStartRadius = a.getFloat(
                    R.styleable.RipplePulseRelativeLayout_rippleStartRadius,
                    rippleStartRadius)
        if (a.hasValue(R.styleable.RipplePulseRelativeLayout_rippleEndRadius))
            _rippleEndRadius = a.getFloat(
                    R.styleable.RipplePulseRelativeLayout_rippleEndRadius,
                    rippleEndRadius)

        a.recycle()

        // Set up a default Paint object
        ripplePaint = Paint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
        }
        // Set up a default Bounds object
        rippleBounds = RectF()
        // Set up a default AnimatorSet object
        animatorSet = AnimatorSet()

        // Initialize Paint and Bounds
        invalidatePaint()
        invalidateBounds()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        // Makes immediate parent to not clip children on its bounds
        // FIXME: Placing this option here does not work immediately
        (parent as? ViewGroup)?.clipChildren = false
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // We get a proper width and height measured here
        initBounds()
    }

    fun startPulse() {
        if (!animatorSet.isRunning) {
            startAnimator()
        }
    }

    fun stopPulse() {
        if (animatorSet.isRunning) {
            animatorSet.cancel()
            invalidate()
        }
    }

    private fun startAnimator() {
        val scale = ValueAnimator.ofFloat(_rippleStartRadius, _rippleEndRadius).apply {
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                radius = it.animatedValue as Float
                fraction = it.animatedFraction
                if (radius > 0) {
                    invalidateBounds()
                    invalidate()
                }
            }
            addListener(object: AnimatorListenerAdapter() {
                override fun onAnimationRepeat(animation: Animator?) {
                    initBounds()
                }
            })
        }

        val alpha = ValueAnimator.ofInt(255, 0).apply {
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                val alpha = it.animatedValue as Int
                ripplePaint.alpha = alpha
            }
        }

        animatorSet.apply {
            duration = _pulseDuration
            interpolator = DecelerateInterpolator()
            playTogether(scale, alpha)
        }
        animatorSet.start()
    }

    private fun invalidatePaint() {
        ripplePaint.apply {
            color = _rippleColor
            strokeWidth = if (_pulseType == STROKE) _rippleStrokeWidth else 0F
            style = if (_pulseType == FILL) Paint.Style.FILL else Paint.Style.STROKE
        }
    }

    private var rippleBoundsWidth: Float = 0F
    private var rippleBoundsHeight: Float = 0F

    private fun initBounds() {
        val halfWidth = (width/2)
        val halfHeight = (height/2)
        rippleBounds.apply {
            left = halfWidth - (halfWidth*(_rippleStartRadius/100.0f))
            top = halfHeight - (halfHeight*(_rippleStartRadius/100.0f))
            right = halfWidth + (halfWidth*(_rippleStartRadius/100.0f))
            bottom = halfHeight + (halfHeight*(_rippleStartRadius/100.0f))
        }
        rippleBoundsWidth = rippleBounds.width()
        rippleBoundsHeight = rippleBounds.height()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopPulse()
    }

    private fun invalidateBounds() {
        val incrementPercent: Float = if (radius > 0) (radius-_rippleStartRadius) else 0F
        val x = ((rippleBoundsWidth/2) * (incrementPercent / 100.0f))
        val y = ((rippleBoundsHeight/2) * (incrementPercent / 100.0f))
        // TODO: measure the amount needed on each iteration to be increased according to the
        // TODO: incrementPercent of the rippleBounds
        rippleBounds.inset(-10F, -10F)
    }

    override fun onDraw(canvas: Canvas?) {

       if (animatorSet.isRunning) {
            // Increases the clipping area progressively allowing the pulse animation
            // to be visible outside the parent View bounds
//            if (canvas != null) {
//                val newRect: Rect = canvas.clipBounds
//                if (radius > width) {
//                    newRect.inset(
//                            (width - radius).toInt(),
//                            (width - radius).toInt())
//                    canvas.clipRect(newRect, Region.Op.REPLACE)
//                    //canvas.clipOutRect(newRect)
//                }
//            }

            // Draws the desired pulse graphics
            if (_pulseType == STROKE) {
                canvas?.drawArc(rippleBounds, ANGLE_360, ANGLE_360, false, ripplePaint)
            } else {
                canvas?.drawCircle(rippleBounds.centerX(), rippleBounds.centerY(), rippleBounds.width() / 2, ripplePaint)
            }
        }

        // ImageView
        super.onDraw(canvas)
    }
}