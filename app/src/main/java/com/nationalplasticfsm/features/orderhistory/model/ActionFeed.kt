package com.nationalplasticfsm.features.orderhistory.model

import java.io.Serializable

/**
 * Created by riddhi on 4/12/17.
 */
interface ActionFeed : Serializable {

    fun refresh()
}