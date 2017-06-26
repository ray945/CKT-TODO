package com.ckt.ckttodo.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.ckt.ckttodo.util.Constants;

/**
 * Created by mozre on 5/18/17.
 */

public class User {


    public static final String MEM_STATUS = "mStatus";

    private static final String MEM_ID = "id";
    private static final String MEM_NAME = "name";
    private static final String MEM_PHONE_NUM = "phoneNum";
    private static final String MEM_LEVEL = "level";
    private static final String MEM_EMAIL = "email";
    private static final String MEM_ICON = "icon";
    private static final String MEM_TOKEN = "token";

    private Integer id;
    private String name;
    private String phoneNum;
    private Integer level;
    private String email;
    private String icon;
    private Boolean isLogin;
    private String token;
    private SharedPreferences sharedPreferences;

    public User() {
    }

    public User(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.SHARE_NAME_CKT, Context.MODE_PRIVATE);
    }

    public User(Context context, UserInfo info) {
        sharedPreferences = context.getSharedPreferences(Constants.SHARE_NAME_CKT, Context.MODE_PRIVATE);
        setId(info.getMem_id());
        setEmail(info.getMem_email());
        setIcon(info.getMem_icon());
        setLevel(info.getMem_level());
        setName(info.getMem_name());
        setPhoneNum(info.getMem_phone_num());
        setIsLogin(true);
    }

    public User(Context context, PostUser info) {
        sharedPreferences = context.getSharedPreferences(Constants.SHARE_NAME_CKT, Context.MODE_PRIVATE);
        setId(info.getMem_id());
        setEmail(info.getMem_email());
        setIcon(info.getMem_icon());
        setLevel(info.getMem_level());
        setName(info.getMem_name());
        setPhoneNum(info.getMem_phone_num());
        setIsLogin(true);
    }

    public Integer getId() {
        if (this.id == null) {
            this.id = sharedPreferences.getInt(MEM_ID, 0);
        }
        return id;
    }

    public void setId(Integer id) {
        if (sharedPreferences.edit().putInt(MEM_ID, id).commit()) {
            this.id = id;
        }
    }

    public String getName() {
        if (this.name == null) {
            this.name = sharedPreferences.getString(MEM_NAME, "");
        }
        return name;

    }

    public void setName(String name) {
        if (sharedPreferences.edit().putString(MEM_NAME, name).commit()) {
            this.name = name;
        }
    }

    public String getPhoneNum() {
        if (this.phoneNum == null) {
            this.phoneNum = sharedPreferences.getString(MEM_PHONE_NUM, "");
        }
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        if (sharedPreferences.edit().putString(MEM_PHONE_NUM, phoneNum).commit()) {
            this.phoneNum = phoneNum;
        }
    }

    public Integer getLevel() {
        if (this.level == null) {
            this.level = sharedPreferences.getInt(MEM_LEVEL, 0);
        }
        return level;

    }

    public void setLevel(Integer level) {
        if (sharedPreferences.edit().putInt(MEM_LEVEL, level).commit()) {
            this.level = level;
        }
    }

    public String getEmail() {
        if (this.email == null) {
            this.email = sharedPreferences.getString(MEM_EMAIL, "");
        }
        return email;
    }

    public void setEmail(String email) {
        if (sharedPreferences.edit().putString(MEM_EMAIL, email).commit()) {
            this.email = email;
        }
    }

    public String getIcon() {
        if (this.icon == null) {
            this.icon = sharedPreferences.getString(MEM_ICON, "");
        }
        return icon;
    }

    public void setIcon(String icon) {
        if (sharedPreferences.edit().putString(MEM_ICON, icon).commit()) {
            this.icon = icon;
        }
    }

    public boolean getIsLogin() {
        if (this.isLogin == null) {
            this.isLogin = sharedPreferences.getBoolean(MEM_STATUS, false);
        }
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        if (sharedPreferences.edit().putBoolean(MEM_STATUS, isLogin).commit()) {
            this.isLogin = isLogin;
        }
    }

    public String getToken() {
        if (token == null) {
            this.token = sharedPreferences.getString(MEM_TOKEN, "");
        }
        return token;
    }

    public void setToken(String token) {
        if (sharedPreferences.edit().putString(MEM_TOKEN, token).commit()) {
            this.token = token;
        }
    }
}
