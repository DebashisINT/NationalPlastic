package com.breezenationalplasticfsm.features.mylearning
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.google.android.flexbox.AlignSelf
import com.google.android.flexbox.FlexboxLayoutManager

class LmsSearchAdapter(private val context: Context, private val itemList: List<LmsSearchData>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<LmsSearchAdapter.ViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    interface OnItemClickListener {
        fun onItemClick(item: LmsSearchData)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_Search_item: TextView = itemView.findViewById(R.id.tv_Search_item)
        val ll_grd_item: LinearLayout = itemView.findViewById(R.id.ll_grd_item)
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
        fun bind(position: Int) {
            val item = itemList[position]
            tv_Search_item.text = item.courseName+" ("+item.video_count+")"
            if (position == selectedPosition) {
                ll_grd_item.background = ContextCompat.getDrawable(context, R.drawable.back_round_corner_18)
                tv_Search_item.setTextColor(ContextCompat.getColor(context, R.color.white))
                //iv_gv.setImageResource(R.drawable.book_n)
            } else {
                ll_grd_item.background = ContextCompat.getDrawable(context, R.drawable.back_round_corner_8)
                tv_Search_item.setTextColor(ContextCompat.getColor(context, R.color.black))
                //iv_gv.setImageResource(R.drawable.book_n)
            }
            val lp: ViewGroup.LayoutParams = tv_Search_item.getLayoutParams()
            if (lp is FlexboxLayoutManager.LayoutParams) {
                val flexboxLp = lp
                flexboxLp.flexGrow = 1.0f
                flexboxLp.alignSelf = AlignSelf.FLEX_END
            }

            ll_grd_item.isSelected = position == selectedPosition
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gridview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun getSelectedPosition(): Int {
        return selectedPosition
    }
}

