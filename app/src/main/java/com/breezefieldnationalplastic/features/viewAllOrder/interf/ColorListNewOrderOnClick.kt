package com.breezefieldnationalplastic.features.viewAllOrder.interf

import com.breezefieldnationalplastic.app.domain.NewOrderColorEntity
import com.breezefieldnationalplastic.app.domain.NewOrderProductEntity

interface ColorListNewOrderOnClick {
    fun productListOnClick(color: NewOrderColorEntity)
}