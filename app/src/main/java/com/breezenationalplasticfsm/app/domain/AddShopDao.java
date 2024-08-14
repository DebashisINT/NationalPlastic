package com.breezenationalplasticfsm.app.domain;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static com.breezenationalplasticfsm.app.AppConstant.SHOP_TABLE;

import com.breezenationalplasticfsm.features.marketAssist.ChurnShopL;
import com.breezenationalplasticfsm.features.marketAssist.ShopDtls;
import com.breezenationalplasticfsm.features.marketAssist.ShopLastVisit;
import com.breezenationalplasticfsm.features.performanceAPP.NoOrderTakenList;
import com.breezenationalplasticfsm.features.performanceAPP.ShopDtlsCustom;

/**
 * Created by sayantan.sarkar on 2/11/17.
 */
@Dao
public interface AddShopDao {

//    @Query("SELECT COUNT(*) AS row_count FROM "  + SHOP_TABLE + " WHERE landline_number = :landlineNumber")
//    int getLandNumber(String landlineNumber);



    //@Query("SELECT landline_number FROM "  + SHOP_TABLE + " WHERE  landline_number = :landline_number IS NOT NULL and landline_number = :landline_number > 0")
//    @Query("SELECT landline_number FROM "  + SHOP_TABLE + " WHERE  landline_number LIKE '%' || :landline_number || '%'  limit 1 ")
//    String getLandNumber(String landline_number);


    @Query("SELECT * FROM " + SHOP_TABLE)
    List<AddShopDBModelEntity> getAll();

    @Query("SELECT * FROM " + SHOP_TABLE +" order by upper(shop_name)")
    List<AddShopDBModelEntity> getOrderByalphabeticallyAll();

    @Query("SELECT * FROM " + SHOP_TABLE+" where isOwnshop=:isOwnshop")
    List<AddShopDBModelEntity> getAllOwn(Boolean isOwnshop);

    @Query("SELECT * FROM " + SHOP_TABLE+" where isOwnshop=:isOwnshop and type!=:type")
    List<AddShopDBModelEntity> getAllOwnFilterType99(Boolean isOwnshop,String type);

    @Query("select distinct shop_detail.* from shop_detail inner join order_details_list on shop_detail.shop_id = order_details_list.shop_id ")
    List<AddShopDBModelEntity> getShopIdHasOrder();

    @Query("select distinct shop_detail.* from shop_detail inner join order_details_list on shop_detail.shop_id = order_details_list.shop_id and assigned_to_dd_id=:assigned_to_dd_id and lastVisitedDate=:lastVisitedDate")
    List<AddShopDBModelEntity> getShopIdHasOrderDDWise(String assigned_to_dd_id,String lastVisitedDate);


    @Query("select  * from shop_detail where shopid < 10")
    List<AddShopDBModelEntity> getTop10();

    @Query("SELECT COUNT(*) from " + SHOP_TABLE)
    int countUsers();

    @Insert
    void insertAll(AddShopDBModelEntity... shops);

    @Insert
    void insert(AddShopDBModelEntity shops);

    @Delete
    void delete(AddShopDBModelEntity user);

    @Query("update shop_detail set isVisited=:isvisited where shop_id=:shopId")
    void updateIsVisited(Boolean isvisited, String shopId);

    @Query("update shop_detail set isVisited=:isvisited")
    void updateIsVisitedToFalse(Boolean isvisited);

    @Query("Select * from shop_detail where shop_id=:shopId and isVisited=:isvisited")
    AddShopDBModelEntity getShopById(Boolean isvisited, String shopId);

    @Query("Select * from shop_detail where shop_id=:shopId")
    AddShopDBModelEntity getShopByIdN(String shopId);

    @Query("Select * from shop_detail where shop_id=:shopId and owner_contact_number=:owner_contact_number")
    List<AddShopDBModelEntity> getShopByIdPhone(String shopId,String owner_contact_number);

    @Query("Select owner_contact_number from shop_detail")
    List<String> getAllOwnerContact();

    @Query("Select * from shop_detail where owner_contact_number=:owner_contact_number")
    List<AddShopDBModelEntity> getShopByPhone(String owner_contact_number);

    @Query("Select * from shop_detail where shop_id=:shopId")
    List<AddShopDBModelEntity> getShopByIdList(String shopId);

