package com.ckt.ckttodo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.databinding.FragmentChartBinding;
import com.ckt.ckttodo.util.ChartManager;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmResults;
import java.util.Locale;

/**
 * Created by zhiwei.li
 */

public class ChartFragment extends Fragment {

    public static final String TITLE = "title";
    private String tabTitle;

    private static final String TAG = "TAG";

    private String[] timeDistributions;
    private String[] quarters;

    private SimpleDateFormat dateFormatDay = new SimpleDateFormat("D", new Locale("zh", "CN"));
    private SimpleDateFormat dateFormatWeek = new SimpleDateFormat("w", new Locale("zh", "CN"));
    private SimpleDateFormat dateFormatWeekOfMonth = new SimpleDateFormat("W",
        new Locale("zh", "CN"));
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MM", new Locale("zh", "CN"));
    private SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy", new Locale("zh", "CN"));
    private SimpleDateFormat dateFormatWeekDay = new SimpleDateFormat("E", new Locale("zh", "CN"));
    private String nowDay;
    private String nowWeek;
    private String nowMonth;
    private String nowQuarter;
    private String nowYear;


    public static ChartFragment getInstance(String title) {
        ChartFragment fragment = new ChartFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        tabTitle = bundle.getString(TITLE);
        timeDistributions = getResources().getStringArray(R.array.time_distribution);
        quarters = getResources().getStringArray(R.array.quarters);

        Date date = new Date();
        // 一年中的第 51 天 一年中第8个星期 一月中第4个星期 在一天中10时 周一
        // SimpleDateFormat dateFormat = new SimpleDateFormat("一年中的第 D 天 一年中第w个星期 一月中第W个星期 在一天中k时 E");
        // Log.e(TAG, dateFormat.format(date.getTime()));

        nowDay = dateFormatDay.format(date);
        nowWeek = dateFormatWeek.format(date);
        nowMonth = dateFormatMonth.format(date);
        nowQuarter = getQuarter(nowMonth);
        nowYear = dateFormatYear.format(date);

        Log.e(TAG, dateFormatWeekOfMonth.format(date));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentChartBinding binding = FragmentChartBinding.inflate(inflater);
        initView(binding);
        return binding.getRoot();
    }


