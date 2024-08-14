package com.breezenationalplasticfsm.features.orderITC

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.breezedsm.app.domain.NewOrderDataEntity
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.AppDatabase
import com.breezenationalplasticfsm.app.NetworkConstant
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.types.FragType
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.app.utils.ToasterMiddle
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.base.presentation.BaseActivity
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.login.api.productlistapi.ProductListRepoProvider
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker
import com.itextpdf.text.BadElementException
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Image
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.html.WebColors
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ViewNewOrdHisAllFrag: BaseFragment(), View.OnClickListener {
    private lateinit var mContext: Context

    private lateinit var tv_ordCnt:TextView
    private lateinit var rv_ordDtls:RecyclerView
    private lateinit var progress_wheel: ProgressWheel

    private lateinit var ll_no_data_root: LinearLayout
    private lateinit var tv_noDataHeader:TextView
    private lateinit var tv_noDataBody:TextView
    private lateinit var img_direction: ImageView

    private lateinit var adapterNewOrdDtls:AdapterNewOrdDtls

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_view_ord_his_all, container, false)
        initView(view)
        return view
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun initView(view: View?) {
        tv_ordCnt = view!!.findViewById(R.id.tv_frag_view_all_ord_count)
        rv_ordDtls = view.findViewById(R.id.rv_frag_view_all_ord_rv)
        progress_wheel = view.findViewById(R.id.pw_frag_view_all_ord)

        ll_no_data_root = view.findViewById(R.id.ll_no_data_root)
        tv_noDataHeader = view.findViewById(R.id.tv_empty_page_msg_head)
        tv_noDataBody = view.findViewById(R.id.tv_empty_page_msg)
        img_direction = view.findViewById(R.id.img_direction)

        img_direction.visibility = View.GONE
        tv_noDataBody.visibility = View.GONE
        tv_noDataHeader.text = "No order found."

        progress_wheel.stopSpinning()

        setData()

    }

    fun setData(){
        progress_wheel.spin()

        var ordL = AppDatabase.getDBInstance()!!.newOrderDataDao().getAllOrderOrderBy() as ArrayList<NewOrderDataEntity>
        if(ordL.size>0){
            ll_no_data_root.visibility=View.GONE
            rv_ordDtls.visibility=View.VISIBLE
            tv_ordCnt.visibility=View.VISIBLE
            tv_ordCnt.text = "Total Order(s) : ${ordL.size}"
            progress_wheel.spin()
            adapterNewOrdDtls = AdapterNewOrdDtls(mContext,ordL,object :AdapterNewOrdDtls.OnCLick{
                override fun onShareCLick(obj: NewOrderDataEntity) {
                    sharePDF(obj)
                }

                override fun onViewCLick(obj: NewOrderDataEntity) {
                    (mContext as DashboardActivity).loadFragment(FragType.ViewOrdDtls,true,obj.order_id)
                }

                override fun onSyncCLick(obj: NewOrderDataEntity) {
                    if(AppUtils.isOnline(mContext)){
                        syncOrd(obj.order_id)
                    }else{
                        ToasterMiddle.msgShort(mContext,mContext.getString(R.string.login_net_disconnected1))
                    }
                }

            })
            rv_ordDtls.adapter = adapterNewOrdDtls
            Handler().postDelayed(Runnable {
                progress_wheel.stopSpinning()
            }, 2000)

        }else{
            progress_wheel.stopSpinning()
            ll_no_data_root.visibility=View.VISIBLE
            rv_ordDtls.visibility=View.GONE
            tv_ordCnt.visibility=View.GONE
        }
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    private fun sharePDF(obj: NewOrderDataEntity){
        var document: Document = Document()
        var fileName = "FTS"+ "_" + obj.order_id
        fileName = fileName.replace("/", "_")
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +"/ORDERDETALIS/"

        var pathNew = ""

        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        try {
            try {
                PdfWriter.getInstance(document, FileOutputStream(path + fileName + ".pdf"))
            }catch (ex:Exception){
                ex.printStackTrace()

                pathNew = mContext.filesDir.toString() + "/" + fileName+".pdf"
                //val file = File(mContext.filesDir.toString() + "/" + fileName)
                PdfWriter.getInstance(document, FileOutputStream(pathNew))
            }
            document.open()
            var font: Font = Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD)
            var font1: Font = Font(Font.FontFamily.HELVETICA, 8f, Font.NORMAL)
            var fontBoldU: Font = Font(Font.FontFamily.HELVETICA, 11f, Font.UNDERLINE or Font.BOLD)
            var fontBoldUColor: Font = Font(Font.FontFamily.HELVETICA, 12f, Font.UNDERLINE or Font.BOLD)
            val myColorpan = WebColors.getRGBColor("#196f84")
            fontBoldUColor.setColor(myColorpan)

            val space10f = Paragraph("", font)
            space10f.spacingAfter = 10f
            val space15f = Paragraph("", font)
            space15f.spacingAfter = 15f

            //image add begin
            val bm: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.breezelogo)
            val bitmap = Bitmap.createScaledBitmap(bm, 80, 80, true);
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            var img: Image? = null
            val byteArray: ByteArray = stream.toByteArray()
            try {
                img = Image.getInstance(byteArray)
                img.scaleToFit(110f, 110f)
                img.scalePercent(70f)
                img.alignment = Image.ALIGN_LEFT
            } catch (e: BadElementException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            //image add end

            val heading = Paragraph("Order Details", fontBoldUColor)
            heading.alignment = Element.ALIGN_CENTER
            val pHead = Paragraph()
            pHead.add(Chunk(img, 0f, -30f))
            pHead.add(heading)
            document.add(pHead)

            document.add(space15f)

            val ordNo = Paragraph("Order No      :   " + obj.order_id, font)
            ordNo.alignment = Element.ALIGN_LEFT
            ordNo.spacingAfter = 2f
            document.add(ordNo)

            val ordDate = Paragraph("Order Date   :   " + AppUtils.convertToDateLikeOrderFormat(obj.order_date), font)
            ordDate.alignment = Element.ALIGN_LEFT
            ordDate.spacingAfter = 2f
            document.add(ordDate)

            document.add(space10f)

            val partyHead = Paragraph("Details of Party", fontBoldU)
            partyHead.indentationLeft = 72f
            partyHead.spacingAfter = 2f
            document.add(partyHead)

            val shop = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopByIdN(obj.shop_id)

            val Parties = Paragraph("Name                 :     " + shop?.shopName, font1)
            Parties.alignment = Element.ALIGN_LEFT
            Parties.spacingAfter = 2f
            document.add(Parties)

            val address = Paragraph("Address              :     " + shop?.address, font1)
            address.alignment = Element.ALIGN_LEFT
            address.spacingAfter = 2f
            document.add(address)

            val Contact = Paragraph("Contact No.        :     " + shop?.ownerContactNumber, font1)
            Contact.alignment = Element.ALIGN_LEFT
            Contact.spacingAfter = 2f
            document.add(Contact)

            document.add(space15f)

            //product table
            var widths = floatArrayOf(0.06f, 0.30f,0.16f,0.16f,0.16f,0.16f )

            var tableHeader: PdfPTable = PdfPTable(widths)
            tableHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT)
            tableHeader.setWidthPercentage(100f)

            val slNo = PdfPCell(Phrase("SL.", font))
            slNo.setHorizontalAlignment(Element.ALIGN_LEFT)
            slNo.borderColor = BaseColor.GRAY
            tableHeader.addCell(slNo)

            val itemDesc = PdfPCell(Phrase("Item Description", font))
            itemDesc.setHorizontalAlignment(Element.ALIGN_LEFT)
            itemDesc.borderColor = BaseColor.GRAY
            tableHeader.addCell(itemDesc);

            val itemUOM = PdfPCell(Phrase("UOM", font))
            itemUOM.setHorizontalAlignment(Element.ALIGN_CENTER);
            itemUOM.borderColor = BaseColor.GRAY
            tableHeader.addCell(itemUOM)

            val itemQty = PdfPCell(Phrase("Qty", font))
            itemQty.setHorizontalAlignment(Element.ALIGN_CENTER);
            itemQty.borderColor = BaseColor.GRAY
            tableHeader.addCell(itemQty)

            val itemRate = PdfPCell(Phrase("Special Rate", font))
            itemRate.setHorizontalAlignment(Element.ALIGN_CENTER);
            itemRate.borderColor = BaseColor.GRAY
            tableHeader.addCell(itemRate)

            val itemTotal = PdfPCell(Phrase("Total", font))
            itemTotal.setHorizontalAlignment(Element.ALIGN_CENTER);
            itemTotal.borderColor = BaseColor.GRAY
            tableHeader.addCell(itemTotal)

            document.add(tableHeader)

            var sLNo: String = ""
            var item: String = ""
            var uom: String = ""
            var qty: String = ""
            var rate: String = ""
            var total: String = ""

            var ordProductL = AppDatabase.getDBInstance()!!.newOrderProductDataDao().getProductsOrder(obj.order_id)
            for(i in 0..ordProductL.size-1){
                sLNo = (i+1).toString() +" "
                item = ordProductL!!.get(i).product_name +  "       "
                uom = AppDatabase.getDBInstance()!!.newProductListDao().getProductDtls(ordProductL.get(i).product_id).UOM.toString()
                qty = ordProductL!!.get(i).submitedQty +" "
                rate = ordProductL!!.get(i).submitedSpecialRate.toString() +" "
                total =  String.format("%.02f",(ordProductL!!.get(i).submitedQty.toDouble() * ordProductL!!.get(i).submitedSpecialRate.toDouble()))

                val tableRows = PdfPTable(widths)
                tableRows.defaultCell.horizontalAlignment = Element.ALIGN_CENTER
                tableRows.setWidthPercentage(100f)

                var cell1 = PdfPCell(Phrase(sLNo, font1))
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell1.borderColor = BaseColor.GRAY
                tableRows.addCell(cell1)

                var cell2 = PdfPCell(Phrase(item, font1))
                cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell2.borderColor = BaseColor.GRAY
                tableRows.addCell(cell2)

                var cell3_0 = PdfPCell(Phrase(uom, font1))
                cell3_0.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3_0.borderColor = BaseColor.GRAY
                tableRows.addCell(cell3_0)

                var cell3 = PdfPCell(Phrase(qty, font1))
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.borderColor = BaseColor.GRAY
                tableRows.addCell(cell3)

                var cell4 = PdfPCell(Phrase(rate, font1))
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.borderColor = BaseColor.GRAY
                tableRows.addCell(cell4)

                var cell5 = PdfPCell(Phrase(total, font1))
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell5.borderColor = BaseColor.GRAY
                tableRows.addCell(cell5)

                document.add(tableRows)

                document.add(Paragraph())
            }

            val totalOrdAmt = Paragraph("Total Order Amount :  " + String.format("%.02f",obj.order_total_amt.toDouble())+"          ", font)
            totalOrdAmt.alignment = Element.ALIGN_RIGHT
            address.spacingAfter = 2f
            document.add(totalOrdAmt)

            document.add(space10f)

            // sales person name begin
            val enteredBy = Paragraph("Entered by : ${Pref.user_name}", font1)
            enteredBy.alignment = Element.ALIGN_LEFT
            enteredBy.spacingAfter = 2f
            document.add(enteredBy)
            // sales person name end

            document.close()

            var sendingPath = path + fileName + ".pdf"
            if(!pathNew.equals("")){
                sendingPath = pathNew
            }
            try{
                val shareIntent = Intent(Intent.ACTION_SEND)
                val fileUrl = Uri.parse(sendingPath)
                val file = File(fileUrl.path)
                val uri: Uri = FileProvider.getUriForFile(mContext, mContext.applicationContext.packageName.toString() + ".provider", file)
                shareIntent.type = "image/png"
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                startActivity(Intent.createChooser(shareIntent, "Share pdf using"))
            }catch (ex:Exception){
                ex.printStackTrace()
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
            }

        }catch (ex:Exception){
            ex.printStackTrace()
        }

    }

    private fun syncOrd(ordId:String){
        progress_wheel.spin()
        var ordDtls = AppDatabase.getDBInstance()!!.newOrderDataDao().getOrderByID(ordId)
        var ordProductDtls = AppDatabase.getDBInstance()!!.newOrderProductDataDao().getProductsOrder(ordId)
        var syncOrd = SyncOrd()
        var syncOrdProductL:ArrayList<SyncOrdProductL> = ArrayList()

        doAsync {
            syncOrd.user_id = Pref.user_id!!
            syncOrd.order_id = ordId
            syncOrd.order_date = ordDtls.order_date
            syncOrd.order_time = ordDtls.order_time
            syncOrd.order_date_time = ordDtls.order_date_time
            syncOrd.shop_id = ordDtls.shop_id
            syncOrd.shop_name = ordDtls.shop_name
            syncOrd.shop_type = ordDtls.shop_type
            syncOrd.isInrange = if(ordDtls.isInrange) 1 else 0
            syncOrd.order_lat = ordDtls.order_lat
            syncOrd.order_long = ordDtls.order_long
            syncOrd.shop_addr = ordDtls.shop_addr
            syncOrd.shop_pincode = ordDtls.shop_pincode
            syncOrd.order_total_amt = ordDtls.order_total_amt.toDouble()
            syncOrd.order_remarks = ordDtls.order_remarks

            for(i in 0..ordProductDtls.size-1){
                var obj = SyncOrdProductL()
                obj.order_id=ordProductDtls.get(i).order_id
                obj.product_id=ordProductDtls.get(i).product_id
                obj.product_name=ordProductDtls.get(i).product_name
                obj.submitedQty=ordProductDtls.get(i).submitedQty.toDouble()
                obj.submitedSpecialRate=ordProductDtls.get(i).submitedSpecialRate.toDouble()

                syncOrdProductL.add(obj)
            }
            syncOrd.product_list = syncOrdProductL

            uiThread {
                val repository = ProductListRepoProvider.productListProvider()
                BaseActivity.compositeDisposable.add(
                    repository.syncProductListITC(syncOrd)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as BaseResponse
                            if (response.status == NetworkConstant.SUCCESS) {
                                doAsync {
                                    AppDatabase.getDBInstance()!!.newOrderDataDao().updateIsUploaded(syncOrd.order_id,true)
                                    uiThread {
                                        progress_wheel.stopSpinning()
                                        ToasterMiddle.msgShort(mContext,mContext.getString(R.string.sync_done))
                                        setData()
                                    }
                                }
                            } else {
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            }
                        }, { error ->
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
                )
            }
        }

    }


}