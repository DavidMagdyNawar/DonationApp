package com.grad.graduationproject.scallinglayout

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.view.ViewCompat

@Suppress("TooManyFunctions")
class ScalingLayout : FrameLayout {
    /**
     * Provides current [ScalingLayoutSettings]
     *
     * @return
     */
    /**
     * Settings
     */
    var settings: ScalingLayoutSettings? = null

    /**
     * Current radius
     */
    private var currentRadius = 0f

    /**
     * Width is dependent value. It depends on
     * radius. If radius gets updated,
     * layout width will be updated according to this change.
     */
    private var currentWidth = 0

    /**
     * If layout has margins, margin has to be change
     * according to radius.
     */
    private var maxMargins: FloatArray? = null
    private lateinit var currentMargins: FloatArray

    /**
     * get current state of layout
     *
     * @return
     */
    /**
     * State for layout.
     */
    var state: State? = null
        private set

    /**
     * Values to draw rounded on layout
     */
    private var path: Path? = null
    private var outlinePath: Path? = null
    lateinit var rectF: RectF
    private var maskPaint: Paint? = null

    /**
     * Animator to expand and collapse
     */
    private var valueAnimator: ValueAnimator? = null

    /**
     * Listener to notify observer about
     * progress and collapse/expand
     */
    private var scalingLayoutListener: ScalingLayoutListener? = null

    /**
     * CustomOutline for elevation shadows
     */
    @Nullable
    private var viewOutline: ScalingLayoutOutlineProvider? = null

    constructor(@NonNull context: Context?) : super(context!!) {
        init(context, null)
    }

    constructor(
        @NonNull context: Context?,
        @Nullable attrs: AttributeSet?
    ) : super(context!!, attrs) {
        init(context, attrs)
    }

