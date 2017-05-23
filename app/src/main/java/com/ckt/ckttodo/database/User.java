package com.ckt.ckttodo.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.ckt.ckttodo.util.Constants;

/**
 * Created by mozre on 5/18/17.
 */

public class User {


    public static final String MEM_STATUS = "mStatus";

    private static final String MEM_ID = "mID";
    private static final String MEM_NAME = "mName";
    private static final String MEM_PHONE_NUM = "mPhoneNum";
    private static final String MEM_LEVEL = "mLevel";
    private static final String MEM_EMAIL = "mEmail";
    private static final String MEM_ICON = "mIcon";
    private static final String MEM_TOKEN = "mToken";

    private Integer mID;
    private String mName;
    private String mPhoneNum;
    private Integer mLevel;
    private String mEmail;
    private String mIcon;
    private Boolean mIsLogin;
    private String mToken;
    private SharedPreferences mSharedPreferences;


    public User(Context context) {
        mSharedPreferences = context.getSharedPreferences(Constants.SHARE_NAME_CKT, Context.MODE_PRIVATE);
    }

    public User(Context context, UserInfo info) {
        mSharedPreferences = context.getSharedPreferences(Constants.SHARE_NAME_CKT, Context.MODE_PRIVATE);
        setmID(info.getMem_id());
        setmEmail(info.getMem_email());
        setmIcon(info.getMem_icon());
        setmLevel(info.getMem_level());
        setmName(info.getMem_name());
        setmPhoneNum(info.getMem_phone_num());
        setmIsLogin(true);
    }

    public Integer getmID() {
        if (this.mID == null) {
            this.mID = mSharedPreferences.getInt(MEM_ID, 0);
        }
        return mID;
    }

    public void setmID(Integer mID) {
        if (mSharedPreferences.edit().putInt(MEM_ID, mID).commit()) {
            this.mID = mID;
        }
    }

    public String getmName() {
        if (this.mName == null) {
            this.mName = mSharedPreferences.getString(MEM_NAME, "");
        }
        return mName;

    }

    public void setmName(String mName) {
        if (mSharedPreferences.edit().putString(MEM_NAME, mName).commit()) {
            this.mName = mName;
        }
    }

    public String getmPhoneNum() {
        if (this.mPhoneNum == null) {
            this.mPhoneNum = mSharedPreferences.getString(MEM_PHONE_NUM, "");
        }
        return mPhoneNum;
    }

    public void setmPhoneNum(String mPhoneNum) {
        if (mSharedPreferences.edit().putString(MEM_PHONE_NUM, mPhoneNum).commit()) {
            this.mPhoneNum = mPhoneNum;
        }
    }

    public Integer getmLevel() {
        if (this.mLevel == null) {
            this.mLevel = mSharedPreferences.getInt(MEM_LEVEL, 0);
        }
        return mLevel;

    }

    public void setmLevel(Integer mLevel) {
        if (mSharedPreferences.edit().putInt(MEM_LEVEL, mLevel).commit()) {
            this.mLevel = mLevel;
        }
    }

    public String getmEmail() {
        if (this.mEmail == null) {
            this.mEmail = mSharedPreferences.getString(MEM_EMAIL, "");
        }
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        if (mSharedPreferences.edit().putString(MEM_EMAIL, mEmail).commit()) {
            this.mEmail = mEmail;
        }
    }

    public String getmIcon() {
        if (this.mIcon == null) {
            this.mIcon = mSharedPreferences.getString(MEM_ICON, "");
        }
        return mIcon;
    }

    public void setmIcon(String mIcon) {
        if (mSharedPreferences.edit().putString(MEM_ICON, mIcon).commit()) {
            this.mIcon = mIcon;
        }
    }

    public boolean getmIsLogin() {
        if (this.mIsLogin == null) {
            this.mIsLogin = mSharedPreferences.getBoolean(MEM_STATUS, false);
        }
        return mIsLogin;
    }

    public void setmIsLogin(boolean mIsLogin) {
        if (mSharedPreferences.edit().putBoolean(MEM_STATUS, mIsLogin).commit()) {
            this.mIsLogin = mIsLogin;
        }
    }

    public String getmToken() {
        if (mToken == null) {
            this.mToken = mSharedPreferences.getString(MEM_TOKEN, "");
        }
        return mToken;
    }

    public void setmToken(String mToken) {
        if (mSharedPreferences.edit().putString(MEM_TOKEN, mToken).commit()) {
            this.mToken = mToken;
        }
    }
}
