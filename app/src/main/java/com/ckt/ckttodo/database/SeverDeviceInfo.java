package com.ckt.ckttodo.database;

import io.realm.RealmObject;

/**
 * Created by admin on 2016/12/13.
 */

public class SeverDeviceInfo extends RealmObject {
/*
    CREATE TABLE "SEVER_DEVICE_INFO" ("_id" INTEGER PRIMARY KEY NOT NULL ,"OBJECTID" INTEGER NOT NULL ,"DEVICETAG" TEXT,
            "DEVICETYPEID" TEXT,"DEVICENAME" TEXT,"USERID" TEXT,"DEVICEID" TEXT,"STATUS" INTEGER NOT NULL ,"CREATEDAT" TEXT,"DEVICEIMG" TEXT);
    */
    private int id;
    private int objectId;
    private String deviceTag;
    private String deviceTypeId;
    private String deviceName;
    private String userId;
    private String deviceId;
    private int status;
    private String createDate;
    private String deviceImg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public String getDeviceTag() {
        return deviceTag;
    }

    public void setDeviceTag(String deviceTag) {
        this.deviceTag = deviceTag;
    }

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDeviceImg() {
        return deviceImg;
    }

    public void setDeviceImg(String deviceImg) {
        this.deviceImg = deviceImg;
    }
}
