package com.breezefieldnationalplastic.features.mylearning
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import de.hdodenhof.circleimageview.CircleImageView

class LeaderboardLMSAdapter(private val mList: List<LeaderboardLmsItemsViewModel>) : RecyclerView.Adapter<LeaderboardLMSAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_leaderboard_frag, parent, false)

        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]

        holder.name_TV.text = itemsViewModel.name
        holder.leader_rank_TV.text = itemsViewModel.rank.toString()
        holder.tv_mobile.text = itemsViewModel.phonenumber
        holder.tv_score.text = itemsViewModel.totalscore

        holder.iv_logoOfPerson.setImageResource(mList.get(position).itemImg)

    }
    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val leader_rank_TV: TextView = itemView.findViewById(R.id.leader_rank_TV)
        val iv_logoOfPerson: CircleImageView = itemView.findViewById(R.id.iv_logoOfPerson)
        val name_TV: TextView = itemView.findViewById(R.id.name_TV)
        val tv_mobile: TextView = itemView.findViewById(R.id.tv_mobile)
        val tv_score: TextView = itemView.findViewById(R.id.tv_score)
    }
}
