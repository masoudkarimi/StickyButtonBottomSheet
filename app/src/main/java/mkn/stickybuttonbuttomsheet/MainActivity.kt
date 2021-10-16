package mkn.stickybuttonbuttomsheet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import mkn.stickybuttonbuttomsheet.utils.AnimatedColor
import mkn.stickybuttonbuttomsheet.utils.setStatusBarColor

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<MaterialButton>(R.id.button)
        val statusBarColorAnimator = AnimatedColor(
            ContextCompat.getColor(this, R.color.purple_700),
            ContextCompat.getColor(this, R.color.gray),
        )
        button.setOnClickListener {
            StickyButtonBottomSheet(this)
                .setItems((1..20).map { "Item #$it" })
                .setBottomSheetFullScreenProgressCallback {
                    setStatusBarColor(statusBarColorAnimator.with(it))
                }
                .show()
        }
    }
}