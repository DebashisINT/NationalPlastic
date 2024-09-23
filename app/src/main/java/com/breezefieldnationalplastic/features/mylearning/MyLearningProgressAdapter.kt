package com.modigoldbreeze.features.mylearning

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.features.mylearning.ContentL
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.inflate_chat_user_item.view.iv_profile_picture
import kotlinx.android.synthetic.main.inflate_micro_learning_item.view.iv_thumbnail
import kotlinx.android.synthetic.main.performance_item.view.learning_progress_status
import kotlinx.android.synthetic.main.performance_item.view.ll_quiz_header
import kotlinx.android.synthetic.main.performance_item.view.perform_thumbnail
import kotlinx.android.synthetic.main.performance_item.view.tv_perform_subtitle
import kotlinx.android.synthetic.main.performance_item.view.tv_perform_title
import kotlinx.android.synthetic.main.performance_item.view.tv_progressStatus
import kotlinx.android.synthetic.main.performance_item.view.tv_progressText
import kotlinx.android.synthetic.main.performance_item.view.tv_quiztatus
import kotlinx.android.synthetic.main.performance_item.view.tv_topic_name
import kotlinx.android.synthetic.main.performance_item.view.tv_watchstatus
import java.time.LocalTime


class MyLearningProgressAdapter(
    private val mContext: Context,
    private val mList: ArrayList<ContentL>,
    private val topic_name: String,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MyLearningProgressAdapter.MyLearningProgressViewHolder>() {

    var isVidQuesComplete = false

    inner class MyLearningProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        fun bindItem() {
            val item = mList[absoluteAdapterPosition]
            //code start for Set thumbnail of a particular content
            if (item.content_url != null) {

                Glide.with(mContext)
                    .load(mList?.get(adapterPosition)?.content_thumbnail)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_image).error(R.drawable.ic_image)
                    )
                    .thumbnail(
                        Glide.with(mContext).load(mList?.get(adapterPosition)?.content_thumbnail)
                    )
                    .into(itemView.perform_thumbnail)
            } else {
                //If thumnail image not available ,set default image of that particular content
                itemView.perform_thumbnail.setImageResource(R.drawable.ic_image)
            }
            //code end for Set thumbnail of a particular content

            itemView.tv_perform_title.text = item.content_title
            itemView.tv_topic_name.text = "Topic: " + topic_name
            itemView.tv_perform_subtitle.text = item.content_description

            if(item.CompletionStatus){
                isVidQuesComplete = true
            }
            if (item.content_watch_completed && item.question_list.size == 0 && item.CompletionStatus == false && isVidQuesComplete) {
                item.CompletionStatus = true
            }
            if ((adapterPosition + 1) % Pref.QuestionAfterNoOfContentForLMS.toInt() == 0 ) {
                isVidQuesComplete = false
            }

            try {
                if (item.question_list.size > 0 || true) {

                    if (item.Watch_Percentage.toInt() == 100) {
                        itemView.tv_progressText.visibility = View.VISIBLE
                        itemView.tv_quiztatus.visibility = View.VISIBLE
                    } else {
                        itemView.tv_progressText.visibility = View.GONE
                        itemView.tv_quiztatus.visibility = View.GONE
                    }
                    if (item.CompletionStatus == true) {
                        itemView.tv_progressText.setImageResource(R.drawable.quiz_done)
                        itemView.tv_quiztatus.text = " Quiz Done"

                    } else {
                        itemView.tv_progressText.setImageResource(R.drawable.quiz_pending)
                        itemView.tv_quiztatus.text = " Quiz Pending "
                    }
                } else {
                    itemView.tv_progressText.visibility = View.GONE
                }

                if ((adapterPosition + 1) % Pref.QuestionAfterNoOfContentForLMS.toInt() != 0) {
                    itemView.ll_quiz_header.visibility = View.GONE
                }
            } catch (e: Exception) {
                itemView.tv_progressText.visibility = View.GONE
            }

            //Code start for if watch done or pending status visible mantis -
            if (!item.Watch_Percentage.equals("")) {
                itemView.learning_progress_status.progress = item.Watch_Percentage.toInt()

                if (item.Watch_Percentage == "100") {
                    itemView.tv_progressStatus.setImageResource(R.drawable.watch_done)

                    itemView.tv_watchstatus.text = " Watch Done "
                } else {
                    itemView.tv_progressStatus.setImageResource(R.drawable.watch_pending)

                    itemView.tv_watchstatus.text = " Watch Pending "
                }

            } else {
                itemView.learning_progress_status.progress = 0
                itemView.tv_progressStatus.setImageResource(R.drawable.watch_pending)
                itemView.tv_watchstatus.text = " Watch Pending "

            }
            //Code end for if watch done or pending status visible mantis -

        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(mList[position], position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: ContentL, position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyLearningProgressViewHolder {
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