    private void initView(FragmentChartBinding binding) {
        // 下标0-3分别对应 周、月、季度、年
        String[] titles = getResources().getStringArray(R.array.chart_title);
        //下标0-2分别对应工作、学习、生活
        String[] kinds = getResources().getStringArray(R.array.kind_list);
        CombinedChart combinedChart = binding.combinedChart;
        PieChart pieChart = binding.pieChart;

        RealmResults<EventTask> tasks = DatebaseHelper.getInstance(getContext())
            .findAll(EventTask.class);

        //以下为展示的数据

        for (int i = 0; i < 4; i++) {
            if (tabTitle.equals(titles[i])) {
                switch (i) {
                    case 0:
                        initPieChart(tasks, kinds, pieChart, dateFormatDay, nowDay, 0);

                        float[] dayWorkTimes = new float[7];
                        float[] dayStudyTimes = new float[7];
                        float[] dayLiveTimes = new float[7];
                        float[] dayRestTimes = new float[7];
                        for (EventTask task : tasks) {
                            long taskStartTime = task.getTaskStartTime();
                            int taskType = task.getTaskType();
                            if (dateFormatWeek.format(taskStartTime).equals(nowWeek)) {
                                switch (dateFormatWeekDay.format(taskStartTime)) {
                                    case "周一":
                                        addTimeByType(taskType, task, dayWorkTimes, dayStudyTimes,
                                            dayLiveTimes, dayRestTimes, 0);
                                        break;
                                    case "周二":
                                        addTimeByType(taskType, task, dayWorkTimes, dayStudyTimes,
                                            dayLiveTimes, dayRestTimes, 1);
                                        break;
                                    case "周三":
                                        addTimeByType(taskType, task, dayWorkTimes, dayStudyTimes,
                                            dayLiveTimes, dayRestTimes, 2);
                                        break;
                                    case "周四":
                                        addTimeByType(taskType, task, dayWorkTimes, dayStudyTimes,
                                            dayLiveTimes, dayRestTimes, 3);
                                        break;
                                    case "周五":
                                        addTimeByType(taskType, task, dayWorkTimes, dayStudyTimes,
                                            dayLiveTimes, dayRestTimes, 4);
                                        break;
                                    case "周六":
                                        addTimeByType(taskType, task, dayWorkTimes, dayStudyTimes,
                                            dayLiveTimes, dayRestTimes, 5);
                                        break;
                                    case "周日":
                                        addTimeByType(taskType, task, dayWorkTimes, dayStudyTimes,
                                            dayLiveTimes, dayRestTimes, 6);
                                        break;
                                }
                            }
                        }
                        ChartManager.initCombinedChart(combinedChart, ChartManager.DAY_FLAG,
                            dayWorkTimes, dayStudyTimes, dayLiveTimes, dayRestTimes);
                        break;
                    case 1:
                        initPieChart(tasks, kinds, pieChart, dateFormatWeek, nowWeek, 1);
                        float[] weekWorkTimes = new float[5];
                        float[] weekStudyTimes = new float[5];
                        float[] weekLiveTimes = new float[5];
                        float[] weekRestTimes = new float[5];
                        for (EventTask task : tasks) {
                            long taskStartTime = task.getTaskStartTime();
                            int taskType = task.getTaskType();
                            if (dateFormatMonth.format(taskStartTime).equals(nowMonth)) {
                                switch (dateFormatWeekOfMonth.format(taskStartTime)) {
                                    case "1":
                                        addTimeByType(taskType, task, weekWorkTimes, weekStudyTimes,
                                            weekLiveTimes, weekRestTimes, 0);
                                        break;
                                    case "2":
                                        addTimeByType(taskType, task, weekWorkTimes, weekStudyTimes,
                                            weekLiveTimes, weekRestTimes, 1);
                                        break;
                                    case "3":
                                        addTimeByType(taskType, task, weekWorkTimes, weekStudyTimes,
                                            weekLiveTimes, weekRestTimes, 2);
                                        break;
                                    case "4":
                                        addTimeByType(taskType, task, weekWorkTimes, weekStudyTimes,
                                            weekLiveTimes, weekRestTimes, 3);
                                        break;
                                    case "5":
                                        addTimeByType(taskType, task, weekWorkTimes, weekStudyTimes,
                                            weekLiveTimes, weekRestTimes, 4);
                                        break;
                                }
                            }
                        }
                        ChartManager.initCombinedChart(combinedChart, ChartManager.WEEK_FLAG,
                            weekWorkTimes, weekStudyTimes, weekLiveTimes, weekRestTimes);
                        break;
                    case 2:
                        initPieChart(tasks, kinds, pieChart, dateFormatMonth, nowMonth, 2);

                        float[] monthWorkTimes = new float[3];
                        float[] monthStudyTimes = new float[3];
                        float[] monthLiveTimes = new float[3];
                        float[] monthRestTimes = new float[3];
                        for (EventTask task : tasks) {
                            long taskStartTime = task.getTaskStartTime();
                            int taskType = task.getTaskType();
                            if (getQuarter(dateFormatMonth.format(taskStartTime)).equals(
                                nowQuarter)) {
                                switch (nowQuarter) {
                                    case "Q1":
                                        switch (dateFormatMonth.format(taskStartTime)) {
                                            case "01":
                                                addTimeByType(taskType, task, monthWorkTimes,
                                                    monthStudyTimes,
                                                    monthLiveTimes, monthRestTimes, 0);
                                                break;
                                            case "02":
                                                addTimeByType(taskType, task, monthWorkTimes,
                                                    monthStudyTimes,
                                                    monthLiveTimes, monthRestTimes, 1);
                                                break;
                                            case "03":
                                                addTimeByType(taskType, task, monthWorkTimes,
                                                    monthStudyTimes,
                                                    monthLiveTimes, monthRestTimes, 2);
                                                break;
                                        }
                                        ChartManager.initCombinedChart(combinedChart,
                                            ChartManager.MONTH_FLAG1, monthWorkTimes,
                                            monthStudyTimes, monthLiveTimes, monthRestTimes);
                                        break;
                                    case "Q2":
                                        switch (dateFormatMonth.format(taskStartTime)) {
                                            case "01":
                                                addTimeByType(taskType, task, monthWorkTimes,
                                                    monthStudyTimes,
                                                    monthLiveTimes, monthRestTimes, 0);
                                                break;
                                            case "02":
                                                addTimeByType(taskType, task, monthWorkTimes,
                                                    monthStudyTimes,
                                                    monthLiveTimes, monthRestTimes, 1);
                                                break;
                                            case "03":
                                                addTimeByType(taskType, task, monthWorkTimes,
                                                    monthStudyTimes,
                                                    monthLiveTimes, monthRestTimes, 2);
                                                break;
                                        }
                                        ChartManager.initCombinedChart(combinedChart,
                                            ChartManager.MONTH_FLAG2, monthWorkTimes,
                                            monthStudyTimes, monthLiveTimes, monthRestTimes);
                                        break;
                                    case "Q3":
                                        switch (dateFormatMonth.format(taskStartTime)) {
                                            case "01":
                                                addTimeByType(taskType, task, monthWorkTimes,
                                                    monthStudyTimes,
                                                    monthLiveTimes, monthRestTimes, 0);
                                                break;
                                            case "02":
                                                addTimeByType(taskType, task, monthWorkTimes,
                                                    monthStudyTimes,
                                                    monthLiveTimes, monthRestTimes, 1);
                                                break;
                                            case "03":
                                                addTimeByType(taskType, task, monthWorkTimes,
                                                    monthStudyTimes,
                                                    monthLiveTimes, monthRestTimes, 2);
                                                break;
                                        }
                                        ChartManager.initCombinedChart(combinedChart,
                                            ChartManager.MONTH_FLAG3, monthWorkTimes,
                                            monthStudyTimes, monthLiveTimes, monthRestTimes);
                                        break;
                                    case "Q4":
                                        switch (dateFormatMonth.format(taskStartTime)) {
                                            case "01":
                                                addTimeByType(taskType, task, monthWorkTimes,
                                                    monthStudyTimes,
                                                    monthLiveTimes, monthRestTimes, 0);
                                                break;
                                            case "02":
                                                addTimeByType(taskType, task, monthWorkTimes,
                                                    monthStudyTimes,
                                                    monthLiveTimes, monthRestTimes, 1);
                                                break;
                                            case "03":
                                                addTimeByType(taskType, task, monthWorkTimes,
                                                    monthStudyTimes,
                                                    monthLiveTimes, monthRestTimes, 2);
                                                break;
                                        }
                                        ChartManager.initCombinedChart(combinedChart,
                                            ChartManager.MONTH_FLAG4, monthWorkTimes,
                                            monthStudyTimes, monthLiveTimes, monthRestTimes);
                                        break;
                                }
                            }
                        }
                        break;
                    case 3:
                        initPieChart(tasks, kinds, pieChart, dateFormatMonth, nowQuarter, 3);

                        float[] quarterWorkTimes = new float[4];
                        float[] quarterStudyTimes = new float[4];
                        float[] quarterLiveTimes = new float[4];
                        float[] quarterRestTimes = new float[4];
                        for (EventTask task : tasks) {
                            long taskStartTime = task.getTaskStartTime();
                            int taskType = task.getTaskType();
                            if (dateFormatYear.format(taskStartTime).equals(nowYear)) {
                                switch (getQuarter(
                                    dateFormatMonth.format(taskStartTime))) {
                                    case "Q1":
                                        addTimeByType(taskType, task, quarterWorkTimes,
                                            quarterStudyTimes,
                                            quarterLiveTimes, quarterRestTimes, 0);
                                        break;
                                    case "Q2":
                                        addTimeByType(taskType, task, quarterWorkTimes,
                                            quarterStudyTimes,
                                            quarterLiveTimes, quarterRestTimes, 1);
                                        break;
                                    case "Q3":
                                        addTimeByType(taskType, task, quarterWorkTimes,
                                            quarterStudyTimes,
                                            quarterLiveTimes, quarterRestTimes, 2);
                                        break;
                                    case "Q4":
                                        addTimeByType(taskType, task, quarterWorkTimes,
                                            quarterStudyTimes,
                                            quarterLiveTimes, quarterRestTimes, 3);
                                        break;
                                }
                            }
                        }
                        ChartManager.initCombinedChart(combinedChart, ChartManager.QUARTER_FLAG,
                            quarterWorkTimes, quarterStudyTimes, quarterLiveTimes,
                            quarterRestTimes);
                        break;
                }

            }
        }

    }


