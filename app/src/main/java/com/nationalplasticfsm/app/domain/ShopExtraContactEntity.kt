package com.nationalplasticfsm.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nationalplasticfsm.app.AppConstant

@Entity(tableName = AppConstant.SHOP_EXTRA_CONTACT)
class ShopExtraContactEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "shop_id")
    var shop_id: String? = null

    @ColumnInfo(name = "contact_serial")
    var contact_serial: String? = null

    @ColumnInfo(name = "contact_name")
    var contact_name: String? = null

    @ColumnInfo(name = "contact_number")
    var contact_number: String? = null

    @ColumnInfo(name = "contact_email")
    var contact_email: String? = null

    @ColumnInfo(name = "contact_doa")
    var contact_doa: String? = null

    @ColumnInfo(name = "contact_dob")
    var contact_dob: String? = null

    @ColumnInfo(name = "isUploaded")
    var isUploaded: Boolean = false

}