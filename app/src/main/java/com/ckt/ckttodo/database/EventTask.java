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
     * */
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
     * */
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
     * */
    public static final int NOT_START = -1;
    public static final int START = 1;
    public static final int PENDING = 2;
    public static final int BLOCK = 3;
    public static final int DONE = 4;

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
    //任务预计花费时间
    private long taskPredictTime;
    //任务提醒时间
    private long taskRemindTime;
    //任务实际花费时间
    private long taskRealSpendTime;
    //任务所属计划
    private int planId;

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

    public long getTaskPredictTime() {
        return taskPredictTime;
    }

    public void setTaskPredictTime(long taskPredictTime) {
        this.taskPredictTime = taskPredictTime;
    }

    public long getTaskRemindTime() {
        return taskRemindTime;
    }

    public void setTaskRemindTime(long taskRemindTime) {
        this.taskRemindTime = taskRemindTime;
    }

    public long getTaskRealSpendTime() {
        return taskRealSpendTime;
    }

    public void setTaskRealSpendTime(long taskRealSpendTime) {
        this.taskRealSpendTime = taskRealSpendTime;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }
}
