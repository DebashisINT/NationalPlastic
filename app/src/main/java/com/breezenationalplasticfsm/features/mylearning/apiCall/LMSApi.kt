package com.breezenationalplasticfsm.features.mylearning.apiCall

import com.breezenationalplasticfsm.app.NetworkConstant
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.addshop.model.AddQuestionSubmitRequestData
import com.breezenationalplasticfsm.features.login.api.opportunity.OpportunityListApi
import com.breezenationalplasticfsm.features.login.model.opportunitymodel.OpportunityStatusListResponseModel
import com.breezenationalplasticfsm.features.mylearning.LMS_CONTENT_INFO
import com.breezenationalplasticfsm.features.mylearning.MyCommentListResponse
import com.breezenationalplasticfsm.features.mylearning.MyLarningListResponse
import com.breezenationalplasticfsm.features.mylearning.TopicListResponse
import com.breezenationalplasticfsm.features.mylearning.VideoTopicWiseResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LMSApi {

    @FormUrlEncoded
    @POST("LMSInfoDetails/TopicLists")
    fun getTopics(@Field("user_id") user_id: String): Observable<TopicListResponse>

    @FormUrlEncoded
    @POST("LMSInfoDetails/TopicWiseLists")
    fun getTopicswiseVideoApi(@Field("user_id") user_id: String,@Field("topic_id") topic_id: String): Observable<VideoTopicWiseResponse>

    @POST("LMSInfoDetails/TopicContentDetailsSave")
    fun saveContentInfoApi(@Body lms_content_info: LMS_CONTENT_INFO?): Observable<BaseResponse>

    @FormUrlEncoded
    @POST("LMSInfoDetails/LearningContentLists")
    fun getMyLearningContentList(@Field("user_id") user_id: String): Observable<MyLarningListResponse>

    @FormUrlEncoded
    @POST("LMSInfoDetails/CommentLists")
    fun getCommentInfo(@Field("topic_id") topic_id: String , @Field("content_id") content_id: String): Observable<MyCommentListResponse>

    companion object Factory {
        fun create(): LMSApi {
            val retrofit = Retrofit.Builder()
                .client(NetworkConstant.setTimeOut())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetworkConstant.BASE_URL)
                .build()

            return retrofit.create(LMSApi::class.java)
        }
    }
}