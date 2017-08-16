package com.ckt.ckttodo.database;

/**
 * Created by mozre on 6/14/17.
 */

public class PostPlan {

    private String planID;
    private int memID;
    private String projectID;
    private String planName;
    private String planDescrition;
    private String planStartTime;
    private String planEndTime;
    private String planCreateTime;
    private String planLastUpdateTime;
    private int planState;
    private String planRealSpendTime;
    private String planAcomplishProgress;

    public String getPlanID() {
        return planID;
    }

    public void setPlanID(String planID) {
        this.planID = planID;
    }

    public int getMemID() {
        return memID;
    }

    public void setMemID(int memID) {
        this.memID = memID;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanDescrition() {
        return planDescrition;
    }

    public void setPlanDescrition(String planDescrition) {
        this.planDescrition = planDescrition;
    }

    public String getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(String planStartTime) {
        this.planStartTime = planStartTime;
    }

    public String getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(String planEndTime) {
        this.planEndTime = planEndTime;
    }

    public String getPlanLastUpdateTime() {
        return planLastUpdateTime;
    }

    public void setPlanLastUpdateTime(String planLastUpdateTime) {
        this.planLastUpdateTime = planLastUpdateTime;
    }

    public int getPlanState() {
        return planState;
    }

    public void setPlanState(int planState) {
        this.planState = planState;
    }

    public String getPlanRealSpendTime() {
        return planRealSpendTime;
    }

    public void setPlanRealSpendTime(String planRealSpendTime) {
        this.planRealSpendTime = planRealSpendTime;
    }

    public String getPlanCreateTime() {
        return planCreateTime;
    }

    public void setPlanCreateTime(String planCreateTime) {
        this.planCreateTime = planCreateTime;
    }

    public String getPlanAcomplishProgress() {
        return planAcomplishProgress;
    }

    public void setPlanAcomplishProgress(String planAcomplishProgress) {
        this.planAcomplishProgress = planAcomplishProgress;
    }
}
