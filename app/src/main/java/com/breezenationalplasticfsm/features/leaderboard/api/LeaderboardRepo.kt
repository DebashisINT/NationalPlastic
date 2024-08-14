package com.breezenationalplasticfsm.features.leaderboard.api

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import com.fasterxml.jackson.databind.ObjectMapper
import com.breezenationalplasticfsm.app.FileUtils
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.addshop.model.AddLogReqData
import com.breezenationalplasticfsm.features.addshop.model.AddShopRequestData
import com.breezenationalplasticfsm.features.addshop.model.AddShopResponse
import com.breezenationalplasticfsm.features.addshop.model.LogFileResponse
import com.breezenationalplasticfsm.features.addshop.model.UpdateAddrReq
import com.breezenationalplasticfsm.features.contacts.CallHisDtls
import com.breezenationalplasticfsm.features.contacts.CompanyReqData
import com.breezenationalplasticfsm.features.contacts.ContactMasterRes
import com.breezenationalplasticfsm.features.contacts.SourceMasterRes
import com.breezenationalplasticfsm.features.contacts.StageMasterRes
import com.breezenationalplasticfsm.features.contacts.StatusMasterRes
import com.breezenationalplasticfsm.features.contacts.TypeMasterRes
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.login.model.WhatsappApiData
import com.breezenationalplasticfsm.features.login.model.WhatsappApiFetchData
import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Puja on 10-10-2024.
 */
class LeaderboardRepo(val apiService: LeaderboardApi) {

    fun branchlist(session_token: String): Observable<LeaderboardBranchData> {
        return apiService.branchList(session_token)
    }
    fun ownDatalist(user_id: String,activitybased: String,branchwise: String,flag: String): Observable<LeaderboardOwnData> {
        return apiService.ownDatalist(user_id,activitybased,branchwise,flag)
    }
    fun overAllAPI(user_id: String,activitybased: String,branchwise: String,flag: String): Observable<LeaderboardOverAllData> {
        return apiService.overAllDatalist(user_id,activitybased,branchwise,flag)
    }
}