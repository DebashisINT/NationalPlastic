package com.breezenationalplasticfsm.features.nearbyshops.presentation

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.AppDatabase
import com.breezenationalplasticfsm.app.NetworkConstant
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.domain.AddShopDBModelEntity
import com.breezenationalplasticfsm.app.domain.CallHisEntity
import com.breezenationalplasticfsm.app.types.FragType
import com.breezenationalplasticfsm.app.uiaction.IntentActionable
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.app.utils.PermissionUtils
import com.breezenationalplasticfsm.app.utils.Toaster
import com.breezenationalplasticfsm.app.widgets.MovableFloatingActionButton
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.base.presentation.BaseActivity
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.contacts.CallHisDtls
import com.breezenationalplasticfsm.features.contacts.StageMasterRes
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.leaveapplynew.LeaveHome
import com.breezenationalplasticfsm.features.shopdetail.presentation.api.EditShopRepoProvider
import com.itextpdf.text.BadElementException
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lecho.lib.hellocharts.model.Line
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ShopCallHisFrag : BaseFragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private var permissionUtils: PermissionUtils? = null
    private lateinit var rvCallLogL : RecyclerView
    private lateinit var adapterCallLogL : AdapterCallLogL
    private lateinit var tvShopInitial : TextView
    private lateinit var tvShopName : TextView
    private lateinit var tvShopAddr : TextView
    private lateinit var tvOwnerName : TextView
    private lateinit var tvOwnerContact : TextView
    private lateinit var ivOwnerContact : ImageView
    private lateinit var llShopAddr : LinearLayout
    private lateinit var mFab: MovableFloatingActionButton
    private lateinit var progress_wheel: ProgressWheel

    private lateinit var tv_empty_page_msg_head:TextView
    private lateinit var tv_empty_page_msg:TextView
    private lateinit var ll_no_data_root:LinearLayout
    private lateinit var img_direction:ImageView

    private var shopDtls = AddShopDBModelEntity()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    companion object {
        var shopID: String = ""
        fun getInstance(objects: Any): ShopCallHisFrag {
            val shopCallHisFrag = ShopCallHisFrag()
            if (!TextUtils.isEmpty(objects.toString())) {
                shopID = objects.toString()
            }
            return shopCallHisFrag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_shop_call_log_his, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        rvCallLogL = view.findViewById(R.id.rv_frag_shop_call_log_list)
        tvShopInitial = view.findViewById(R.id.tv_frag_shop_call_his_shop_initial)
        tvShopName = view.findViewById(R.id.tv_frag_shop_call_his_shop_name)
        tvShopAddr = view.findViewById(R.id.tv_frag_shop_call_his_shop_addr)
        llShopAddr = view.findViewById(R.id.ll_frag_shop_call_log_his_addr)
        tvOwnerName = view.findViewById(R.id.tv_frag_shop_call_his_shop_owner_name)
        tvOwnerContact = view.findViewById(R.id.tv_frag_shop_call_his_shop_owner_contact)
        ivOwnerContact = view.findViewById(R.id.iv_frag_shop_call_his_shop_owner_contact)
        progress_wheel = view.findViewById(R.id.progress_wheel_frag_shop_Call_his)

        mFab = view.findViewById(R.id.fab_frag_call_his_share)

        tv_empty_page_msg_head = view.findViewById(R.id.tv_empty_page_msg_head)
        tv_empty_page_msg = view.findViewById(R.id.tv_empty_page_msg)
        ll_no_data_root = view.findViewById(R.id.ll_no_data_root)
        img_direction = view.findViewById(R.id.img_direction)
        img_direction.visibility = View.GONE

        shopDtls = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopID)
        tvShopInitial.text = shopDtls.shopName!!.get(0).toString()
        tvShopName.text = shopDtls.shopName!!
        tvShopAddr.text = shopDtls.address!!
        tvOwnerName.text = shopDtls.ownerName
        tvOwnerContact.text = shopDtls.ownerContactNumber

        llShopAddr.setOnClickListener(this)
        ivOwnerContact.setOnClickListener(this)

        saveCallHisToDB()

        mFab.setCustomClickListener {
            if((AppDatabase.getDBInstance()!!.callhisDao().getCallListByID(shopID) as ArrayList<CallHisEntity>).size>0){
                generatePdf(AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopByIdN(shopID)!!,AppDatabase.getDBInstance()!!.callhisDao().getCallListByID(shopID) as ArrayList<CallHisEntity>)
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.ll_frag_shop_call_log_his_addr -> {
                var intentGmap: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=${shopDtls.shopLat},${shopDtls.shopLong}&mode=1"))
                intentGmap.setPackage("com.google.android.apps.maps")
                if(intentGmap.resolveActivity(mContext.packageManager) !=null){
                    mContext.startActivity(intentGmap)
                }
            }
            R.id.iv_frag_shop_call_his_shop_owner_contact ->{
                try{
                    IntentActionable.initiatePhoneCall(mContext, tvOwnerContact.text.toString())
                }catch (ex:Exception){
                    ex.printStackTrace()
                }
            }
        }
    }

    fun saveCallHisToDB(){
        doAsync {
            progress_wheel.spin()
            //var callHisL = AppUtils.obtenerDetallesLlamadas(mContext) as ArrayList<AppUtils.Companion.PhoneCallDtls>
            var callHisL = AppUtils.obtenerDetallesLlamadasByNumber(mContext,shopDtls.ownerContactNumber) as ArrayList<AppUtils.Companion.PhoneCallDtls>
            if(callHisL.size>0){
                for(i in 0..callHisL.size-1){
                    try{
                        var obj:CallHisEntity = CallHisEntity()
                        var callNo = if(callHisL.get(i).number!!.length>10) callHisL.get(i).number!!.replace("+","").removeRange(0,2) else callHisL.get(i).number!!
                        var isMyShop = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdPhone(shopID!!,callNo) as ArrayList<AddShopDBModelEntity>
                        if(isMyShop.size>0){
                            obj.apply {
                                shop_id = shopID
                                call_number = callNo
                                call_date = callHisL.get(i).callDateTime!!.split(" ").get(0)
                                call_time = callHisL.get(i).callDateTime!!.split(" ").get(1)
                                call_date_time = callHisL.get(i).callDateTime!!
                                call_type = callHisL.get(i).type!!
                                if(call_type.equals("MISSED",ignoreCase = true)){
                                    call_duration_sec = "0"
                                }else{
                                    call_duration_sec = callHisL.get(i).callDuration!!
                                }
                                call_duration = AppUtils.getMMSSfromSeconds(call_duration_sec.toInt())
                            }
                            var isPresent = (AppDatabase.getDBInstance()!!.callhisDao().getFilterData(obj.call_number,obj.call_date,obj.call_time,obj.call_type,obj.call_duration_sec) as ArrayList<CallHisEntity>).size
                            if(isPresent==0){
                                Timber.d("tag_log_insert ${obj.call_number} ${obj.call_duration}")
                                AppDatabase.getDBInstance()!!.callhisDao().insert(obj)
                            }
                        }
                    }catch (ex:Exception){
                        ex.printStackTrace()
                    }
                }
            }
            uiThread {
                progress_wheel.stopSpinning()
                showCallHisData()
            }
        }
    }

    fun showCallHisData(){
        var callL = AppDatabase.getDBInstance()!!.callhisDao().getCallListByID(shopID) as ArrayList<CallHisEntity>
        if(callL.size>0){
            tvOwnerContact.text = shopDtls.ownerContactNumber+" (Call Count : ${callL.size})"
            ll_no_data_root.visibility = View.GONE
            rvCallLogL.visibility = View.VISIBLE
            adapterCallLogL = AdapterCallLogL(mContext,callL,true,object :AdapterCallLogL.onClick{
                override fun onSyncClick(obj: CallHisEntity) {
                    println("tag_check ${obj.call_number}")
                    syncCallHisInfo(obj)
                }
            })
            rvCallLogL.adapter=adapterCallLogL
        }else{
            ll_no_data_root.visibility = View.VISIBLE
            tv_empty_page_msg_head.text = "No record found."
            tv_empty_page_msg.visibility = View.GONE
            rvCallLogL.visibility = View.GONE
            //(mContext as DashboardActivity).showSnackMessage(getString(R.string.no_data_available))
        }
    }

    fun syncCallHisInfo(obj: CallHisEntity) {
        var syncObj: CallHisDtls = CallHisDtls()
        syncObj.user_id = Pref.user_id.toString()
        syncObj.call_his_list.add(obj)


        val repository = EditShopRepoProvider.provideEditShopWithoutImageRepository()
        BaseActivity.compositeDisposable.add(
            repository.callLogListSaveApi(syncObj)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val resp = result as BaseResponse
                    if (resp.status == NetworkConstant.SUCCESS) {
                        AppDatabase.getDBInstance()!!.callhisDao().updateCallHisIsUpload(
                            obj.shop_id,
                            obj.call_number,
                            obj.call_time,
                            obj.call_date,
                            true
                        )
                        (mContext as DashboardActivity).showSnackMessage("Synced successfully")
                        showCallHisData()
                    } else {
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    }
                }, { error ->
                    error.printStackTrace()
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                })
        )


    }

    fun generatePdf(shopObj:AddShopDBModelEntity,callHisL:ArrayList<CallHisEntity>){
        var document: Document = Document(PageSize.A4, 36f, 36f, 36f, 80f)
        val time = System.currentTimeMillis()
        var fileName = "CallHistory" +  "_" + time
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/FSMApp/CallHis/"

        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        try{
            var pdfWriter : PdfWriter = PdfWriter.getInstance(document, FileOutputStream(path + fileName + ".pdf"))
            document.open()

            var font: Font = Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD)
            var font1: Font = Font(Font.FontFamily.HELVETICA, 9f, Font.NORMAL)
            var fontBoldU: Font = Font(Font.FontFamily.HELVETICA, 9f, Font.UNDERLINE or Font.BOLD)
            var fontBoldUHeader: Font = Font(Font.FontFamily.HELVETICA, 12f, Font.UNDERLINE or Font.BOLD)

            //image add
            //val bm: Bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            val bm: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.breezelogo)
            val bitmap = Bitmap.createScaledBitmap(bm, 50, 50, true);
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            var img: Image? = null
            val byteArray: ByteArray = stream.toByteArray()
            try {
                img = Image.getInstance(byteArray)
                img.scaleToFit(70f, 70f)
                img.scalePercent(50f)
                img.alignment = Image.ALIGN_LEFT
            } catch (e: BadElementException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            document.add(img)

            val para = Paragraph()
            para.alignment = Element.ALIGN_CENTER
            para.indentationLeft = 220f
            val glue = Chunk(VerticalPositionMark())
            val ph = Phrase()
            ph.add(Chunk("Call History", fontBoldUHeader))
            ph.add(glue)
            ph.add(Chunk("DATE: " + AppUtils.getCurrentDate_DD_MM_YYYY() + " ", font1))
            para.add(ph)
            document.add(para)

            val spac = Paragraph("", font)
            spac.spacingAfter = 15f
            document.add(spac)

            val HeadingPartyDetls = Paragraph("Details of ${Pref.shopText} ", fontBoldU)
            //HeadingPartyDetls.indentationLeft = 82f
            HeadingPartyDetls.spacingAfter = 2f
            document.add(HeadingPartyDetls)

            val Parties = Paragraph("Name                    :      " + shopObj?.shopName, font1)
            Parties.alignment = Element.ALIGN_LEFT
            Parties.spacingAfter = 2f
            document.add(Parties)

            val address = Paragraph("Address                :      " + shopObj?.address, font1)
            address.alignment = Element.ALIGN_LEFT
            address.spacingAfter = 2f
            document.add(address)

            val Contact = Paragraph("Contact No.          :      " + shopObj?.ownerContactNumber, font1)
            Contact.alignment = Element.ALIGN_LEFT
            Contact.spacingAfter = 2f
            document.add(Contact)

            document.add(spac)

            var widths = floatArrayOf(0.12f, 0.22f,0.22f,0.22f,0.22f )
            var tableHeader: PdfPTable = PdfPTable(widths)
            tableHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT)
            tableHeader.setWidthPercentage(100f)

            val cell1 = PdfPCell(Phrase("SL. ", font))
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT)
            cell1.borderColor = BaseColor.GRAY
            tableHeader.addCell(cell1);

            val cell2 = PdfPCell(Phrase("Call Date", font))
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT)
            cell2.borderColor = BaseColor.GRAY
            tableHeader.addCell(cell2);

            val cell3 = PdfPCell(Phrase("Call Time", font))
            cell3.setHorizontalAlignment(Element.ALIGN_LEFT)
            cell3.borderColor = BaseColor.GRAY
            tableHeader.addCell(cell3);

            val cell4 = PdfPCell(Phrase("Duration", font))
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT)
            cell4.borderColor = BaseColor.GRAY
            tableHeader.addCell(cell4);

            val cell5 = PdfPCell(Phrase("Call Type", font))
            cell5.setHorizontalAlignment(Element.ALIGN_LEFT)
            cell5.borderColor = BaseColor.GRAY
            tableHeader.addCell(cell5);

            document.add(tableHeader)

            for(i in 0..callHisL.size-1){
                val tableRows = PdfPTable(widths)
                tableRows.defaultCell.horizontalAlignment = Element.ALIGN_CENTER
                tableRows.setWidthPercentage(100f);

                var cellBodySl = PdfPCell(Phrase("${i+1}", font1))
                cellBodySl.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellBodySl.borderColor = BaseColor.GRAY
                tableRows.addCell(cellBodySl)

                var cellBodyCallDt = PdfPCell(Phrase("${AppUtils.getFormatedDateNew(callHisL.get(i).call_date,"yyyy-mm-dd","dd-mm-yyyy")}", font1))
                cellBodyCallDt.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellBodyCallDt.borderColor = BaseColor.GRAY
                tableRows.addCell(cellBodyCallDt)

                var cellBodyCallTime = PdfPCell(Phrase("${callHisL.get(i).call_time}", font1))
                cellBodyCallTime.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellBodyCallTime.borderColor = BaseColor.GRAY
                tableRows.addCell(cellBodyCallTime)

                var cellBodyCallDuration = PdfPCell(Phrase("${callHisL.get(i).call_duration}", font1))
                cellBodyCallDuration.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellBodyCallDuration.borderColor = BaseColor.GRAY
                tableRows.addCell(cellBodyCallDuration)

                var cellBodyCallType = PdfPCell(Phrase("${callHisL.get(i).call_type}", font1))
                cellBodyCallType.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellBodyCallType.borderColor = BaseColor.GRAY
                tableRows.addCell(cellBodyCallType)

                document.add(tableRows)

                document.add(Paragraph())
            }


            document.close()

            var sendingPath = path + fileName + ".pdf"
            if (!TextUtils.isEmpty(sendingPath)) {
                try {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    val fileUrl = Uri.parse(sendingPath)
                    val file = File(fileUrl.path)
                    val uri: Uri = FileProvider.getUriForFile(mContext, mContext.applicationContext.packageName.toString() + ".provider", file)
                    shareIntent.type = "image/png"
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                    startActivity(Intent.createChooser(shareIntent, "Share pdf using"))
                } catch (e: Exception) {
                    e.printStackTrace()
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong1))
                }
            }

        }catch (ex:Exception){
            ex.printStackTrace()
        }


    }

}