    @Query("Select * from shop_detail where visitDate=:date and isVisited=:isvisited")
    List<AddShopDBModelEntity> getShopsVisitedPerDay(String date, Boolean isvisited);

    @Query("Select * from shop_detail where type=:type")
    List<AddShopDBModelEntity> getShopsAccordingToType(String type);

    @Query("Select * from shop_detail where isVisited=:isvisited")
    List<AddShopDBModelEntity> getTotalShopVisited(Boolean isvisited);

    @Query("Select * from shop_detail where visitDate=:visitDate and isVisited=:isvisited")
    List<AddShopDBModelEntity> getTotalShopVisitedForADay(String visitDate, Boolean isvisited);

    @Query("Select lastVisitedDate from shop_detail where shop_id=:shopId")
    String getLastVisitedDate(String shopId);

    @Query("Select * from shop_detail where shop_name=:shopName and isVisited=:isvisited")
    List<AddShopDBModelEntity> getVisitedShopListByName(String shopName, Boolean isvisited);

    @Query("Select * from shop_detail group by shop_name")
    List<AddShopDBModelEntity> getUniqueShoplist();

    @Query("Select * from shop_detail where isVisited=:isvisited")
    List<AddShopDBModelEntity> getAllVisitedShops(Boolean isvisited);

    @Query("update shop_detail set endTimeStamp=:endTimeStamp where shop_id=:shopId")
    void updateEndTimeSpan(String endTimeStamp, String shopId);

    @Query("update shop_detail set duration=:timeDuration where shop_id=:shopId")
    void updateTimeDuration(String timeDuration, String shopId);

    @Query("Select duration from shop_detail where shop_id=:shopId and visitDate=:date")
    String getTimeDurationForDayOfShop(String shopId, String date);

    @Query("Select * from shop_detail where visitDate=:date and isVisited=:isvisited and isUploaded=:isUploaded")
    List<AddShopDBModelEntity> getShopsNotUploaded(String date, Boolean isvisited, Boolean isUploaded);

    @Query("Select * from shop_detail where isVisited=:isvisited and isUploaded=:isUploaded")
    List<AddShopDBModelEntity> getAllShopsNotUploaded(Boolean isvisited, Boolean isUploaded);
//    @Query("Select * from shop_detail where shopId=:shopId and visitDate=:visitDate and isVisited=:isvisited")
//    AddShopDBModelEntity getShopVisitCouint(Boolean isvisited, String shopId);

    @Query("Select * from shop_detail where visitDate=:visitDate and isVisited=:isvisited")
    AddShopDBModelEntity getDayWiseShopList(String visitDate, Boolean isvisited);

    @Query("SELECT count(DISTINCT visitDate) FROM shop_detail")
    int getTotalDays();

    @Query("update shop_detail set isUploaded=:isUploaded where shop_id=:shopId")
    void updateIsUploaded(Boolean isUploaded, String shopId);

    @Query("update shop_detail set companyName_id=:companyName_id where shop_id=:shopId")
    void updateCompanyID(String companyName_id,String shopId);

    @Query("update shop_detail set isEditUploaded=:isEditUploaded where shop_id=:shopId")
    void updateIsEditUploaded(int isEditUploaded, String shopId);

    @Query("update shop_detail set totalVisitCount=:totalCount where shop_id=:shopId")
    void updateTotalCount(String totalCount, String shopId);

    @Query("update shop_detail set lastVisitedDate=:visitDate where shop_id=:shopId")
    void updateLastVisitDate(String visitDate, String shopId);

    @Query("Select * from shop_detail where shop_id=:shopId")
    AddShopDBModelEntity getShopDetail(String shopId);

    @Query("DELETE FROM shop_detail where shop_id=:shopId")
    int deleteShopById(String shopId);

    @Query("Select * from shop_detail where isUploaded=:isUploaded")
    List<AddShopDBModelEntity> getUnSyncedShops(Boolean isUploaded);



    @Query("Select * from shop_detail where owner_contact_number=:contactNum")
    List<AddShopDBModelEntity> getDuplicateShopData(String contactNum);

    @Query("Select * from shop_detail where owner_contact_number=:contactNum and shop_id!=:shop_id")
    List<AddShopDBModelEntity> getDuplicateShopData(String contactNum,String shop_id);

