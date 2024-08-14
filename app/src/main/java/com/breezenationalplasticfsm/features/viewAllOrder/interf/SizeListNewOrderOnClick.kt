package com.breezenationalplasticfsm.features.viewAllOrder.interf

import com.breezenationalplasticfsm.app.domain.NewOrderProductEntity
import com.breezenationalplasticfsm.app.domain.NewOrderSizeEntity

interface SizeListNewOrderOnClick {
    fun sizeListOnClick(size: NewOrderSizeEntity)
}