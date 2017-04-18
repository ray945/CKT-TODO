package com.ckt.ckttodo.database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Team extends RealmObject {

    @PrimaryKey
    private String teamId;
    @Required
    private String teamName;
    @Required
    private String teamDescription;

    private RealmList<UserInfo> userInfos;

    public String getTeamId() {
        return teamId;
    }


    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }


    public String getTeamName() {
        return teamName;
    }


    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }


    public String getTeamDescription() {
        return teamDescription;
    }


    public void setTeamDescription(String teamDescription) {
        this.teamDescription = teamDescription;
    }

    public RealmList<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(RealmList<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }
}