    @Query("Select * from shop_detail where shop_name LIKE '%' || :shopNameorNum  || '%' OR owner_contact_number LIKE '%' || :shopNameorNum  || '%' ")
    List<AddShopDBModelEntity> getShopBySearchData(String shopNameorNum);

    @Query("Select * from shop_detail where shop_name LIKE '%' || :shopNameorNum  || '%' OR owner_contact_number LIKE '%' || :shopNameorNum  ||  '%' OR owner_name LIKE '%' || :shopNameorNum  || '%' ")
   // @Query("Select * from shop_detail where shop_name LIKE '%' || :shopNameorNum )
    List<AddShopDBModelEntity> getShopBySearchDataNew(String shopNameorNum);

    @Query("Select * from shop_detail where (shop_name LIKE '%' || :shopNameorNum  || '%' OR owner_contact_number LIKE '%' || :shopNameorNum  ||  '%' OR owner_name LIKE '%' || :shopNameorNum  || '%') and isOwnshop=:isOwnshop")
        // @Query("Select * from shop_detail where shop_name LIKE '%' || :shopNameorNum )
    List<AddShopDBModelEntity> getShopBySearchDataNewWithOwnShop(String shopNameorNum,Boolean isOwnshop);

    @Query("Select * from shop_detail where (shop_name LIKE '%' || :shopNameorNum  || '%' OR owner_contact_number LIKE '%' || :shopNameorNum  ||  '%' OR owner_name LIKE '%' || :shopNameorNum  || '%') and isOwnshop=:isOwnshop and type!=:type ")
        // @Query("Select * from shop_detail where shop_name LIKE '%' || :shopNameorNum )
    List<AddShopDBModelEntity> getShopBySearchDataNewWithOwnShopType(String shopNameorNum,Boolean isOwnshop,String type);

    @Update
    int updateAddShop(AddShopDBModelEntity mAddShopDBModelEntity);


    @Query("DELETE FROM " + SHOP_TABLE)
    void deleteAll();

    @Update
    public int updateShopDao(AddShopDBModelEntity... mAddShopDBModelEntity);

    @Query("update shop_detail set isAddressUpdated=:isAddressUpdated where shop_id=:shopId")
    public int updateIsAddressUpdated(String shopId, Boolean isAddressUpdated);

    @Query("update shop_detail set owner_contact_number=:owner_contact_number where shop_id=:shopId")
    public int updateContactNo(String shopId, String owner_contact_number);

    @Query("update shop_detail set shop_image_local_path=:shop_image_local_path where shop_id=:shopId")
    void updateShopImage(String shop_image_local_path, String shopId);

    @Query("update shop_detail set shop_name=:shop_name where shop_id=:shopId")
    void updateShopName(String shop_name, String shopId);

    @Query("update shop_detail set owner_contact_number=:owner_contact_number where shop_id=:shopId")
    void updateOwnerContactNumber(String owner_contact_number, String shopId);

    @Query("update shop_detail set owner_email=:owner_email where shop_id=:shopId")
    void updateOwnerEmail(String owner_email, String shopId);

    @Query("update shop_detail set owner_name=:owner_name where shop_id=:shopId")
    void updateOwnerName(String owner_name, String shopId);

    @Query("update shop_detail set dateOfBirth=:dateOfBirth where shop_id=:shopId")
    void updateDOB(String dateOfBirth, String shopId);

    @Query("update shop_detail set dateOfAniversary=:dateOfAniversary where shop_id=:shopId")
    void updateDOA(String dateOfAniversary, String shopId);

    @Query("update shop_detail set type=:type where shop_id=:shopId")
    void updateType(String type, String shopId);

    @Query("update shop_detail set assigned_to_dd_id=:assigned_to_dd_id where shop_id=:shopId")
    void updateDDid(String assigned_to_dd_id, String shopId);

    @Query("update shop_detail set assigned_to_pp_id=:assigned_to_pp_id where shop_id=:shopId")
    void updatePPid(String assigned_to_pp_id, String shopId);

    @Query("update shop_detail set is_otp_verified=:is_otp_verified where shop_id=:shopId")
    void updateIsOtpVerified(String is_otp_verified, String shopId);

