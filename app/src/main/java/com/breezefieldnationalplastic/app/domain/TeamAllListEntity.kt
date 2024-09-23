package com.breezefieldnationalplastic.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezefieldnationalplastic.app.AppConstant

@Entity(tableName = AppConstant.TEAM_LIST_ALL)
data class TeamAllListEntity  (
    @PrimaryKey(autoGenerate = true) var sl_no: Int = 0,
    @ColumnInfo var user_id:String = "",
    @ColumnInfo var user_name:String = "",
    @ColumnInfo var contact_no:String = "",
    @ColumnInfo var Employee_Code:String = "",
    @ColumnInfo var EMP_ContactID:String = ""
)