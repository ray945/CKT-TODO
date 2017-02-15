package com.ckt.ckttodo.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.databinding.FragmentChartBinding;
import com.ckt.ckttodo.util.ChartManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Random;;

/**
 * Created by zhiwei.li
 */


public class ChartFragment extends Fragment {

    public static final String TITLE = "title";
    private String tabTitle;

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
        LineChart lineChart = binding.lineChart;
        PieChart pieChart = binding.pieChart;

        //以下为图表展示的数据实际的逻辑代码

//        if (tabTitle.equals(titles[0])) {
//            RealmResults<EventTask> tasks = DatebaseHelper.getInstance(getContext()).findAll(EventTask.class);
//            float workTime = 0;
//            float studyTime = 0;
//            float liveTime = 0;
//            for (EventTask task : tasks) {
//                Date date = new Date(task.getTaskStartTime());
//                switch (task.getTaskStatus()) {
//                    case EventTask.WORK:
//                        workTime += task.getTaskRealSpendTime();
//                    case EventTask.STUDY:
//                        studyTime += task.getTaskRealSpendTime();
//                    case EventTask.LIVE:
//                        liveTime += task.getTaskRealSpendTime();
//                }
//            }
//
//        }

        //以下为展示的数据

        for (int i = 0; i < 4; i++) {
            if (tabTitle.equals(titles[i])) {
                ArrayList<PieEntry> pieEntries = new ArrayList<>();
                pieEntries.add(new PieEntry(45f, kinds[0]));
                pieEntries.add(new PieEntry(20f, kinds[1]));
                pieEntries.add(new PieEntry(100f, kinds[2]));
                ChartManager.initPieChart(pieChart, pieEntries, getResources().getString(R.string.time_distribution));
                ArrayList<Entry> yValues1 = new ArrayList<>();
                ArrayList<Entry> yValues2 = new ArrayList<>();
                if (i == 0) {
                    for (int j = 0; j < 7; j++) {
                        yValues1.add(new Entry(j, new Random().nextFloat() * 7));
                        yValues2.add(new Entry(j, new Random().nextFloat() * 7));
                    }
                    lineChart.getXAxis().setLabelCount(6);
                    ChartManager.initDoubleLineChart(lineChart, getResources().getStringArray(R.array.weeks),
                            getString(R.string.predict_time), yValues1, getString(R.string.real_spend_time), yValues2);
                } else if (i == 1) {
                    for (int j = 0; j < 12; j++) {
                        yValues1.add(new Entry(j, new Random().nextFloat() * 30));
                        yValues2.add(new Entry(j, new Random().nextFloat() * 30));
                    }
                    lineChart.getXAxis().setLabelCount(11);
                    ChartManager.initDoubleLineChart(lineChart, getResources().getStringArray(R.array.months),
                            getString(R.string.predict_time), yValues1, getString(R.string.real_spend_time), yValues2);
                } else if (i == 2) {
                    for (int j = 0; j < 4; j++) {
                        yValues1.add(new Entry(j, new Random().nextFloat() * 90));
                        yValues2.add(new Entry(j, new Random().nextFloat() * 90));
                    }
                    lineChart.getXAxis().setLabelCount(3);
                    ChartManager.initDoubleLineChart(lineChart, getResources().getStringArray(R.array.quarters),
                            getString(R.string.predict_time), yValues1, getString(R.string.real_spend_time), yValues2);
                } else if (i == 3) {
                    for (int j = 0; j < 2; j++) {
                        yValues1.add(new Entry(j, new Random().nextFloat() * 366));
                        yValues2.add(new Entry(j, new Random().nextFloat() * 366));
                    }
                    lineChart.getXAxis().setLabelCount(2, true);
                    ChartManager.initDoubleLineChart(lineChart, getResources().getStringArray(R.array.years),
                            getString(R.string.predict_time), yValues1, getString(R.string.real_spend_time), yValues2);
                }
            }
        }

    }

}
