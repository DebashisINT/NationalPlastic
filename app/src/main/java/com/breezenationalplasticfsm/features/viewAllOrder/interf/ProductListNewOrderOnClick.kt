package com.breezenationalplasticfsm.features.viewAllOrder.interf

import com.breezenationalplasticfsm.app.domain.NewOrderGenderEntity
import com.breezenationalplasticfsm.app.domain.NewOrderProductEntity

interface ProductListNewOrderOnClick {
    fun productListOnClick(product: NewOrderProductEntity)
}