    constructor(
        @NonNull context: Context?,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context!!, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @SuppressLint("NewApi")
    constructor(
        @NonNull context: Context?,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context!!, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    /**
     * Initialize layout
     *
     * @param context
     * @param attributeSet
     */

    fun init(
        context: Context?,
        attributeSet: AttributeSet?
    ) {
        settings = ScalingLayoutSettings(context!!, attributeSet)
        settings!!.elevation = ViewCompat.getElevation(this)
        state = State.COLLAPSED
        path = Path()
        outlinePath = Path()
        rectF = RectF(0f, 0f, 0f, 0f)
        maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        maskPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
        valueAnimator = ValueAnimator.ofFloat(0f, 0f)
        valueAnimator!!.duration = Companion.duration
        valueAnimator!!.addUpdateListener(
            AnimatorUpdateListener
            { valueAnimator ->
                setRadius(
                    valueAnimator.animatedValue as Float
                )
            }
        )
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (maxMargins == null) {
            maxMargins = FloatArray(Companion.four)
            currentMargins = FloatArray(Companion.four)
            val marginLayoutParams = layoutParams as MarginLayoutParams
            maxMargins!![Companion.zero] = marginLayoutParams.leftMargin.toFloat()
            currentMargins[Companion.zero] = maxMargins!![Companion.zero]
            maxMargins!![Companion.one] = marginLayoutParams.topMargin.toFloat()
            currentMargins[Companion.one] = maxMargins!![Companion.one]
            maxMargins!![Companion.two] = marginLayoutParams.rightMargin.toFloat()
            maxMargins!![Companion.three] = marginLayoutParams.bottomMargin.toFloat()
            currentMargins[Companion.two] = maxMargins!![Companion.two]
            currentMargins[Companion.three] = maxMargins!![Companion.three]
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (!settings!!.isInitialized) {
            settings!!.initialize(w, h)
            currentWidth = w
            currentRadius = settings!!.maxRadius
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewOutline = ScalingLayoutOutlineProvider(w, h, currentRadius)
            }
        }
        rectF!![0f, 0f, w.toFloat()] = h.toFloat()
        updateViewOutline(h, currentWidth, currentRadius)
        invalidate()
    }

    override fun dispatchDraw(canvas: Canvas) {
        val save = canvas.save()
        super.dispatchDraw(canvas)
        canvas.restoreToCount(save)
        path!!.reset()
        path!!.addRoundRect(rectF, currentRadius, currentRadius, Path.Direction.CCW)
        canvas.drawPath(path!!, maskPaint!!)
    }

    /**
     * Expand layout to screen
     */
    fun expand() {
        valueAnimator!!.setFloatValues(settings!!.maxRadius, 0f)
        valueAnimator!!.start()
    }

    /**
     * Collapse layout to initial position
     */
    fun collapse() {
        valueAnimator!!.setFloatValues(0f, settings!!.maxRadius)
        valueAnimator!!.start()
    }

    /**
     * This method takes a progress parameter value
     * between 0.0f and 1.0f. And apply this
     * progress value to layout.
     *
     * @param progress
     */
    fun setProgress(progress: Float) {
        if (progress > 1.0f || progress < 0.0f) {
            return
        }
        setRadius(settings!!.maxRadius - settings!!.maxRadius * progress)
    }

    fun setListener(scalingLayoutListener: ScalingLayoutListener?) {
        this.scalingLayoutListener = scalingLayoutListener
    }

    /**
     * Updates view outline borders and radius
     *
     * @param height
     * @param width
     * @param radius
     */

    private fun updateViewOutline(
        height: Int,
        width: Int,
        radius: Float
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && ViewCompat.getElevation(
            this
        ) > 0f
        ) {
            try {
                viewOutline!!.height = height
                viewOutline!!.width = width
                viewOutline!!.radius = radius
                setOutlineProvider(viewOutline)
            } catch (exception: PackageManager.NameNotFoundException ) {
                exception.printStackTrace()
            }
        }
    }

    /**
     * Set radius will update layout radius
     * Also layouts margins and width depend on
     * radius. So If you update radius, your layout's width
     * and margins will be updated.
     *
     * @param radius
     */

    private fun setRadius(radius: Float) {
        if (radius < 0) {
            return
        }
        updateCurrentRadius(radius)
        updateCurrentWidth(currentRadius)
        updateCurrentMargins(currentRadius)
        updateState(currentRadius)
        updateCurrentElevation()
        layoutParams.width = currentWidth
        (layoutParams as MarginLayoutParams)
            .setMargins(
                currentMargins[Companion.zero].toInt(),
                currentMargins[Companion.one].toInt(),
                currentMargins[Companion.two].toInt(),
                currentMargins[Companion.three].toInt()
            )
        requestLayout()
    }

    /**
     * Update current radius
     *
     * @param radius
     */
    private fun updateCurrentRadius(radius: Float) {
        currentRadius = if (radius < settings!!.maxRadius) radius else settings!!.maxRadius
    }

    /**
     * Update layout width with given radius value
     *
     * @param currentRadius
     */
    private fun updateCurrentWidth(currentRadius: Float) {
        val diffPixel = settings!!.maxWidth - settings!!.initialWidth
        val calculatedWidth =
            diffPixel - currentRadius * diffPixel / settings!!.maxRadius + settings!!.initialWidth
        currentWidth = if (calculatedWidth > settings!!.maxWidth) {
            settings!!.maxWidth
        } else if (calculatedWidth < settings!!.initialWidth) {
            settings!!.initialWidth
        } else {
            calculatedWidth.toInt()
        }
    }

    /**
     * Update layout margins with given radius value
     *
     * @param currentRadius
     */

    private fun updateCurrentMargins(currentRadius: Float) {
        currentMargins[Companion.zero] = maxMargins!![Companion.zero] * currentRadius / settings!!.maxRadius
        currentMargins[Companion.one] = maxMargins!![Companion.one] * currentRadius / settings!!.maxRadius
        currentMargins[Companion.two] = maxMargins!![Companion.two] * currentRadius / settings!!.maxRadius
        currentMargins[Companion.three] = maxMargins!![Companion.three] * currentRadius / settings!!.maxRadius
    }

    /**
     * Updates layout state
     *
     * @param currentRadius
     */
    private fun updateState(currentRadius: Float) {
        state = if (currentRadius == 0f) {
            State.EXPANDED
        } else if (currentRadius == settings!!.maxRadius) {
            State.COLLAPSED
        } else {
            State.PROGRESSING
        }
        notifyListener()
    }

    private fun updateCurrentElevation() {
        ViewCompat.setElevation(this, settings!!.elevation)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && ViewCompat.getElevation(
            this
        ) > 0f
        ) {
            try {
                outlineProvider = outlineProvider
            } catch (exception: PackageManager.NameNotFoundException ) {
                exception.printStackTrace()
            }
        }
    }

    /**
     * Notify observers about change
     */
    private fun notifyListener() {
        if (scalingLayoutListener != null) {
            if (state === State.COLLAPSED) {
                scalingLayoutListener!!.onCollapsed()
            } else if (state === State.EXPANDED) {
                scalingLayoutListener!!.onExpanded()
            } else {
                scalingLayoutListener!!.onProgress(currentRadius / settings!!.maxRadius)
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getOutlineProvider(): ViewOutlineProvider {
        return object : ViewOutlineProvider() {
            override fun getOutline(
                view: View,
                outline: Outline
            ) {
                outline.setConvexPath(path!!)
            }
        }
    }
    companion object {
        private const val duration = 200L
        private const val zero = 0
        private const val one = 1
        private const val two = 2
        private const val three = 3
        private const val four = 4

    }}
