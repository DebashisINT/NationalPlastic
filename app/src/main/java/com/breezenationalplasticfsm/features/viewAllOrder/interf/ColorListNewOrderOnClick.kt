package com.breezenationalplasticfsm.features.viewAllOrder.interf

import com.breezenationalplasticfsm.app.domain.NewOrderColorEntity
import com.breezenationalplasticfsm.app.domain.NewOrderProductEntity

interface ColorListNewOrderOnClick {
    fun productListOnClick(color: NewOrderColorEntity)
}