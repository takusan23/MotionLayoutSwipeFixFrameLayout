package io.github.takusan23.motionlayoutswipefixframelayoutexample.DataClass

import android.graphics.drawable.Drawable

/** [io.github.takusan23.motionlayoutswipefixframelayoutexample.Adapter.VideoListAdapter]で使うデータクラス */
data class VideoListData(
    val title: String,
    val drawable: Drawable?
)