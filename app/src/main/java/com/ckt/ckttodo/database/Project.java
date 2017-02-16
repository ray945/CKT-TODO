package com.ckt.ckttodo.database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Project extends RealmObject {


    //项目id
    @PrimaryKey
    private String projectId;
    //项目名称
    @Required
    private String projectTitle;
    //项目描述
    private String projectSummary;
    //项目创建人id
    private String userId;
    //项目创建时间
    private long createTime;
    //项目结束时间
    private long endTime;
    //项目最新更新时间
    private long lastUpdateTime;
    //项目进度
    private String accomplishProgress;
    //项目包含的计划
    private RealmList<Plan> plans;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectSummary() {
        return projectSummary;
    }

    public void setProjectSummary(String projectSummary) {
        this.projectSummary = projectSummary;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getAccomplishProgress() {
        return accomplishProgress;
    }

    public void setAccomplishProgress(String accomplishProgress) {
        this.accomplishProgress = accomplishProgress;
    }

    public RealmList<Plan> getPlans() {
        return plans;
    }

    public void setPlans(RealmList<Plan> plans) {
        this.plans = plans;
    }
}
