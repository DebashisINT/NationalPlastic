package com.breezefieldnationalplastic.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezefieldnationalplastic.app.AppConstant

@Entity(tableName = AppConstant.STOCK_ALL)
data class StockAllEntity(
    @PrimaryKey(autoGenerate = true) var sl_no: Int = 0,
    @ColumnInfo var stock_shopcode:String = "",
    @ColumnInfo var stock_shopentitycode:String = "",
    @ColumnInfo var stock_productid:String = "",
    @ColumnInfo var stock_productname:String = "",
    @ColumnInfo var stock_productqty:String = "",
    @ColumnInfo var stock_productbalqty:String = ""
)