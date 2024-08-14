package com.breezenationalplasticfsm.features.beatCustom.api

import com.breezenationalplasticfsm.features.beatCustom.BeatGetStatusModel
import com.breezenationalplasticfsm.features.beatCustom.BeatUpdateModel
import io.reactivex.Observable

class GetBeatListRegRepository(val apiService : GetBeatProductListApi) {

    fun getBeat(user_id: String,beat_date: String,session_token: String): Observable<BeatGetStatusModel>{
        return apiService.getBeatstatus(user_id,beat_date,session_token)
    }

    fun getUpdateBeat(user_id: String,updating_beat_id: String,updating_date: String): Observable<BeatUpdateModel>{
        return apiService.updateBeatstatus(user_id,updating_beat_id,updating_date)
    }

}