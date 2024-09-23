package com.breezefieldnationalplastic.features.mylearning
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.util.Log
import android.util.LruCache
import android.util.Size
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.breezefieldnationalplastic.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoGridAdapter(private val context: Context, private val videoList: List<GridViewAllVideoModal>) : BaseAdapter() {

    private val memoryCache: LruCache<String, Bitmap> by lazy {
        val cacheSize = (Runtime.getRuntime().maxMemory() / 1024 / 8).toInt()
        LruCache<String, Bitmap>(cacheSize)
    }

    override fun getCount(): Int = videoList.size

    override fun getItem(position: Int): Any = videoList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.gridview_item_all_vdo, parent, false)
            viewHolder = ViewHolder(view, context)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val videoData = videoList[position]
        viewHolder.bind(videoData, memoryCache)

        return view
    }

    class ViewHolder(private val view: View, private val context: Context) {
        private val imageView: ImageView = view.findViewById(R.id.iv_thumnail)
        private val textView: TextView = view.findViewById(R.id.tv_videoname)

        fun bind(videoData: GridViewAllVideoModal, memoryCache: LruCache<String, Bitmap>) {
            textView.text = videoData.videoName

            val videoUrl = "http://3.7.30.86:8073/Commonfolder/LMS/ContentUpload/Sell Me This Pen.mp4"
            val bitmap = extractFrameFromVideo(videoUrl)

            /*val bitmap = extractFrameFromVideo("http://3.7.30.86:8073/Commonfolder/LMS/ContentUpload/Sell Me This Pen.mp4")

            bitmap?.let {
                imageView.setImageBitmap(it)
            }*/

            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(videoData.videoImg, HashMap<String, String>())
            val bmFrame = mediaMetadataRetriever.getFrameAtTime(1000000) //unit in microsecond
            imageView.setImageBitmap(bmFrame)

        }

        private fun extractFrameFromVideo(videoPath: String): Bitmap? {
            val retriever = MediaMetadataRetriever()
            return try {
                retriever.setDataSource(videoPath)
                retriever.getFrameAtTime(10000) // 1 second (1000000 microseconds)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                null
            } finally {
                retriever.release()
            }
        }
    }
}
