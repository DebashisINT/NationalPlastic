package com.nationalplasticfsm.features.photoReg

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.hardware.camera2.CameraCharacteristics
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.nationalplasticfsm.CustomStatic
import com.nationalplasticfsm.MySingleton
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.NetworkConstant
import com.nationalplasticfsm.app.Pref
import com.nationalplasticfsm.app.types.FragType
import com.nationalplasticfsm.app.uiaction.IntentActionable
import com.nationalplasticfsm.app.utils.AppUtils
import com.nationalplasticfsm.app.utils.PermissionUtils
import com.nationalplasticfsm.app.utils.Toaster
import com.nationalplasticfsm.base.presentation.BaseActivity
import com.nationalplasticfsm.base.presentation.BaseFragment
import com.nationalplasticfsm.faceRec.DetectorActivity
import com.nationalplasticfsm.faceRec.FaceStartActivity
import com.nationalplasticfsm.faceRec.FaceStartActivity.detector
import com.nationalplasticfsm.faceRec.tflite.SimilarityClassifier.Recognition
import com.nationalplasticfsm.faceRec.tflite.TFLiteObjectDetectionAPIModel
import com.nationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.nationalplasticfsm.features.photoReg.api.GetUserListPhotoRegProvider
import com.nationalplasticfsm.features.photoReg.model.FaceRegResponse
import com.nationalplasticfsm.features.photoReg.model.UserListResponseModel
import com.nationalplasticfsm.features.photoReg.model.UserPhotoRegModel
import com.nationalplasticfsm.widgets.AppCustomTextView
import com.elvishew.xlog.XLog
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.pnikosis.materialishprogress.ProgressWheel
import com.themechangeapp.pickimage.PermissionHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_register_face.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.*

class RegisTerFaceFragment: BaseFragment(), View.OnClickListener {
    private lateinit var mContext: Context
    private var imagePath: String = ""
    private lateinit var nameTV: AppCustomTextView
    private lateinit var phoneTV: AppCustomTextView
    private lateinit var registerTV: Button
    private lateinit var registerTV_voter: Button
    private lateinit var registerTV_pan: Button
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var ll_phone : LinearLayout

    private lateinit var tv_register : TextView
    private lateinit var ll_docRoot : LinearLayout

    private lateinit var shopLargeImg:ImageView
    private lateinit var photoRegCameraIcon:ImageView

    var takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    lateinit var imgUri:Uri
    var facePicTag:Boolean = false

