package com.nationalplasticfsm.features.viewAllOrder.interf

import com.nationalplasticfsm.app.domain.NewOrderGenderEntity
import com.nationalplasticfsm.app.domain.NewOrderProductEntity

interface ProductListNewOrderOnClick {
    fun productListOnClick(product: NewOrderProductEntity)
}