package com.ckt.ckttodo.util;

import android.content.Context;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.database.Note;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.database.Project;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;

/**
 * Created by zhiwei.li
 */

public class InitDataBase {

    private static final String NOT_USER_ID = "userId";
    private static final String PROJECT_ID = "projectId";
    private static final String PLAN_ID = "planId";


    public static void initData(final Context context) {

        final long time = new Date().getTime();

        for (int i = 0; i < 3; i++) {
            Project project = new Project();
            project.setProjectId(UUID.randomUUID().toString());
            project.setProjectTitle(context.getResources().getString(R.string.init_project_name));
            project.setProjectSummary(
                context.getResources().getString(R.string.init_project_summary));
            project.setCreateTime(time);
            project.setEndTime(time);
            project.setLastUpdateTime(time);
            DatebaseHelper.getInstance(context).insert(project);
            final String projectId = project.getProjectId();

            for (int j = 0; j < 4; j++) {
                final String planId = UUID.randomUUID().toString();
                DatebaseHelper.getInstance(context)
                    .getRealm()
                    .executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Project sProject = DatebaseHelper.getInstance(context)
                                .getRealm()
                                .where(Project.class)
                                .equalTo(PROJECT_ID, projectId)
                                .findFirst();
                            Plan plan = new Plan();
                            plan.setPlanId(planId);
                            plan.setPlanName(
                                context.getResources().getString(R.string.init_plan_name));
                            plan.setPlanContent(
                                context.getResources().getString(R.string.init_plan_content));
                            plan.setCreateTime(time);
                            plan.setStartTime(time);
                            plan.setEndTime(time);
                            plan.setLastUpdateTime(time);
                            plan.setStatus(Plan.PLAN_START);
                            plan.setPredictSpendTime(20f);
                            plan.setProjectId(projectId);
                            sProject.getPlans().add(plan);
                            //                        DatebaseHelper.getInstance(context).insert(plan);
                        }
                    });

                if (j == 0) {
                    DatebaseHelper.getInstance(context)
                        .getRealm()
                        .executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Plan sPlan = DatebaseHelper.getInstance(context)
                                    .getRealm()
                                    .where(Plan.class)
                                    .equalTo(PLAN_ID, planId)
                                    .findFirst();
                                for (int j = 0; j < 4; j++) {
                                    EventTask task = new EventTask();
                                    task.setTaskId(UUID.randomUUID().toString());
                                    task.setTaskTitle(
                                        context.getResources().getString(R.string.init_task_title) +
                                            0);
                                    task.setTaskContent(context.getResources()
                                        .getString(R.string.init_task_content));
                                    task.setTaskType(EventTask.WORK);
                                    task.setTaskPriority(EventTask.ENHANCEMENT);
                                    task.setTaskRemindTime(Constants.TEN_MIN_TO_SEC);
                                    task.setTaskStartTime(new Date().getTime());
                                    task.setTaskPredictTime(2f);
                                    task.setTaskStatus(EventTask.DONE);
                                    task.setTaskUpdateTime(time);
                                    task.setPlan(sPlan);
                                    sPlan.getEventTasks().add(task);
                                }
                            }
                        });
                } else if (j == 1) {
                    DatebaseHelper.getInstance(context)
                        .getRealm()
                        .executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Plan sPlan = DatebaseHelper.getInstance(context)
                                    .getRealm()
                                    .where(Plan.class)
                                    .equalTo(PLAN_ID, planId)
                                    .findFirst();
                                for (int j = 0; j < 2; j++) {
                                    EventTask task = new EventTask();
                                    task.setTaskId(UUID.randomUUID().toString());
                                    task.setTaskTitle(
                                        context.getResources().getString(R.string.init_task_title) +
                                            1);
                                    task.setTaskContent(context.getResources()
                                        .getString(R.string.init_task_content));
                                    task.setTaskType(EventTask.STUDY);
                                    task.setTaskPriority(EventTask.ENHANCEMENT);
                                    task.setTaskStatus(EventTask.NOT_START);
                                    task.setTaskRemindTime(Constants.TWENTY_MIN_TO_SEC);
                                    task.setTaskUpdateTime(time);
                                    task.setTaskStartTime(new Date().getTime());
                                    task.setTaskPredictTime(2f);
                                    task.setPlan(sPlan);
                                    sPlan.getEventTasks().add(task);
                                }
                            }
                        });
                } else if (j == 2) {
                    DatebaseHelper.getInstance(context)
                        .getRealm()
                        .executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Plan sPlan = DatebaseHelper.getInstance(context)
                                    .getRealm()
                                    .where(Plan.class)
                                    .equalTo(PLAN_ID, planId)
                                    .findFirst();
                                for (int j = 0; j < 4; j++) {
                                    EventTask task = new EventTask();
                                    task.setTaskId(UUID.randomUUID().toString());
                                    task.setTaskTitle(
                                        context.getResources().getString(R.string.init_task_title) +
                                            2);
                                    task.setTaskContent(context.getResources()
                                        .getString(R.string.init_task_content));
                                    task.setTaskType(EventTask.LIVE);
                                    task.setTaskPriority(EventTask.ENHANCEMENT);
                                    task.setTaskRemindTime(Constants.HALF_HOUR_TO_SEC);
                                    task.setTaskUpdateTime(time);
                                    task.setTaskStartTime(new Date().getTime());
                                    task.setTaskPredictTime(2f);
                                    if (j == 1 || j == 2) {
                                        task.setTaskStatus(EventTask.DONE);
                                    } else {
                                        task.setTaskStatus(EventTask.START);
                                    }
                                    task.setPlan(sPlan);
                                    sPlan.getEventTasks().add(task);
                                }
                            }
                        });
                }
            }

        }

        Note note = new Note();
        note.setNoteId(UUID.randomUUID().toString());
        note.setNoteTitle(context.getResources().getString(R.string.init_note_title));
        note.setNoteContent(context.getResources().getString(R.string.init_note_content));
        note.setNoteCreateTime(time);
        note.setNoteUpdateTime(time);
        DatebaseHelper.getInstance(context).insert(note);
    }
}
