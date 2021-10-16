package mkn.stickybuttonbuttomsheet.utils

import android.content.res.Resources
import kotlin.math.ceil

val Int.dpf: Float
    get() {
        return this.dp.toFloat()
    }

val Float.dpf: Float
    get() {
        return this.dp.toFloat()
    }

val Int.dp: Int
    get() {
        return if (this == 0) {
            0
        } else ceil((Resources.getSystem().displayMetrics.density * this).toDouble()).toInt()
    }

val Float.dp: Int
    get() {
        return if (this == 0f) {
            0
        } else ceil((Resources.getSystem().displayMetrics.density * this).toDouble()).toInt()
    }