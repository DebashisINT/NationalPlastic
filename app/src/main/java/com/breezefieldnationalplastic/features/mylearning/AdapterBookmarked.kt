package com.breezefieldnationalplastic.features.mylearning

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.features.orderITC.AdapterProductList
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.performance_item.view.perform_thumbnail
import kotlinx.android.synthetic.main.row_bookmark.view.iv_bookmark_del
import kotlinx.android.synthetic.main.row_bookmark.view.iv_row_book_img
import kotlinx.android.synthetic.main.row_bookmark.view.ll_bookmark_root
import kotlinx.android.synthetic.main.row_bookmark.view.tv_row_book_content_desc
import kotlinx.android.synthetic.main.row_bookmark.view.tv_row_book_content_title
import kotlinx.android.synthetic.main.row_bookmark.view.tv_row_book_topic_name

class AdapterBookmarkedprivate (val mContext: Context, private val mList: ArrayList<VidBookmark>,var listner: OnClick):RecyclerView.Adapter<AdapterBookmarkedprivate.BookmarkViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_bookmark,parent,false)
        return BookmarkViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bindItems()
    }
    inner class BookmarkViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            itemView.tv_row_book_topic_name.text = mList.get(adapterPosition).topic_name
            if (!mList.get(adapterPosition).content_bitmap.equals("")) {
                Glide.with(mContext)
                    .load(mList.get(adapterPosition).content_bitmap)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_image).error(R.drawable.ic_image))
                    .into(itemView.iv_row_book_img)
            }
            else{
                itemView.perform_thumbnail.setImageResource(R.drawable.ic_image)
            }
            itemView.tv_row_book_content_title.text = mList.get(adapterPosition).content_name
            itemView.tv_row_book_content_desc.text = mList.get(adapterPosition).content_desc

            itemView.ll_bookmark_root.setOnClickListener {
                listner.onClick(mList.get(adapterPosition))
            }
            itemView.iv_bookmark_del.setOnClickListener {
                listner.onDelClick(mList.get(adapterPosition))
            }
        }
    }

    interface OnClick {
        fun onClick(obj:VidBookmark)
        fun onDelClick(obj:VidBookmark)
    }
}