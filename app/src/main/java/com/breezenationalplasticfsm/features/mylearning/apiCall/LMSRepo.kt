package com.breezenationalplasticfsm.features.mylearning.apiCall

import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.login.api.opportunity.OpportunityListApi
import com.breezenationalplasticfsm.features.login.model.opportunitymodel.OpportunityStatusListResponseModel
import com.breezenationalplasticfsm.features.mylearning.LMS_CONTENT_INFO
import com.breezenationalplasticfsm.features.mylearning.MyCommentListResponse
import com.breezenationalplasticfsm.features.mylearning.MyLarningListResponse
import com.breezenationalplasticfsm.features.mylearning.TopicListResponse
import com.breezenationalplasticfsm.features.mylearning.VideoPlayLMS
import com.breezenationalplasticfsm.features.mylearning.VideoTopicWiseResponse
import io.reactivex.Observable

class LMSRepo(val apiService: LMSApi) {

    fun getTopics(user_id: String): Observable<TopicListResponse> {
        return apiService.getTopics(user_id)
    }

    fun getTopicsWiseVideo(user_id: String ,topic_id: String): Observable<VideoTopicWiseResponse> {
        return apiService.getTopicswiseVideoApi(user_id,topic_id)
    }

    fun saveContentInfoApi(lms_content_info: LMS_CONTENT_INFO): Observable<BaseResponse> {
        return apiService.saveContentInfoApi(lms_content_info)
    }

    fun getMyLraningInfo(user_id: String): Observable<MyLarningListResponse> {
        return apiService.getMyLearningContentList(user_id)
    }

    fun getCommentInfo(topic_id: String ,content_id: String): Observable<MyCommentListResponse> {
        return apiService.getCommentInfo(topic_id , content_id)
    }
}