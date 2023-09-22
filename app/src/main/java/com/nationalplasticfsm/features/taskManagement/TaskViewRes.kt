package com.nationalplasticfsm.features.taskManagement


data class TaskViewRes(
        var status:String,
        var message:String,
        var task_id:String,
                       var task_status_dtls_list:ArrayList<Taskdtls_list>)

data class Taskdtls_list(var task_status_id:String,
                              var task_date:String,
                              var task_time:String,
                              var task_status:String,
                              var task_details:String,
                              var other_remarks:String,
                              var task_next_date:String,
                              var isactive_status:String)
