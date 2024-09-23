package com.breezefieldnationalplastic.features.photoReg.model

class UserListResponseModel {

    var user_name: String? = ""
    var user_login_id: String? = ""
    var user_id: Int? = 0
    var user_contactid: String? = ""
    var face_image_link: String? = ""
    var isFaceRegistered: Boolean? = false
    var IsPhotoDeleteShow: Boolean? = false
    var ShowDDInFaceRegistration: String? = ""
    var registration_date_time: String? = ""
    var IsAadhaarRegistered: Boolean? = false
    var RegisteredAadhaarNo: String? = ""
    var RegisteredAadhaarDocLink: String? = ""
    var aadhaar_remarks: String? = ""
    var aadhar_image_link: String? = ""
    var type_id: Int? = 0
    var type_name: String? = ""
    var Registered_with: String? = ""
    var emp_phone_no: String? = ""
    var IsShowManualPhotoRegnInApp: Boolean? = false
    var IsTeamAttenWithoutPhoto: Boolean? = false
    var IsAllowClickForVisitForSpecificUser: Boolean? = false
    var IsActiveUser: Boolean? = true
    var UpdateOtherID: Boolean? = true
    var UpdateUserID: Boolean? = true
    var OtherID: String? = ""
    var IsShowTypeInRegistrationForSpecificUser: Boolean? = false
    var Employee_Designation: String? = ""
}

