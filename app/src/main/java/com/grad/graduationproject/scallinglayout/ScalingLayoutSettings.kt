package com.grad.graduationproject.scallinglayout

import android.content.Context
import android.util.AttributeSet
import com.grad.graduationproject.R

class ScalingLayoutSettings(
    context: Context,
    attributeSet: AttributeSet?
) {
    private var radiusFactor: Float
    var initialWidth = 0
        private set
    val maxWidth: Int
    var maxRadius = 0f
        private set
    var elevation = 0f
    var isInitialized = false
        private set

    fun initialize(width: Int, height: Int) {
        if (!isInitialized) {
            isInitialized = true
            initialWidth = width
            val radiusLimit = height / 2.toFloat()
            maxRadius = radiusLimit * radiusFactor
        }
    }

    companion object {
        private const val DEFAULT_RADIUS_FACTOR = 1.0f
    }

    init {
        val typedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.ScalingLayout)
        radiusFactor = typedArray.getFloat(
            R.styleable.ScalingLayout_radiusFactor,
            DEFAULT_RADIUS_FACTOR
        )
        maxWidth = context.resources.displayMetrics.widthPixels
        typedArray.recycle()
        if (radiusFactor > DEFAULT_RADIUS_FACTOR) {
            radiusFactor = DEFAULT_RADIUS_FACTOR
        }
    }
}
