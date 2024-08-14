package com.breezenationalplasticfsm.features.newcollectionreport

import com.breezenationalplasticfsm.features.photoReg.model.UserListResponseModel

interface PendingCollListner {
    fun getUserInfoOnLick(obj: PendingCollData)
}