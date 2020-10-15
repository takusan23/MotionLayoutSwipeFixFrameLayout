package io.github.takusan23.motionlayoutswipefixframelayoutexample.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.takusan23.motionlayoutswipefixframelayoutexample.DataClass.VideoListData
import io.github.takusan23.motionlayoutswipefixframelayoutexample.R

/** MainActivityに表示するRecyclerViewのAdapter */
class VideoListAdapter(val list: ArrayList<VideoListData>) : RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.adapter_imageview)
        val textView = itemView.findViewById<TextView>(R.id.adapter_textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_video, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.apply {
            imageView.setImageDrawable(item.drawable)
            textView.text = item.title
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}