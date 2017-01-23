package com.ckt.ckttodo.database;

import io.realm.RealmObject;

/**
 * Created by admin on 2016/12/13.
 */

public class RecommendInfo  extends RealmObject {
/*
    CREATE TABLE "RECOMMEND_INFO" ("_id" INTEGER PRIMARY KEY ,"OBJECT_ID" TEXT NOT NULL ,"TIME" INTEGER NOT NULL );
*/

    private int id;
    private String objectId;
    private int time;

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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
