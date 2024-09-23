package com.breezedsm.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezefieldnationalplastic.app.AppConstant


@Entity(tableName = AppConstant.NEW_PRODUCT_LIST)
data class NewProductListEntity (
    @PrimaryKey(autoGenerate = false) var product_id:String = "",
    @ColumnInfo var product_name:String = "",
    @ColumnInfo var brand_id:String = "",
    @ColumnInfo var brand_name:String = "",
    @ColumnInfo var category_id:String = "",
    @ColumnInfo var category_name:String = "",
    @ColumnInfo var watt_id:String = "",
    @ColumnInfo var watt_name:String = "",
    @ColumnInfo var UOM:String = ""
)