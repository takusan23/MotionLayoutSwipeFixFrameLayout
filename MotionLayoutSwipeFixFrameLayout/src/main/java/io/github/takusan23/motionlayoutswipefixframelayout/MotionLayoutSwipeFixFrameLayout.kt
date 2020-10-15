package io.github.takusan23.motionlayoutswipefixframelayout

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.motion.widget.MotionLayout


/**
 * MotionLayoutの`<onSwipe>`の問題を直すFrameLayout。
 *
 * 以下の問題を解決できます。
 * - MotionLayoutの下(階層的に上)にRecyclerViewを置くとクリックイベントが奪われてスクロール出来ない
 * -`<onSwipe>`に指定したViewには[setOnClickListener]が使えないので押したときの処理ができない
 * - <onSwipe>`で指定したView以外でもなぜかスワイプできてしまう。
 *
 * この問題を直すFrameLayout。
 *
 * あとViewの下（階層的に上。親）にViewを置く場合は、[MotionLayoutSwipeFixFrameLayout]に追加するのではなく、(階層的に)上のViewGroupに置くようにしてください。
 *
 * - 以下例
 * - FrameLayout
 *      - RecyclerView <- 重ねる際に下に表示するView。ほかもここに置く。一例
 *      - [MotionLayoutSwipeFixFrameLayout]  <- 傘下にはMotionLayoutだけ。この傘下に置くとタッチがいかなくなるので
 *          - [MotionLayout]
 *              - 動かすView
 * */
class MotionLayoutSwipeFixFrameLayout(context: Context, attributeSet: AttributeSet? = null) : FrameLayout(context, attributeSet) {

    /** ドラッグする（スワイプに設定した）View。 */
    var swipeTargetView: View? = null

    /**
     * 強制的にクリックを渡す時に使う。ここで指定しない場合、MotionLayout傘下にRecyclerView等が有ってもスクロール出来ない可能性があります。
     * そのためのこの配列。
     *
     * MotionLayoutのConstraintSetで指定したIDを引数に入れることでそのIDの状態ならタッチを渡すことが出来ます。
     *
     * ただし、クリックの行き場がない場合はMotionLayoutが動作してしまうので、RecyclerViewを置く、setOnClickListener / isClickable を設定する等してください。
     *
     * ここで入れたIDとタッチ中MotionLayoutの状態を取得して、一致している場合はタッチを特別に渡します。
     * */
    val allowIdList = arrayListOf<Int>()

    /** MotionLayoutの状態を知るためにMotionLayoutが必要 */
    var motionLayout: MotionLayout? = null

    /**
     * [swipeTargetView]のクリックイベント
     * 注意：ほんのちょっとだけ遅延させてから高階関数を呼んでいます。理由はこの上（子のView）のクリックイベントがうまく処理できないため。
     * ちょっと無理矢理感あるね
     * */
    var onSwipeTargetViewClickFunc: (() -> Unit)? = null

    /** [onSwipeTargetViewClickFunc]を呼ぶまでどれぐらい遅延させるか。 */
    var onSwipeTargetViewClickFuncDelayMs = 100L

    /** 子のViewへタッチイベントを渡すかどうか */
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (swipeTargetView != null && ev != null && motionLayout != null) {
            // タッチがswipeTargetViewの中にあるときのみタッチイベントを渡す
            if (ev.x > swipeTargetView!!.left && ev.x < swipeTargetView!!.right && ev.y > swipeTargetView!!.top && ev.y < swipeTargetView!!.bottom) {
                // クリックさせるなど
                if (ev.action == MotionEvent.ACTION_DOWN) {
                    postDelayed({ onSwipeTargetViewClickFunc?.invoke() }, onSwipeTargetViewClickFuncDelayMs)
                }
                // 指定したViewを動かしている場合は渡す
                return super.onInterceptTouchEvent(ev)
            } else if (allowIdList.contains(motionLayout!!.currentState)) {
                // タッチイベントを渡すことが許可されているIDなら渡す
                return super.onInterceptTouchEvent(ev)
/*
            } else if (motionLayout!!.progress != 0f && motionLayout!!.progress != 1f) {
                // もしMotionLayout進行中なら
                return super.onInterceptTouchEvent(ev)
*/
            } else {
                return true // true渡すとタッチイベントを渡さない（MotionLayoutが動く）
            }
        } else {
            return super.onInterceptTouchEvent(ev)
        }
    }

}