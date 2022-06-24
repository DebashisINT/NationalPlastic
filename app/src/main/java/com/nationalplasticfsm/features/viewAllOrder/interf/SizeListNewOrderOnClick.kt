package com.nationalplasticfsm.features.viewAllOrder.interf

import com.nationalplasticfsm.app.domain.NewOrderProductEntity
import com.nationalplasticfsm.app.domain.NewOrderSizeEntity

interface SizeListNewOrderOnClick {
    fun sizeListOnClick(size: NewOrderSizeEntity)
}