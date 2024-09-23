package com.breezefieldnationalplastic.features.NewQuotation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.itextpdf.text.*
import com.itextpdf.text.pdf.ColumnText
import com.itextpdf.text.pdf.PdfContentByte
import com.itextpdf.text.pdf.PdfPageEventHelper
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream
import java.io.IOException

// 1.0 HeaderFooterPageEvent  AppV 4.0.7  Suman 27/02/2023 footer image with text mantis 25705

class HeaderFooterPageEvent(var companyN:String,var salesmanN:String,var salesmanDegin:String,var salesPh:String,var salesEmail:String) : PdfPageEventHelper() {


    override fun onStartPage(writer: PdfWriter?, document: Document?) {
        var bm:Bitmap? = null
        if(Pref.IsShowQuotationFooterforEurobond){
            bm = BitmapFactory.decodeResource(AppUtils.contx!!.resources, R.drawable.pdf_logo)
        }else{
            bm = BitmapFactory.decodeResource(AppUtils.contx!!.resources, R.drawable.breezelogo)
        }
        val bitmap = Bitmap.createScaledBitmap(bm, 180, 50, true);
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        var img: Image? = null
        val byteArray: ByteArray = stream.toByteArray()
        try {
            img = Image.getInstance(byteArray)
            img.scalePercent(50f)
            img.alignment=Image.ALIGN_RIGHT
        } catch (e: BadElementException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var imgSoc:Image = Image.getInstance(img)
        imgSoc.setAbsolutePosition(document!!.right()-110f, document.top()-1f);
        var cb : PdfContentByte = writer!!.getDirectContent() as PdfContentByte
        cb.addImage(imgSoc)

        //super.onStartPage(writer, document)
        //ColumnText.showTextAligned(writer!!.getDirectContent(), Element.ALIGN_CENTER,  Phrase("Top Left"), 30f, 800f, 0f);
        //ColumnText.showTextAligned(writer!!.getDirectContent(), Element.ALIGN_CENTER,  Phrase("Top Right"), 550f, 800f, 0f);
    }

    override fun onEndPage(writer: PdfWriter?, document: Document?) {
        //super.onEndPage(writer, document)
        //ColumnText.showTextAligned(writer!!.getDirectContent(), Element.ALIGN_CENTER,  Phrase(""), 110f, 30f, 0f);
        //ColumnText.showTextAligned(writer!!.getDirectContent(), Element.ALIGN_CENTER,  Phrase("page " + document!!.getPageNumber()), 550f, 30f, 0f);

        //Hardcoded for EuroBond
//        val bm: Bitmap = BitmapFactory.decodeResource(AppUtils.contx!!.resources, R.drawable.ics_image_full)
//          val bm: Bitmap = BitmapFactory.decodeResource(AppUtils.contx!!.resources, R.drawable.footer_icon_euro)
        var bm: Bitmap
        if(Pref.IsShowQuotationFooterforEurobond){
            bm = BitmapFactory.decodeResource(AppUtils.contx!!.resources, R.drawable.footer_icon_euro)
        }else{
            bm = BitmapFactory.decodeResource(AppUtils.contx!!.resources, R.drawable.strip_line)
        }

        val bitmap = Bitmap.createScaledBitmap(bm, 950, 80, true);
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        var img: Image? = null
        val byteArray: ByteArray = stream.toByteArray()
        try {
            img = Image.getInstance(byteArray)
          //  img.scaleToFit(155f,90f)
            img.scalePercent(50f)
            img.alignment=Image.ALIGN_RIGHT
        } catch (e: BadElementException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var imgSoc:Image = Image.getInstance(img)
        imgSoc.setAbsolutePosition(15f, 10f);
        var cb : PdfContentByte = writer!!.getDirectContent() as PdfContentByte
        if(Pref.IsShowQuotationFooterforEurobond){
            cb.addImage(imgSoc)
        }

        val bm1: Bitmap = BitmapFactory.decodeResource(AppUtils.contx!!.resources, R.drawable.strip_line)
        val bitmap1 = Bitmap.createScaledBitmap(bm1, 1250, 17, false);
        val stream1 = ByteArrayOutputStream()
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1)
        var img1: Image? = null
        val byteArray1: ByteArray = stream1.toByteArray()
        try {
            img1 = Image.getInstance(byteArray1)
            //img.scaleToFit(200f,120f)
            img1.scalePercent(50f)
            img1.alignment=Image.ALIGN_RIGHT
        } catch (e: BadElementException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var imgSoc1:Image = Image.getInstance(img1)
        imgSoc1.setAbsolutePosition(1f, 1f);
        cb.addImage(imgSoc1)



/*
        ////footer text
        var font: Font = Font(Font.FontFamily.HELVETICA, 8f, Font.BOLD)
        var xAxisSpace = 35f//(document!!.right() - document.left()) / 1 + document.leftMargin()
        var yAxisSpace = 12f
        var lineSpace = 3f
        val footer1 = Phrase("Thanks & Regards", font)
        val footer2 = Phrase(companyN, font)
        val footer3 = Phrase(salesmanN, font)
        val footer4 = Phrase(salesmanDegin, font)
        val footer5 = Paragraph("Mob : " +  salesPh, font)
        val footer6 = Paragraph("Email : " +  salesEmail, font)
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
            footer1,
            xAxisSpace,
            yAxisSpace*6+lineSpace+40f, 0f);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
            footer2,
            xAxisSpace,
            yAxisSpace*5+lineSpace+40f, 0f);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
            footer3,
            xAxisSpace,
            yAxisSpace*4+lineSpace+40f, 0f);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
            footer4,
            xAxisSpace,
            yAxisSpace*3+lineSpace+40f, 0f);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
            footer5,
            xAxisSpace,
            yAxisSpace*2+lineSpace+40f, 0f);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
            footer6,
            xAxisSpace,
            yAxisSpace*1+lineSpace+40f, 0f);
*/
    }

}