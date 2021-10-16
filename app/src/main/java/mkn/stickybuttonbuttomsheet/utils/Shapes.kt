package mkn.stickybuttonbuttomsheet.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import androidx.annotation.ColorInt
import com.google.android.material.shape.MaterialShapeDrawable

inline fun materialShape(init: MaterialShapeDrawable.() -> Unit = {}): MaterialShapeDrawable {
    return MaterialShapeDrawable().apply(init)
}

data class RippleDrawableBuilder(
    @ColorInt
    var rippleColor: Int = 0x20000000,

    var backCornerRadius: Float = 0f,

    @ColorInt
    var backColor: Int = 0xFFFFFF,

    var maskDrawable: Drawable? = null,

    var preLollipopPressedColor: Int = 0x20000000,

    var preLollipopCornerSize: Float = backCornerRadius,

    var strokeWidth: Float = 0f,

    @ColorInt
    var strokeColor: Int = Color.TRANSPARENT
) {
    fun build(): Drawable {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RippleDrawable(
                ColorStateList(
                    arrayOf(intArrayOf()),
                    intArrayOf(rippleColor)
                ), MaterialShapeDrawable().apply {
                    fillColor = ColorStateList.valueOf(backColor)
                    setCornerSize(backCornerRadius)
                    if (this@RippleDrawableBuilder.strokeWidth > 0f) {
                        strokeWidth = this@RippleDrawableBuilder.strokeWidth
                        strokeColor = ColorStateList.valueOf(this@RippleDrawableBuilder.strokeColor)
                    }
                }, maskDrawable
            )

        } else {
            val states = arrayOf(
                intArrayOf(-android.R.attr.state_pressed),
                intArrayOf(android.R.attr.state_pressed)
            )

            val colors = intArrayOf(
                backColor,
                preLollipopPressedColor
            )

            MaterialShapeDrawable().apply {
                tintList = ColorStateList(states, colors)
                setCornerSize(preLollipopCornerSize)
            }
        }
    }
}

inline fun rippleDrawable(init: RippleDrawableBuilder.() -> Unit): Drawable {
    val builder = RippleDrawableBuilder().apply(init)
    return builder.build()
}