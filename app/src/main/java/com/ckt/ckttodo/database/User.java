package com.ckt.ckttodo.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.ckt.ckttodo.util.Constants;

/**
 * Created by ckt on 5/18/17.
 */

public class User {


    private static final String MEM_ID = "mem_id";
    private static final String MEM_NAME = "mem_name";
    private static final String MEM_PASSWORD = "mem_password";
    private static final String MEM_PHONE_NUM = "mem_phone_num";
    private static final String MEM_LEVEL = "mem_level";
    private static final String MEM_EMAIL = "mem_email";
    private static final String MEM_ICON = "mem_icon";

    private Integer mem_id;

    private String mem_name;


    private String mem_phone_num;

    private Integer mem_level;

    private String mem_email;

    private String mem_icon;

    private SharedPreferences mSharedPreferences;


    public User(Context context) {
        mSharedPreferences = context.getSharedPreferences(Constants.SHARE_NAME_CKT,Context.MODE_PRIVATE);
    }

    public User(Context context, UserInfo info) {
        mSharedPreferences = context.getSharedPreferences(Constants.SHARE_NAME_CKT, Context.MODE_PRIVATE);
        setMem_id(info.getMem_id());
        setMem_email(info.getMem_email());
        setMem_icon(info.getMem_icon());
        setMem_level(info.getMem_level());
        setMem_name(info.getMem_name());
        setMem_phone_num(info.getMem_phone_num());
    }

    public Integer getMem_id() {
        if (this.mem_id == null) {
            this.mem_id = mSharedPreferences.getInt(MEM_ID, 0);
        }
        return mem_id;
    }

    public void setMem_id(Integer mem_id) {
        if (mSharedPreferences.edit().putInt(MEM_ID, mem_id).commit()) {
            this.mem_id = mem_id;
        }
    }

    public String getMem_name() {
        if (this.mem_name == null) {
            this.mem_name = mSharedPreferences.getString(MEM_NAME, "");
        }
        return mem_name;

    }

    public void setMem_name(String mem_name) {
        if (mSharedPreferences.edit().putString(MEM_NAME, mem_name).commit()) {
            this.mem_name = mem_name;
        }
    }

    public String getMem_phone_num() {
        if (this.mem_phone_num == null) {
            this.mem_phone_num = mSharedPreferences.getString(MEM_PHONE_NUM, "");
        }
        return mem_phone_num;
    }

    public void setMem_phone_num(String mem_phone_num) {
        if (mSharedPreferences.edit().putString(MEM_PHONE_NUM, mem_phone_num).commit()) {
            this.mem_phone_num = mem_phone_num;
        }
    }

    public Integer getMem_level() {
        if (this.mem_level == null) {
            this.mem_level = mSharedPreferences.getInt(MEM_LEVEL, 0);
        }
        return mem_level;

    }

    public void setMem_level(Integer mem_level) {
        if (mSharedPreferences.edit().putInt(MEM_LEVEL, mem_level).commit()) {
            this.mem_level = mem_level;
        }
    }

    public String getMem_email() {
        if (this.mem_email == null) {
            this.mem_email = mSharedPreferences.getString(MEM_EMAIL, "");
        }
        return mem_email;
    }

    public void setMem_email(String mem_email) {
        if (mSharedPreferences.edit().putString(MEM_EMAIL, mem_email).commit()) {
            this.mem_email = mem_email;
        }
    }

    public String getMem_icon() {
        if (this.mem_icon == null) {
            this.mem_icon = mSharedPreferences.getString(MEM_ICON, "");
        }
        return mem_icon;
    }

    public void setMem_icon(String mem_icon) {
        if (mSharedPreferences.edit().putString(MEM_ICON, mem_icon).commit()) {
            this.mem_icon = mem_icon;
        }
    }

}
