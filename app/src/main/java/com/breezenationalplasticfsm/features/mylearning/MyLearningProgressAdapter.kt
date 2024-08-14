package com.breezenationalplasticfsm.features.mylearning

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.DialogLoading.getResources
import com.breezenationalplasticfsm.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.inflate_micro_learning_item.view.iv_thumbnail
import kotlinx.android.synthetic.main.performance_item.view.learning_progress_status
import kotlinx.android.synthetic.main.performance_item.view.perform_thumbnail
import kotlinx.android.synthetic.main.performance_item.view.tv_perform_subtitle
import kotlinx.android.synthetic.main.performance_item.view.tv_perform_title
import kotlinx.android.synthetic.main.performance_item.view.tv_progressStatus
import kotlinx.android.synthetic.main.performance_item.view.tv_progressText
import kotlinx.android.synthetic.main.performance_item.view.tv_topic_name
import java.time.LocalTime


class MyLearningProgressAdapter(
    private val mContext: Context,
    private val mList: ArrayList<ContentL>,
    private val topic_name: String,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MyLearningProgressAdapter.MyLearningProgressViewHolder>() {

    inner class MyLearningProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bindItem() {
            val item = mList[absoluteAdapterPosition]

            if (item.content_url != null) {
                /*val thumb: Long = (layoutPosition * 1000).toLong()
                val options = RequestOptions().frame(thumb)
                Glide.with(mContext).load(item.content_url).apply(options).into(itemView.perform_thumbnail)*/
                Glide.with(mContext)
                    .load(mList?.get(adapterPosition)?.content_thumbnail)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_image).error(R.drawable.ic_image))
                    .thumbnail(Glide.with(mContext).load(mList?.get(adapterPosition)?.content_thumbnail))
                    .into(itemView.perform_thumbnail)
            }
            else{
                itemView.perform_thumbnail.setImageResource(R.drawable.ic_image)
            }

            itemView.tv_perform_title.text = item.content_title
            itemView.tv_topic_name.text = "Topic: "+topic_name
            itemView.tv_perform_subtitle.text = item.content_description


            if (!item.Watch_Percentage.equals("")) {
                itemView.learning_progress_status.progress = item.Watch_Percentage.toInt()
                itemView.tv_progressText.text = "${item.Watch_Percentage}% complete"

                if (item.Watch_Percentage == "100") {
                    itemView.tv_progressStatus.text = "Completed"
                    itemView.tv_progressStatus.setTextColor(mContext.resources.getColor(R.color.approved_green))
                } else {
                    itemView.tv_progressStatus.text = "Continue"
                    itemView.tv_progressStatus.setTextColor(mContext.resources.getColor(R.color.toolbar_lms))
                }

            }else{
                itemView.learning_progress_status.progress = 0
                itemView.tv_progressText.text = "0 % complete"
                itemView.tv_progressStatus.text = "Not Started Yet"
                itemView.tv_progressStatus.setTextColor(mContext.resources.getColor(R.color.toolbar_lms))

            }


        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(mList[position] ,position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: ContentL ,position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyLearningProgressViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.performance_item, parent, false)
        return MyLearningProgressViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MyLearningProgressViewHolder, position: Int) {
        holder.bindItem()
    }

}
