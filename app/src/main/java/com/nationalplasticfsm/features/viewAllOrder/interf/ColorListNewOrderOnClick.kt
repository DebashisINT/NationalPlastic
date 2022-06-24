package com.nationalplasticfsm.features.viewAllOrder.interf

import com.nationalplasticfsm.app.domain.NewOrderColorEntity
import com.nationalplasticfsm.app.domain.NewOrderProductEntity

interface ColorListNewOrderOnClick {
    fun productListOnClick(color: NewOrderColorEntity)
}