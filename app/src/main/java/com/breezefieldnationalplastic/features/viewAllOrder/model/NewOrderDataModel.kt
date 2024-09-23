package com.breezefieldnationalplastic.features.viewAllOrder.model

import com.breezefieldnationalplastic.app.domain.NewOrderColorEntity
import com.breezefieldnationalplastic.app.domain.NewOrderGenderEntity
import com.breezefieldnationalplastic.app.domain.NewOrderProductEntity
import com.breezefieldnationalplastic.app.domain.NewOrderSizeEntity
import com.breezefieldnationalplastic.features.stockCompetetorStock.model.CompetetorStockGetDataDtls

class NewOrderDataModel {
    var status:String ? = null
    var message:String ? = null
    var Gender_list :ArrayList<NewOrderGenderEntity>? = null
    var Product_list :ArrayList<NewOrderProductEntity>? = null
    var Color_list :ArrayList<NewOrderColorEntity>? = null
    var size_list :ArrayList<NewOrderSizeEntity>? = null
}

