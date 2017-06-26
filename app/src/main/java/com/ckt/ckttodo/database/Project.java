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
    private int ownerId;
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

    private RealmList<UserInfo> userInfos;

    private int sprintCount;

    private boolean isSync;


    public static final int PROJECT_PRIVATE = 0;
    public static final int PROJECT_PUBLIC = 1;
    public static final String PROJECT_ID = "projectId";

    public Project() {
    }

    public Project(Project project) {
        this.projectId = project.getProjectId();
        this.projectSummary = project.getProjectSummary();
        this.ownerId = project.getOwnerId();
        this.createTime = project.getCreateTime();
        this.endTime = project.getEndTime();
        this.lastUpdateTime = project.getLastUpdateTime();
        this.accomplishProgress = project.getAccomplishProgress();
        this.plans = project.getPlans();
        this.userInfos = project.getUserInfos();
        this.sprintCount = project.getSprintCount();
        this.isSync = project.isSync();

    }

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

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public RealmList<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(RealmList<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public int getSprintCount() {
        return sprintCount;
    }

    public void setSprintCount(int sprintCount) {
        this.sprintCount = sprintCount;
    }
}
