package com.breezenationalplasticfsm.features.viewAllOrder.interf

import com.breezenationalplasticfsm.features.viewAllOrder.model.ProductOrder

interface EmptyProductOnClick {
    fun emptyProductOnCLick(emptyFound:Boolean)
    fun delProductOnCLick(isDel:Boolean)
}