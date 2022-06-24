package com.nationalplasticfsm.features.viewAllOrder.interf

import com.nationalplasticfsm.app.domain.NewOrderGenderEntity
import com.nationalplasticfsm.features.viewAllOrder.model.ProductOrder
import java.text.FieldPosition

interface NewOrderSizeQtyDelOnClick {
    fun sizeQtySelListOnClick(product_size_qty: ArrayList<ProductOrder>)
    fun sizeQtyListOnClick(product_size_qty: ProductOrder,position: Int)
}