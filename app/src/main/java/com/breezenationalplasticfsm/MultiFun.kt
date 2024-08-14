package com.breezenationalplasticfsm

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import android.widget.Toast
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.domain.AddShopDBModelEntity
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.features.NewQuotation.Mail
import com.itextpdf.text.BadElementException
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object MultiFun {



    fun generateContactDtlsPdf(shopObj: AddShopDBModelEntity,mContext:Context){
    var document: Document = Document(PageSize.A4, 36f, 36f, 36f, 80f)
        val time = System.currentTimeMillis()
        var fileName = "Contact" +  "_" + time
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/FSMApp/CallHis/"
       // val path = "/storage/emulated/0/Android/data/com.breezenationalplasticfsm/files/"

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
            //code start by Puja mantis-0027395 date-23.04.24 v4.2.6
            //val bm: Bitmap = BitmapFactory.decodeResource(mContext.resources, R.mipmap.ic_launcher)
            val bm: Bitmap = BitmapFactory.decodeResource(mContext.resources, R.drawable.breezelogo)
            //code end by Puja mantis-0027395 date-23.04.24 v4.2.6
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
            ph.add(Chunk("Contact Details", fontBoldUHeader))
            ph.add(glue)
            ph.add(Chunk("DATE: " + AppUtils.getCurrentDate_DD_MM_YYYY() + " ", font1))
            para.add(ph)
            document.add(para)

            val spac = Paragraph("", font)
            spac.spacingAfter = 15f
            document.add(spac)

            val name = Paragraph("Name                         :      " + shopObj?.shopName, font1)
            name.alignment = Element.ALIGN_LEFT
            name.spacingAfter = 2f
            document.add(name)

            val addr = Paragraph("Address                     :      " + shopObj?.address+" "+shopObj?.pinCode, font1)
            addr.alignment = Element.ALIGN_LEFT
            addr.spacingAfter = 2f
            document.add(addr)

            val contNo = Paragraph("Contact No.               :      " + shopObj?.ownerContactNumber, font1)
            contNo.alignment = Element.ALIGN_LEFT
            contNo.spacingAfter = 2f
            document.add(contNo)

            val email = Paragraph("Email                         :      " + if(shopObj?.ownerEmailId.isNullOrEmpty()) "None" else shopObj?.ownerEmailId, font1)
            email.alignment = Element.ALIGN_LEFT
            email.spacingAfter = 2f
            document.add(email)

            val crm_company = Paragraph("Company                  :      " + if(shopObj?.companyName.isNullOrEmpty()) "None" else shopObj?.companyName, font1)
            crm_company.alignment = Element.ALIGN_LEFT
            crm_company.spacingAfter = 2f
            document.add(crm_company)

            val jobTitle = Paragraph("Job Title                    :      " + if(shopObj?.jobTitle.isNullOrEmpty()) "None" else shopObj?.jobTitle, font1)
            jobTitle.alignment = Element.ALIGN_LEFT
            jobTitle.spacingAfter = 2f
            document.add(jobTitle)

            val type = Paragraph("Type                         :      " + if(shopObj?.crm_type.isNullOrEmpty()) "None" else shopObj?.crm_type, font1)
            type.alignment = Element.ALIGN_LEFT
            type.spacingAfter = 2f
            document.add(type)

            val source = Paragraph("Source                      :      " + if(shopObj?.crm_source.isNullOrEmpty()) "None" else shopObj?.crm_source, font1)
            source.alignment = Element.ALIGN_LEFT
            source.spacingAfter = 2f
            document.add(source)

            val ref = Paragraph("Reference                 :      " + if(shopObj?.crm_reference.isNullOrEmpty()) "None" else shopObj?.crm_reference, font1)
            ref.alignment = Element.ALIGN_LEFT
            ref.spacingAfter = 2f
            document.add(ref)

            val stage = Paragraph("Stage                        :      " + if(shopObj?.crm_stage.isNullOrEmpty()) "None" else shopObj?.crm_stage, font1)
            stage.alignment = Element.ALIGN_LEFT
            stage.spacingAfter = 2f
            document.add(stage)

            val status = Paragraph("Status                       :      " + if(shopObj?.crm_status.isNullOrEmpty()) "None" else shopObj?.crm_status, font1)
            status.alignment = Element.ALIGN_LEFT
            status.spacingAfter = 2f
            document.add(status)

            var added_date = shopObj.added_date.replace("T"," ").split(" ").get(0).toString()
            var added_time = shopObj.added_date.replace("T"," ").split(" ").get(1).toString()
            val addedDtTi = Paragraph("Added Date-Time     :      " + if(added_date.isNullOrEmpty()) "None" else AppUtils.getFormatedDateNew(added_date,"yyyy-mm-dd","dd-mm-yyyy")+" - "+added_time.substring(0,5).toString(), font1)
            addedDtTi.alignment = Element.ALIGN_LEFT
            addedDtTi.spacingAfter = 2f
            document.add(addedDtTi)

            val crmFrom = Paragraph("Contact From           :      " + if(shopObj?.crm_saved_from.isNullOrEmpty()) "None" else shopObj?.crm_saved_from, font1)
            crmFrom.alignment = Element.ALIGN_LEFT
            crmFrom.spacingAfter = 2f
            document.add(crmFrom)

            val expSaleV = Paragraph("Expected Sale Value  :      " + if(shopObj?.amount.isNullOrEmpty()) "0.00" else shopObj?.amount, font1)
            expSaleV.alignment = Element.ALIGN_LEFT
            expSaleV.spacingAfter = 2f
            document.add(expSaleV)

            document.add(spac)

            document.close()


            var sendingPath = path + fileName + ".pdf"
           // var sendingPath = Pref.scheduler_file

            var m = Mail()
            var toArr = arrayOf("")
            m = Mail("suman.bachar@indusnet.co.in", "jfekuhstltfkarrv") // generate under 2-step verification -> app password
            toArr = arrayOf(shopObj.ownerEmailId)
            m.setTo(toArr)
            m.setFrom("TEAM");
            m.setSubject("Scheduler Activity.")
           // m.setBody("Hello ,  \n Please find attachment below. \n\n\n Regards \n${Pref.user_name}.")
            var mailBody = Pref.scheduler_template!!.replace("@name","${shopObj.ownerName}").replace("@Name","${shopObj.ownerName}")
           // m.setBody("Hello ,  \n ${Pref.scheduler_template}.")
            m.setBody(mailBody)

            doAsync {
                try{
                     val fileUrl = Uri.parse(sendingPath)
                    m.send()
                }catch (ex:Exception){
                    ex.printStackTrace()
                }
                uiThread {

                }
            }
        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }

    interface OnMailAction{
        fun onStatus(isSuccess:Boolean)
    }

    fun autoMailScheduler1(emailToSent:String,body:String,shopObj:AddShopDBModelEntity,schName:String,listnr:OnMailAction){
        doAsync {
            try {
                var m = Mail()
                //m = Mail("suman.bachar@indusnet.co.in", "jfekuhstltfkarrv") // generate under 2-step verification -> app password
                m = Mail(Pref.storeGmailId, Pref.storeGmailPassword) // generate under 2-step verification -> app password
                var toArr = arrayOf(emailToSent)
                m.setTo(toArr)
                m.setFrom("TEAM");
                m.setSubject(schName)
                var mailBody = body.replace("@FromName","${Pref.user_name}").replace("@ToName","${shopObj.ownerName}")
                m.setBody(mailBody)
                m.send()

            uiThread {
                listnr?.onStatus(true)
                }
            } catch (e: Exception) {
                e.printStackTrace();
                uiThread {
                    listnr?.onStatus(false)
                }
            }
        }
    }

    fun autoMailScheduler(emailToSent:String,body:String,shopObj:AddShopDBModelEntity,schName:String){
        try{
            var m = Mail()
            //m = Mail("suman.bachar@indusnet.co.in", "jfekuhstltfkarrv") // generate under 2-step verification -> app password
            m = Mail(Pref.storeGmailId, Pref.storeGmailPassword) // generate under 2-step verification -> app password
            var toArr = arrayOf(emailToSent)
            m.setTo(toArr)
            m.setFrom("TEAM");
            m.setSubject(schName)
            var bbody = body.replace("@toname","@ToName").replace("@fromname","@FromName")
            var mailBody = bbody.replace("@FromName","${Pref.user_name}").replace("@ToName","${shopObj.ownerName}")
            m.setBody(mailBody)

            doAsync {
                try{
                    m.send()
                }catch (ex:Exception){
                    ex.printStackTrace()
                    Timber.e("Mail sent exception: "+ ex.message)
                }
                uiThread {

                }
            }
        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }

    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    // Checks if a volume containing external storage is available to at least read.
    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

    @SuppressLint("Range")
    fun copyFileToInternal(context: Context, fileUri: Uri?): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val cursor = context.contentResolver.query(
                fileUri!!,
                arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE),
                null,
                null
            )
            cursor!!.moveToFirst()
            val displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            val size = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE))
            val file = File(context.filesDir.toString() + "/" + displayName)
            try {
                val fileOutputStream = FileOutputStream(file)
                val inputStream = context.contentResolver.openInputStream(fileUri)
                val buffers = ByteArray(1024)
                var read: Int
                while (inputStream!!.read(buffers).also { read = it } != -1) {
                    fileOutputStream.write(buffers, 0, read)
                }
                inputStream.close()
                fileOutputStream.close()
                return file.path
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun sendAutoMailWithFile(sendingPath:String,emailToSent:String,mailBo:String,shopObj:AddShopDBModelEntity,schName:String){
        try{
            var m = Mail(Pref.storeGmailId, Pref.storeGmailPassword)
            m.setTo(arrayOf(emailToSent))
            m.setFrom("FSM");
            m.setSubject(schName)
            var mailBoRectify = mailBo.replace("@toname","@ToName").replace("@fromname","@FromName")
            var mailBody = mailBoRectify.replace("@FromName","${Pref.user_name}").replace("@ToName","${shopObj.ownerName}")
            m.setBody(mailBody)
            doAsync {
                try{
                    if(sendingPath.equals("")){
                        m.send()
                    }else{
                        m.send(sendingPath)
                    }
                }catch (ex:Exception){
                    ex.printStackTrace()
                    println("tag_main Mail sent exception ${ex.message}")
                    Timber.e("Mail sent exception: "+ ex.message)
                }
                uiThread {

                }
            }
        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }

    fun sendEmailManually(mContext:Context,uri:Uri) {
        try {
            var email = "sumanbacharofc@gmail.com"
            var subject = "subject"
            var message = "msg"
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "plain/text"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri)
            emailIntent.putExtra(Intent.EXTRA_TEXT, message)
            mContext.startActivity(Intent.createChooser(emailIntent, "Sending email..."))
        }
        catch (t: Throwable) {
            Toast.makeText(mContext, "Request failed try again: $t", Toast.LENGTH_LONG).show()
        }
    }

}