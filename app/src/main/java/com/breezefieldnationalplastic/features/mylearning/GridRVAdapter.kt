import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.features.mylearning.ContentL
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.grid_rv_adapter.view.idIVCourse
import com.breezefieldnationalplastic.features.mylearning.GridViewAllVideoModal
import com.breezefieldnationalplastic.features.mylearning.KnowledgeHubAllVideoList
import kotlinx.android.synthetic.main.grid_rv_adapter.view.*
import kotlinx.android.synthetic.main.grid_rv_adapter_new.view.iv_knowledge_all_video_thumbnail
import kotlinx.android.synthetic.main.grid_rv_adapter_new.view.tv_knowledge_all_video_descrip
import kotlinx.android.synthetic.main.grid_rv_adapter_new.view.tv_knowledge_all_video_title


class GridRVAdapter(
    private val mContext: Context,
    /*
        private val mList: List<GridViewAllVideoModal>,
    */
    private val mList: List<ContentL>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<GridRVAdapter.GridRVViewHolder>() {


    inner class GridRVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        fun bindItem(){
            //itemView.idIVCourse.setImageBitmap(retriveVideoFrameFromVideo(mList.get(adapterPosition).videoImg))
            itemView.tv_knowledge_all_video_title.text = mList.get(adapterPosition).content_title
            itemView.tv_knowledge_all_video_descrip.text = mList.get(adapterPosition).content_description
            val thumb: Long = (getLayoutPosition() * 1000).toLong()
            val options = RequestOptions().frame(thumb)
            Glide.with(mContext).load(/*"http://3.7.30.86:8073"+*/mList.get(adapterPosition).content_url).apply(options).into(itemView.iv_knowledge_all_video_thumbnail)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(mList[position])
            }
        }
    }
    interface OnItemClickListener {
        fun onItemClick(item: ContentL)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridRVViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.grid_rv_adapter_new, parent, false)
        return GridRVViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: GridRVViewHolder, position: Int) {
        holder.bindItem()
    }

}
