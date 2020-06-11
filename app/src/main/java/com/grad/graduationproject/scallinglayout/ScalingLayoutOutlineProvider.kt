package com.grad.graduationproject.scallinglayout

import android.graphics.Outline
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class ScalingLayoutOutlineProvider internal constructor(
    var width: Int,
    var height: Int,
    var radius: Float
) : ViewOutlineProvider() {
    override fun getOutline(
        view: View,
        outline: Outline
    ) {
        outline.setRoundRect(0, 0, width, height, radius)
    }
}
