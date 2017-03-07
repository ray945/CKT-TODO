package com.ckt.ckttodo.util.excelutil;

/**
 * 目前支持的Excel格式(见共享库)：  \\10.120.10.100\ckt_cd_share\SmartPhone\team\Framework\UED\Docs\2017研究型项目\目标管理工具\支持导入的excel格式.xls
 *
 * 表名：任务表
 * 标题：String
 * 内容：String
 * 类别：int -> 1、2、3、4 分别对应工作、学习、生活、休息
 * 优先级:int -> 1、2、3、4 分别对应4种优先级
 * 预计花费时间：float
 * 任务提醒时间：int -> 10、20、30、60 分别对应 提前 10分钟、20分钟、30分钟、60分钟 提醒
 * 任务所属计划：若任务的该计划已在数据库中存在，则建立对应关系，否则该任务的计划为空
 *
 * Created by zhiwei.li on 2017/3/3.
 */
@ExcelSheet(sheetName = "任务表")
public class EventTaskExcelBean {

    public EventTaskExcelBean() {
    }


    @ExcelContent(titleName = "标题")
    private String taskTitle;

    @ExcelContent(titleName = "内容")
    private String taskContent;

    // int: 1->工作 2->学习 3->生活 4 -> 休息
    @ExcelContent(titleName = "类别")
    private String taskType;

    // int: 1 -> 很重要-很紧急  2 -> 很重要-不紧急  3 -> 不重要-很紧急  4 -> 不重要-不紧急
    @ExcelContent(titleName = "优先级")
    private String taskPriority;

    // float
    @ExcelContent(titleName = "预计花费时间")
    private String taskPredictTime;

    // int 10 -> 10分钟  20 -> 20分钟 30 -> 30分钟 60 -> 60分钟
    @ExcelContent(titleName = "任务提醒时间")
    private String taskRemindTime;

    @ExcelContent(titleName = "任务所属计划")
    private String planName;


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


    public String getTaskType() {
        return taskType;
    }


    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }


    public String getTaskPriority() {
        return taskPriority;
    }


    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
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


    public String getPlanName() {
        return planName;
    }


    public void setPlanName(String planName) {
        this.planName = planName;
    }

}
