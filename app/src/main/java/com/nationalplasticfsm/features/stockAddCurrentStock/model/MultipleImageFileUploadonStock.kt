package com.nationalplasticfsm.features.stockAddCurrentStock.model

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.NetworkConstant
import com.nationalplasticfsm.app.Pref
import com.nationalplasticfsm.app.domain.AddShopDBModelEntity
import com.nationalplasticfsm.app.utils.AppUtils
import com.nationalplasticfsm.app.utils.PermissionUtils
import com.nationalplasticfsm.app.utils.ProcessImageUtils_v1
import com.nationalplasticfsm.base.BaseResponse
import com.nationalplasticfsm.base.presentation.BaseActivity
import com.nationalplasticfsm.base.presentation.BaseFragment
import com.nationalplasticfsm.features.addshop.api.AddShopRepositoryProvider
import com.nationalplasticfsm.features.addshop.model.ImagestockwiseListResponse
import com.nationalplasticfsm.features.addshop.model.assigntopplist.AddshopImageMultiReqbody1
import com.nationalplasticfsm.features.addshop.model.imageListResponse
import com.nationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.nationalplasticfsm.features.nearbyshops.multipleattachImage.MultipleImageFragment
import com.nationalplasticfsm.features.stockAddCurrentStock.ViewStockDetailsFragment
import com.nationalplasticfsm.widgets.AppCustomTextView
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.pnikosis.materialishprogress.ProgressWheel
import com.themechangeapp.pickimage.PermissionHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.io.File
import java.io.FileInputStream

