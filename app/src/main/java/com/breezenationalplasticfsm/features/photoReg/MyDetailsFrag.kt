package com.breezenationalplasticfsm.features.photoReg

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.FileProvider
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.NetworkConstant
import com.breezenationalplasticfsm.app.NewFileUtils
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.app.utils.FTStorageUtils
import com.breezenationalplasticfsm.app.utils.Toaster
import com.breezenationalplasticfsm.base.presentation.BaseActivity
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.photoReg.api.GetUserListPhotoRegProvider
import com.breezenationalplasticfsm.features.photoReg.model.GetUserListResponse
import com.breezenationalplasticfsm.features.photoReg.model.UserFacePicUrlResponse
import com.breezenationalplasticfsm.features.photoReg.model.UserListResponseModel
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader

import com.squareup.picasso.Cache
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.io.File

class MyDetailsFrag : BaseFragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var ivPic: ImageView
    private lateinit var ivDoc: ImageView
    private lateinit var ivShare: ImageView
    private lateinit var ivDocShare: ImageView
    private lateinit var picUrl: String
    private lateinit var docUrl: String

    var userList: ArrayList<UserListResponseModel> = ArrayList()

    private lateinit var progress_wheel: com.pnikosis.materialishprogress.ProgressWheel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_my_details, container, false)
        initView(view)

        if (AppUtils.isOnline(mContext))
            getPicUrl()
            //getRegDoc()
        else{
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            progress_wheel.stopSpinning()
        }
        return view
    }

    private fun initView(view: View) {
        picUrl=""
        docUrl=""
        ivPic = view.findViewById(R.id.iv_frag_my_details_face_pic)
        ivShare = view.findViewById(R.id.iv_frag_my_details_face_pic_share)
        ivDoc = view.findViewById(R.id.iv_frag_my_details_doc_pic)
        ivDocShare = view.findViewById(R.id.iv_frag_my_details_doc_pic_share)
        ivShare.setOnClickListener(this)
        ivDocShare.setOnClickListener(this)

        progress_wheel = view.findViewById(R.id.progress_wheel)
    }

    fun getPicUrl(){
        progress_wheel.spin()
        BaseActivity.isApiInitiated=false
        val repository = GetUserListPhotoRegProvider.provideUserListPhotoReg()
        BaseActivity.compositeDisposable.add(
                repository.getUserFacePicUrlApi(Pref.user_id!!, Pref.session_token!!)
                //repository.getUserFacePicUrlApi("50560", Pref.session_token!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as UserFacePicUrlResponse
                            if(response.status== NetworkConstant.SUCCESS){
                                picUrl = response.face_image_link!!
                                val picasso = Picasso.Builder(mContext)
                                        .memoryCache(Cache.NONE)
                                        .indicatorsEnabled(false)
                                        .loggingEnabled(true)
                                        .build()

                                picasso.load(Uri.parse(picUrl))
                                        .centerCrop()
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .networkPolicy(NetworkPolicy.NO_CACHE)
                                        .resize(500, 500)
                                        .into(ivPic)

                                Handler(Looper.getMainLooper()).postDelayed({
                                    progress_wheel.stopSpinning()
                                    getRegDoc()
                                }, 2000)


                            }else{
                                BaseActivity.isApiInitiated = false
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_reg_face))
                                Timber.d("MyDetailsFrag : FaceGet : " + response.status.toString() +", : "  + ", Failed: ")
                                progress_wheel.stopSpinning()
                            }
                        },{
                            error ->
                            if (error != null) {
                                Timber.d("MyDetailsFrag : FaceGet : " + " : "  + ", ERROR: " + error.localizedMessage)
                            }
                            progress_wheel.stopSpinning()
                            BaseActivity.isApiInitiated = false
                        })
        )
    }

    fun getRegDoc(){
        progress_wheel.spin()
        val repository = GetUserListPhotoRegProvider.provideUserListPhotoReg()
        BaseActivity.compositeDisposable.add(
                repository.getUserListApi(Pref.user_id!!, Pref.session_token!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            var response = result as GetUserListResponse
                            if (response.status == NetworkConstant.SUCCESS) {
                                if (response.user_list!!.size > 0 && response.user_list!! != null) {
                                        userList = response.user_list!!
                                        for(i in 0..userList.size-1){
                                            if(userList.get(i).user_id.toString().equals(Pref.user_id)){
                                                if(userList.get(i).aadhar_image_link!!.contains("CommonFolder")){
                                                    docUrl = userList.get(i).aadhar_image_link!!

                                                    val picasso = Picasso.Builder(mContext)
                                                            .memoryCache(Cache.NONE)
                                                            .indicatorsEnabled(false)
                                                            .loggingEnabled(true)
                                                            .build()

                                                    picasso.load(Uri.parse(docUrl))
                                                            .centerCrop()
                                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                            .networkPolicy(NetworkPolicy.NO_CACHE)
                                                            .resize(500, 500)
                                                            .into(ivDoc)

                                                    Handler(Looper.getMainLooper()).postDelayed({
                                                        progress_wheel.stopSpinning()
                                                    }, 1000)

                                                }
                                            }
                                        }

                                } else {
                                    progress_wheel.stopSpinning()
                                    (mContext as DashboardActivity).showSnackMessage("Document Not Found")
                                }
                            } else {
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).showSnackMessage("Document Not Found")
                            }
                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
//                            (mContext as DashboardActivity).showSnackMessage("ERROR")
                        })
        )
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_frag_my_details_face_pic_share -> {
                if(picUrl.equals("")){
                    (mContext as DashboardActivity).showSnackMessage("No URL found")
                }else{
                    var fileName = File(picUrl).name
                    downloadFile(picUrl,fileName)
                }
            }
            R.id.iv_frag_my_details_doc_pic_share->{
                if(docUrl.equals("")){
                    (mContext as DashboardActivity).showSnackMessage("No URL found")
                }else{
                var fileName = File(docUrl).name
                downloadFile(docUrl,fileName)
                }
            }
        }
    }

    private fun shareDoc(attachment: String) {
        val file = File(attachment)
        val mimeType = NewFileUtils.getMemeTypeFromFile(file.absolutePath + "." + NewFileUtils.getExtension(file))

        if (mimeType?.equals("application/pdf")!!)
            shareFile(attachment, "application/pdf")
        else if (mimeType == "application/msword")
            shareFile(attachment, "application/msword")
        else if (mimeType == "application/vnd.ms-excel")
            shareFile(attachment, "application/vnd.ms-excel")
        else if (mimeType == "application/vnd.openxmlformats-officedocument.wordprocessingml.template")
            shareFile(attachment, "application/vnd.openxmlformats-officedocument.wordprocessingml.template")
        else if (mimeType == "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
            shareFile(attachment, "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
        else if (mimeType == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            shareFile(attachment, "application/vnd.ms-excel")
        else if (mimeType == "application/vnd.openxmlformats-officedocument.spreadsheetml.template")
            shareFile(attachment, "application/vnd.ms-excel")
        else if (mimeType == "image/jpeg" || mimeType == "image/png" || mimeType == "image/jpg")
            shareFile(attachment, "image/*")
    }

    private fun shareFile(attachment: String, mimeType: String) {
        val intent = Intent(Intent.ACTION_SEND)
        val fileUrl = Uri.parse(File(attachment).path)

        val file = File(fileUrl.path)
        //val uri = Uri.fromFile(file)
        val uri:Uri= FileProvider.getUriForFile(mContext, requireContext().applicationContext.packageName.toString() + ".provider", file)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.type = mimeType
        startActivity(Intent.createChooser(intent, "Share Image via..."))

    }

    private fun downloadFile(downloadUrl: String?, fileName: String) {
        try {

            if (!AppUtils.isOnline(mContext)){
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                return
            }

//            progress_wheel.spin()



            PRDownloader.download(downloadUrl, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + File.separator, fileName)
                    .build()
                    .setOnProgressListener {
                        Log.e("Document List", "Attachment Download Progress======> $it")
                    }
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {

                            doAsync {

                                uiThread {
//                                    progress_wheel.stopSpinning()
                                    var filePath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + File.separator+ fileName
                                    (mContext as DashboardActivity).showSnackMessage("File Downloaded")
                                    shareDoc(filePath )

                                }
                            }
                        }

                        override fun onError(error: Error) {
//                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage("Download failed")
                            Log.e("Image Details", "Attachment download error msg=======> " + error.serverErrorMessage)
                        }
                    })

        } catch (e: Exception) {
            (mContext as DashboardActivity).showSnackMessage("Download failed")
//            progress_wheel.stopSpinning()
            e.printStackTrace()
        }

    }
}