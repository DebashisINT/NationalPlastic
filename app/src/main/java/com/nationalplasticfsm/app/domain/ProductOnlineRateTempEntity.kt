package com.nationalplasticfsm.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nationalplasticfsm.app.AppConstant

@Entity(tableName = AppConstant.PRODUCT_ONLINE_RATE_TEMP_TABLE)
class ProductOnlineRateTempEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "product_id")
    var product_id: String? = null

    @ColumnInfo(name = "rate")
    var rate: String? = null

    @ColumnInfo(name = "stock_amount")
    var stock_amount: String? = null

    @ColumnInfo(name = "stock_unit")
    var stock_unit: String? = null

    @ColumnInfo(name = "isStockShow")
    var isStockShow: Boolean = false

    @ColumnInfo(name = "isRateShow")
    var isRateShow: Boolean = false

    @ColumnInfo(name = "Qty_per_Unit")
    var Qty_per_Unit: Double? = null
    @ColumnInfo(name = "Scheme_Qty")
    var Scheme_Qty: Double? = null
   @ColumnInfo(name = "Effective_Rate")
    var Effective_Rate: Double? = null

}