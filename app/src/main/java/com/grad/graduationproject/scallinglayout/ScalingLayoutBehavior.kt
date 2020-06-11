package com.grad.graduationproject.scallinglayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.grad.graduationproject.R

class ScalingLayoutBehavior(
    context: Context,
    attrs: AttributeSet?
) : CoordinatorLayout.Behavior<ScalingLayout>(context, attrs) {
    private val toolbarHeightInPixel: Float
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: ScalingLayout,
        dependency: View
    ): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: ScalingLayout,
        dependency: View
    ): Boolean {
        val totalScrollRange = (dependency as AppBarLayout).totalScrollRange
        child.setProgress(-dependency.getY() / totalScrollRange)
        if (totalScrollRange + dependency.getY() > child.measuredHeight.toFloat() / 2) {
            child.translationY =
                totalScrollRange +
                dependency.getY() +
                toolbarHeightInPixel -
                child.measuredHeight.toFloat() / 2
        } else {
            child.translationY = toolbarHeightInPixel
        }
        return super.onDependentViewChanged(parent, child, dependency)
    }

    init {
        toolbarHeightInPixel =
            context.resources.getDimensionPixelSize(R.dimen.sl_toolbar_size).toFloat()
    }
}
