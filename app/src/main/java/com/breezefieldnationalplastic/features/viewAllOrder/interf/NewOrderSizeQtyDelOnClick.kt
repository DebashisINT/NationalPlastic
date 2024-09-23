package com.breezefieldnationalplastic.features.viewAllOrder.interf

import com.breezefieldnationalplastic.app.domain.NewOrderGenderEntity
import com.breezefieldnationalplastic.features.viewAllOrder.model.ProductOrder
import java.text.FieldPosition

interface NewOrderSizeQtyDelOnClick {
    fun sizeQtySelListOnClick(product_size_qty: ArrayList<ProductOrder>)
    fun sizeQtyListOnClick(product_size_qty: ProductOrder,position: Int)
}