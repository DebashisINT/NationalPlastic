package com.breezenationalplasticfsm.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezenationalplasticfsm.app.AppConstant


@Entity(tableName = AppConstant.CRM_Opportunity_Add)
data class OpportunityAddEntity
    (
    @PrimaryKey(autoGenerate = true) var sl_no: Int = 0,
    @ColumnInfo var shop_id:String = "",
    @ColumnInfo var shop_name:String = "",
    @ColumnInfo var shop_type:String = "",
    @ColumnInfo var opportunity_id:String = "",
    @ColumnInfo var opportunity_description:String = "",
    @ColumnInfo var opportunity_amount:String = "",
    @ColumnInfo var opportunity_status_id:String = "",
    @ColumnInfo var opportunity_status_name:String = "",
    @ColumnInfo var opportunity_created_date:String = "",
    @ColumnInfo var opportunity_created_time:String = "",
    @ColumnInfo var opportunity_created_date_time:String = "",
    @ColumnInfo var opportunity_edited_date_time:String = "",
    @ColumnInfo var isUpload:Boolean = false,
    @ColumnInfo var isDeleted:Boolean = false,
    @ColumnInfo var isEdited:Boolean = false,
            )