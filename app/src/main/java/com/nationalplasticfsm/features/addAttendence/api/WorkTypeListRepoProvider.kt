package com.nationalplasticfsm.features.addAttendence.api

/**
 * Created by Saikat on 31-08-2018.
 */
object WorkTypeListRepoProvider {
    fun workTypeListRepo(): WorkTypeListRepo {
        return WorkTypeListRepo(WorkTypeListApi.create())
    }
}