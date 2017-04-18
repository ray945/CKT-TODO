package com.ckt.ckttodo.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EventTask extends RealmObject {

    /**
     * 任务类别 taskType
     * 工作：WORK
     * 学习：STUDY
     * 生活:LIVE
     * 休息:REST
     */
    public static final int WORK = 1;
    public static final int STUDY = 2;
    public static final int LIVE = 3;
    public static final int REST = 4;

    /**
     * 任务优先级 taskPriority
     * 很重要-很紧急：CRITICAL critical
     * 很重要-不紧急：MAJOR major
     * 不重要-很紧急:MINOR minor
     * 不重要-不紧急:ENHANCEMENT enhancement
     */
    public static final int CRITICAL = 1;
    public static final int MAJOR = 2;
    public static final int MINOR = 3;
    public static final int ENHANCEMENT = 4;

    /**
     * 任务状态 taskStatus
     * 未开始：NOT_START
     * 开始：START
     * 等待:PENDING
     * 阻塞:BLOCK
     * 完成:DONE
     */
    public static final int NOT_START = -1;
    public static final int START = 1;
    public static final int PENDING = 2;
    public static final int BLOCK = 3;
    public static final int DONE = 4;

    public static final String TASK_STATUS = "taskStatus";
    public static final String TASK_ID = "taskId";

    public static final int TOP_ONE = 1;
    public static final int TOP_TWO = 2;
    public static final int TOP_THREE = 3;
    public static final int TOP_NORMAL = -1;


    //任务id
    @PrimaryKey
    private String taskId;
    //任务标题
    private String taskTitle;
    //任务内容
    private String taskContent;
    //任务创建人
    private String createUerId;
    //任务执行人
    private String execUserId;
    //任务类别
    private int taskType;
    //任务优先级
    private int taskPriority;
    //任务状态
    private int taskStatus;
    //任务开始时间
    private long taskStartTime;
    //任务预计花费时间EventTask
    private float taskPredictTime;
    //任务提醒时间
    private long taskRemindTime;
    //任务实际花费时间
    private float taskRealSpendTime;
    //任务所属计划
    private String planId;
    //任务最新更新时间
    private long taskUpdateTime;
    //置顶，默认值为-1，置顶后数字往上增加，取消置顶还原为-1
    private int topNumber = -1;

    private Plan plan;

    private UserInfo userInfo;

    public int getTopNumber() {
        return topNumber;
    }

    public void setTopNumber(int topNumber) {
        this.topNumber = topNumber;
    }

    public long getTaskUpdateTime() {
        return taskUpdateTime;
    }

    public void setTaskUpdateTime(long taskUpdateTime) {
        this.taskUpdateTime = taskUpdateTime;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public String getCreateUerId() {
        return createUerId;
    }

    public void setCreateUerId(String createUerId) {
        this.createUerId = createUerId;
    }

    public String getExecUserId() {
        return execUserId;
    }

    public void setExecUserId(String execUserId) {
        this.execUserId = execUserId;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public int getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(int taskPriority) {
        this.taskPriority = taskPriority;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public long getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(long taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public float getTaskPredictTime() {
        return taskPredictTime;
    }

    public void setTaskPredictTime(float taskPredictTime) {
        this.taskPredictTime = taskPredictTime;
    }

    public long getTaskRemindTime() {
        return taskRemindTime;
    }

    public void setTaskRemindTime(long taskRemindTime) {
        this.taskRemindTime = taskRemindTime;
    }

    public float getTaskRealSpendTime() {
        return taskRealSpendTime;
    }

    public void setTaskRealSpendTime(float taskRealSpendTime) {
        this.taskRealSpendTime = taskRealSpendTime;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
