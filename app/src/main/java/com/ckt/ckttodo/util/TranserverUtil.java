package com.ckt.ckttodo.util;

import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.database.PostProject;
import com.ckt.ckttodo.database.PostTask;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.database.UserInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ckt on 2/23/17.
 */

public class TranserverUtil {

    public static final String DATE_FORMAT = "yyyy年MM月dd日 HH时mm分";

    public static String hourTime(Long time) {
        return new SimpleDateFormat("HH:mm").format(new Date(time)).trim();
    }

    public static String monthDay(Long time) {
        return new SimpleDateFormat("MM月dd日").format(new Date(time)).trim();
    }

    public static String millsToDate(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return (new SimpleDateFormat(DATE_FORMAT)).format(calendar.getTime());
    }

    public static void transEventTask(EventTask newTask, EventTask task) {
        newTask.setTaskId(task.getTaskId());
        newTask.setTaskTitle(task.getTaskTitle());
        newTask.setTaskContent(task.getTaskContent());
        newTask.setCreateUerId(task.getCreateUerId());
        newTask.setExecUserId(task.getExecUserId());
        newTask.setTaskType(task.getTaskType());
        newTask.setTaskPriority(task.getTaskPriority());
        newTask.setTaskStatus(task.getTaskStatus());
        newTask.setTaskStartTime(task.getTaskStartTime());
        newTask.setTaskPredictTime(task.getTaskPredictTime());
        newTask.setTaskRemindTime(task.getTaskRemindTime());
        newTask.setTaskRealSpendTime(task.getTaskRealSpendTime());
        newTask.setPlanId(task.getPlanId());
        newTask.setTaskUpdateTime(task.getTaskUpdateTime());
        newTask.setTopNumber(task.getTopNumber());
        newTask.setPlan(task.getPlan());
    }


    public static boolean isNumber(String str) {
//        String reg = "^[0-9]+(.[0-9]+)?$";
        String reg = "-?[0-9]+.*[0-9]*";
        return str.matches(reg);
    }

    public static boolean isLegalNum(String str) {
        int count = 0;
        if (str.charAt(str.length() - 1) == '.') {
            return false;
        }

        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) == '.') {
                ++count;
            }
        }
        if (count > 1) {
            return false;
        }
        return true;
    }


    public static String filterZero(Float num) {
        boolean isFloat = false;
        if (num.compareTo(new Float(0)) == 0) {
            return "0";
        }
        Integer zero = Integer.valueOf('0');
        String tmp = String.valueOf(num);
        int pointIndex = tmp.indexOf('.');
        int len = tmp.length();
        StringBuilder builder = new StringBuilder(tmp.substring(0, pointIndex));
        Integer in;
        char ch;
        for (int i = pointIndex + 1; i < len; ++i) {
            ch = tmp.charAt(i);
            in = Integer.valueOf(ch);
            if (in.compareTo(zero) != 0) {
                builder.append(ch);
                isFloat = true;
            }
        }
        if (isFloat) {
            builder.insert(pointIndex, '.');
        }
        return builder.toString();
    }

    public static String filterPlanName(Plan plan) {
        if (plan == null) {
            return "无";
        }
        return plan.getPlanName();
    }


    public static String formatTime(int thisTime) {
        int min, hour, sec;
        StringBuilder build = new StringBuilder();
        hour = thisTime / (60 * 60);
        min = (thisTime - hour * 60 * 60) / 60;
        sec = (thisTime - hour * 60 * 60 - min * 60);
        if (hour > 0) {
            build = formatText(build, hour);
        }
        build = formatText(build, min);
        build = formatText(build, sec);
        build.deleteCharAt(build.length() - 1);
        return build.toString();
    }

    private static StringBuilder formatText(StringBuilder build, long t) {
        if (t < 10) {
            build.append("0").append(t).append(":");
        } else {
            build.append(t).append(":");
        }

        return build;
    }

    public static Project transProject(DatebaseHelper helper, PostProject postProject) {
        Project project = new Project();
        project.setProjectId(postProject.getProjectId());
        project.setProjectTitle(postProject.getProjectTitle());

        // TODO 手机数据库缺少TeamID 字段
//        postProject.getTeamId();

        // TODO 这里关联User有问题需要解决
        // helper.getRealm().where(UserInfo.class).beginsWith()
        project.setOwnerId(postProject.getMemId());
        if (postProject.getProjectSummary() != null) {
            project.setProjectSummary(postProject.getProjectSummary());
        }
        if (postProject.getAccomplishProgress() != null) {
            project.setAccomplishProgress(postProject.getAccomplishProgress());
        }
        if (postProject.getCreateTime() != null && !postProject.getCreateTime().equals("")) {
            project.setCreateTime(Long.valueOf(postProject.getCreateTime()));
        }
        if (postProject.getEndTime() != null && !postProject.getEndTime().equals("")) {
            project.setEndTime(Long.valueOf(postProject.getEndTime()));
        }
        project.setLastUpdateTime(Long.valueOf(postProject.getLastUpdateTime()));
        project.setSync(true);
        return project;
    }

    public static EventTask transTask(PostTask postTask) {
        EventTask task = new EventTask();
        task.setTaskId(postTask.getTaskId());
        task.setTaskTitle(postTask.getTaskTitle());
        task.setCreateUerId(postTask.getMem_id());
        if (postTask.getTaskContent() != null) {
            task.setTaskContent(postTask.getTaskContent());
        }
        if (postTask.getPlanId() != null) {
            //TODO
//            task.setPlan();
        }
        if (postTask.getTaskPredictTime() != null && !postTask.getTaskPredictTime().equals("")) {
            task.setTaskPredictTime(Float.valueOf(postTask.getTaskPredictTime()));
        }
        task.setTaskPriority(postTask.getTaskPriority());
        if (postTask.getTaskRealSpendTime() != null && !postTask.getTaskRealSpendTime().equals("")) {
            task.setTaskRealSpendTime(Float.valueOf(postTask.getTaskRealSpendTime()));
        }
        if (postTask.getTaskRemindTime() != null && !postTask.getTaskRemindTime().equals("")) {
            task.setTaskRemindTime(Long.valueOf(postTask.getTaskRemindTime()));
        }
        if (postTask.getTaskStartTime() != null && !postTask.getTaskStartTime().equals("")) {
            task.setTaskStartTime(Long.valueOf(postTask.getTaskStartTime()));
        }
        task.setTaskStatus(postTask.getTaskStatus());
        task.setTaskType(postTask.getTaskType());
        if (postTask.getTaskUpdateTime() != null && !postTask.getTaskUpdateTime().equals("")) {
            task.setTaskUpdateTime(Long.valueOf(postTask.getTaskUpdateTime()));
        }
        return task;
    }
}
