package com.breezefieldnationalplastic.features.viewAllOrder.interf

import com.breezefieldnationalplastic.app.domain.NewOrderGenderEntity
import com.breezefieldnationalplastic.features.viewAllOrder.model.ProductOrder

interface ColorListOnCLick {
    fun colorListOnCLick(size_qty_list: ArrayList<ProductOrder>, adpPosition:Int)
}