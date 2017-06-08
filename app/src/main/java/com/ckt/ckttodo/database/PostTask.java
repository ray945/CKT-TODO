package com.ckt.ckttodo.database;

/**
 * Created by mozre on 2017/5/25.
 */
public class PostTask {

    private String taskId;
    //任务标题
    private String taskTitle;
    //任务内容
    private String taskContent;
    //创建人id
    private int mem_id;
    //任务类别
    private int taskType;
    //任务优先级
    private int taskPriority;
    //任务状态
    private int taskStatus;
    //任务开始时间
    private String taskStartTime;
    //任务预计花费时间EventTask
    private String taskPredictTime;
    //任务提醒时间
    private String taskRemindTime;
    //任务实际花费时间
    private String taskRealSpendTime;
    //任务所属计划
    private String planId;
    //任务最新更新时间
    private String taskUpdateTime;


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

    public int getMem_id() {
        return mem_id;
    }

    public void setMem_id(int mem_id) {
        this.mem_id = mem_id;
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

    public String getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(String taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public String getTaskPredictTime() {
        return taskPredictTime;
    }

    public void setTaskPredictTime(String taskPredictTime) {
        this.taskPredictTime = taskPredictTime;
    }

    public String getTaskRemindTime() {
        return taskRemindTime;
    }

    public void setTaskRemindTime(String taskRemindTime) {
        this.taskRemindTime = taskRemindTime;
    }

    public String getTaskRealSpendTime() {
        return taskRealSpendTime;
    }

    public void setTaskRealSpendTime(String taskRealSpendTime) {
        this.taskRealSpendTime = taskRealSpendTime;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getTaskUpdateTime() {
        return taskUpdateTime;
    }

    public void setTaskUpdateTime(String taskUpdateTime) {
        this.taskUpdateTime = taskUpdateTime;
    }

}
