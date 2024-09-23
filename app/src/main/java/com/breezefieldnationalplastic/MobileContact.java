package com.breezefieldnationalplastic;

import static com.breezefieldnationalplastic.app.utils.FileLoggingTree.context;
import static java.sql.DriverManager.println;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.breezefieldnationalplastic.app.utils.AppUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobileContact {
    public String name;
    public String contact;
    public Type type;
    public String group;
    public Context mContext;

    public MobileContact(String contact, Type type,Context mContext) {
        name = "";
        this.contact = contact;
        this.type = type;
        this.mContext=mContext;
        this.group = "";
    }

    public MobileContact(String name, String contact, Type type,Context mContext) {
        this.name = name;
        this.contact = contact;
        this.type = type;
        this.mContext=mContext;
        this.group = "";
    }

    public enum Type {
        EMAIL, PHONE
    }

    @Override
    public String toString() {
        return "MobileContact{" +
                "name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                ", type=" + type +
                '}';
    }


    // method for collect contacts
    public List<MobileContact> getAllContacts() {
        println("get all contacts");
        List<MobileContact> mobileContacts = new ArrayList<>();
        ContentResolver contentResolver = mContext.getContentResolver();

        // add all mobiles contact
        Cursor phonesCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.CONTACT_ID}, null, null, null);
        while (phonesCursor != null && phonesCursor.moveToNext()) {
            @SuppressLint("Range") String contactId = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            @SuppressLint("Range") String phoneNumber = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "");
            mobileContacts.add(new MobileContact(contactId, phoneNumber, MobileContact.Type.PHONE,mContext));
        }
        if (phonesCursor != null) {
            phonesCursor.close();
        }

        // add all email contact
        /*Cursor emailCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Email.DATA, ContactsContract.CommonDataKinds.Email.CONTACT_ID}, null, null, null);
        while (emailCursor != null && emailCursor.moveToNext()) {
            @SuppressLint("Range") String contactId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID));
            @SuppressLint("Range") String email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            mobileContacts.add(new MobileContact(contactId, email, MobileContact.Type.EMAIL,mContext));
        }
        if (emailCursor != null) {
            emailCursor.close();
        }*/

        // get contact name map
        Map<String, String> contactMap = new HashMap<>();
        Cursor contactsCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER}, null, null, ContactsContract.Contacts.DISPLAY_NAME);
        while (contactsCursor != null && contactsCursor.moveToNext()) {
            @SuppressLint("Range") String contactId = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
            @SuppressLint("Range") String contactName = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            contactMap.put(contactId, contactName);
        }
        if (phonesCursor != null) {
            phonesCursor.close();
        }


        // replace contactId to display name
        for (MobileContact mobileContact : mobileContacts) {
            String displayName = contactMap.get(mobileContact.name);
            mobileContact.name = displayName != null ? displayName : "";
        }

        // sort list by name
        Collections.sort(mobileContacts, new Comparator<MobileContact>() {
            @Override
            public int compare(MobileContact c1, MobileContact c2) {
                return c1.name.compareTo(c2.name);
            }
        });

        return mobileContacts;
    }

    public  class A{
        public String id = "";
        public String name = "";
        public String phone = "";
    }

    public List<MobileContact> getAllContactsNew(String groupId) {
        println("get all contacts");
        List<MobileContact> mobileContacts = new ArrayList<>();
        ContentResolver contentResolver = mContext.getContentResolver();

        String[] grArray = new String[1];
        grArray[0]= groupId.toString();

        ArrayList<AppUtils.Companion.ContactDtlsByGr> contFinalL = new ArrayList<AppUtils.Companion.ContactDtlsByGr>();

        //get contact by group
        Cursor xCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID},
                ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "= ?" + " AND "
                        + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "='"
                        + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'"
                , grArray, null);
        Map<String, String> xMap = new HashMap<>();
        while (xCursor != null && xCursor.moveToNext()) {
            @SuppressLint("Range") String contactId = xCursor.getString(xCursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));
            @SuppressLint("Range") String name = xCursor.getString(xCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)).replace(" ", "");
            contFinalL.add(new AppUtils.Companion.ContactDtlsByGr(contactId,name,""));
        }
        if (xCursor != null) {
            xCursor.close();
        }

        // add all mobiles contact
        Cursor phonesCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.CONTACT_ID},
                null
                , null, null);
        while (phonesCursor != null && phonesCursor.moveToNext()) {
            @SuppressLint("Range") String contactId = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            @SuppressLint("Range") String phoneNumber = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "");
            mobileContacts.add(new MobileContact(contactId, phoneNumber, MobileContact.Type.PHONE,mContext));
        }
        if (phonesCursor != null) {
            phonesCursor.close();
        }


        // get contact name map
        Map<String, String> contactMap = new HashMap<>();
        Cursor contactsCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER}, null, null, ContactsContract.Contacts.DISPLAY_NAME);
        while (contactsCursor != null && contactsCursor.moveToNext()) {
            @SuppressLint("Range") String contactId = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
            @SuppressLint("Range") String contactName = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            contactMap.put(contactId, contactName);
        }
        if (phonesCursor != null) {
            phonesCursor.close();
        }

        Cursor grCursor = contentResolver.query(ContactsContract.Groups.CONTENT_URI, new String[]{ContactsContract.Groups._ID, ContactsContract.Groups.TITLE},
                null
                , null, null);
        Map<String, String> contactGrMap = new HashMap<>();
        while (grCursor != null && grCursor.moveToNext()) {
            @SuppressLint("Range") String contactId = grCursor.getString(grCursor.getColumnIndex(ContactsContract.Contacts._ID));
            @SuppressLint("Range") String contactName = grCursor.getString(grCursor.getColumnIndex(ContactsContract.Groups.TITLE));
            contactGrMap.put(contactId, contactName);
        }
        if (grCursor != null) {
            grCursor.close();
        }


        // replace contactId to display name
        for (MobileContact mobileContact : mobileContacts) {
            String displayName = contactMap.get(mobileContact.name);
            mobileContact.name = displayName != null ? displayName : "";
        }

        // sort list by name
        Collections.sort(mobileContacts, new Comparator<MobileContact>() {
            @Override
            public int compare(MobileContact c1, MobileContact c2) {
                return c1.name.compareTo(c2.name);
            }
        });

        return mobileContacts;
    }
}
