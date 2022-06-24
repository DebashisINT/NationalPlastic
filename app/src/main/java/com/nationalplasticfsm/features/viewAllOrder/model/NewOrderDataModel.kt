package com.nationalplasticfsm.features.viewAllOrder.model

import com.nationalplasticfsm.app.domain.NewOrderColorEntity
import com.nationalplasticfsm.app.domain.NewOrderGenderEntity
import com.nationalplasticfsm.app.domain.NewOrderProductEntity
import com.nationalplasticfsm.app.domain.NewOrderSizeEntity
import com.nationalplasticfsm.features.stockCompetetorStock.model.CompetetorStockGetDataDtls

class NewOrderDataModel {
    var status:String ? = null
    var message:String ? = null
    var Gender_list :ArrayList<NewOrderGenderEntity>? = null
    var Product_list :ArrayList<NewOrderProductEntity>? = null
    var Color_list :ArrayList<NewOrderColorEntity>? = null
    var size_list :ArrayList<NewOrderSizeEntity>? = null
}

