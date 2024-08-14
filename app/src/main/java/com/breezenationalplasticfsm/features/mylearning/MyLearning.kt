package com.breezenationalplasticfsm.features.mylearning

import android.content.SyncRequest
import com.breezenationalplasticfsm.base.BaseResponse

data class GridDashboard(var imagepath:String = "" , var name :String = "")

data class TopicListResponse(var user_id:String="",var topic_list:ArrayList<TopicList>):BaseResponse()
data class MyLarningListResponse(var user_id:String="",var learning_content_info_list:ArrayList<LarningList>):BaseResponse()
data class MyCommentListResponse(var comment_list:ArrayList<CommentL>):BaseResponse()


data class VideoTopicWiseResponse(var topic_id:String="",var topic_name:String="",var content_list:ArrayList<ContentL>):BaseResponse()
data class ContentL(var content_id:String="",var content_url:String="",var content_thumbnail:String="",var content_title:String="",var content_description:String="",var content_play_sequence:String="",
    var isAllowLike:Boolean = false,var isAllowComment:Boolean = false,var isAllowShare:Boolean=false,var no_of_comment:Int = 0 ,var like_flag:Boolean = false ,var share_count:Int = 0,
                    var content_length:String = "",var content_watch_length:String = "",var content_watch_completed:Boolean = false,var content_last_view_date_time:String = "",var WatchStartTime:String = "",
                    var WatchEndTime:String = "",var WatchedDuration:String = "",var Timestamp:String = "",var DeviceType:String = "",var Operating_System:String = "",
                    var Location:String = "",var PlaybackSpeed:String = "",var Watch_Percentage:String = "",var QuizAttemptsNo:Int = 0,var QuizScores:Double = 0.0,
                    var CompletionStatus:Boolean=false,
                    var question_list:ArrayList<QuestionL> = ArrayList(),var isLiked:Boolean=false
)
data class QuestionL(var topic_id:String="",var content_id:String="",var question_id:String="",var question:String="",var question_description:String="",
                     var option_list:ArrayList<OptionL> = ArrayList())
data class SequenceQuestion(var index:Int=0, var question_list:ArrayList<QuestionL> = ArrayList())

data class OptionL(var question_id:String="",var option_id:String="",
                   var option_no_1:String="",var option_point_1:String="",var isCorrect_1:Boolean=false,var isSelected_1: Boolean,
                   var option_no_2:String="",var option_point_2:String="",var isCorrect_2:Boolean=false,var isSelected_2: Boolean,
                   var option_no_3:String="",var option_point_3:String="",var isCorrect_3:Boolean=false,var isSelected_3: Boolean,
                   var option_no_4:String="",var option_point_4:String="",var isCorrect_4:Boolean=false,var isSelected_4: Boolean)

data class TopicList(var topic_id:Int=0,var topic_name:String="",var video_count:Int=0)
data class LarningList(var topic_id:Int=0,var topic_name:String="",var content_id:Int=0,var content_url:String="",var content_title:String="",var content_description:String="", var content_length:String="", var content_watch_length:String="", var content_watch_completed:Boolean=false,var content_last_view_date_time:String="",
                       var WatchStartTime:String="",var WatchEndTime:String="",var WatchedDuration:String="",var Timestamp:String="",var DeviceType:String="",var Operating_System:String="",var Location:String="",var PlaybackSpeed:String="",var Watch_Percentage:String="",var QuizAttemptsNo:Int=0,var QuizScores:Int=0,var CompletionStatus:Boolean=false)

data class LMS_CONTENT_INFO(var user_id:String = "",var topic_id:Int = 0,var topic_name:String = "",var content_id:Int = 0,
                            var like_flag:Boolean=false,var share_count:Int = 0,var no_of_comment:Int = 0,var content_length:String = "",
                            var content_watch_length:String = "",var content_watch_start_date:String = "",var content_watch_end_date:String = "",var content_watch_completed:Boolean =false,
                            var content_last_view_date_time:String = "",var WatchStartTime:String = "",var WatchEndTime:String = "",
                            var WatchedDuration:String = "",var Timestamp:String = "",var DeviceType:String = "",
                            var Operating_System:String = "",var Location:String = "",var PlaybackSpeed:String = "",
                            var Watch_Percentage:String = "",var QuizAttemptsNo:Int = 0,
                            var QuizScores:Int = 0,
                            var CompletionStatus:Boolean = false,var comment_list:ArrayList<CommentL> = ArrayList())

data class CommentL(var topic_id:String="",var content_id:String="",var commented_user_id:String="",var commented_user_name:String="",var comment_id:String="",var comment_description:String="",var comment_date_time:String="")
data class SavedContentIds(var content_id: MutableSet<Int> = mutableSetOf())
