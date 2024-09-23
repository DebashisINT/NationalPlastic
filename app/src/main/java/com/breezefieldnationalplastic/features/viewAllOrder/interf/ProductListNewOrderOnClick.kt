package com.breezefieldnationalplastic.features.viewAllOrder.interf

import com.breezefieldnationalplastic.app.domain.NewOrderGenderEntity
import com.breezefieldnationalplastic.app.domain.NewOrderProductEntity

interface ProductListNewOrderOnClick {
    fun productListOnClick(product: NewOrderProductEntity)
}