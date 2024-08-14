package com.breezenationalplasticfsm.features.viewAllOrder.model

import com.breezenationalplasticfsm.app.domain.NewOrderColorEntity
import com.breezenationalplasticfsm.app.domain.NewOrderGenderEntity
import com.breezenationalplasticfsm.app.domain.NewOrderProductEntity
import com.breezenationalplasticfsm.app.domain.NewOrderSizeEntity
import com.breezenationalplasticfsm.features.stockCompetetorStock.model.CompetetorStockGetDataDtls

class NewOrderDataModel {
    var status:String ? = null
    var message:String ? = null
    var Gender_list :ArrayList<NewOrderGenderEntity>? = null
    var Product_list :ArrayList<NewOrderProductEntity>? = null
    var Color_list :ArrayList<NewOrderColorEntity>? = null
    var size_list :ArrayList<NewOrderSizeEntity>? = null
}

