package com.nationalplasticfsm.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nationalplasticfsm.app.AppConstant
import com.nationalplasticfsm.app.utils.AppUtils

/**
 * Created by Saikat on 08-11-2018.
 */
//Revision History
// 1.0   AppV 4.0.6  Saheli    01/02/2023  product_list   migration changes
@Entity(tableName = AppConstant.PRODUCT_LIST_TABLE)
class ProductListEntity {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "date")
    var date: String? = AppUtils.getCurrentDateForShopActi()

    @ColumnInfo(name = "product_name")
    var product_name: String? = null

    @ColumnInfo(name = "brand_id")
    var brand_id: String? = null

    @ColumnInfo(name = "brand")
    var brand: String? = null

    @ColumnInfo(name = "category_id")
    var category_id: String? = null

    @ColumnInfo(name = "category")
    var category: String? = null

    @ColumnInfo(name = "watt_id")
    var watt_id: String? = null

    @ColumnInfo(name = "watt")
    var watt: String? = null

    @ColumnInfo(name = "product_mrp_show")// 1.0   AppV 4.0.6  product_list   migration changes
    var product_mrp_show: String? = null

    @ColumnInfo(name = "product_discount_show")// 1.0   AppV 4.0.6  product_list   migration changes
    var product_discount_show: String? = null
}