package com.ckt.ckttodo.util;


import android.content.Context;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.database.Note;
import com.ckt.ckttodo.database.Plan;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;

public class InitDataBase {

    public static void initData(final Context context) {

        long time = new Date().getTime();

        for (int i = 0; i < 3; i++) {
            Plan plan = new Plan();
            plan.setPlanId(i);
            plan.setPlanName(context.getResources().getString(R.string.init_plan_name) + i);
            plan.setCreateTime(time);
            DatebaseHelper.getInstance(context).insert(plan);

            if (i == 0) {
                DatebaseHelper.getInstance(context).getRealm().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Plan sPlan = DatebaseHelper.getInstance(context).getRealm().where(Plan.class).equalTo("planId", 0).findFirst();
                        for (int j = 0; j < 4; j++) {
                            EventTask task = new EventTask();
                            task.setTaskId(UUID.randomUUID().toString());
                            task.setTaskTitle(context.getResources().getString(R.string.init_task_title));
                            task.setTaskContent(context.getResources().getString(R.string.init_task_content));
                            task.setTaskStartTime(new Date().getTime());
                            task.setTaskPredictTime(2f);
                            task.setPlan(sPlan);
                            sPlan.getEventTasks().add(task);
                        }
                    }
                });
            } else if (i == 1) {
                DatebaseHelper.getInstance(context).getRealm().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Plan sPlan = DatebaseHelper.getInstance(context).getRealm().where(Plan.class).equalTo("planId", 1).findFirst();
                        for (int j = 0; j < 2; j++) {
                            EventTask task = new EventTask();
                            task.setTaskId(UUID.randomUUID().toString());
                            task.setTaskTitle(context.getResources().getString(R.string.init_task_title));
                            task.setTaskContent(context.getResources().getString(R.string.init_task_content));
                            task.setTaskStartTime(new Date().getTime());
                            task.setTaskPredictTime(2f);
                            task.setPlan(sPlan);
                            sPlan.getEventTasks().add(task);
                        }
                    }
                });
            } else if (i == 2) {
                DatebaseHelper.getInstance(context).getRealm().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Plan sPlan = DatebaseHelper.getInstance(context).getRealm().where(Plan.class).equalTo("planId", 2).findFirst();
                        for (int j = 0; j < 4; j++) {
                            EventTask task = new EventTask();
                            task.setTaskId(UUID.randomUUID().toString());
                            task.setTaskTitle(context.getResources().getString(R.string.init_task_title));
                            task.setTaskContent(context.getResources().getString(R.string.init_task_content));
                            task.setTaskStartTime(new Date().getTime());
                            task.setTaskPredictTime(2f);
                            task.setPlan(sPlan);
                            sPlan.getEventTasks().add(task);
                        }
                    }
                });
            }
        }
        Note note = new Note();
        note.setNoteId(UUID.randomUUID().toString());
        note.setNoteTitle(context.getResources().getString(R.string.init_note_title));
        note.setNoteContent(context.getResources().getString(R.string.init_note_content));
        note.setNoteCreateTime(time);
        DatebaseHelper.getInstance(context).insert(note);
    }
}