    ////
    protected var previewWidth = 0
    protected var previewHeight = 0
    private var portraitBmp: Bitmap? = null
    private var rgbFrameBitmap: Bitmap? = null
    private var faceBmp: Bitmap? = null
    var faceDetector: FaceDetector? = null
    private val TF_OD_API_MODEL_FILE = "mobile_face_net.tflite"
    val TF_OD_API_IS_QUANTIZED = false
    val TF_OD_API_LABELS_FILE = "file:///android_asset/labelmap.txt"
    val TF_OD_API_INPUT_SIZE = 112

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    companion object{
        var user_id: String? = null
        var user_name: String? = null
        var user_login_id: String? = null
        var user_contactid: String? = null
        var isOnlyFacePic: Boolean? = false
        var valueData:UserListResponseModel = UserListResponseModel()
        fun getInstance(objects: Any): RegisTerFaceFragment {
            val regisTerFaceFragment = RegisTerFaceFragment()
            if (!TextUtils.isEmpty(objects.toString())) {

                var obj = objects as UserListResponseModel
                valueData=obj
                user_id=obj!!.user_id.toString()
                user_name=obj!!.user_name
                user_login_id=obj!!.user_login_id
                user_contactid=obj!!.user_contactid
                isOnlyFacePic=obj!!.IsShowManualPhotoRegnInApp
            }
            return regisTerFaceFragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_register_face, container, false)
        initView(view)
        return view
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initView(view:View){
        nameTV = view.findViewById(R.id.tv_frag_reg_face_name)
        phoneTV = view.findViewById(R.id.tv_frag_reg_face_phone)
        registerTV = view.findViewById(R.id.btn_frag_reg_face_register_aadhaar)
        registerTV_voter = view.findViewById(R.id.btn_frag_reg_face_register_voter)
        registerTV_pan = view.findViewById(R.id.btn_frag_reg_face_register_pan)
        photoRegCameraIcon = view.findViewById(R.id.iv_frag_photo_reg_face_camera_icon)
        tv_register = view.findViewById(R.id.tv_frag_register_face_register)
        ll_docRoot = view.findViewById(R.id.ll_frag_register_face_doc_root)

        progress_wheel = view.findViewById(R.id.progress_wheel)
        progress_wheel.stopSpinning()
        registerTV.setOnClickListener(this)
        registerTV_voter.setOnClickListener(this)
        registerTV_pan.setOnClickListener(this)
        photoRegCameraIcon.setOnClickListener(this)
        tv_register.setOnClickListener(this)

        nameTV.text = user_name!!
        phoneTV.text = user_login_id!!

        shopLargeImg = view.findViewById(R.id.iv_frag_reg_face)

        ll_phone = view.findViewById(R.id.ll_regis_face_phone);
        ll_phone.setOnClickListener(this)

        if(Pref.IsShowInPortalManualPhotoRegn){
            //if(Pref.IsShowManualPhotoRegnInApp){
            if(isOnlyFacePic!!){
                tv_register.text="Register"
                ll_docRoot.visibility=View.GONE
            }}


        faceDetectorSetUp()
        faceDetectorSetUpRandom()

      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            initPermissionCheck()
        else {
            launchCamera()
        }*/
        //showPictureDialog()

        //startActivity(Intent(mContext, CustomCameraActivity::class.java))

        launchCamera()


        //(mContext as DashboardActivity).loadFragment(FragType.PhotoRegAadhaarFragment,true,valueData)
    }


    fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(mContext)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems) { dialog, which ->
            when (which) {
                0 -> selectImageInAlbum()
                1 -> launchCamera()
            }
        }
        pictureDialog.show()
    }

    fun selectImageInAlbum() {
        if (PermissionHelper.checkStoragePermission(mContext as DashboardActivity)) {
            val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            (mContext as DashboardActivity).startActivityForResult(intent, PermissionHelper.REQUEST_CODE_STORAGE)
        }

    }


    private var permissionUtils: PermissionUtils? = null
    private fun initPermissionCheck() {
        permissionUtils = PermissionUtils(mContext as Activity, object : PermissionUtils.OnPermissionListener {
            override fun onPermissionGranted() {
                launchCamera()
            }

            override fun onPermissionNotGranted() {
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.accept_permission))
            }

        }, arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    fun launchCamera() {
        if (PermissionHelper.checkCameraPermission(mContext as DashboardActivity) && PermissionHelper.checkStoragePermission(mContext as DashboardActivity)) {
            /*val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, (mContext as DashboardActivity).getPhotoFileUri(System.currentTimeMillis().toString() + ".png"))
            (mContext as DashboardActivity).startActivityForResult(intent, PermissionHelper.REQUEST_CODE_CAMERA)*/

//            Pref.FaceRegistrationOpenFrontCamera = true
//            Pref.FaceRegistrationFrontCamera = true
            if(Pref.FaceRegistrationOpenFrontCamera && Pref.FaceRegistrationFrontCamera){
                (mContext as DashboardActivity).captureFrontImage()
            }
            else{
                (mContext as DashboardActivity).captureImage()
            }

//            (mContext as DashboardActivity).captureImage()
        }
    }

    fun setImage(imgRealPath: Uri, fileSizeInKB: Long) {
        imgUri=imgRealPath
        imagePath = imgRealPath.toString()

        if(imagePath!=""){
            photoRegCameraIcon.visibility=View.GONE
        }

        getBitmap(imgRealPath.path)


            /*Picasso.get()
                    .load(imgRealPath)
                    .resize(500, 500)
                    .into(shopLargeImg)*/

    }


    private fun registerFaceApiNewFlow(){
        CustomStatic.FaceRegFaceImgPath=imagePath
        if(CustomStatic.IsAadhaarForPhotoReg)
            showAadhaarIns(valueData,getString(R.string.aadhaar_reg_guide_header),getString(R.string.aadhaar_reg_guide_body))
        else if(CustomStatic.IsVoterForPhotoReg)
            showAadhaarIns(valueData,getString(R.string.voter_reg_guide_header),getString(R.string.voter_reg_guide_body))
        else if(CustomStatic.IsPanForPhotoReg)
            showAadhaarIns(valueData,getString(R.string.pan_reg_guide_header),getString(R.string.pan_reg_guide_body))
    }

    private fun registerFaceApi(){

        progress_wheel.spin()
        var obj= UserPhotoRegModel()
        //obj.user_id= Pref.user_id
        obj.user_id= user_id
        obj.session_token=Pref.session_token

        //obj.registration_date_time=AppUtils.getCurrentDateTimeDDMMYY()
        obj.registration_date_time=AppUtils.getCurrentDateTime()

        val repository = GetUserListPhotoRegProvider.providePhotoReg()
        BaseActivity.compositeDisposable.add(
                repository.addUserFaceRegImg(obj,imagePath,mContext,user_contactid)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as FaceRegResponse
                            if(response.status== NetworkConstant.SUCCESS){
                                XLog.d("Face Reg Url : "+response.face_image_link)
                                //(mContext as DashboardActivity).showSnackMessage(getString(R.string.face_reg_success))
                                Handler(Looper.getMainLooper()).postDelayed({
                                    progress_wheel.stopSpinning()
                                    //(mContext as DashboardActivity).loadFragment(FragType.ProtoRegistrationFragment, false, "")
                                    CustomStatic.FacePicRegUrl=response.face_image_link

                                    //afterFaceRegistered()
                                    afterFaceRegisteredOne()

                           /*         if(CustomStatic.IsAadhaarForPhotoReg)
                                        showAadhaarIns(valueData,getString(R.string.aadhaar_reg_guide_header),getString(R.string.aadhaar_reg_guide_body))
                                    else if(CustomStatic.IsVoterForPhotoReg)
                                        showAadhaarIns(valueData,getString(R.string.voter_reg_guide_header),getString(R.string.voter_reg_guide_body))
                                    else if(CustomStatic.IsPanForPhotoReg)
                                        showAadhaarIns(valueData,getString(R.string.pan_reg_guide_header),getString(R.string.pan_reg_guide_body))*/

                                    //(mContext as DashboardActivity).loadFragment(FragType.PhotoRegAadhaarFragment,true,valueData)
                                }, 500)

                                XLog.d(" RegisTerFaceFragment : FaceImageDetection/FaceImage" +response.status.toString() +", : "  + ", Success: "+AppUtils.getCurrentDateTime().toString())
                            }else{
                                progress_wheel.stopSpinning()
                                CustomStatic.FacePicRegUrl=""
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_reg_face))
                                XLog.d("RegisTerFaceFragment : FaceImageDetection/FaceImage : " + response.status.toString() +", : "  + ", Failed: "+AppUtils.getCurrentDateTime().toString())
                            }
                        },{
                            error ->
                            progress_wheel.stopSpinning()
                            CustomStatic.FacePicRegUrl=""
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_reg_face))
                            if (error != null) {
                                XLog.d("RegisTerFaceFragment : FaceImageDetection/FaceImage : " + " : "  + ", ERROR: " + error.localizedMessage)
                            }
                        })
        )
    }

    private fun showAadhaarIns(valueData: UserListResponseModel,headerTxt:String,bodyTxt:String){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(true)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_message_face_aadhaar_guide)
        val body = simpleDialog.findViewById(R.id.dialog_message_header_TV) as TextView
        val header = simpleDialog.findViewById(R.id.dialog_message_headerTV) as TextView
        val iv_photo = simpleDialog.findViewById(R.id.iv_dialog_msg_face_aadhaar_g) as ImageView


        if(CustomStatic.IsAadhaarForPhotoReg)
            iv_photo.setImageDrawable(getResources().getDrawable(R.drawable.aadhar_sample));
        else if(CustomStatic.IsVoterForPhotoReg)
            iv_photo.setImageDrawable(getResources().getDrawable(R.drawable.voter_sample));
        else if(CustomStatic.IsPanForPhotoReg)
            iv_photo.setImageDrawable(getResources().getDrawable(R.drawable.pan_sample));

        header.text = headerTxt
        body.text = bodyTxt

        val dialogYes = simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            //simpleDialog.cancel()

            val simpleDialogYN = Dialog(mContext)
            simpleDialogYN.setCancelable(false)
            simpleDialogYN.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            simpleDialogYN.setContentView(R.layout.dialog_yes_no)
            val dialogHeader = simpleDialogYN.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
            val dialog_yes_no_headerTV = simpleDialogYN.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
            dialog_yes_no_headerTV.text = "Hi "+Pref.user_name!!+"!"
            dialogHeader.text = "Have you read the instruction?"
            val dialogYes = simpleDialogYN.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
            val dialogNo = simpleDialogYN.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView
            dialogYes.setOnClickListener({ view ->
                simpleDialog.cancel()
                simpleDialogYN.cancel()
                (mContext as DashboardActivity).loadFragment(FragType.PhotoRegAadhaarFragment,true,valueData)
            })
            dialogNo.setOnClickListener({ view ->
                simpleDialogYN.cancel()
            })
            simpleDialogYN.show()


        })
        simpleDialog.show()
    }

    fun afterFaceRegistered(){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_ok_cancel_new)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
        dialogHeader.text = "Face Registered successfully."
        val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
        val dialogNo = simpleDialog.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
            (mContext as DashboardActivity).loadFragment(FragType.PhotoRegAadhaarFragment,true,valueData)
        })
        dialogNo.setOnClickListener({ view ->
            simpleDialog.cancel()
        })
        simpleDialog.show()
    }

    private fun afterFaceRegisteredOne(){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_message)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
        val dialog_yes_no_headerTV = simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
        dialog_yes_no_headerTV.text = "Hi "+Pref.user_name!!+"!"
        dialogHeader.text = "Face Registered successfully."
        val dialogYes = simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
            (mContext as DashboardActivity).loadFragment(FragType.ProtoRegistrationFragment,false,valueData)
        })
        simpleDialog.show()
    }

    override fun onClick(p0: View?) {
        if(p0!=null){
            when(p0.id){
                R.id.btn_frag_reg_face_register_aadhaar -> {
                    CustomStatic.IsAadhaarForPhotoReg=true
                    CustomStatic.IsVoterForPhotoReg=false
                    CustomStatic.IsPanForPhotoReg=false
                    registerClick()
                }
                R.id.btn_frag_reg_face_register_voter -> {
                    CustomStatic.IsAadhaarForPhotoReg=false
                    CustomStatic.IsVoterForPhotoReg=true
                    CustomStatic.IsPanForPhotoReg=false
                    registerClick()
                }
                R.id.btn_frag_reg_face_register_pan -> {
                    CustomStatic.IsAadhaarForPhotoReg=false
                    CustomStatic.IsVoterForPhotoReg=false
                    CustomStatic.IsPanForPhotoReg=true
                    registerClick()
                }

                R.id.ll_regis_face_phone ->{
                    IntentActionable.initiatePhoneCall(mContext, phoneTV.text.toString())
                }
                R.id.iv_frag_photo_reg_face_camera_icon->{
                    launchCamera()
                }
                R.id.tv_frag_register_face_register->{
                    if(Pref.IsShowInPortalManualPhotoRegn){
                        //if(Pref.IsShowManualPhotoRegnInApp){
                        if(isOnlyFacePic!!){
                            registerClick()
                        }}

                }


            }
        }
    }

    private fun registerClick(){
        //if(registerTV.isEnabled==false){
        if(!facePicTag){
            Toaster.msgShort(mContext,"Please capture valid image")
            return
        }

        if(imagePath.length>0 && imagePath!="") {
            //registerFaceApi()

            //registerFaceApiNewFlow()

            if(Pref.IsShowInPortalManualPhotoRegn){
                //if(Pref.IsShowManualPhotoRegnInApp){
                if(isOnlyFacePic!!){
                    tv_register.text="Register"
                    ll_docRoot.visibility=View.GONE
                    val simpleDialogg = Dialog(mContext)
                    simpleDialogg.setCancelable(false)
                    simpleDialogg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    simpleDialogg.setContentView(R.layout.dialog_yes_no)
                    val dialogHeader = simpleDialogg.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
                    dialogHeader.text="Do you want to Register ?"
                    val dialogYes = simpleDialogg.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
                    val dialogNo = simpleDialogg.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView

                    dialogYes.setOnClickListener( { view ->
                        simpleDialogg.cancel()
                        if (AppUtils.isOnline(mContext)){
                            registerFaceApi()
                        }else{
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                        }

                    })
                    dialogNo.setOnClickListener( { view ->
                        simpleDialogg.cancel()
                    })
                    simpleDialogg.show()
                }else{
                    registerFaceApiNewFlow()
                }
            }
            else{
                registerFaceApiNewFlow()
            }

        }

        // registerFaceApi()
    }





    private fun saveImageToGallery() {
        iv_frag_reg_face.setRotation(90f)
        iv_frag_reg_face.setDrawingCacheEnabled(true)
        val b: Bitmap = iv_frag_reg_face.getDrawingCache()
        MediaStore.Images.Media.insertImage(requireActivity().contentResolver, b, imgUri.toString(), "")
    }

