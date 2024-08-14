package com.breezenationalplasticfsm.features.login

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezenationalplasticfsm.app.AppConstant

@Entity(tableName = AppConstant.TBL_SHOP_FEEDBACK)
class ShopFeedbackEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "shop_id")
    var shop_id: String? = null

    @ColumnInfo(name = "feedback")
    var feedback: String? = null

    @ColumnInfo(name = "date_time")
    var date_time: String? = null

    @ColumnInfo(name = "multi_contact_name")
    var multi_contact_name: String? = null

    @ColumnInfo(name = "multi_contact_number")
    var multi_contact_number: String? = null

}