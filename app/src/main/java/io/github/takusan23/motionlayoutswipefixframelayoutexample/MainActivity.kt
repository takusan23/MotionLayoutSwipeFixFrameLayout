package io.github.takusan23.motionlayoutswipefixframelayoutexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.takusan23.motionlayoutswipefixframelayoutexample.Adapter.ListAdapter
import io.github.takusan23.motionlayoutswipefixframelayoutexample.DataClass.ListData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.activity_main_motionlayout
import kotlinx.android.synthetic.main.activity_main.player_image_view
import kotlinx.android.synthetic.main.activity_main.view.*

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

            // タブルタップ版
            onSwipeTargetViewDoubleClickFunc = { ev ->
                Toast.makeText(context, "ダブルタップ", Toast.LENGTH_SHORT).show()
            }

        }

        // RecyclerView
        val list = arrayListOf<ListData>()
        repeat(100) {
            list.add(ListData("動画 Part ${it}", getDrawable(R.drawable.ic_baseline_local_movies_24)))
        }
        activity_main_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ListAdapter(list)
        }

        // こめんと
        val commentList = arrayListOf<ListData>()
        repeat(50) {
            commentList.add(ListData("${it}コメ : わこつ", getDrawable(R.drawable.ic_baseline_comment_24)))
        }
        player_comment.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ListAdapter(commentList)
        }

    }

}