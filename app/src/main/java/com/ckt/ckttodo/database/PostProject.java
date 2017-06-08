package com.ckt.ckttodo.database;

/**
 * Created by mozre on 2017/5/25.
 */
public class PostProject {

    private String projectId;
    //项目名称
    private String projectTitle;
    //项目描述
    private String projectSummary;
    //项目创建人id
    private int memId;
    //项目创建时间
    private String createTime;
    //项目结束时间
    private String endTime;
    //项目最新更新时间
    private String lastUpdateTime;
    //项目进度
    private String accomplishProgress;
    //所属team
    private int teamId;
    //所属sprint
    private int sprint;
    // project 可见性
    private int projectVisibility;

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

    public int getMemId() {
        return memId;
    }

    public void setMemId(int memId) {
        this.memId = memId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getAccomplishProgress() {
        return accomplishProgress;
    }

    public void setAccomplishProgress(String accomplishProgress) {
        this.accomplishProgress = accomplishProgress;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getSprint() {
        return sprint;
    }

    public void setSprint(int sprint) {
        this.sprint = sprint;
    }

    public int getProjectVisibility() {
        return projectVisibility;
    }

    public void setProjectVisibility(int projectVisibility) {
        this.projectVisibility = projectVisibility;
    }
}
