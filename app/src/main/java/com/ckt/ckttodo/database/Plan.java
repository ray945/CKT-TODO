package com.ckt.ckttodo.database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Plan extends RealmObject {

    /**
     * 计划状态 status
     * 未开始：PLAN_NOT_START
     * 开始：PLAN_START
     * 等待:PLAN_PENDING
     * 阻塞:PLAN_BLOCK
     * 完成:DONE
     */
    public static final int PLAN_NOT_START = -1;
    public static final int PLAN_START = 1;
    public static final int PLAN_PENDING = 2;
    public static final int PLAN_BLOCK = 3;
    public static final int DONE = 4;

    //计划id
    @PrimaryKey
    private String planId;
    //计划名称
    @Required
    private String planName;
    //计划描述
    private String planContent;
    //计划创建人id
    private String userId;
    //计划创建时间
    private long createTime;
    //计划最新更新时间
    private long lastUpdateTime;
    //计划周期开始时间
    private long startTime;

    //计划周期结束时间
    private long endTime;

    //计划状态
    private int status = PLAN_NOT_START;
    //计划进度
    private String accomplishProgress;
    //计划预计时间
    private float predictSpendTime;
    //计划实际花费时间
    private float realSpendTime;
    //计划包含的任务
    private RealmList<EventTask> eventTasks;
    //所属项目
    private String projectId;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getPlanContent() {
        return planContent;
    }

    public void setPlanContent(String planContent) {
        this.planContent = planContent;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
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

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAccomplishProgress() {
        return accomplishProgress;
    }

    public void setAccomplishProgress(String accomplishProgress) {
        this.accomplishProgress = accomplishProgress;
    }

    public float getPredictSpendTime() {
        return predictSpendTime;
    }

    public void setPredictSpendTime(float predictSpendTime) {
        this.predictSpendTime = predictSpendTime;
    }

    public float getRealSpendTime() {
        return realSpendTime;
    }

    public void setRealSpendTime(float realSpendTime) {
        this.realSpendTime = realSpendTime;
    }

    public RealmList<EventTask> getEventTasks() {
        return eventTasks;
    }

    public void setEventTasks(RealmList<EventTask> eventTasks) {
        this.eventTasks = eventTasks;
    }
}
