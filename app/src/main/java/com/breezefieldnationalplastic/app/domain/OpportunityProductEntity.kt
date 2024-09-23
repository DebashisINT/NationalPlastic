package com.breezefieldnationalplastic.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezefieldnationalplastic.app.AppConstant


@Entity(tableName = AppConstant.CRM_Opportunity_Product)
data class OpportunityProductEntity
    (
    @PrimaryKey(autoGenerate = true) var sl_no: Int = 0,
    @ColumnInfo var shop_id:String = "",
    @ColumnInfo var opportunity_id:String = "",
    @ColumnInfo var product_id:String = "",
    @ColumnInfo var product_name:String = ""
            )