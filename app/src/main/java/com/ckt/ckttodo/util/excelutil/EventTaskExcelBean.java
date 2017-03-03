package com.ckt.ckttodo.util.excelutil;

/**
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
}
