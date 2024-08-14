package com.breezenationalplasticfsm.features.photoReg.adapter

import com.breezenationalplasticfsm.features.photoReg.model.ProsCustom
import com.breezenationalplasticfsm.features.photoReg.model.UserListResponseModel

interface ProsListSelectionListner {
    fun getInfo(obj: ProsCustom)
}