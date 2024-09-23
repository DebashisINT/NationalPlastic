package com.breezefieldnationalplastic.features.leaderboard.api

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import com.fasterxml.jackson.databind.ObjectMapper
import com.breezefieldnationalplastic.app.FileUtils
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.features.addshop.model.AddLogReqData
import com.breezefieldnationalplastic.features.addshop.model.AddShopRequestData
import com.breezefieldnationalplastic.features.addshop.model.AddShopResponse
import com.breezefieldnationalplastic.features.addshop.model.LogFileResponse
import com.breezefieldnationalplastic.features.addshop.model.UpdateAddrReq
import com.breezefieldnationalplastic.features.contacts.CallHisDtls
import com.breezefieldnationalplastic.features.contacts.CompanyReqData
import com.breezefieldnationalplastic.features.contacts.ContactMasterRes
import com.breezefieldnationalplastic.features.contacts.SourceMasterRes
import com.breezefieldnationalplastic.features.contacts.StageMasterRes
import com.breezefieldnationalplastic.features.contacts.StatusMasterRes
import com.breezefieldnationalplastic.features.contacts.TypeMasterRes
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.login.model.WhatsappApiData
import com.breezefieldnationalplastic.features.login.model.WhatsappApiFetchData
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