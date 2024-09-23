package com.breezefieldnationalplastic.features.viewAllOrder.interf

import com.breezefieldnationalplastic.app.domain.NewOrderProductEntity
import com.breezefieldnationalplastic.app.domain.NewOrderSizeEntity

interface SizeListNewOrderOnClick {
    fun sizeListOnClick(size: NewOrderSizeEntity)
}