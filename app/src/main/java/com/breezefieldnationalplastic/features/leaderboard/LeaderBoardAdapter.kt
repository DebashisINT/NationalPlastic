package com.breezefieldnationalplastic.features.leaderboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.features.leaderboard.api.OverallUserListData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView

class LeaderBoardAdapter(
    private val mLeaderBoardData: ArrayList<OverallUserListData>,
    private val mContext: Context
):RecyclerView.Adapter<FoodViewHolder>(){
    // class FoodViewHolder here..

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val viewLayout = LayoutInflater.from(parent.context).inflate(
            R.layout.row_leaderboard_frag,
            parent,false)
        return FoodViewHolder(viewLayout)
    }

    override fun getItemCount(): Int {
      return  mLeaderBoardData.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val mLeaderBoardPositionData = mLeaderBoardData[position]
        holder.name_TV.text = mLeaderBoardPositionData.user_name
        //holder.tv_designation.text = mLeaderBoardPositionData.user_list.get(position).user_name
        holder.tv_mobile.text = mLeaderBoardPositionData.user_phone
        holder.tv_score.text = mLeaderBoardPositionData.totalscore.toString()
        Glide.with(mContext)
            .load(mLeaderBoardPositionData.profile_pictures_url)
            .apply(RequestOptions.placeholderOf(R.drawable.user_blank).error(R.drawable.user_blank))
            .into(holder.iv_logoOfPerson)
        if (mLeaderBoardPositionData.position == 1){
            holder.iv_badge.setBackgroundResource(R.drawable.first_icon)

        }else if (mLeaderBoardPositionData.position == 2){
            holder.iv_badge.setBackgroundResource(R.drawable.second_icon)

        }
        else if (mLeaderBoardPositionData.position == 3){
            holder.iv_badge.setBackgroundResource(R.drawable.third_icon)

        }
        else{
            holder.leader_rank_TV.visibility=View.VISIBLE
            holder.leader_rank_TV.text = "#"+mLeaderBoardPositionData.position.toString()

        }
    }

}

class FoodViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
{
    val name_TV : TextView = itemView.findViewById(R.id.name_TV)
    val iv_badge : ImageView = itemView.findViewById(R.id.iv_badge)
    val leader_rank_TV : TextView = itemView.findViewById(R.id.leader_rank_TV)
    val tv_designation : TextView = itemView.findViewById(R.id.tv_designation)
    val tv_score : TextView = itemView.findViewById(R.id.tv_score)
    val tv_mobile : TextView = itemView.findViewById(R.id.tv_mobile)
    val iv_logoOfPerson : CircleImageView = itemView.findViewById(R.id.iv_logoOfPerson)
}
