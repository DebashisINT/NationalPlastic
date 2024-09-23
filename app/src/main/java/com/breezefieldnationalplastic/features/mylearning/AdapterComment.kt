package com.breezefieldnationalplastic.features.mylearning

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import kotlinx.android.synthetic.main.row_video_comment.view.tv_row_video_cmt
import kotlinx.android.synthetic.main.row_video_comment.view.tv_row_video_cmt_date_time

class AdapterComment(var mContext:Context,var commentL:ArrayList<CommentL>):
    RecyclerView.Adapter<AdapterComment.CommentVIewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentVIewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_video_comment, parent, false)
        return CommentVIewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return commentL.size
    }

    override fun onBindViewHolder(holder: CommentVIewHolder, position: Int) {
        holder.bind()
    }

    fun clear() {
            commentL.clear() // Clear the comments list
    }

    inner class CommentVIewHolder(itemVIew:View):RecyclerView.ViewHolder(itemVIew){
        fun bind(){
            try {
                if (!commentL.get(absoluteAdapterPosition).comment_description.isNullOrEmpty()) {
                    itemView.tv_row_video_cmt.text = commentL.get(absoluteAdapterPosition).comment_description.toString()
                    itemView.tv_row_video_cmt_date_time.text = commentL.get(absoluteAdapterPosition).comment_date_time
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}