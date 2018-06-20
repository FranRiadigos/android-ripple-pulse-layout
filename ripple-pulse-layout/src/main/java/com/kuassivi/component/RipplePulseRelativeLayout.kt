package com.kuassivi.component

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout


class RipplePulseRelativeLayout : RelativeLayout {

    /**
     * Constant values
     */
    companion object {
        private const val STROKE = 1
        private const val FILL = 0
        private const val ANGLE_360 = 360F
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

    /**
     * The updated radius & fraction
     */
    private var radius = 0F
    private var fraction = 0F

    /**
     * The ripple Color
     */
    private var _rippleColor = 0
    var rippleColor
        get() = _rippleColor
        set(value) {
            _rippleColor = value
            invalidatePaint()
        }

    /**
     * The pulse Type
     */
    private var _pulseType = STROKE
    var pulseType
        get() = _pulseType
        set(value) {
            _pulseType = value
            invalidatePaint()
        }

    /**
     * The ripple Stroke Width
     */
    private var _rippleStrokeWidth = 0F
    var rippleStrokeWidth
        get() = _rippleStrokeWidth
        set(value) {
            _rippleStrokeWidth = value
            invalidatePaint()
        }

    /**
     * The pulse animation Duration
     */
    private var _pulseDuration = 0
    var pulseDuration
        get() = _pulseDuration
        set(value) {
            _pulseDuration = value
            stopPulse()
            startPulse()
        }

    /**
     * The pulse animation startDelay
     */
    private var _startDelay = 0
    var startDelay
        get() = _startDelay
        set(value) {
            _startDelay = value
            stopPulse()
            startPulse()
        }

    /**
     * The pulse animation endDelay
     */
    private var _endDelay = 0
    var endDelay
        get() = _endDelay
        set(value) {
            _endDelay = value
            stopPulse()
            startPulse()
        }

    /**
     * The ripple Start Radius
     */
    private var _rippleStartRadiusPercent = 0F
    var rippleStartRadiusPercent
        get() = _rippleStartRadiusPercent
        set(value) {
            _rippleStartRadiusPercent = value
            stopPulse()
            startPulse()
        }

    /**
     * The ripple End Radius
     */
    private var _rippleEndRadiusPercent = 150F
    var rippleEndRadiusPercent
        get() = _rippleEndRadiusPercent
        set(value) {
            _rippleEndRadiusPercent = value
            stopPulse()
            startPulse()
        }

    /**
     * The Interpolator to use on for the pulse
     */
    private var _pulseInterpolator = android.R.anim.decelerate_interpolator
    var pulseInterpolator
        get() = _pulseInterpolator
        set(value) {
            _pulseInterpolator = value
            stopPulse()
            startPulse()
        }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        attrs?.let { initAttributes(it, defStyle) }
    }

    init {
        _rippleColor = ContextCompat.getColor(context, R.color.com_chattylabs_component_color_green)
        _pulseDuration = resources.getInteger(android.R.integer.config_longAnimTime)
        _rippleStrokeWidth = resources.getDimension(R.dimen.com_chattylabs_component_dimen_stroke)
        // This option is needed to get onDraw called
        // otherwise we would need to re-measure the bounds of this View
        setWillNotDraw(false)
    }

    private fun initAttributes(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.RipplePulseRelativeLayout, defStyle, 0)

        val typedValue = TypedValue()

        if (a.getValue(R.styleable.RipplePulseRelativeLayout_rippleColor, typedValue))
            _rippleColor = typedValue.data

        if (a.getValue(R.styleable.RipplePulseRelativeLayout_pulseDuration, typedValue))
            _pulseDuration = typedValue.data

        if (a.getValue(R.styleable.RipplePulseRelativeLayout_rippleStrokeWidth, typedValue))
            _rippleStrokeWidth = typedValue.float

        if (a.getValue(R.styleable.RipplePulseRelativeLayout_startDelay, typedValue))
            _startDelay = typedValue.data

