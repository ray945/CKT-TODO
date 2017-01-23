package com.ckt.ckttodo.database;


import io.realm.RealmObject;

public class UserToSystemMessageTime extends RealmObject {
/*

    CREATE TABLE "USER_TO_SYSTEM_MESSAGE_TIME" ("OBJECTID" TEXT PRIMARY KEY NOT NULL ,"COUNT" INTEGER NOT NULL ,"LATEST" INTEGER NOT NULL );

*/


    private String objectID;
    private int count;
    private int latest;

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLatest() {
        return latest;
    }

    public void setLatest(int latest) {
        this.latest = latest;
    }
}
