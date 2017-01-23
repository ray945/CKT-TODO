package com.ckt.ckttodo.database;

import io.realm.RealmObject;

/**
 * Created by admin on 2016/12/15.
 */

public class AdPlaceList extends RealmObject{
/*    CREATE TABLE "AD_PLACE_LIST" ("OBJECTID" TEXT PRIMARY KEY NOT NULL ,"ADNAME" TEXT,"SHOWORDER" INTEGER NOT NULL ,"ENABLE" INTEGER NOT NULL
    ,"ADTYPE" TEXT,"ADTAG" TEXT,"ADIMAGE" TEXT,"DESCRIPTION" TEXT,"CREATEDAT" TEXT,"URL" TEXT,"AD_SOURCE_IDS" TEXT);*/

    private String objectId;
    private String adName;
    private int showOrder;
    private int enable;
    private String adType;
    private String adTag;
    private String adImage;
    private String description;
    private String createDate;
    private String url;
    private String adSourceIds;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public int getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(int showOrder) {
        this.showOrder = showOrder;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getAdTag() {
        return adTag;
    }

    public void setAdTag(String adTag) {
        this.adTag = adTag;
    }

    public String getAdImage() {
        return adImage;
    }

    public void setAdImage(String adImage) {
        this.adImage = adImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAdSourceIds() {
        return adSourceIds;
    }

    public void setAdSourceIds(String adSourceIds) {
        this.adSourceIds = adSourceIds;
    }
}
