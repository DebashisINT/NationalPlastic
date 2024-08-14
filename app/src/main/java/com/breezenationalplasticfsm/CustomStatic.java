package com.breezenationalplasticfsm;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import com.breezenationalplasticfsm.features.lead.model.CustomerListReq;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomStatic {
    public static Boolean IsFaceRec=false;
    public static String FaceUrl="";

    public static Boolean IsChooseTab=false;

    public static Boolean IsDocZero=true;

    public static Boolean IsFromViewNewOdrScr=true;
    public static String IsFromViewNewOdrScrOrderID="";
    public static String IsFromViewNewOdrScrOrderDate="";


    public static Boolean IsOrderFromTotalOrder=false;


    public static Boolean IsNewOrdDeleteConfirmOkClick=false;


    public static String NewOrderGenderMale="";
    public static String NewOrderGenderFeMale="";

    public static Boolean IsCommDLeftBtnColor = false;
    public static Boolean IsCommDRightBtnColor = false;

    public static Boolean IsSnycClickablebyOnce = false;

    public static Boolean IsCameraFacingFromTeamAttd=false;
    public static Boolean IsCameraFacingFromTeamAttdCametaStatus=false;

    public static Integer NewOrderTotalCartItem=0;

    public static String FaceDetectionAccuracyLower="";
    public static String FaceDetectionAccuracyUpper="";
    public static Boolean IsFaceRecognitionOnEyeblink=false;

    public static Boolean IsPowerSaverFragShowing=false;

    public static Boolean IsquestionnaireClickbyUser=false;

    public static Boolean IsPJPAddEdited=false;

    public static Boolean IsNewQuotEdit=false;


    public static Boolean IsViewLeadFromInProcess=false;
    public static Boolean IsViewLeadAddUpdate=false;

    public static Boolean IsViewTaskFromInProcess=false;
    public static Boolean IsViewTaskAddUpdate=false;
    public static String ShopFeedBachHisUserId="";

    public static Boolean IsCollectionViewFromTeam = false;
    public static Boolean IsBreakageViewFromTeam = false;
    public static String BreakageViewFromTeam_Name = "";

    public static String QutoNoFromNoti = "";


    public static HashMap<Integer,String> productQtyEdi = new HashMap<>();
    public static HashMap<Integer,String> productRateEdi = new HashMap<>();
    public static ArrayList<Integer> productAddedID = new ArrayList<>();

    public static Boolean IsAadhaarForPhotoReg=false;
    public static Boolean IsVoterForPhotoReg=false;
    public static Boolean IsPanForPhotoReg=false;
    public static String FaceRegFaceImgPath="";
    public static String AadhaarPicRegUrl="";
    public static String FacePicRegUrl="";

    public static String TeamUserSelect_user_id = "";

    public static Boolean IsBackFromNewOptiCart = false;
    public static Boolean IsOrderLoadFromCRM = false;
    public static Boolean IsOrderLoadFromShop = false;

    public static String lead_msgBody = "";
    public static String lead_msgLeadDate = "";
    public static String lead_msgLeadEnquiry = "";

    public static Boolean IsTeamAllParty = false;

    public static String TOPIC_SEL = "";

}
