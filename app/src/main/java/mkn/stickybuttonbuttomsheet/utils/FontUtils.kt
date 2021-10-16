package mkn.stickybuttonbuttomsheet.utils

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import android.util.SparseArray
import mkn.stickybuttonbuttomsheet.R

object FontUtils {
    @FontKey
    val REGULAR: Int = R.font.regular

    @FontKey
    val BOLD: Int = R.font.bold

    private val fontCache = SparseArray<Typeface>()

    @Retention(AnnotationRetention.SOURCE)
    annotation class FontKey

    @JvmStatic
    fun getRegularFont(context: Context): Typeface {
        if (fontCache.indexOfKey(REGULAR) < 0) {
            fontCache.put(REGULAR, ResourcesCompat.getFont(context, REGULAR))
        }
        return fontCache[REGULAR]
    }

    @JvmStatic
    fun getBoldFont(context: Context): Typeface {
        if (fontCache.indexOfKey(BOLD) < 0) {
            fontCache.put(BOLD, ResourcesCompat.getFont(context, BOLD))
        }
        return fontCache[BOLD]
    }

    @JvmStatic
    fun getFont(context: Context, @FontKey font: Int): Typeface? {
        if (fontCache.indexOfKey(font) < 0) {
            fontCache.put(font, ResourcesCompat.getFont(context, font))
        }

        return fontCache[font]
    }

}
