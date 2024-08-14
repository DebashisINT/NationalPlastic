package com.breezenationalplasticfsm.features.viewAllOrder.interf

import com.breezenationalplasticfsm.app.domain.NewOrderGenderEntity
import com.breezenationalplasticfsm.features.viewAllOrder.model.ProductOrder

interface ColorListOnCLick {
    fun colorListOnCLick(size_qty_list: ArrayList<ProductOrder>, adpPosition:Int)
}