package com.breezenationalplasticfsm.features.orderhistory.api

import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.orderhistory.model.LocationUpdateRequest
import io.reactivex.Observable

/**
 * Created by Pratishruti on 23-11-2017.
 */
class LocationUpdateRepository(val apiService:LocationUpdateApi) {
    fun sendLocationUpdate(location: LocationUpdateRequest): Observable<BaseResponse> {
        for(i in 0..location.location_details!!.size-1){
            var ob = location.location_details!!.get(i)
            //println("distance_loc_tag LocationUpdateRepository ${ob.latitude} ${ob.longitude} ${ob.distance_covered} ${ob.date}")
            println("distance_loc_tag LocationUpdateRepositoryvalue ${ob.location_name} ${ob.latitude} ${ob.longitude} ${ob.distance_covered} ${ob.date} " +
                    "${ob.last_update_time} ${ob.locationId} ${ob.meeting_attended} ${ob.home_distance} ${ob.network_status} ${ob.battery_percentage} ${ob.home_duration} ")
        }
        return apiService.sendLocationUpdates(location)
    }
}