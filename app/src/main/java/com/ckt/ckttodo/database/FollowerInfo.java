package com.ckt.ckttodo.database;

import io.realm.RealmObject;

/**
 * Created by admin on 2016/12/13.
 */

public class FollowerInfo extends RealmObject {
    /*    CREATE TABLE "FOLLOWER_INFO" ("ID" TEXT PRIMARY KEY NOT NULL ,"OBJECT_ID" TEXT NOT NULL ,"SENDER" TEXT NOT NULL ,"TIME" INTEGER NOT NULL );*/
    private String id;
    private String objectId;
    private String sender;
    private int time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
