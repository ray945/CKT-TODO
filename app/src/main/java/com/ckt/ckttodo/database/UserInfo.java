package com.ckt.ckttodo.database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserInfo extends RealmObject{

    @PrimaryKey
    private String userId;
    private String userName;
    private String userIcon;
    private String userEmail;
    private int userLevel;
    private String phoneNumber;
    private long createTime;
    private int role;

    private String token;
    private long tokenCreateTime;

    private Team team;

    private RealmList<Project> projects;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTokenCreateTime() {
        return tokenCreateTime;
    }

    public void setTokenCreateTime(long tokenCreateTime) {
        this.tokenCreateTime = tokenCreateTime;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public RealmList<Project> getProjects() {
        return projects;
    }

    public void setProjects(RealmList<Project> projects) {
        this.projects = projects;
    }
}
