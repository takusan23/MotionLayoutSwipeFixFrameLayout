package io.github.takusan23.motionlayoutswipefixframelayoutexample.DataClass

import android.graphics.drawable.Drawable

/** [io.github.takusan23.motionlayoutswipefixframelayoutexample.Adapter.ListAdapter]で使うデータクラス */
data class ListData(
    val title: String,
    val drawable: Drawable?
)