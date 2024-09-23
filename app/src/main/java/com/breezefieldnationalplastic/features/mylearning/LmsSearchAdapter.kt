package com.breezefieldnationalplastic.features.mylearning
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.types.FragType
import com.google.android.flexbox.AlignSelf
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.gridview_item_new.view.learning_progress_status_topic
import kotlinx.android.synthetic.main.gridview_item_new.view.ll_status
import kotlinx.android.synthetic.main.gridview_item_new.view.tv_parcentage
import kotlinx.android.synthetic.main.gridview_item_new.view.tv_parcentage_status

class LmsSearchAdapter(
    private val context: Context,
    private val itemList: List<LmsSearchData>,
    private val fragType: FragType, // Make fragType a property of the class
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<LmsSearchAdapter.ViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    interface OnItemClickListener {
        fun onItemClick(item: LmsSearchData)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_Search_item: TextView = itemView.findViewById(R.id.tv_Search_item)
        val ll_grd_item: CardView = itemView.findViewById(R.id.ll_grd_item)
        val iv_gv: ImageView = itemView.findViewById(R.id.iv_gv)

        init {
            ll_grd_item.setOnClickListener {
                val previousPosition = selectedPosition
                if (selectedPosition == adapterPosition) {
                    selectedPosition = RecyclerView.NO_POSITION
                } else {
                    selectedPosition = adapterPosition
                    itemClickListener.onItemClick(itemList[adapterPosition])
                }
                notifyItemChanged(previousPosition)
                notifyItemChanged(adapterPosition)
            }
        }

        @SuppressLint("WrongConstant")
        fun bind() {
            var position: Int = adapterPosition
            val item = itemList[adapterPosition]
            tv_Search_item.text = "${item.courseName} (${item.video_count})"

            itemView.learning_progress_status_topic.progress = item.topic_parcentage
            if (item.topic_parcentage > 100){
                item.topic_parcentage = 100
            }
            itemView.tv_parcentage.text = "${item.topic_parcentage}%"
            itemView.tv_parcentage.text = item.topic_parcentage.toString()+"%"

            /*if (position == selectedPosition) {
                ll_grd_item.background = ContextCompat.getDrawable(context, R.drawable.back_round_corner_18)
                tv_Search_item.setTextColor(ContextCompat.getColor(context, R.color.white))
                // iv_gv.setImageResource(R.drawable.book_n)
            } else {
                ll_grd_item.background = ContextCompat.getDrawable(context, R.drawable.back_round_corner_8)
                tv_Search_item.setTextColor(ContextCompat.getColor(context, R.color.black))
                // iv_gv.setImageResource(R.drawable.book_n)
            }*/


                if (fragType.name.equals("SearchLmsFrag") ) {
                    itemView.learning_progress_status_topic.visibility = View.VISIBLE
                    itemView.ll_status.visibility = View.VISIBLE

                    if (item.topic_parcentage == 100) {
                        itemView.tv_parcentage_status.text = "Completed"
                        itemView.tv_parcentage_status.setTextColor(ContextCompat.getColor(context, R.color.lms_cmplt))
                    } else if (item.topic_parcentage == 0) {
                        itemView.tv_parcentage_status.text = "Pending"
                        itemView.tv_parcentage_status.setTextColor(ContextCompat.getColor(context, R.color.red_dot))
                    } else {
                        itemView.tv_parcentage_status.text = "Pending"
                        itemView.tv_parcentage_status.setTextColor(ContextCompat.getColor(context, R.color.red_dot))
                    }
                }
                else if (fragType.name.equals("SearchLmsKnowledgeFrag")){
                itemView.learning_progress_status_topic.visibility = View.GONE
                itemView.ll_status.visibility = View.GONE
            }
            else{
                itemView.learning_progress_status_topic.visibility = View.VISIBLE
                itemView.ll_status.visibility = View.VISIBLE
                    if (item.topic_parcentage == 100) {
                        itemView.tv_parcentage_status.text = "Completed"
                        itemView.tv_parcentage_status.setTextColor(ContextCompat.getColor(context, R.color.lms_cmplt))
                    }else {
                        itemView.tv_parcentage_status.text = "Pending"
                        itemView.tv_parcentage_status.setTextColor(ContextCompat.getColor(context, R.color.red_dot))
                    }
                }

            val lp: ViewGroup.LayoutParams = tv_Search_item.layoutParams
            if (lp is FlexboxLayoutManager.LayoutParams) {
                val flexboxLp = lp
                flexboxLp.flexGrow = 1.0f
                flexboxLp.alignSelf = AlignSelf.FLEX_END
            }

            ll_grd_item.isSelected = position == selectedPosition
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gridview_item_new, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun getSelectedPosition(): Int {
        return selectedPosition
    }
}
