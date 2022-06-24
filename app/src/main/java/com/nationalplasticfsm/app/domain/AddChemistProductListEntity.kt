package com.nationalplasticfsm.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nationalplasticfsm.app.AppConstant

/**
 * Created by Saikat on 08-01-2020.
 */
@Entity(tableName = AppConstant.CHEMIST_VISIT_PRODUCT_TABLE)
class AddChemistProductListEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "chemist_visit_id")
    var chemist_visit_id: String? = null

    @ColumnInfo(name = "shop_id")
    var shop_id: String? = null

    @ColumnInfo(name = "product_id")
    var product_id: String? = null

    @ColumnInfo(name = "product_name")
    var product_name: String? = null

    @ColumnInfo(name = "isPob")
    var isPob: Boolean = false
}