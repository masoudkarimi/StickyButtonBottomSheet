package mkn.stickybuttonbuttomsheet.utils

import android.graphics.Color
import androidx.annotation.ColorInt

class AnimatedColor(private val start: Int, private val end: Int) {

    private val argb0: IntArray
    private val argb1: IntArray

    init {
        this.argb0 = toARGB(start)
        this.argb1 = toARGB(end)
    }

    @ColorInt
    fun with(delta: Float): Int {
        if (delta <= 0.0f)
            return start
        if (delta>=1.0f)
            return end

        val argb = move(argb0, argb1, delta)
        return Color.argb(argb[0],argb[1],argb[2],argb[3])
    }

    private fun move(vector0: IntArray, vector1: IntArray, delta: Float): IntArray {
        val vector = IntArray(4)
        val dif0 = vector1[0] - vector0[0]
        val dif1 = vector1[1] - vector0[1] // 150    100 => 50
        val dif2 = vector1[2] - vector0[2]
        val dif3 = vector1[3] - vector0[3]

        vector[0] = vector0[0] + (dif0 *  delta).toInt()
        vector[1] = vector0[1] + (dif1 *  delta).toInt()
        vector[2] = vector0[2] + (dif2 *  delta).toInt()
        vector[3] = vector0[3] + (dif3 *  delta).toInt()
        return vector
    }

    private fun toARGB(color: Int): IntArray {
        val argb = IntArray(4)
        argb[0] = Color.alpha(color)
        argb[1] = Color.red(color)
        argb[2] = Color.green(color)
        argb[3] = Color.blue(color)
        return argb
    }
}
