package io.github.takusan23.motionlayoutswipefixframelayoutexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.takusan23.motionlayoutswipefixframelayoutexample.Adapter.ListAdapter
import io.github.takusan23.motionlayoutswipefixframelayoutexample.DataClass.ListData
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 使用例
 *
 * レイアウトでは、[io.github.takusan23.motionlayoutswipefixframelayout.MotionLayoutSwipeFixFrameLayout]の上にMotionLayoutをおいてください
 *
 * */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // RecyclerView
        val list = arrayListOf<ListData>()
        val videoTitleList = arrayListOf("実況動画", "技術動画", "料理動画", "その他")
        repeat(100) {
            list.add(ListData(videoTitleList.random(), getDrawable(R.drawable.ic_baseline_local_movies_24)))
        }
        activity_main_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ListAdapter(list)
        }


    }

}