// created by saheli mantis 26101 12-05-2023
// 1.0 mantis 26013 saheli 15-05-2023 v04.0.8
class MultipleImageFileUploadonStock: BaseFragment(),
    View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var iv_hand1_click_one: ImageView
    private lateinit var iv_upload_attach_one: ImageView
    private lateinit var iv_upload_attach_two: ImageView
    private lateinit var iv_hand1_click_two: ImageView
    private lateinit var upload_tvClick: AppCustomTextView
    private lateinit var tv_text1_image1: TextView
    private lateinit var tv_text2_image2:TextView
    private lateinit var iv_down1_image1: ImageView
    private lateinit var iv_down2_image2: ImageView
    private var isDocDegree = -1
    private var imagePathupload: String = ""
    private var imagePathupload2: String = ""
    private var degreeImgLink = ""
    private var dataPath = ""
    private var dataPath1 = ""

    // start 1.0 rev mantis 26013 saheli v 4.0.8 15-05-2023
    private var str_imageView1: String = ""
    private var str_imageView2: String = ""
    // end  1.0 rev

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    companion object{
        var stockID: String = ""
        fun getInstance(objects: Any): MultipleImageFileUploadonStock {
            val stockIdFragment = MultipleImageFileUploadonStock()
            if (!TextUtils.isEmpty(objects.toString())) {
                stockID=objects.toString()
            }
            return stockIdFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_mutliple_image_stock_attach, container, false)
        initView(view)
        return view
    }

    @SuppressLint("RestrictedApi")
    private fun initView(view: View) {
        initPermissionCheck()
        progress_wheel = view.findViewById(R.id.progress_wheel)
        progress_wheel.stopSpinning()
        iv_hand1_click_one = view.findViewById(R.id.iv_hand1_frag_multiple_image_stock)
        iv_upload_attach_one = view.findViewById(R.id.iv_upload_image_viewfrag_multiple_image_stock)
        iv_upload_attach_two = view.findViewById(R.id.iv_upload_image_view_image1frag_multiple_image_stock)
        iv_hand1_click_two = view.findViewById(R.id.iv_hand2frag_multiple_image_stock)
        upload_tvClick = view.findViewById(R.id.upload_TVfrag_multiple_image_stock)
        tv_text1_image1 = view.findViewById(R.id.tv_text1_frag_multiple_image_stock)
        tv_text2_image2 = view.findViewById(R.id.tv_text2frag_multiple_image_stock)
        iv_down1_image1 = view.findViewById(R.id.iv_down1_frag_multiple_image_stock)
        iv_down2_image2 = view.findViewById(R.id.iv_down2_frag_multiple_image_stock)

        iv_hand1_click_one.setOnClickListener(this)
        iv_down1_image1.setOnClickListener(this)
        iv_down1_image1.visibility = View.GONE
        iv_down2_image2.visibility = View.GONE
        iv_hand1_click_two.setOnClickListener(this)
        iv_down2_image2.setOnClickListener(this)
        // start 1.0 rev mantis 26013 saheli v 4.0.8 15-05-2023
        upload_tvClick.visibility = View.GONE
        upload_tvClick.setOnClickListener(this)


        if (!AppUtils.isOnline(mContext)) {
            (this as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }else{
            fetchStockwiseImageList()
        }
        // end 1.0 rev mantis 26013 saheli v 4.0.8 15-05-2023
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_hand1_frag_multiple_image_stock -> {
                isDocDegree = 0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    initPermissionCheckOne()
                else
                    showPictureDialog()
            }

            R.id.iv_hand2frag_multiple_image_stock -> {
                isDocDegree = 1
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    initPermissionCheckOne()
                else
                    showPictureDialog()
            }

            R.id.iv_down1_frag_multiple_image_stock -> {
              val  strFileName1 = str_imageView1.substring(str_imageView1.lastIndexOf("/")!! + 1)
                downloadFile(str_imageView1, strFileName1)
            }

            R.id.iv_down2_frag_multiple_image_stock -> {
                val  strFileName2 = str_imageView2.substring(str_imageView2.lastIndexOf("/")!! + 1)
                downloadFile(str_imageView2, strFileName2)
            }

            // start 1.0 rev mantis 26013 saheli v 4.0.8 15-05-2023
            R.id.upload_TVfrag_multiple_image_stock -> {
                if (!AppUtils.isOnline(mContext)) {
                    (this as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                    return
                } else if (str_imageView1.equals("")) {
                    addStockMutlipleUploadImg1(stockID)
                } else if (str_imageView2.equals("")) {
                    addStockMutlipleUploadImg2(stockID)
                } else {

                }
            }
            // end 1.0 rev mantis 26013 saheli v 4.0.8 15-05-2023

        }
    }

        private var permissionUtils: PermissionUtils? = null
        private fun initPermissionCheckOne() {
            permissionUtils = PermissionUtils(
                mContext as Activity,
                object : PermissionUtils.OnPermissionListener {
                    override fun onPermissionGranted() {
                        showPictureDialog()
                    }

                    override fun onPermissionNotGranted() {
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.accept_permission))
                    }

                },
                arrayOf<String>(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }

        private fun initPermissionCheck() {
            permissionUtils = PermissionUtils(
                mContext as Activity,
                object : PermissionUtils.OnPermissionListener {
                    override fun onPermissionGranted() {
                    }

                    override fun onPermissionNotGranted() {
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.accept_permission))
                    }

                },
                arrayOf<String>(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }

        fun showPictureDialog() {
            val pictureDialog = AlertDialog.Builder(mContext)
            pictureDialog.setTitle("Select Action")
            val pictureDialogItems = arrayOf(
                "Select photo from gallery",
                "Capture photo from camera",
                "Select file from file manager"
            )
            pictureDialog.setItems(pictureDialogItems) { dialog, which ->
                when (which) {
                    0 -> {
                        //isDocDegree = 0
                        selectImageInAlbum()
                    }
                    1 -> {
                        //isDocDegree = 1
                        launchCamera()
                    }
                    2 -> {
                        //isDocDegree = 2
                        (mContext as DashboardActivity).openPDFFileManager()
                    }
                }
            }
            pictureDialog.show()
        }

        fun onRequestPermission(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            permissionUtils?.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

        private fun selectImageInAlbum() {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            (mContext as DashboardActivity).startActivityForResult(
                galleryIntent,
                PermissionHelper.REQUEST_CODE_STORAGE
            )
        }

        fun launchCamera() {
            if (PermissionHelper.checkCameraPermission(mContext as DashboardActivity) && PermissionHelper.checkStoragePermission(
                    mContext as DashboardActivity
                )
            ) {
                (mContext as DashboardActivity).captureImage()
            }

        }

        fun setImage(file: File) {
            progress_wheel.spin()
            if (isDocDegree == 0) {
                (tv_text1_image1 as TextView).text = file.name
                upload_tvClick.visibility = View.VISIBLE
                progress_wheel.stopSpinning()
                dataPath = file.absolutePath
                imagePathupload = dataPath

            } else if (isDocDegree == 1) {
                (tv_text2_image2 as TextView).text = file.name
                upload_tvClick.visibility = View.VISIBLE
                progress_wheel.stopSpinning()
                dataPath1 = file.absolutePath
                imagePathupload2 = dataPath1

            }

        }

        fun setImagecapture(filePath: String) {
            progress_wheel.spin()
            val file = File(filePath)
            var newFile: File? = null
            println("stock_img set img setImagecapture hit $isDocDegree")
            doAsync {
                val processImage = ProcessImageUtils_v1(mContext, file, 50, false)
                newFile = processImage.ProcessImageSelfie()
                val fileSize = AppUtils.getCompressOldImage(filePath, mContext)
                uiThread {
                    if (newFile != null) {
                        if (isDocDegree == 0) {
                            tv_text1_image1.setText(newFile!!.name)
                            progress_wheel.stopSpinning()
                            upload_tvClick.visibility = View.VISIBLE
                            imagePathupload = newFile!!.absolutePath
                            val f = File(newFile!!.absolutePath)
                            val options: BitmapFactory.Options = BitmapFactory.Options()
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888
                            var bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, options)
                        } else if (isDocDegree == 1) {
                            tv_text2_image2.setText(newFile!!.name)
                            progress_wheel.stopSpinning()
                            upload_tvClick.visibility = View.VISIBLE
                            imagePathupload2 = newFile!!.absolutePath
                            val f = File(newFile!!.absolutePath)
                            val options: BitmapFactory.Options = BitmapFactory.Options()
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888
                            var bitmap =
                                BitmapFactory.decodeStream(FileInputStream(f), null, options)
                        }

                    } else {
                        // Image compression
                        upload_tvClick.visibility = View.VISIBLE
                        progress_wheel.stopSpinning()
                        if (fileSize <= 5 * 1024) {
                            degreeImgLink = newFile?.absolutePath!!.toString()
                        } else{
                            (mContext as DashboardActivity).showSnackMessage("Image size can not be greater than 5 MB")
                        }


                    }
                }
            }
        }

        //from gallery
        fun setImageFromPath(filePath: String) {
            progress_wheel.spin()
            val file = File(filePath)
            var newFile: File? = null
            doAsync {
                val processImage = ProcessImageUtils_v1(mContext, file, 50)
                newFile = processImage.ProcessImage()
                uiThread {
                    if (newFile != null) {
                        val fileSize = AppUtils.getCompressOldImage(filePath, mContext)
                        if (isDocDegree == 0) {
                            imagePathupload = newFile?.absolutePath!!
                            progress_wheel.stopSpinning()
                            tv_text1_image1.setText(newFile?.absolutePath!!.split("/").last())
                            upload_tvClick.visibility = View.VISIBLE
                        } else if (isDocDegree == 1) {
                            progress_wheel.stopSpinning()
                            imagePathupload2 = newFile?.absolutePath!!.toString()
                            tv_text2_image2.setText(newFile?.absolutePath!!.split("/").last())
                            upload_tvClick.visibility = View.VISIBLE
                        } else {
                            progress_wheel.stopSpinning()
                            upload_tvClick.visibility = View.VISIBLE
                            if (fileSize <= 5 * 1024 * 1024) {
                                degreeImgLink = newFile?.absolutePath!!.toString()
                            } else{
                                (mContext as DashboardActivity).showSnackMessage("Image size can not be greater than 5 MB")
                            }
                        }
                    }
                }
            }
        }

    // start 1.0 rev mantis 26013 saheli v 4.0.8 15-05-2023
        private fun downloadFile(downloadUrl: String?, fileName: String) {
            try {
                if (!AppUtils.isOnline(mContext)) {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                    return
                }
                PRDownloader.download(
                    downloadUrl, Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS
                    ).toString() + File.separator, fileName
                )
                    .build()
                    .setOnProgressListener {
                        progress_wheel.spin()
                        Timber.d("Listing ", "Attachment Download Progress======> $it")
                    }
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            doAsync {
                                uiThread {
                                    progress_wheel.stopSpinning()
                                    (mContext as DashboardActivity).showSnackMessage("File Downloaded")
                                }
                            }
                        }

                        override fun onError(error: Error) {
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage("Download failed")
                        }
                    })

            } catch (e: Exception) {
                progress_wheel.stopSpinning()
                (mContext as DashboardActivity).showSnackMessage("Download failed Exception")
                e.printStackTrace()
            }

        }


        private fun addStockMutlipleUploadImg1(stockID: String) {
            progress_wheel.spin()
            if(!str_imageView1.equals("")){
                addStockMutlipleUploadImg2(stockID)
            }else{
                var objCompetetor: AddstockImageMultiReqbody1 = AddstockImageMultiReqbody1()
                objCompetetor.session_token = Pref.session_token
                objCompetetor.stock_id = stockID
                objCompetetor.user_id = Pref.user_id

                val repository = AddShopRepositoryProvider.provideAddShopRepository()
                BaseActivity.compositeDisposable.add(
                    repository.addStockWithImageuploadMultipleImg1(objCompetetor, imagePathupload, mContext)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as BaseResponse
                            if (response.status == NetworkConstant.SUCCESS) {
                                iv_down1_image1.visibility = View.VISIBLE
                                progress_wheel.stopSpinning()
                                if (!imagePathupload2.equals("")) {
                                    addStockMutlipleUploadImg2(stockID)
                                }
                                else{
                                    openDialogPopup("Hi ${Pref.user_name} !","upload 1 attcmentment successfully.")
                                }
                            }
                        }, { error ->
                            progress_wheel.stopSpinning()
                            if (error != null) {
                                Timber.d("AddStock : Image Upload 1" + ", SHOP: " + stockID + ", ERROR: " + error.localizedMessage)
                            }
                        })
                )
            }
        }

        private fun addStockMutlipleUploadImg2(stockID: String) {
            progress_wheel.spin()
            var objCompetetor: AddstockImageMultiReqbody1 = AddstockImageMultiReqbody1()
            objCompetetor.session_token = Pref.session_token
            objCompetetor.stock_id = stockID
            objCompetetor.user_id = Pref.user_id
                val repository = AddShopRepositoryProvider.provideAddShopRepository()
                BaseActivity.compositeDisposable.add(
                    repository.addStockWithImageuploadMultipleImg2(objCompetetor, imagePathupload2, mContext)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as BaseResponse
                            if (response.status == NetworkConstant.SUCCESS) {
                                progress_wheel.stopSpinning()
                                iv_down2_image2.visibility = View.VISIBLE
                                openDialogPopup("Hi ${Pref.user_name} !","upload 2 attcmentment successfully.")

                            }
                        }, { error ->
                            if (error != null) {
                                progress_wheel.stopSpinning()
                                openDialogPopup("Hi ${Pref.user_name} !","upload 2 attcmentment not successfully.")
                                Timber.d("AddStock : Image Upload 1" + ", SHOP: " + stockID + ", ERROR: " + error.localizedMessage)
                            }
                        })
                )
        }

        fun openDialogPopup(header:String,text:String){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_ok_imei)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_yes_header) as AppCustomTextView
        val dialogBody = simpleDialog.findViewById(R.id.dialog_yes_body) as AppCustomTextView
        dialogHeader.text = header
        dialogBody.text = text
        val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
            if(text.contains("successfully",ignoreCase = true)){
                fetchStockwiseImageList()
            }
        })
        simpleDialog.show()
    }

    private fun fetchStockwiseImageList() {
        progress_wheel.spin()
        try{
            println("stock_img fetchStockwiseImageList hit")
            val repository = AddShopRepositoryProvider.provideAddShopWithoutImageRepository()
            BaseActivity.compositeDisposable.add(
                repository.getStockwiseimagelist(stockID)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val viewResult = result as ImagestockwiseListResponse
                        println("stock_img fetchStockwiseImageList ${viewResult.status}")
                        if (viewResult!!.status == NetworkConstant.SUCCESS) {
                            progress_wheel.stopSpinning()
                            str_imageView1 = viewResult!!.stockwise_image_list!!.get(0).attachment_stock_image1
                            str_imageView2 = viewResult!!.stockwise_image_list!!.get(0).attachment_stock_image2
                            if(str_imageView1.contains("CommonFolder")){
                                tv_text1_image1.setText(str_imageView1.split("/").last())
                            }else{
                                str_imageView1=""
                            }
                            if(str_imageView2.contains("CommonFolder")){
                                tv_text2_image2.setText(str_imageView2.split("/").last())
                            }else{
                                str_imageView2=""
                            }
                            if(str_imageView1!=""){
                                if(str_imageView2!=""){
                                            iv_down1_image1.visibility = View.VISIBLE
                                            iv_down2_image2.visibility = View.VISIBLE
                                            upload_tvClick.visibility = View.GONE
                                            iv_hand1_click_one.visibility = View.GONE
                                            iv_hand1_click_two.visibility = View.GONE
                                        }
                                        else{
                                            iv_down1_image1.visibility = View.VISIBLE
                                            iv_down2_image2.visibility = View.GONE
                                            upload_tvClick.visibility = View.VISIBLE
                                            iv_hand1_click_one.visibility = View.GONE
                                        }
                                    }
                                    else{
                                        iv_down1_image1.visibility = View.GONE
                                        iv_down2_image2.visibility = View.GONE
                                        upload_tvClick.visibility = View.VISIBLE
                                        iv_hand1_click_one.visibility = View.VISIBLE
                                        iv_hand1_click_two.visibility = View.VISIBLE
                                    }
                                }
                                else {
                                println("stock_img fetchStockwiseImageList else ${viewResult.status}")
                                progress_wheel.stopSpinning()
                                //(mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong1))
                        }
                        },
                        { error ->
                        progress_wheel.stopSpinning()
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    })
            )
        }
        catch (ex:Exception){
            ex.printStackTrace()
            progress_wheel.stopSpinning()
        }
    }

    // end 1.0 rev mantis 26013 saheli v 4.0.8 15-05-2023
    }