////////////////////////////////////////////////////////////


    fun faceDetectorSetUp(){
        try {
            FaceStartActivity.detector = TFLiteObjectDetectionAPIModel.create(
                    mContext.getAssets(),
                    TF_OD_API_MODEL_FILE,
                    TF_OD_API_LABELS_FILE,
                    TF_OD_API_INPUT_SIZE,
                    TF_OD_API_IS_QUANTIZED)
            //cropSize = TF_OD_API_INPUT_SIZE;
        } catch (e: IOException) {
            e.printStackTrace()
            //LOGGER.e(e, "Exception initializing classifier!");
            val toast = Toast.makeText(mContext, "Classifier could not be initialized", Toast.LENGTH_SHORT)
            toast.show()
            //finish()
        }
        val options = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setContourMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build()

        val detector = FaceDetection.getClient(options)

        faceDetector = detector
    }


    lateinit var face1:Face
    lateinit var face2:Face

    private fun registerFace(mBitmap: Bitmap?) {

/*        val face_dec:com.google.android.gms.vision.face.FaceDetector = com.google.android.gms.vision.face.FaceDetector.Builder(mContext)
                .setTrackingEnabled(false)
                .setLandmarkType(com.google.android.gms.vision.face.FaceDetector.ALL_LANDMARKS)
                .setMode(com.google.android.gms.vision.face.FaceDetector.FAST_MODE)
                .build()

        val frame: Frame = Frame.Builder().setBitmap(mBitmap).build()
        //val faces: SparseArray = face_dec.detect(frame)
        val facesa: SparseArray<com.google.android.gms.vision.face.Face> = face_dec.detect(frame)
        var ss="asf"
        Toaster.msgShort(mContext,"facesa"+facesa.size().toString())*/

        try {
            if (mBitmap == null) {
                //Toast.makeText(this, "No File", Toast.LENGTH_SHORT).show()
                return
            }
            //ivFace.setImageBitmap(mBitmap)
            previewWidth = mBitmap.width
            previewHeight = mBitmap.height
            rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888)
            portraitBmp = mBitmap
            val image = InputImage.fromBitmap(mBitmap, 0)
            faceBmp = Bitmap.createBitmap(TF_OD_API_INPUT_SIZE, TF_OD_API_INPUT_SIZE, Bitmap.Config.ARGB_8888)
            faceDetector?.process(image)?.addOnSuccessListener(OnSuccessListener<List<Face>> { faces ->
                if (faces.size == 0) {
                    Toaster.msgShort(mContext,"Please choose proper face")
                    facePicTag=false
                    return@OnSuccessListener
                }
                Handler().post {
                    object : Thread() {
                        override fun run() {
                            //action
                            //onFacesDetected(1, faces, true) //no need to add currtime
                            face1=faces.get(0)
                            activateRegisterFace()
                        }
                    }.start()
                }
            })


        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun activateRegisterFace(){
        doAsync {
            uiThread {
                //registerTV.isEnabled=true
                //faceDetector?.close()
                facePicTag=true
                //GetImageFromUrl().execute("http://3.7.30.86:82/CommonFolder/FaceImageDetection/EMS0000070.jpg")
                //Toaster.msgShort(mContext,"face present in pic")
            }
        }


    }

    //fun getBitmap(path: String?): Bitmap? {
    fun getBitmap(path: String?) {
        var bitmap: Bitmap? = null
        try {
            val f = File(path)
            val options: BitmapFactory.Options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, options)
            shopLargeImg.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        registerFace(bitmap)
        //return bitmap
    }


    ///////////////////////////////////////testtttttttttt
    var faceDetectorRandom: FaceDetector? = null

    fun faceDetectorSetUpRandom(){
        try {
            FaceStartActivity.detector = TFLiteObjectDetectionAPIModel.create(
                    mContext.getAssets(),
                    TF_OD_API_MODEL_FILE,
                    TF_OD_API_LABELS_FILE,
                    TF_OD_API_INPUT_SIZE,
                    TF_OD_API_IS_QUANTIZED)
            //cropSize = TF_OD_API_INPUT_SIZE;
        } catch (e: IOException) {
            e.printStackTrace()
            //LOGGER.e(e, "Exception initializing classifier!");
            val toast = Toast.makeText(mContext, "Classifier could not be initialized", Toast.LENGTH_SHORT)
            toast.show()
            //finish()
        }
        val options = FaceDetectorOptions.Builder()
                //.setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setContourMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build()

        val detector = FaceDetection.getClient(options)

        faceDetectorRandom = detector
    }

    private fun registerFaceRandom(mBitmap: Bitmap?) {

/*        val face_dec:com.google.android.gms.vision.face.FaceDetector = com.google.android.gms.vision.face.FaceDetector.Builder(mContext)
                .setTrackingEnabled(false)
                .setLandmarkType(com.google.android.gms.vision.face.FaceDetector.ALL_LANDMARKS)
                .setMode(com.google.android.gms.vision.face.FaceDetector.FAST_MODE)
                .build()

        val frame: Frame = Frame.Builder().setBitmap(mBitmap).build()
        //val faces: SparseArray = face_dec.detect(frame)
        val facesa: SparseArray<com.google.android.gms.vision.face.Face> = face_dec.detect(frame)
        var ss="asf"
        Toaster.msgShort(mContext,"facesa"+facesa.size().toString())*/

        try {
            if (mBitmap == null) {
                //Toast.makeText(this, "No File", Toast.LENGTH_SHORT).show()
                return
            }
            //ivFace.setImageBitmap(mBitmap)
            previewWidth = mBitmap.width
            previewHeight = mBitmap.height
            rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888)
            portraitBmp = mBitmap
            val image = InputImage.fromBitmap(mBitmap, 0)
            faceBmp = Bitmap.createBitmap(TF_OD_API_INPUT_SIZE, TF_OD_API_INPUT_SIZE, Bitmap.Config.ARGB_8888)
            faceDetectorRandom?.process(image)?.addOnSuccessListener(OnSuccessListener<List<Face>> { faces ->
                if (faces.size == 0) {
                    Toaster.msgShort(mContext,"Please choose proper face")
                    facePicTag=false
                    return@OnSuccessListener
                }
                Handler().post {
                    object : Thread() {
                        override fun run() {
                            //action
                            //onFacesDetected(1, faces, true) //no need to add currtime
                            face2=faces.get(0)
                           var ss="asd"
                            if(face1==face2){
                                var a="as"
                            }else{
                                var b="ty"
                            }
                            onFacesDetected(1, faces, true)
                        }
                    }.start()
                }
            })


        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    var cropToFrameTransform: Matrix? = Matrix()

    fun onFacesDetected(currTimestamp: Long, faces: List<Face>, add: Boolean) {
        val paint = Paint()
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.0f
        val mappedRecognitions: MutableList<Recognition> = LinkedList()


        //final List<Classifier.Recognition> results = new ArrayList<>();

        // Note this can be done only once
        val sourceW = rgbFrameBitmap!!.width
        val sourceH = rgbFrameBitmap!!.height
        val targetW = portraitBmp!!.width
        val targetH = portraitBmp!!.height
        val transform = createTransform(
                sourceW,
                sourceH,
                targetW,
                targetH,
                90)
        val mutableBitmap = portraitBmp!!.copy(Bitmap.Config.ARGB_8888, true)
        val cv = Canvas(mutableBitmap)

        // draws the original image in portrait mode.
        cv.drawBitmap(rgbFrameBitmap!!, transform!!, null)
        val cvFace = Canvas(faceBmp!!)
        val saved = false
        for (face in faces) {
            //results = detector.recognizeImage(croppedBitmap);
            val boundingBox = RectF(face.boundingBox)

            //final boolean goodConfidence = result.getConfidence() >= minimumConfidence;
            val goodConfidence = true //face.get;
            if (boundingBox != null && goodConfidence) {

                // maps crop coordinates to original
                cropToFrameTransform?.mapRect(boundingBox)

                // maps original coordinates to portrait coordinates
                val faceBB = RectF(boundingBox)
                transform.mapRect(faceBB)

                // translates portrait to origin and scales to fit input inference size
                //cv.drawRect(faceBB, paint);
                val sx = TF_OD_API_INPUT_SIZE.toFloat() / faceBB.width()
                val sy = TF_OD_API_INPUT_SIZE.toFloat() / faceBB.height()
                val matrix = Matrix()
                matrix.postTranslate(-faceBB.left, -faceBB.top)
                matrix.postScale(sx, sy)
                cvFace.drawBitmap(portraitBmp!!, matrix, null)

                //canvas.drawRect(faceBB, paint);
                var label = ""
                var confidence = -1f
                var color = Color.BLUE
                var extra: Any? = null
                var crop: Bitmap? = null
                if (add) {
                    try {
                        crop = Bitmap.createBitmap(portraitBmp!!,
                                faceBB.left.toInt(),
                                faceBB.top.toInt(),
                                faceBB.width().toInt(),
                                faceBB.height().toInt())
                    } catch (eon: java.lang.Exception) {
                        //runOnUiThread(Runnable { Toast.makeText(mContext, "Failed to detect", Toast.LENGTH_LONG) })
                    }
                }
                val startTime = SystemClock.uptimeMillis()
                val resultsAux = FaceStartActivity.detector.recognizeImage(faceBmp, add)
                val lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime
                if (resultsAux.size > 0) {
                    val result = resultsAux[0]
                    extra = result.extra
                    //          Object extra = result.getExtra();
//          if (extra != null) {
//            LOGGER.i("embeeding retrieved " + extra.toString());
//          }
                    val conf = result.distance
                    if (conf < 1.0f) {
                        confidence = conf
                        label = result.title
                        color = if (result.id == "0") {
                            Color.GREEN
                        } else {
                            Color.RED
                        }
                    }
                }
                val flip = Matrix()
                flip.postScale(1f, -1f, previewWidth / 2.0f, previewHeight / 2.0f)

                //flip.postScale(1, -1, targetW / 2.0f, targetH / 2.0f);
                flip.mapRect(boundingBox)
                val result = Recognition(
                        "0", label, confidence, boundingBox)
                result.color = color
                result.location = boundingBox
                result.extra = extra
                result.crop = crop
                mappedRecognitions.add(result)
            }
        }

        //    if (saved) {
//      lastSaved = System.currentTimeMillis();
//    }

        Log.e("xc", "startabc" )
        val rec = mappedRecognitions[0]
        FaceStartActivity.detector.register("", rec)
        //val intent = Intent(mContext, DetectorActivity::class.java)
        //startActivityForResult(intent, 171)
//        startActivity(new Intent(this,DetectorActivity.class));
//        finish();

        // detector.register("Sakil", rec);
        /*   runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivFace.setImageBitmap(rec.getCrop());
                //showAddFaceDialog(rec);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.image_edit_dialog, null);
                ImageView ivFace = dialogLayout.findViewById(R.id.dlg_image);
                TextView tvTitle = dialogLayout.findViewById(R.id.dlg_title);
                EditText etName = dialogLayout.findViewById(R.id.dlg_input);

                tvTitle.setText("Register Your Face");
                ivFace.setImageBitmap(rec.getCrop());
                etName.setHint("Please tell your name");
                detector.register("sam", rec); //for register a face

                //button.setPressed(true);
                //button.performClick();
            }

        });*/

        // updateResults(currTimestamp, mappedRecognitions);
    }

    fun createTransform(srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int, applyRotation: Int): Matrix? {
        val matrix = Matrix()
        if (applyRotation != 0) {
            if (applyRotation % 90 != 0) {
                // LOGGER.w("Rotation of %d % 90 != 0", applyRotation);
            }

            // Translate so center of image is at origin.
            matrix.postTranslate(-srcWidth / 2.0f, -srcHeight / 2.0f)

            // Rotate around origin.
            matrix.postRotate(applyRotation.toFloat())
        }

//        // Account for the already applied rotation, if any, and then determine how
//        // much scaling is needed for each axis.
//        final boolean transpose = (Math.abs(applyRotation) + 90) % 180 == 0;
//        final int inWidth = transpose ? srcHeight : srcWidth;
//        final int inHeight = transpose ? srcWidth : srcHeight;
        if (applyRotation != 0) {

            // Translate back from origin centered reference to destination frame.
            matrix.postTranslate(dstWidth / 2.0f, dstHeight / 2.0f)
        }
        return matrix
    }


    inner class GetImageFromUrl : AsyncTask<String?, Void?, Bitmap?>() {
        fun GetImageFromUrl() {
            //this.imageView = img;
        }
        override fun doInBackground(vararg url: String?): Bitmap {
            var bitmappppx: Bitmap? = null
            val stringUrl = url[0]
            bitmappppx = null
            val inputStream: InputStream
            try {
                inputStream = URL(stringUrl).openStream()
                bitmappppx = BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return bitmappppx!!
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            registerFaceRandom(result)
        }

    }


}