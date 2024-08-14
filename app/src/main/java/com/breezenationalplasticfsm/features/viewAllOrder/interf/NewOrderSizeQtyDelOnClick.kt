package com.breezenationalplasticfsm.features.viewAllOrder.interf

import com.breezenationalplasticfsm.app.domain.NewOrderGenderEntity
import com.breezenationalplasticfsm.features.viewAllOrder.model.ProductOrder
import java.text.FieldPosition

interface NewOrderSizeQtyDelOnClick {
    fun sizeQtySelListOnClick(product_size_qty: ArrayList<ProductOrder>)
    fun sizeQtyListOnClick(product_size_qty: ProductOrder,position: Int)
}