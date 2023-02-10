package com.nationalplasticfsm.features.photoReg.present

import com.nationalplasticfsm.app.domain.ProspectEntity
import com.nationalplasticfsm.features.photoReg.model.UserListResponseModel

interface DsStatusListner {
    fun getDSInfoOnLick(obj: ProspectEntity)
}