package com.ckt.ckttodo.database;

import io.realm.RealmObject;

/**
 * Created by admin on 2016/12/13.
 */

public class MediaInfo extends RealmObject {

  /*  CREATE TABLE "MEDIA_INFO" ("_id" INTEGER PRIMARY KEY AUTOINCREMENT ,"OBJECTID" TEXT,"TYPE" INTEGER NOT NULL ,
            "CONVERSATIONID" TEXT,"FAVORITES" INTEGER NOT NULL ,"WATCHCOUNT" INTEGER NOT NULL ,"CHATCOUNT" INTEGER NOT NULL ,
            "THUMBNAIL" TEXT,"PREVIEW" TEXT,"ISMULTI" INTEGER NOT NULL ,"TITLE" TEXT,"VIDEOTAG" TEXT,"ONLIVENUM" INTEGER NOT NULL ,
            "ACL" INTEGER NOT NULL ,"LIVENUM" INTEGER NOT NULL ,"ISLIVE" INTEGER NOT NULL ,"ADDRESS" TEXT,"STREAM" TEXT,
            "REPLAYURL" TEXT,"DURING" INTEGER NOT NULL ,"STARTDATE" INTEGER NOT NULL ,"NICKNAME" TEXT,"SIOEYEID" TEXT,
            "DESCRIPTION" TEXT,"AVATAR" TEXT,"LOCATION_JSON" TEXT,"VIDEOTIPS_JSON" TEXT,"ISREPOST" INTEGER NOT NULL ,
            "REPOST_TIME" INTEGER NOT NULL ,"REPOST_COUNT" INTEGER NOT NULL ,"UPDATETIMESTAMP" INTEGER NOT NULL ,
            "REPOST_USER_JSON" TEXT,"BROADCASTSCOUNT" INTEGER NOT NULL );*/

    private int id;
    private String objectId;
    private int type;
    private String conversationId;
    private int favorites;
    private int watchCount;
    private int chatCount;
    private String thumbNall;
    private String preview;
    private int isMulti;
    private String title;
    private String videoTag;
    private int onlivwnum;
    private int acl;
    private int liveNum;
    private int isLive;
    private String address;
    private String stream;
    private String replayUrl;
    private int during;
    private int startDate;
    private String nickName;
    private String sioeyeId;
    private String description;
    private String avatar;
    private String locationJson;
    private String videoTipsJson;
    private int isRePost;
    private int rePostTime;
    private int rePostCount;
    private int updateTimeStamp;
    private String rePostUserJson;
    private int broadCastsCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public int getWatchCount() {
        return watchCount;
    }

    public void setWatchCount(int watchCount) {
        this.watchCount = watchCount;
    }

    public int getChatCount() {
        return chatCount;
    }

    public void setChatCount(int chatCount) {
        this.chatCount = chatCount;
    }

    public String getThumbNall() {
        return thumbNall;
    }

    public void setThumbNall(String thumbNall) {
        this.thumbNall = thumbNall;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public int getIsMulti() {
        return isMulti;
    }

    public void setIsMulti(int isMulti) {
        this.isMulti = isMulti;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoTag() {
        return videoTag;
    }

    public void setVideoTag(String videoTag) {
        this.videoTag = videoTag;
    }

    public int getOnlivwnum() {
        return onlivwnum;
    }

    public void setOnlivwnum(int onlivwnum) {
        this.onlivwnum = onlivwnum;
    }

    public int getAcl() {
        return acl;
    }

    public void setAcl(int acl) {
        this.acl = acl;
    }

    public int getLiveNum() {
        return liveNum;
    }

    public void setLiveNum(int liveNum) {
        this.liveNum = liveNum;
    }

    public int getIsLive() {
        return isLive;
    }

    public void setIsLive(int isLive) {
        this.isLive = isLive;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getReplayUrl() {
        return replayUrl;
    }

    public void setReplayUrl(String replayUrl) {
        this.replayUrl = replayUrl;
    }

    public int getDuring() {
        return during;
    }

    public void setDuring(int during) {
        this.during = during;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSioeyeId() {
        return sioeyeId;
    }

    public void setSioeyeId(String sioeyeId) {
        this.sioeyeId = sioeyeId;
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

    public String getLocationJson() {
        return locationJson;
    }

    public void setLocationJson(String locationJson) {
        this.locationJson = locationJson;
    }

    public String getVideoTipsJson() {
        return videoTipsJson;
    }

    public void setVideoTipsJson(String videoTipsJson) {
        this.videoTipsJson = videoTipsJson;
    }

    public int getIsRePost() {
        return isRePost;
    }

    public void setIsRePost(int isRePost) {
        this.isRePost = isRePost;
    }

    public int getRePostTime() {
        return rePostTime;
    }

    public void setRePostTime(int rePostTime) {
        this.rePostTime = rePostTime;
    }

    public int getRePostCount() {
        return rePostCount;
    }

    public void setRePostCount(int rePostCount) {
        this.rePostCount = rePostCount;
    }

    public int getUpdateTimeStamp() {
        return updateTimeStamp;
    }

    public void setUpdateTimeStamp(int updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }

    public String getRePostUserJson() {
        return rePostUserJson;
    }

    public void setRePostUserJson(String rePostUserJson) {
        this.rePostUserJson = rePostUserJson;
    }

    public int getBroadCastsCount() {
        return broadCastsCount;
    }

    public void setBroadCastsCount(int broadCastsCount) {
        this.broadCastsCount = broadCastsCount;
    }
}
