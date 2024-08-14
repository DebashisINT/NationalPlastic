package com.breezenationalplasticfsm.features.gpsstatus

/**
 * Created by Pratishruti on 18-01-2018.
 */
interface LocationCallBack {
    /**
     * on Location switch triggered
     */
    fun onLocationTriggered(status:Boolean)
}