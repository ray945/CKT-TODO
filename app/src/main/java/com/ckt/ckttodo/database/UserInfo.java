package com.ckt.ckttodo.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserInfo extends RealmObject{

    @PrimaryKey
    private Integer mem_id;

    private String mem_name;

    private String mem_password;

    private String mem_phone_num;

    private Integer mem_level;

    private String mem_email;

    private String mem_icon;


    public static final String MEM_ID = "mem_id";
    public static final String MEM_EMAIL = "mem_email";

    public Integer getMem_id() {
        return mem_id;
    }

    public void setMem_id(Integer mem_id) {
        this.mem_id = mem_id;
    }

    public String getMem_name() {
        return mem_name;
    }

    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }

    public String getMem_password() {
        return mem_password;
    }

    public void setMem_password(String mem_password) {
        this.mem_password = mem_password;
    }

    public String getMem_phone_num() {
        return mem_phone_num;
    }

    public void setMem_phone_num(String mem_phone_num) {
        this.mem_phone_num = mem_phone_num;
    }

    public Integer getMem_level() {
        return mem_level;
    }

    public void setMem_level(Integer mem_level) {
        this.mem_level = mem_level;
    }

    public String getMem_email() {
        return mem_email;
    }

    public void setMem_email(String mem_email) {
        this.mem_email = mem_email;
    }

    public String getMem_icon() {
        return mem_icon;
    }

    public void setMem_icon(String mem_icon) {
        this.mem_icon = mem_icon;
    }
}
