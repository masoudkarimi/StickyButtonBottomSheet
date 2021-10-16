package mkn.stickybuttonbuttomsheet.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

import android.os.Build
import android.view.*
import androidx.annotation.ColorInt


fun Context.getScreenHeight(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics: WindowMetrics = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).currentWindowMetrics
        val insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds.height() - insets.top - insets.bottom
    } else {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        dm.heightPixels
    }
}

@Suppress("DEPRECATION")
fun Activity.setStatusBarColor(@ColorInt statusBarColor: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = statusBarColor

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val flag = (WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS)
            window?.insetsController?.setSystemBarsAppearance(
                flag,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val flag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            window.decorView.systemUiVisibility = flag
        }
    }
}