package com.nationalplasticfsm.features.login.presentation

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.AppDatabase
import com.nationalplasticfsm.app.NetworkConstant
import com.nationalplasticfsm.app.Pref
import com.nationalplasticfsm.app.domain.MemberShopEntity
import com.nationalplasticfsm.app.utils.AppUtils
import com.nationalplasticfsm.base.presentation.BaseActivity
import com.nationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.nationalplasticfsm.features.member.api.TeamRepoProvider
import com.nationalplasticfsm.features.member.model.TeamShopListResponseModel

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber

class MemberShopListIntentService : IntentService("") {

    override fun onHandleIntent(intent: Intent?) {
        val list = AppDatabase.getDBInstance()?.memberShopDao()?.getAll()

        if (list == null || list.isEmpty()) {
            Pref.isOfflineShopSaved = false
            Timber.e("==============call offline member shop api(Service)==============")
            Timber.d("PJP api callMemberShopListApi MemberShopListIntentService call")
            val repository = TeamRepoProvider.teamRepoProvider()
            BaseActivity.compositeDisposable.add(
                    repository.offlineTeamShopList("")
                            //.observeOn(AndroidSchedulers.mainThread())
                            //.subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val response = result as TeamShopListResponseModel
                                Timber.d("OFFLINE MEMBER SHOP LIST: " + "RESPONSE : " + response.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ", MESSAGE : " + response.message)
                                if (response.status == NetworkConstant.SUCCESS) {

                                    if (response.shop_list != null && response.shop_list!!.isNotEmpty()) {

                                        //doAsync {

                                            response.shop_list?.forEach {
                                                val memberShop = MemberShopEntity()
                                                AppDatabase.getDBInstance()?.memberShopDao()?.insertAll(memberShop.apply {
                                                    user_id = it.user_id
                                                    shop_id = it.shop_id
                                                    shop_name = it.shop_name
                                                    shop_lat = it.shop_lat
                                                    shop_long = it.shop_long
                                                    shop_address = it.shop_address
                                                    shop_pincode = it.shop_pincode
                                                    shop_contact = it.shop_contact
                                                    total_visited = it.total_visited
                                                    last_visit_date = it.last_visit_date
                                                    shop_type = it.shop_type
                                                    dd_name = it.dd_name
                                                    entity_code = it.entity_code
                                                    model_id = it.model_id
                                                    primary_app_id = it.primary_app_id
                                                    secondary_app_id = it.secondary_app_id
                                                    lead_id = it.lead_id
                                                    funnel_stage_id = it.funnel_stage_id
                                                    stage_id = it.stage_id
                                                    booking_amount = it.booking_amount
                                                    type_id = it.type_id
                                                    area_id = it.area_id
                                                    assign_to_pp_id = it.assign_to_pp_id
                                                    assign_to_dd_id = it.assign_to_dd_id
                                                    isUploaded = true
                                                    date_time = AppUtils.getCurrentISODateTime()
                                                })
                                            }
                                        Timber.e("==============offline member shop added to db(Service)==============")
                                        Pref.isOfflineShopSaved = true

                                        val intent_ = Intent()
                                        intent_.action = "OFFLINE_SHOP_BROADCAST"
                                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent_)

                                            /*uiThread {
                                                Timber.e("==============offline member shop added to db(Service)==============")
                                            }
                                        }*/
                                    }
                                    else
                                        Pref.isOfflineShopSaved = true
                                }
                                else
                                    Pref.isOfflineShopSaved = true

                            }, { error ->
                                error.printStackTrace()
                                Pref.isOfflineShopSaved = true
                                Timber.d("OFFLINE MEMBER SHOP LIST: " + "ERROR : " + error.localizedMessage + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name)
                            })
            )
        }
        else
            Pref.isOfflineShopSaved = true
    }
}