    @Query("Select * from shop_detail where isEditUploaded=:isEditUploaded and isUploaded=:isUploaded")
    List<AddShopDBModelEntity> getUnsyncEditShop(int isEditUploaded, Boolean isUploaded);

    @Query("Select owner_contact_number from shop_detail where shop_id=:shopId")
    String getContactNumber(String shopId);

    @Query("Select type from shop_detail where shop_id=:shop_id")
    String getShopType(String shop_id);

    @Query("update shop_detail set party_status_id=:party_status_id where shop_id=:shopId")
    void updatePartyStatus(String party_status_id, String shopId);

    @Query("Select * from shop_detail where beat_id=:beat_id")
    List<AddShopDBModelEntity> getShopBeatWise(String beat_id);

    @Query("Select * from shop_detail where beat_id=:beat_id and assigned_to_dd_id =:assigned_to_dd_id")
    List<AddShopDBModelEntity> getShopBeatWiseDD(String beat_id,String assigned_to_dd_id);


    @Query("Select * from shop_detail where shop_name LIKE '%' || :shopNameorNum  || '%' OR owner_contact_number LIKE '%' || :shopNameorNum  || '%' and beat_id=:beat_id")
    List<AddShopDBModelEntity> getSearchedShopBeatWise(String beat_id, String shopNameorNum);

    @Query("update shop_detail set account_holder=:account_holder, account_no=:account_no, bank_name=:bank_name, ifsc_code=:ifsc_code, upi_id=:upi_id where shop_id=:shopId")
    void updateBankDetails(String account_holder, String account_no, String bank_name, String ifsc_code, String upi_id, String shopId);

    @Query("Select project_name from shop_detail where shop_id=:shopId")
    String getProjectName(String shopId);

    @Query("Select landline_number from shop_detail where shop_id=:shopId")
    String getLand(String shopId);


    @Query("Select address from shop_detail where shop_id=:shopId")
    String getshopDetailsaddress(String shopId);

    @Query("Select shop_name from shop_detail where shop_id=:shopId")
    String getshopDetailsShopName(String shopId);

    @Query("select * FROM " + SHOP_TABLE +" where shop_id=:shop_id")
    List<AddShopDBModelEntity> getShopIdFromDtls(String shop_id);


    @Query("SELECT * FROM " + SHOP_TABLE+" where assigned_to_dd_id=:assigned_to_dd_id")
    List<AddShopDBModelEntity> getShopByDD(String assigned_to_dd_id);

    @Query("SELECT * FROM " + SHOP_TABLE+" where visitDate=:visitDate")
    List<AddShopDBModelEntity> getShopCreatedToday(String visitDate);

    @Query("Select *  FROM " + SHOP_TABLE+" where type=:type")
    List<AddShopDBModelEntity> getShopNameByDD(String type);

    @Query("SELECT beat_id FROM " + SHOP_TABLE+" where assigned_to_dd_id=:assigned_to_dd_id")
    List<String> getDistinctBeatID(String assigned_to_dd_id);

    @Query("Select Shopowner_PAN from shop_detail where shop_id=:shopId")
    String getPancardNumber(String shopId);

    @Query("Select GSTN_Number from shop_detail where shop_id=:shopId")
    String getGSTINNumber(String shopId);

    @Query("update shop_detail set shopStatusUpdate=:shopStatusUpdate where shop_id=:shop_id")
    void updateShopStatus(String shop_id, String shopStatusUpdate);


