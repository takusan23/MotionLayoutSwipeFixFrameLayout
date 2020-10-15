package io.github.takusan23.motionlayoutswipefixframelayoutexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.takusan23.motionlayoutswipefixframelayoutexample.Adapter.VideoListAdapter
import io.github.takusan23.motionlayoutswipefixframelayoutexample.DataClass.VideoListData
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

        /** [io.github.takusan23.motionlayoutswipefixframelayout.MotionLayoutSwipeFixFrameLayout]の設定をする */
        activity_main_swipe_fix_framelayout.apply {
            allowIdList.add(R.id.activity_main_motion_transition_end)
            // 以下2つは必須
            swipeTargetView = player_image_view
            motionLayout = activity_main_motionlayout
            // swipeTargetViewをクリックさせたい場合は指定してね
            onSwipeTargetViewClickFunc = {
                // プレイヤー押したとき。setOnClickListener代わり
                Toast.makeText(context, "プレイヤー押した！", Toast.LENGTH_SHORT).show()
            }
        }

        // RecyclerView
        val list = arrayListOf<VideoListData>()
        repeat(20) {
            list.add(VideoListData("動画 Part:${it}", getDrawable(R.drawable.ic_baseline_local_movies_24)))
        }
        activity_main_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = VideoListAdapter(list)
        }

        // こめんと
        val commentList = arrayListOf<VideoListData>()
        repeat(50) {
            commentList.add(VideoListData("${it}コメ : わこつ", getDrawable(R.drawable.ic_baseline_comment_24)))
        }
        player_comment.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = VideoListAdapter(commentList)
        }

    }

}