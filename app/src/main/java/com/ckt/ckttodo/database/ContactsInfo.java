package com.ckt.ckttodo.database;

import io.realm.RealmObject;

/**
 * Created by admin on 2016/12/13.
 */

public class ContactsInfo extends RealmObject {
/*    CREATE TABLE "CONTACTS_INFO" ("OBJECTID" TEXT PRIMARY KEY NOT NULL ,"USERID" TEXT,"BROADCASTS" INTEGER NOT NULL ,
            "FOLLOWERNUM" INTEGER NOT NULL ,"FOLLOWEENUM" INTEGER NOT NULL ,"RECEIVELIKE" TEXT,
            "NOTIFICATIONSWITCH" INTEGER NOT NULL ,"LASTPULL" TEXT,"TWILIOIDENTITY" TEXT,"NICKNAME" TEXT,
            "LOCATION" TEXT,"PHONE" TEXT,"SIOEYEID" TEXT,"HOBBYS" TEXT,"GENDER" INTEGER NOT NULL ,"DESCRIPTION" TEXT,
            "SIOEYEIDCHANGED" INTEGER NOT NULL ,"CREATEDAT" TEXT,"AVATAR" TEXT,"USERNAME" TEXT,"EMAIL" TEXT,"REGPHONE" TEXT,
            "SEARCHDISABLE" INTEGER NOT NULL ,"STATUS" INTEGER NOT NULL ,"ISFOLLOWER" INTEGER NOT NULL ,
            "ISFOLLOWING" INTEGER NOT NULL ,"BGIMAGE" TEXT,"MYFOLLOWERNUM" INTEGER NOT NULL );*/

    private String objectId;
    private String usedId;
    private int broadcasts;
    private int followernum;
    private String receiveLike;
    private int notificationSwitch;
    private String lastPull;
    private String twilioIdentity;
    private String nickName;
    private String location;
    private String phone;
    private String sioeyeId;
    private String hobbys;
    private int gender;
    private String description;
    private int sioeyeIdChanged;
    private String createDate;
    private String avatar;
    private String userName;
    private String email;
    private String regPhone;
    private int searchDisable;
    private int status;
    private int isFollower;
    private int isFollowing;
    private String bgImage;
    private int myFollowerNum;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUsedId() {
        return usedId;
    }

    public void setUsedId(String usedId) {
        this.usedId = usedId;
    }

    public int getBroadcasts() {
        return broadcasts;
    }

    public void setBroadcasts(int broadcasts) {
        this.broadcasts = broadcasts;
    }

    public int getFollowernum() {
        return followernum;
    }

    public void setFollowernum(int followernum) {
        this.followernum = followernum;
    }

    public String getReceiveLike() {
        return receiveLike;
    }

    public void setReceiveLike(String receiveLike) {
        this.receiveLike = receiveLike;
    }

    public int getNotificationSwitch() {
        return notificationSwitch;
    }

    public void setNotificationSwitch(int notificationSwitch) {
        this.notificationSwitch = notificationSwitch;
    }

    public String getLastPull() {
        return lastPull;
    }

    public void setLastPull(String lastPull) {
        this.lastPull = lastPull;
    }

    public String getTwilioIdentity() {
        return twilioIdentity;
    }

    public void setTwilioIdentity(String twilioIdentity) {
        this.twilioIdentity = twilioIdentity;
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

    public int getSioeyeIdChanged() {
        return sioeyeIdChanged;
    }

    public void setSioeyeIdChanged(int sioeyeIdChanged) {
        this.sioeyeIdChanged = sioeyeIdChanged;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public int getIsFollower() {
        return isFollower;
    }

    public void setIsFollower(int isFollower) {
        this.isFollower = isFollower;
    }

    public int getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(int isFollowing) {
        this.isFollowing = isFollowing;
    }

    public String getBgImage() {
        return bgImage;
    }

    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }

    public int getMyFollowerNum() {
        return myFollowerNum;
    }

    public void setMyFollowerNum(int myFollowerNum) {
        this.myFollowerNum = myFollowerNum;
    }
}
