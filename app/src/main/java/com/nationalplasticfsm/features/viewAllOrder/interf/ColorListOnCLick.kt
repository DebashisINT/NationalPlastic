package com.nationalplasticfsm.features.viewAllOrder.interf

import com.nationalplasticfsm.app.domain.NewOrderGenderEntity
import com.nationalplasticfsm.features.viewAllOrder.model.ProductOrder

interface ColorListOnCLick {
    fun colorListOnCLick(size_qty_list: ArrayList<ProductOrder>, adpPosition:Int)
}