    @Query("update shop_detail set shop_name=:shop_name,address=:address,pin_code=:pin_code,owner_name=:owner_name," +
            " owner_contact_number=:owner_contact_number,owner_email=:owner_email,shopLat=:shopLat,shopLong=:shopLong,dateOfBirth=:dateOfBirth," +
            " dateOfAniversary=:dateOfAniversary, lastVisitedDate=:lastVisitedDate,totalVisitCount=:totalVisitCount,type=:type,type_id=:type_id," +
            " assigned_to_pp_id=:assigned_to_pp_id,assigned_to_dd_id=:assigned_to_dd_id,amount=:amount,entity_code=:entity_code,area_id=:area_id," +
            " model_id=:model_id,lead_id=:lead_id,funnel_stage_id=:funnel_stage_id=:stage_id,party_status_id=:party_status_id,retailer_id=:retailer_id," +
            " beat_id=:beat_id,assigned_to_shop_id=:assigned_to_shop_id,agency_name=:agency_name,GSTN_Number=:GSTN_Number,ShopOwner_PAN=:ShopOwner_PAN," +
            " project_name=:project_name,dealer_id=:dealer_id,account_holder=:account_holder,account_no=:account_no,bank_name=:bank_name," +
            " ifsc_code=:ifsc_code,upi_id=:upi where shop_id=:shop_id")
    void updateShopDtlsAll(String shop_id, String shop_name,String address,String pin_code,String owner_name,
                           String owner_contact_number,String owner_email,String shopLat,String shopLong,String dateOfBirth,
                           String dateOfAniversary,String lastVisitedDate,String totalVisitCount,String type,String type_id,
                           String assigned_to_pp_id,String assigned_to_dd_id,String amount,String entity_code,String area_id,
                           String model_id,String lead_id,String funnel_stage_id,String stage_id,String party_status_id,String retailer_id,
                           String beat_id,String assigned_to_shop_id,String agency_name,String GSTN_Number,String ShopOwner_PAN,
                           String project_name,String dealer_id,String account_holder,String account_no,String bank_name,String ifsc_code,String upi);

//    @Query("INSERT OR REPLACE INTO SHOP_TABLE (shopId,shopName,address,pinCode,ownerName,isVisited) VALUES (:id, :title, :url, COALESCE((SELECT isSubscribed FROM articles WHERE id = :id), 0));")
//    void insertOrUpdateShop(long id, String title, String url);

    /*@Query("select shop_id,shop_name,address,owner_name,owner_contact_number,shopLat,shopLong,\n" +
            "case when shop_type_list.shoptype_name IS NULL then '' else shop_type_list.shoptype_name END as shopType,\n" +
            "\t   case when beat_list.name  IS NULL then '' else beat_list.name END as beatName\n" +
            "\t   from shop_detail left JOIN shop_type_list\n" +
            "on shop_detail.type = shop_type_list.shoptype_id left join beat_list\n" +
            "on shop_detail.beat_id = beat_list.beat_id")
    List<ShopDtls> getShopForMarketAssist();*/

    @Query("select shop_id,shop_name,address,owner_name,owner_contact_number,shopLat,shopLong,\n" +
            "case when shop_type_list.shoptype_name IS NULL then '' else shop_type_list.shoptype_name END as shopType,\n" +
            "case when beat_list.name  IS NULL then '' else beat_list.name END as beatName,\n" +
            "case when shop_detail.retailer_id IS NULL then '' else shop_detail.retailer_id END as retailer_id,\n" +
            "case when shop_detail.party_status_id IS NULL then '' else shop_detail.party_status_id END as party_status_id\n" +
            "from shop_detail left JOIN shop_type_list\n" +
            "on shop_detail.type = shop_type_list.shoptype_id left join beat_list\n" +
            "on shop_detail.beat_id = beat_list.beat_id order by shop_name")
    List<ShopDtls> getShopForMarketAssist();

    @Query("select shop_id,shop_name,address,owner_name,owner_contact_number,shopLat,shopLong,\n" +
            "case when shop_type_list.shoptype_name IS NULL then '' else shop_type_list.shoptype_name END as shopType,\n" +
            "case when beat_list.name  IS NULL then '' else beat_list.name END as beatName,\n" +
            "case when shop_detail.retailer_id IS NULL then '' else shop_detail.retailer_id END as retailer_id,\n" +
            "case when shop_detail.party_status_id IS NULL then '' else shop_detail.party_status_id END as party_status_id,lastVisitedDate,0 as tag1,0 as tag2,0 as tag3,0 as tag4,0 as tag5,0 as tag6," +
            "'' as lastPurchaseAge,'' as lastVisitAge,'' as avgShopOrdAmt,'' as avgTimeSinceFirstOrd,'' as shopVisitAvg,'' as orderBehav \n" +
            "from shop_detail left JOIN shop_type_list\n" +
            "on shop_detail.type = shop_type_list.shoptype_id left join beat_list\n" +
            "on shop_detail.beat_id = beat_list.beat_id order by shop_name")
    List<ChurnShopL> getShopForChurn();

