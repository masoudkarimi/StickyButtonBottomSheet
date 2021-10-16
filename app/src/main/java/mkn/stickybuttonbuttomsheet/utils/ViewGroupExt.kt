package mkn.stickybuttonbuttomsheet.utils

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.recyclerview.widget.RecyclerView

inline fun Context.frameLayout(init: FrameLayout.() -> Unit = {}): FrameLayout {
    return FrameLayout(this).apply(init)
}

inline fun ViewGroup.recycler(init: RecyclerView.() -> Unit): RecyclerView {
    return RecyclerView(context).apply(init)
}

inline fun ViewGroup.scrollView(init: ScrollView.() -> Unit): ScrollView {
    return ScrollView(context).apply(init)
}