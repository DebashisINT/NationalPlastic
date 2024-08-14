package com.breezenationalplasticfsm.features.photoReg.present

import com.breezenationalplasticfsm.app.domain.ProspectEntity
import com.breezenationalplasticfsm.features.photoReg.model.UserListResponseModel

interface DsStatusListner {
    fun getDSInfoOnLick(obj: ProspectEntity)
}