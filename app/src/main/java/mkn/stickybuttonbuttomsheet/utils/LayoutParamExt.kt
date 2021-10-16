package mkn.stickybuttonbuttomsheet.utils

import android.widget.FrameLayout

fun FrameLayout.matchWidthWrapHeight(initLayout: FrameLayout.LayoutParams.() -> Unit = {}): FrameLayout.LayoutParams {
    return FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.WRAP_CONTENT
    ).apply(initLayout)
}

fun FrameLayout.matchWidthCustomHeight(
    hDp: Int,
    initLayout: FrameLayout.LayoutParams.() -> Unit = {}
): FrameLayout.LayoutParams {
    return FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        hDp.dp
    ).apply(initLayout)
}

fun FrameLayout.wrapWidthWrapHeight(initLayout: FrameLayout.LayoutParams.() -> Unit = {}): FrameLayout.LayoutParams {
    return FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.WRAP_CONTENT,
        FrameLayout.LayoutParams.WRAP_CONTENT
    ).apply(initLayout)
}

fun FrameLayout.wrapWidthMatchHeight(initLayout: FrameLayout.LayoutParams.() -> Unit = {}): FrameLayout.LayoutParams {
    return FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.WRAP_CONTENT,
        FrameLayout.LayoutParams.MATCH_PARENT
    ).apply(initLayout)
}

fun FrameLayout.wrapWidthCustomHeight(
    hDp: Int,
    initLayout: FrameLayout.LayoutParams.() -> Unit = {}
): FrameLayout.LayoutParams {
    return FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.WRAP_CONTENT,
        hDp.dp
    ).apply(initLayout)
}

fun FrameLayout.customWidthAnHeight(
    wInDp: Int,
    hInDp: Int,
    initLayout: FrameLayout.LayoutParams.() -> Unit = {}
): FrameLayout.LayoutParams {
    return FrameLayout.LayoutParams(wInDp.dp, hInDp.dp).apply(initLayout)
}

fun FrameLayout.customWidthMatchHeight(
    wInDp: Int,
    initLayout: FrameLayout.LayoutParams.() -> Unit = {}
): FrameLayout.LayoutParams {
    return FrameLayout.LayoutParams(wInDp.dp, FrameLayout.LayoutParams.MATCH_PARENT)
        .apply(initLayout)
}

fun FrameLayout.customWidthWrapHeight(
    wInDp: Int,
    initLayout: FrameLayout.LayoutParams.() -> Unit = {}
): FrameLayout.LayoutParams {
    return FrameLayout.LayoutParams(wInDp.dp, FrameLayout.LayoutParams.WRAP_CONTENT)
        .apply(initLayout)
}

fun FrameLayout.matchWidthMatchHeight(initLayout: FrameLayout.LayoutParams.() -> Unit = {}): FrameLayout.LayoutParams {
    return FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.MATCH_PARENT
    ).apply(initLayout)
}
