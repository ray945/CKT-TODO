package com.ckt.ckttodo.database;

import io.realm.RealmObject;

/**
 * Created by admin on 2016/12/13.
 */

public class UserInfo extends RealmObject {
/*
    CREATE TABLE "USER_INFO" ("OBJECTID" TEXT PRIMARY KEY NOT NULL ,"USERNAME" TEXT,"USERTYPE" TEXT,"CREATEDAT" TEXT,
            "UPDATEDAT" TEXT,"EMAIL" TEXT,"REGPHONE" TEXT,"SEARCHDISABLE" INTEGER NOT NULL ,"STATUS" INTEGER NOT NULL ,
            "SESSIONTOKEN" TEXT,"REFRESHTOKEN" TEXT,"NICKNAME" TEXT,"LOCATION" TEXT,"PHONE" TEXT,"SIOEYEID" TEXT,"HOBBYS" TEXT,
            "GENDER" INTEGER NOT NULL ,"DESCRIPTION" TEXT,"AVATAR" TEXT,"BROADCASTS" INTEGER NOT NULL ,"FOLLOWEENUM" INTEGER NOT NULL ,
            "FOLLOWERNUM" INTEGER NOT NULL ,"RECEIVELIKE" INTEGER NOT NULL ,"BGIMAGE" TEXT,"BINDDEVICES_JSON" TEXT,
            "NOTIFICATIONSWITCH" INTEGER NOT NULL ,"USERID" TEXT,"SIOEYEIDCHANGED" INTEGER NOT NULL ,"LASTPULL" TEXT,
            "TWILIOIDENTITY" TEXT,"IM_ENV" TEXT,"QRCODE" TEXT,"DIRECTORSWITCH" INTEGER NOT NULL ,"ISTHRIDLOGIN" INTEGER NOT NULL );
    */
    private String objectId;
    private String userName;
    private String userType;
    private String createDate;
    private String updateDate;
    private String email;
    private String regPhone;
    private int searchDisable;
    private int status;
    private String sessionToken;
    private String refreshToken;
    private String nickName;
    private String location;
    private String phone;
    private String sioeyeId;
    private String hobbys;
    private int gender;
    private String description;
    private String avatar;
    private int broadcasts;
    private int followEenum;
    private int FollowerNum;
    private int receiveLike;
    private String bgImage;
    private String bindDevicesJso;
    private int notificationSwitc;
    private String userId;
    private int sioeyeIdChange;
    private String lastPull;
    private String twilioIdEntity;
    private String imEnv;
    private String qrcode;
    private int directorSwitch;
    private int isThridLogin;
    private static UserInfo INSTANCE;

    public static UserInfo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserInfo();
        }
        return INSTANCE;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegPhone() {
        return regPhone;
    }

    public void setRegPhone(String regPhone) {
        this.regPhone = regPhone;
    }

    public int getSearchDisable() {
        return searchDisable;
    }

    public void setSearchDisable(int searchDisable) {
        this.searchDisable = searchDisable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSioeyeId() {
        return sioeyeId;
    }

    public void setSioeyeId(String sioeyeId) {
        this.sioeyeId = sioeyeId;
    }

    public String getHobbys() {
        return hobbys;
    }

    public void setHobbys(String hobbys) {
        this.hobbys = hobbys;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getBroadcasts() {
        return broadcasts;
    }

    public void setBroadcasts(int broadcasts) {
        this.broadcasts = broadcasts;
    }

    public int getFollowEenum() {
        return followEenum;
    }

    public void setFollowEenum(int followEenum) {
        this.followEenum = followEenum;
    }

    public int getFollowerNum() {
        return FollowerNum;
    }

    public void setFollowerNum(int followerNum) {
        FollowerNum = followerNum;
    }

    public int getReceiveLike() {
        return receiveLike;
    }

    public void setReceiveLike(int receiveLike) {
        this.receiveLike = receiveLike;
    }

    public String getBgImage() {
        return bgImage;
    }

    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }

    public String getBindDevicesJso() {
        return bindDevicesJso;
    }

    public void setBindDevicesJso(String bindDevicesJso) {
        this.bindDevicesJso = bindDevicesJso;
    }

    public int getNotificationSwitc() {
        return notificationSwitc;
    }

    public void setNotificationSwitc(int notificationSwitc) {
        this.notificationSwitc = notificationSwitc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSioeyeIdChange() {
        return sioeyeIdChange;
    }

    public void setSioeyeIdChange(int sioeyeIdChange) {
        this.sioeyeIdChange = sioeyeIdChange;
    }

    public String getLastPull() {
        return lastPull;
    }

    public void setLastPull(String lastPull) {
        this.lastPull = lastPull;
    }

    public String getTwilioIdEntity() {
        return twilioIdEntity;
    }

    public void setTwilioIdEntity(String twilioIdEntity) {
        this.twilioIdEntity = twilioIdEntity;
    }

    public String getImEnv() {
        return imEnv;
    }

    public void setImEnv(String imEnv) {
        this.imEnv = imEnv;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public int getDirectorSwitch() {
        return directorSwitch;
    }

    public void setDirectorSwitch(int directorSwitch) {
        this.directorSwitch = directorSwitch;
    }

    public int getIsThridLogin() {
        return isThridLogin;
    }

    public void setIsThridLogin(int isThridLogin) {
        this.isThridLogin = isThridLogin;
    }
}
