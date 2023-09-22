package com.nationalplasticfsm.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nationalplasticfsm.app.AppConstant

@Entity(tableName = AppConstant.SHOP_VISIT_REVISIT_WHATSAPP_STATUS)
data class VisitRevisitWhatsappStatus (
    @PrimaryKey(autoGenerate = true) var sl_no: Int = 0,
    @ColumnInfo var shop_id:String = "",
    @ColumnInfo var shop_name:String = "",
    @ColumnInfo var contactNo:String = "",
    @ColumnInfo var isNewShop:Boolean = false,
    @ColumnInfo var date:String = "",
    @ColumnInfo var time:String = "",
    @ColumnInfo var isWhatsappSent:Boolean = false,
    @ColumnInfo var whatsappSentMsg:String = "",
    @ColumnInfo var transactionId:String = "",
    @ColumnInfo var isUploaded:Boolean = false,
)