    @Query("select shop_id,shop_name,owner_contact_number,address,case when owner_name IS NULL then '' else owner_name END as owner_name,type, JULIANDAY(date())- JULIANDAY(added_date) as age_since_party_creation_count," +
            " date(added_date) as dateAdded,lastVisitedDate from shop_detail where shop_id not in (\n" +
            "select shopid from shop_activity \n" +
            ") and isOwnshop = 1")
    List<ShopDtlsCustom>  getShopDtlsCUstom();

    @Query("select shop_id,shop_name,owner_contact_number,address,case when owner_name IS NULL then '' else owner_name END as owner_name,type, JULIANDAY(date())- JULIANDAY(added_date) as age_since_party_creation_count," +
            " date(added_date) as dateAdded,lastVisitedDate from shop_detail where isOwnshop = 1 order by upper(trim(shop_name))")
    List<ShopDtlsCustom>  getShopDtlsCUstom1();

    @Query("Select shop_id,shop_name,owner_contact_number,address,case when owner_name IS NULL then '' else owner_name END as owner_name," +
            "type, JULIANDAY(date())- JULIANDAY(added_date) as age_since_party_creation_count from shop_detail where shop_id=:shopId")
    NoOrderTakenList getCustomShopDtls(String shopId);


    @Query("select shop_id,lastVisitedDate,'' as lastVIsitAge,totalVisitCount from shop_detail")
    List<ShopLastVisit> getShopListLastVisit();

    @Query("select shop_id,lastVisitedDate,'' as lastVIsitAge,totalVisitCount from shop_detail where shop_id=:shop_id ")
    ShopLastVisit getShopListLastVisitByShop(String shop_id);

    @Query("select lastVisitedDate from shop_detail where shop_id=:shop_id")
    String getShopListLastVisit(String shop_id);

    @Query("Select * from shop_detail where type=:type and assigned_to_dd_id=:assigned_to_dd_id and lower(shop_name)=:shop_name")
    List<AddShopDBModelEntity> getShopsAccordingToTypeDD(String type,String assigned_to_dd_id,String shop_name);

    @Query("select * from shop_detail where shop_name=:shop_name and owner_contact_number=:owner_contact_number")
    List<AddShopDBModelEntity> getCustomData(String shop_name,String owner_contact_number);

    @Query("select * from shop_detail where type='99' and isOwnshop = 1 and shopStatusUpdate = 1 order by shop_name COLLATE NOCASE ASC")
    List<AddShopDBModelEntity> getContatcShops();

    @Query("select * from shop_detail where shop_name LIKE '%' || :searchParam  || '%' OR owner_contact_number LIKE '%' || :searchParam  || '%' OR " +
            "crm_stage LIKE '%' || :searchParam  || '%' OR crm_source LIKE '%' || :searchParam  || '%' OR crm_status LIKE '%' || :searchParam  || '%' OR " +
            "crm_type LIKE '%' || :searchParam  || '%' OR crm_saved_from LIKE '%' || :searchParam  || '%' OR owner_email LIKE '%' || :searchParam  || '%' " +
            "and type='99' and isOwnshop = 1 and shopStatusUpdate = 1 order by shop_name COLLATE NOCASE ASC")
    List<AddShopDBModelEntity> getContatcShopsFilter(String searchParam);

    @Query("select * from shop_detail where type = '99' order by added_date desc")
    List<AddShopDBModelEntity> getContatcShopsByAddedDate();

    @Query("select * from shop_detail where type = '99' and crm_status_ID=:crm_status_ID")
    List<AddShopDBModelEntity> getContatcShopsBycrm_status_ID(String crm_status_ID);

    @Query("select * from shop_detail where type = '99' and crm_stage_ID=:crm_stage_ID")
    List<AddShopDBModelEntity> getContatcShopsBycrm_stage_ID(String crm_stage_ID);

    @Query("select * from shop_detail where type = '99' and crm_type_ID=:crm_type_ID")
    List<AddShopDBModelEntity> getContatcShopsBycrm_type_ID(String crm_type_ID);

    @Query("select * from shop_detail where type = '99' and crm_source_ID=:crm_source_ID")
    List<AddShopDBModelEntity> getContatcShopsBycrm_source_ID(String crm_source_ID);

    @Query("select * from shop_detail where type = '99' order by shop_name COLLATE NOCASE ASC")
    List<AddShopDBModelEntity> getContatcShopsByName();

