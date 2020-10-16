package io.github.takusan23.motionlayoutswipefixframelayoutexample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.takusan23.motionlayoutswipefixframelayoutexample.Adapter.ListAdapter
import io.github.takusan23.motionlayoutswipefixframelayoutexample.DataClass.ListData
import kotlinx.android.synthetic.main.fragment_video_player.*

class VideoPlayerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** [io.github.takusan23.motionlayoutswipefixframelayout.MotionLayoutSwipeFixFrameLayout]の設定をする */
        activity_main_swipe_fix_framelayout.apply {
            allowIdList.add(R.id.fragment_video_player_motion_transition_end)
            // 以下2つは必須
            swipeTargetView = player_image_view
            motionLayout = fragment_video_player_motionlayout

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

        // こめんと
        val commentList = arrayListOf<ListData>()
        val comments = arrayListOf("わこつ", "8888", "失踪するな", "ここすき")
        repeat(50) {
            commentList.add(ListData("${it}コメ : ${comments.random()}", requireContext().getDrawable(R.drawable.ic_baseline_comment_24)))
        }
        player_comment.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ListAdapter(commentList)
        }

    }

}