package com.nationalplasticfsm.features.taskManagement.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nationalplasticfsm.app.AppConstant

@Entity(tableName = AppConstant.TASK_ACTIVITY)
class TaskManagmentEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "task_status_id")
    var task_status_id: String? = null


    @ColumnInfo(name = "task_date")
    var task_date: String? = null

    @ColumnInfo(name = "task_time")
    var task_time: String? = null

    @ColumnInfo(name = "task_status")
    var task_status: String? = null

    @ColumnInfo(name = "task_details")
    var task_details: String? = null

    @ColumnInfo(name = "other_remarks")
    var other_remarks: String? = null

    @ColumnInfo(name = "task_next_date")
    var task_next_date: String? = null


}