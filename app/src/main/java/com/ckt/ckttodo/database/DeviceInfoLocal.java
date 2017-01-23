package com.ckt.ckttodo.database;

import io.realm.RealmObject;

/**
 * Created by admin on 2016/12/13.
 */

public class DeviceInfoLocal  extends RealmObject {
/*    CREATE TABLE "DEVICE_INFO_LOCAL" ("DEVICE_NAME" TEXT PRIMARY KEY NOT NULL ,"IP" TEXT,"DEVICE_VERSION" TEXT,"PORT" TEXT,"WEB_PORT" TEXT,
            "AP_ENABLED" TEXT,"URL" TEXT,"SEARCHED_TIME" INTEGER NOT NULL );*/
    private String deviceName;
    private String ip;
    private String deviceVersion;
    private String port;
    private String webPort;
    private String apEnabled;
    private String url;
    private int searchedTime;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getWebPort() {
        return webPort;
    }

    public void setWebPort(String webPort) {
        this.webPort = webPort;
    }

    public String getApEnabled() {
        return apEnabled;
    }

    public void setApEnabled(String apEnabled) {
        this.apEnabled = apEnabled;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSearchedTime() {
        return searchedTime;
    }

    public void setSearchedTime(int searchedTime) {
        this.searchedTime = searchedTime;
    }
}
