package com.breezenationalplasticfsm.features.mylearning

data class LmsSearchData(val searchid: String,val courseName: String, var video_count: Int = 0, var isSelected: Boolean = false/*, val courseImg: Int*/)
data class HeaderItem(val headerText: String, val valueItems: List<ValueItem>)
data class ValueItem(val valueHeader: String, val valueText: String, val imageResId: Int)

data class CommentHis(var contentID:String="",var comment:String="",var dateTime:String="")

data class QuestionViewL(var serial: Int=0,var question_id:String="",var question:String="",var optionL:ArrayList<QuestionOptions> = ArrayList())
data class QuestionOptions(var serial:Int=0,var desc:String="",var points:Int=0,var isCorrect:Boolean=false,var isSelected:Boolean=false)