        if (a.getValue(R.styleable.RipplePulseRelativeLayout_endDelay, typedValue))
            _endDelay = typedValue.data

        if (a.hasValue(R.styleable.RipplePulseRelativeLayout_pulseType))
            _pulseType = a.getInt(
                    R.styleable.RipplePulseRelativeLayout_pulseType,
                    pulseType)

        if (a.hasValue(R.styleable.RipplePulseRelativeLayout_rippleStartRadiusPercent))
            _rippleStartRadiusPercent = a.getFloat(
                    R.styleable.RipplePulseRelativeLayout_rippleStartRadiusPercent,
                    rippleStartRadiusPercent)

        if (a.hasValue(R.styleable.RipplePulseRelativeLayout_rippleEndRadiusPercent))
            _rippleEndRadiusPercent = a.getFloat(
                    R.styleable.RipplePulseRelativeLayout_rippleEndRadiusPercent,
                    rippleEndRadiusPercent)

        if (a.hasValue(R.styleable.RipplePulseRelativeLayout_pulseInterpolator))
            _pulseInterpolator = a.getResourceId(
                    R.styleable.RipplePulseRelativeLayout_pulseInterpolator,
                    pulseInterpolator)

        a.recycle()

        // Set up a default Paint object
        ripplePaint = Paint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
        }

        // Set up a default Bounds object
        rippleBounds = RectF()

        // Set up a default AnimatorSet object
        animatorSet = AnimatorSet()

        // Initialize Paint
        invalidatePaint()
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
        val scale = ValueAnimator.ofFloat(_rippleStartRadiusPercent, _rippleEndRadiusPercent).apply {
            addUpdateListener {
                radius = it.animatedValue as Float
                fraction = it.animatedFraction
                if (radius > 0) {
                    invalidate(rippleBounds, radius)
                    invalidate()
                }
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    postDelayed({
                        if (!animatorSet.isRunning) {
                            invalidate(rippleBounds, _rippleStartRadiusPercent)
                            animatorSet.start()
                        }
                    }, _endDelay.toLong())
                }
            })
        }

        val alpha = ValueAnimator.ofInt(255, 0).apply {
            addUpdateListener {
                val alpha = it.animatedValue as Int
                ripplePaint.alpha = alpha
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    ripplePaint.alpha = 255
                }
            })
        }

        animatorSet.apply {
            duration = _pulseDuration.toLong()
            startDelay = _startDelay.toLong()
            interpolator = AnimationUtils.loadInterpolator(context, _pulseInterpolator)
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

    private fun invalidate(bounds: RectF, radius: Float) {
        val halfWidth = (width / 2)
        val halfHeight = (height / 2)
        bounds.apply {
            left = halfWidth - (halfWidth * (radius / 100.0f))
            top = halfHeight - (halfHeight * (radius / 100.0f))
            right = halfWidth + (halfWidth * (radius / 100.0f))
            bottom = halfHeight + (halfHeight * (radius / 100.0f))
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopPulse()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (isInEditMode) {
            invalidate(rippleBounds, _rippleStartRadiusPercent)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Makes immediate parent to not clip children on its bounds
        (parent as? ViewGroup)?.clipChildren = false
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (animatorSet.isRunning || isInEditMode) {

            drawPulse(canvas, rippleBounds, ripplePaint)

//            if (isInEditMode) {
//                val paint = Paint(ripplePaint)
//                paint.alpha = 20
//                val bounds = RectF(rippleBounds)
//                invalidate(bounds, _rippleEndRadiusPercent)
//                drawPulse(canvas, bounds, paint)
//            }
        }
    }

    private fun drawPulse(canvas: Canvas?, bounds: RectF, paint: Paint) {
        // Draws the desired pulse graphics according to the pulse Type
        if (_pulseType == STROKE) {
            canvas?.drawArc(bounds, ANGLE_360, ANGLE_360, false, paint)
        } else {
            canvas?.drawCircle(bounds.centerX(), bounds.centerY(), bounds.width() / 2, paint)
        }
    }
}