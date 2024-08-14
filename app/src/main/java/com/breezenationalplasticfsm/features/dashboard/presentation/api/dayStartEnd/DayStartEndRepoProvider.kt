package com.breezenationalplasticfsm.features.dashboard.presentation.api.dayStartEnd

import com.breezenationalplasticfsm.features.stockCompetetorStock.api.AddCompStockApi
import com.breezenationalplasticfsm.features.stockCompetetorStock.api.AddCompStockRepository

object DayStartEndRepoProvider {
    fun dayStartRepositiry(): DayStartEndRepository {
        return DayStartEndRepository(DayStartEndApi.create())
    }

}