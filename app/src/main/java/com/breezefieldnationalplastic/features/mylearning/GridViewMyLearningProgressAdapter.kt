package com.breezefieldnationalplastic.features.mylearning

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

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class GridViewMyLearningProgressAdapter(
    private val mContext: Context,
    private val mList: ArrayList<ContentL>,
    private val mListener: OnItemClickListener
) : ArrayAdapter<ContentL?>(mContext, 0, mList as ArrayList<ContentL?>) {

    interface OnItemClickListener {
        fun onItemClick(position: Int, courseModel: ContentL)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listitemView = convertView
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(mContext).inflate(R.layout.card_item, parent, false)
        }

        val courseModel: ContentL? = getItem(position)
        val courseTV = listitemView!!.findViewById<TextView>(R.id.tv_content_title)
        val courseIV = listitemView.findViewById<ImageView>(R.id.idIVcourse)

        //code start for Set thumbnail of a particular content
        if (courseModel!!.content_url != null) {
            Glide.with(mContext)
                .load(courseModel.content_thumbnail)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_image).error(R.drawable.ic_image)
                )
                .thumbnail(
                    Glide.with(mContext).load(courseModel.content_thumbnail)
                )
                .into(courseIV)
        } else {
            //If thumbnail image not available ,set default image of that particular content
            courseIV.setImageResource(R.drawable.ic_image)
        }
        //code end for Set thumbnail of a particular content

        courseTV.setText(courseModel.content_title)

        listitemView.setOnClickListener {
            mListener.onItemClick(position, courseModel)
        }

        return listitemView
    }
}