    @Query("select * from shop_detail where type = '99' and isUploaded=:isUploaded")
    List<AddShopDBModelEntity> getContatcUnsyncList(Boolean isUploaded);

    @Query("select * from shop_detail where type = '99' and isUploaded=:isUploaded limit :count")
    List<AddShopDBModelEntity> getContatcUnsyncListTopX(Boolean isUploaded,Integer count);

    @Query("delete from shop_detail where type='99'")
    void del99();

    @Query("Select owner_contact_number from shop_detail where length(owner_contact_number) = 10")
    List<String> getOwnerContactL();

    @Query("select '' || '+91' || owner_contact_number as owner_contact_number from shop_detail where length(owner_contact_number) = 10")
    List<String> getOwnerContactLWithPrefix();

    @Query("update shop_detail\n" +
            "set jobTitle=:jobTitle,shop_name=:shopName,owner_name=:ownerName,crm_firstName=:crm_firstName,crm_lastName=:crm_lastName,companyName_id=:companyName_id,companyName=:companyName, owner_email=:ownerEmailId, owner_contact_number=:ownerContactNumber, address=:address, pin_code=:pinCode, shopLat=:shopLat, shopLong=:shopLong, crm_assignTo=:crm_assignTo, crm_assignTo_ID=:crm_assignTo_ID, crm_type=:crm_type, crm_type_ID=:crm_type_ID, crm_status=:crm_status, crm_source=:crm_source, crm_source_ID=:crm_source_ID, remarks=:remarks, amount=:amount, crm_stage=:crm_stage, crm_stage_ID=:crm_stage_ID, crm_reference=:crm_reference, crm_reference_ID=:crm_reference_ID, crm_reference_ID_type=:crm_reference_ID_type, crm_saved_from=:crm_saved_from, isEditUploaded=:isEditUploaded,whatsappNoForCustomer=:whatsappNoForCustomer," +
            "Shop_NextFollowupDate=:Shop_NextFollowupDate,dateOfBirth=:dob,dateOfAniversary=:doa " +
            " where shop_id =:shopId ")
    public int updateContactDtls(String shopId,String shopName,String ownerName,String crm_firstName,String crm_lastName,String companyName_id,String companyName,String jobTitle,String ownerEmailId,String ownerContactNumber,String address,String pinCode,Double shopLat,Double shopLong,
                                 String crm_assignTo,String crm_assignTo_ID,String crm_type,String crm_type_ID,String crm_status,String crm_source,String crm_source_ID,
                                 String remarks,String amount,String crm_stage,String crm_stage_ID,
                                 String crm_reference,String crm_reference_ID,String crm_reference_ID_type,String crm_saved_from,int isEditUploaded,String whatsappNoForCustomer,String Shop_NextFollowupDate,String dob,String doa);

    @Query("update shop_detail set shopLat=:shopLat, shopLong=:shopLong, address=:address, pin_code=:pinCode, actual_address=:actual_address where shop_id=:shopId")
    void updateShopDetails(Double shopLat, Double shopLong, String address, String pinCode, String actual_address, String shopId);

    @Query("update shop_detail set crm_source=:crm_source, crm_source_ID=:crm_source_ID,isEditUploaded=:isEditUploaded where shop_id=:shopId")
    void updateSourceOnly(String shopId,String crm_source_ID,String crm_source,int isEditUploaded);

    @Query("update shop_detail set crm_stage=:crm_stage, crm_stage_ID=:crm_stage_ID,isEditUploaded=:isEditUploaded where shop_id=:shopId")
    void updateStageOnly(String shopId,String crm_stage_ID,String crm_stage,int isEditUploaded);

    @Query("update shop_detail set crm_status=:crm_status, crm_status_ID=:crm_status_ID,isEditUploaded=:isEditUploaded where shop_id=:shopId")
    void updateStatusOnly(String shopId,String crm_status_ID,String crm_status,int isEditUploaded);

    @Query("update shop_detail set crm_assignTo=:crm_assignTo, crm_assignTo_ID=:crm_assignTo_ID,isEditUploaded=:isEditUploaded where shop_id=:shopId")
    void updateAssignToOnly(String shopId,String crm_assignTo_ID,String crm_assignTo,int isEditUploaded);

}
