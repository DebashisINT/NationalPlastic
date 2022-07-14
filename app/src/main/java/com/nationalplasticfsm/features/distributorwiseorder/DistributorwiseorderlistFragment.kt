package com.nationalplasticfsm.features.distributorwiseorder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.os.Environment
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.FileProvider
import com.nationalplasticfsm.NumberToWords
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.AppDatabase
import com.nationalplasticfsm.app.Pref
import com.nationalplasticfsm.app.domain.*
import com.nationalplasticfsm.app.utils.AppUtils
import com.nationalplasticfsm.app.utils.Toaster
import com.nationalplasticfsm.base.presentation.BaseFragment
import com.nationalplasticfsm.features.addshop.presentation.AssignedToDDDialog
import com.nationalplasticfsm.features.addshop.presentation.AssignedToPPDialog
import com.nationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.nationalplasticfsm.widgets.AppCustomTextView
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import com.pnikosis.materialishprogress.ProgressWheel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class DistributorwiseorderlistFragment : BaseFragment(), View.OnClickListener {
    private lateinit var mContext: Context
    private lateinit var assign_to_tv: AppCustomTextView
    private lateinit var tv_assign_to_dd: AppCustomTextView
    private lateinit var fromDate: AppCompatRadioButton
    private lateinit var toDate: AppCompatRadioButton
    private lateinit var genereatePdfTv: AppCustomTextView
    private lateinit var progress_wheel: ProgressWheel
    private var assignedToPPId = ""
    private var assignedToDDId = ""
    private var fromDateSel: String = ""
    private var toDateSel: String = ""
    private var bill: BillingEntity? = null
    private lateinit var rl_assign_to_dd: LinearLayout
    private lateinit var assign_to_rl_pp: LinearLayout
    val FromCalender = Calendar.getInstance(Locale.ENGLISH)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_distributor_wise_order_list, container, false)
        initView(view)
        return view
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun initView(view: View) {
        assign_to_tv = view.findViewById(R.id.assign_to_tv)
        tv_assign_to_dd = view.findViewById(R.id.tv_assign_to_dd)
        fromDate = view.findViewById(R.id.frag_distribute_wise_order_list_from_date_range)
        toDate = view.findViewById(R.id.frag_distribute_wise_order_list_to_date_range)
        genereatePdfTv = view.findViewById(R.id.frag_distributor_wise_order_generated_pdf_TV)
        rl_assign_to_dd = view.findViewById(R.id.rl_assign_to_dd)
        assign_to_rl_pp = view.findViewById(R.id.assign_to_rl)
        progress_wheel = view.findViewById(R.id.progress_wheel)
        assign_to_rl_pp.setOnClickListener(this)
        rl_assign_to_dd.setOnClickListener(this)
        fromDate.setOnClickListener(this)
        toDate.setOnClickListener(this)
        genereatePdfTv.setOnClickListener(this)
        progress_wheel.stopSpinning()

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.assign_to_rl -> {
                val assignPPList = AppDatabase.getDBInstance()?.ppListDao()?.getAll()
                if (assignPPList == null || assignPPList.isEmpty()) {
                    if (!TextUtils.isEmpty(Pref.profile_state)) {
                        if (AppUtils.isOnline(mContext))
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_data_available))
                        else
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                    }
                } else {
                    showAssignedToPPDialog(assignPPList, "")
                }
            }
            R.id.rl_assign_to_dd -> {
                val assignDDList = AppDatabase.getDBInstance()?.ddListDao()?.getAll()
                if (assignDDList == null || assignDDList.isEmpty()) {
                    if (!TextUtils.isEmpty(Pref.profile_state)) {
                        if (AppUtils.isOnline(mContext))
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_data_available))
                        else
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                    }
                } else
                    showAssignedToDDDialog(assignDDList)
            }

            R.id.frag_distribute_wise_order_list_from_date_range -> {
                fromDate.error = null
                toDateSel = ""
                toDate.text = "To Date"
                val datePicker = android.app.DatePickerDialog(
                    mContext, R.style.DatePickerTheme, date, myCalendar.get(
                        Calendar.YEAR
                    ),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
                )
                datePicker.getDatePicker().maxDate = FromCalender.timeInMillis

                datePicker.show()
            }

            R.id.frag_distribute_wise_order_list_to_date_range -> {
                if (fromDateSel.equals("") && fromDateSel.length == 0) {
                    Toaster.msgShort(mContext, "Please select From Date")
                    return

                }

                toDate.error = null
                val datePicker = android.app.DatePickerDialog(
                    mContext, R.style.DatePickerTheme, date1, myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
                )
                datePicker.getDatePicker().maxDate =
                    FromCalender.timeInMillis //+ (60*60*1000*24*90)
                datePicker.getDatePicker().minDate =
                    selectFromDate.timeInMillis //+ (60*60*1000*24*80)
                datePicker.show()
            }
            R.id.frag_distributor_wise_order_generated_pdf_TV -> {
                saveDataAsPdf(assignedToDDId)
            }
        }
    }

    private fun saveDataAsPdf(assignedToDDId: String) {
        var dd_name =
            AppDatabase.getDBInstance()?.ddListDao()?.getSingleDDValue(assignedToDDId)?.dd_name
        //PDF Created
        var document: Document = Document()
        var fileName = "FTS" + "_" + dd_name
        fileName = fileName.replace("/", "_")

        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString() + "/nationalplasticfsmApp/DISTRIBUTORWISEORDERDETALIS/"

        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        try {
            PdfWriter.getInstance(document, FileOutputStream(path + fileName + ".pdf"))
            document.open()
            var font: Font = Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD)
            var fontBoldU: Font = Font(Font.FontFamily.HELVETICA, 12f, Font.UNDERLINE or Font.BOLD)
            var font1: Font = Font(Font.FontFamily.HELVETICA, 8f, Font.NORMAL)
            val grayFront = Font(Font.FontFamily.HELVETICA, 8f, Font.NORMAL, BaseColor.GRAY)
            //image add
            val bm: Bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            val bitmap = Bitmap.createScaledBitmap(bm, 50, 50, true);
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            var img: Image? = null
            val byteArray: ByteArray = stream.toByteArray()
            try {
                img = Image.getInstance(byteArray)
                img.scaleToFit(90f, 90f)
                img.scalePercent(70f)
                img.alignment = Image.ALIGN_LEFT
            } catch (e: BadElementException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
/////////////////////////////
            val sp = Paragraph("", font)
            sp.spacingAfter = 50f
            document.add(sp)

            val h = Paragraph("SALES ORDER OF<" + dd_name + ">", fontBoldU)
            h.alignment = Element.ALIGN_CENTER

            val pHead = Paragraph()
            pHead.add(Chunk(img, 0f, -30f))
            pHead.add(h)
            document.add(pHead)

            val x = Paragraph("", font)
            x.spacingAfter = 20f
            document.add(x)
            var shopListOnthisDD = AppDatabase.getDBInstance()?.addShopEntryDao()
                ?.getShopIdHasOrderDDWise(assignedToDDId)
            var invoiceNo = "";
            var invoiceDate = "";
            var invoiceAmount = "";
            var orderAmount = "";
            var productList: List<OrderProductListEntity>? = null
            var OrderNo = "";
            var OrderDate = "";
            if (shopListOnthisDD!!.size > 0) {
                for (i in 0..shopListOnthisDD!!.size - 1) {
                    var isShopOrder = AppDatabase.getDBInstance()!!.orderDetailsListDao()
                        .getListAccordingTodateonOrderDD(
                            fromDateSel,
                            toDateSel,
                            shopListOnthisDD.get(i).shop_id.toString()
                        )
                    if (isShopOrder.size > 0) {
                        for (j in 0..isShopOrder.size - 1) {
                            var order = AppDatabase.getDBInstance()!!.orderDetailsListDao()
                                .getSingleOrder(isShopOrder.get(j).order_id.toString())
                            OrderNo = AppDatabase.getDBInstance()!!.orderDetailsListDao()
                                .getOrderId(isShopOrder.get(j).order_id.toString())
                            OrderDate = AppDatabase.getDBInstance()!!.orderDetailsListDao()
                                .getOrderDate(isShopOrder.get(j).order_id.toString())
                            invoiceNo = AppDatabase.getDBInstance()!!.billingDao()
                                .getInvoice(isShopOrder.get(j).order_id.toString())
                            invoiceDate = AppDatabase.getDBInstance()!!.billingDao()
                                .getInvoiceDate(isShopOrder.get(j).order_id.toString())
                            invoiceAmount = AppDatabase.getDBInstance()!!.billingDao()
                                .getInvoiceSumAmt(isShopOrder.get(j).order_id.toString())
                            orderAmount = AppDatabase.getDBInstance()!!.orderDetailsListDao()
                                .getOrderAmt(isShopOrder.get(j).order_id.toString())
                            productList = AppDatabase.getDBInstance()!!.orderProductListDao()
                                .getDataAccordingToOrderId(isShopOrder.get(j).order_id.toString())
                        }
                    }

                    val widthsOrder = floatArrayOf(0.50f, 0.50f)

                    var tableHeaderOrder: PdfPTable = PdfPTable(widthsOrder)
                    tableHeaderOrder.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER)
                    tableHeaderOrder.setWidthPercentage(100f)
                    try {
                        if (invoiceNo == null) {
                            invoiceNo = ""
                        }
                    } catch (ex: Exception) {
                        invoiceNo = ""
                    }
                    try {
                        if (invoiceDate == null) {
                            invoiceDate = ""
                        }
                    } catch (ex: Exception) {
                        invoiceDate = ""
                    }
                    try {
                        if (invoiceAmount == null) {
                            invoiceAmount = ""
                        }
                    } catch (ex: Exception) {
                        invoiceAmount = ""
                    }

                    val cell11 = PdfPCell(
                        Phrase(
                            "Invoice No       :     " + invoiceNo + "\n\n" + "Invoice Date    :     " + AppUtils.convertToCommonFormat(
                                invoiceDate
                            ), font
                        )
                    )
                    cell11.setHorizontalAlignment(Element.ALIGN_LEFT)
                    cell11.borderColor = BaseColor.GRAY
                    tableHeaderOrder.addCell(cell11)


                    val cell222 = PdfPCell(
                        Phrase(
                            "Order No     :     " + OrderNo + "\n\n" + "Order Date  :     " + OrderDate,
                            font
                        )
                    )
                    cell222.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell222.borderColor = BaseColor.GRAY
                    cell222.paddingBottom = 10f
                    tableHeaderOrder.addCell(cell222)
                    document.add(tableHeaderOrder)
                    document.add(Paragraph())
                    val xz = Paragraph("", font)
                    xz.spacingAfter = 10f
                    document.add(xz)

                    val HeadingPartyDetls = Paragraph("Details of Party ", fontBoldU)
                    HeadingPartyDetls.indentationLeft = 82f
//            HeadingPartyDetls.alignment = Element.ALIGN_LEFT
                    HeadingPartyDetls.spacingAfter = 2f
                    document.add(HeadingPartyDetls)

                    val shop = AppDatabase.getDBInstance()?.addShopEntryDao()
                        ?.getShopByIdN(shopListOnthisDD.get(i).shop_id.toString())

                    val Parties =
                        Paragraph("Name                    :      " + shop?.shopName, font1)
                    Parties.alignment = Element.ALIGN_LEFT
                    Parties.spacingAfter = 2f
                    document.add(Parties)

                    val address = Paragraph("Address                :      " + shop?.address, font1)
                    address.alignment = Element.ALIGN_LEFT
                    address.spacingAfter = 2f
                    document.add(address)


                    val Contact =
                        Paragraph("Contact No.          :      " + shop?.ownerContactNumber, font1)
                    Contact.alignment = Element.ALIGN_LEFT
                    Contact.spacingAfter = 2f
                    document.add(Contact)


                    val xze = Paragraph("", font)
                    xze.spacingAfter = 10f
                    document.add(xze)

                    // table header
                    //val widths = floatArrayOf(0.55f, 0.05f, 0.2f, 0.2f)
                    val widths = floatArrayOf(0.06f, 0.58f, 0.07f, 0.07f, 0.07f, 0.15f)

                    var tableHeader: PdfPTable = PdfPTable(widths)
                    tableHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT)
                    tableHeader.setWidthPercentage(100f)

                    val cell111 = PdfPCell(Phrase("SL. ", font))
                    cell111.setHorizontalAlignment(Element.ALIGN_LEFT)
                    cell111.borderColor = BaseColor.GRAY
                    tableHeader.addCell(cell111);

                    val cell1 = PdfPCell(Phrase("Item Description ", font))
                    cell1.setHorizontalAlignment(Element.ALIGN_LEFT)
                    cell1.borderColor = BaseColor.GRAY
                    tableHeader.addCell(cell1);

                    val cell2 = PdfPCell(Phrase("Qty ", font))
                    cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell2.borderColor = BaseColor.GRAY
                    tableHeader.addCell(cell2);

                    val cell21 = PdfPCell(Phrase("Unit ", font))
                    cell21.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell21.borderColor = BaseColor.GRAY
                    tableHeader.addCell(cell21);

                    val cell3 = PdfPCell(Phrase("Rate ", font))
                    cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell3.borderColor = BaseColor.GRAY
                    tableHeader.addCell(cell3);

                    val cell4 = PdfPCell(Phrase("Amount ", font))
                    cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell4.borderColor = BaseColor.GRAY
                    tableHeader.addCell(cell4);

                    document.add(tableHeader)

                    //table body
                    var srNo: String = ""
                    var item: String = ""
                    var qty: String = ""
                    var unit: String = ""
                    var rate: String = ""
                    var amount: String = ""




                    for (i in 0..productList!!.size - 1) {
                        srNo = (i + 1).toString() + " "
                        item = productList!!.get(i).product_name + "       "
                        qty = productList!!.get(i).qty + " "
                        unit = "KG" + " "
                        rate =
                            getString(R.string.rupee_symbol_with_space) + " " + productList!!.get(i).rate + " "
                        amount =
                            getString(R.string.rupee_symbol_with_space) + " " + productList!!.get(i).total_price + " "


                        val tableRows = PdfPTable(widths)
                        tableRows.defaultCell.horizontalAlignment = Element.ALIGN_CENTER
                        tableRows.setWidthPercentage(100f);


                        var cellBodySr = PdfPCell(Phrase(srNo, font1))
                        cellBodySr.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cellBodySr.borderColor = BaseColor.GRAY
                        tableRows.addCell(cellBodySr)

                        var cellBodySl = PdfPCell(Phrase(item, font1))
                        cellBodySl.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cellBodySl.borderColor = BaseColor.GRAY
                        tableRows.addCell(cellBodySl)

                        var cellBody2 = PdfPCell(Phrase(qty, font1))
                        cellBody2.setHorizontalAlignment(Element.ALIGN_LEFT)
                        cellBody2.borderColor = BaseColor.GRAY
                        tableRows.addCell(cellBody2)


                        var cellBody21 = PdfPCell(Phrase(unit, font1))
                        cellBody21.setHorizontalAlignment(Element.ALIGN_LEFT)
                        cellBody21.borderColor = BaseColor.GRAY
                        tableRows.addCell(cellBody21)

                        var cellBody3 = PdfPCell(Phrase(rate, font1))
                        cellBody3.setHorizontalAlignment(Element.ALIGN_LEFT)
                        cellBody3.borderColor = BaseColor.GRAY
                        tableRows.addCell(cellBody3)

                        var cellBody4 = PdfPCell(Phrase(amount, font1))
                        cellBody4.setHorizontalAlignment(Element.ALIGN_LEFT)
                        cellBody4.borderColor = BaseColor.GRAY
                        tableRows.addCell(cellBody4)

                        document.add(tableRows)

                        document.add(Paragraph())
                    }
                    val xffx = Paragraph("", font)
                    xffx.spacingAfter = 12f
                    document.add(xffx)


                    val para1 = Paragraph()
                    val glue1 = Chunk(VerticalPositionMark())
                    val ph11 = Phrase()
                    val main1 = Paragraph()
                    ph11.add(
                        Chunk(
                            "Rupees " + NumberToWords.numberToWord(
                                orderAmount.toDouble().toInt()!!
                            )!!.toUpperCase() + " Only  ", font
                        )
                    )
                    ph11.add(glue1) // Here I add special chunk to the same phrase.

                    ph11.add(Chunk("Total  Amount: " + "\u20B9" + orderAmount, font))
                    para1.add(ph11)
                    document.add(para1)


                    val xfx = Paragraph("", font)
                    xfx.spacingAfter = 12f
                    document.add(xfx)


                    val widthsSalesPerson = floatArrayOf(1f)

                    var tablewidthsSalesPersonHeader: PdfPTable = PdfPTable(widthsSalesPerson)
                    tablewidthsSalesPersonHeader.getDefaultCell()
                        .setHorizontalAlignment(Element.ALIGN_LEFT)
                    tablewidthsSalesPersonHeader.setWidthPercentage(100f)

                    val cellsales = PdfPCell(Phrase("Entered by: " + Pref.user_name, font1))
                    cellsales.setHorizontalAlignment(Element.ALIGN_LEFT)
                    cellsales.borderColor = BaseColor.GRAY
                    tablewidthsSalesPersonHeader.addCell(cellsales)


                    document.add(tablewidthsSalesPersonHeader)


                    document.close()
                }
            }


            var sendingPath = path + fileName + ".pdf"
            if (!TextUtils.isEmpty(sendingPath)) {
                try {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    val fileUrl = Uri.parse(sendingPath)
                    val file = File(fileUrl.path)
                    val uri: Uri = FileProvider.getUriForFile(
                        mContext,
                        mContext.applicationContext.packageName.toString() + ".provider",
                        file
                    )
                    shareIntent.type = "image/png"
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                    startActivity(Intent.createChooser(shareIntent, "Share pdf using"))
                } catch (e: Exception) {
                    e.printStackTrace()
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong1))
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
        }
    }


    private val myCalendar: Calendar by lazy {
        Calendar.getInstance(Locale.ENGLISH)
    }

    var selectFromDate = Calendar.getInstance()

    val date = android.app.DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        selectFromDate.set(year, monthOfYear, dayOfMonth)
        //tv_date_dialog.text=  AppUtils.getFormatedDateNew(AppUtils.getBillingDateFromCorrectDate(AppUtils.getFormattedDateForApi(myCalendar.time)),"dd-mm-yyyy","yyyy-mm-dd")
        fromDate.text =
            AppUtils.getBillingDateFromCorrectDate(AppUtils.getFormattedDateForApi(myCalendar.time))
        fromDateSel = AppUtils.getFormattedDateForApi(myCalendar.time)
    }
    val date1 = android.app.DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        //tv_date_dialog.text=  AppUtils.getFormatedDateNew(AppUtils.getBillingDateFromCorrectDate(AppUtils.getFormattedDateForApi(myCalendar.time)),"dd-mm-yyyy","yyyy-mm-dd")
        toDate.text =
            AppUtils.getBillingDateFromCorrectDate(AppUtils.getFormattedDateForApi(myCalendar.time))
        toDateSel = AppUtils.getFormattedDateForApi(myCalendar.time)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun showAssignedToPPDialog(mAssignedList: List<AssignToPPEntity>?, type: String?) {
        AssignedToPPDialog.newInstance(
            mAssignedList,
            type!!,
            object : AssignedToPPDialog.OnItemSelectedListener {
                override fun onItemSelect(pp: AssignToPPEntity?) {
                    assign_to_tv.text = pp?.pp_name + " (" + pp?.pp_phn_no + ")"
                    assignedToPPId = pp?.pp_id.toString()
                }
            }).show(fragmentManager!!, "")
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun showAssignedToDDDialog(mAssignedList: List<AssignToDDEntity>?) {
        AssignedToDDDialog.newInstance(
            mAssignedList,
            object : AssignedToDDDialog.OnItemSelectedListener {
                override fun onItemSelect(dd: AssignToDDEntity?) {
                    tv_assign_to_dd.text = dd?.dd_name + " (" + dd?.dd_phn_no + ")"
                    assignedToDDId = dd?.dd_id.toString()
                }
            }).show(fragmentManager!!, "")
    }


}