    private void addTimeByType(int taskType, EventTask task, float[] workTimes, float[] studyTimes, float[] liveTimes, float[] restTimes, int sign) {
        if (taskType == EventTask.WORK) {
            workTimes[sign] += task.getTaskPredictTime();
        } else if (taskType == EventTask.STUDY) {
            studyTimes[sign] += task.getTaskPredictTime();
        } else if (taskType == EventTask.LIVE) {
            liveTimes[sign] += task.getTaskPredictTime();
        } else if (taskType == EventTask.REST) {
            restTimes[sign] += task.getTaskPredictTime();
        }
    }


    private void initPieChart(RealmResults<EventTask> tasks, String[] kinds, PieChart pieChart, SimpleDateFormat nowDateFormat, String nowType, int i) {
        float workTime = 0;
        float studyTime = 0;
        float liveTime = 0;
        float restTime = 0;
        for (EventTask task : tasks) {
            if (i == 3) {
                if (getQuarter(nowDateFormat.format(task.getTaskStartTime())).equals(nowType)) {
                    int taskType = task.getTaskType();
                    if (taskType == EventTask.WORK) {
                        workTime += task.getTaskPredictTime();
                    } else if (taskType == EventTask.STUDY) {
                        studyTime += task.getTaskPredictTime();
                    } else if (taskType == EventTask.LIVE) {
                        liveTime += task.getTaskPredictTime();
                    } else if (taskType == EventTask.REST) {
                        restTime += task.getTaskPredictTime();
                    }
                }
            } else {
                if (nowDateFormat.format(task.getTaskStartTime()).equals(nowType)) {
                    int taskType = task.getTaskType();
                    if (taskType == EventTask.WORK) {
                        workTime += task.getTaskPredictTime();
                    } else if (taskType == EventTask.STUDY) {
                        studyTime += task.getTaskPredictTime();
                    } else if (taskType == EventTask.LIVE) {
                        liveTime += task.getTaskPredictTime();
                    } else if (taskType == EventTask.REST) {
                        restTime += task.getTaskPredictTime();
                    }
                }
            }
        }
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(workTime, kinds[0]));
        pieEntries.add(new PieEntry(studyTime, kinds[1]));
        pieEntries.add(new PieEntry(liveTime, kinds[2]));
        //        pieEntries.add(new PieEntry(restTime,kinds[3]));
        ChartManager.initPieChart(pieChart, pieEntries, timeDistributions[i]);

    }


    private String getQuarter(String month) {
        String quarter = "";
        switch (month) {
            case "01":
            case "02":
            case "03":
                quarter = quarters[0];
                break;
            case "04":
            case "05":
            case "06":
                quarter = quarters[1];
                break;
            case "07":
            case "08":
            case "09":
                quarter = quarters[2];
                break;
            case "10":
            case "11":
            case "12":
                quarter = quarters[3];
                break;
        }
        return quarter;
    }

}
