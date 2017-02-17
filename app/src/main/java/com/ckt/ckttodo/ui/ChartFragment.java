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
import com.github.mikephil.charting.charts.CombinedChart;
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
        CombinedChart combinedChart = binding.combinedChart;
        PieChart pieChart = binding.pieChart;

        //以下为展示的数据

        for (int i = 0; i < 4; i++) {
            if (tabTitle.equals(titles[i])) {
                ArrayList<PieEntry> pieEntries = new ArrayList<>();
                pieEntries.add(new PieEntry(45f, kinds[0]));
                pieEntries.add(new PieEntry(20f, kinds[1]));
                pieEntries.add(new PieEntry(100f, kinds[2]));
                ChartManager.initPieChart(pieChart, pieEntries, getResources().getString(R.string.time_distribution));
                switch (i){
                    case 0:
                        ChartManager.initCombinedChart(combinedChart,ChartManager.DAY_FLAG);
                        break;
                    case 1:
                        ChartManager.initCombinedChart(combinedChart,ChartManager.WEEK_FLAG);
                        break;
                    case 2:
                        ChartManager.initCombinedChart(combinedChart,ChartManager.MONTH_FLAG);
                        break;
                    case 3:
                        ChartManager.initCombinedChart(combinedChart,ChartManager.QUARTER_FLAG);
                        break;
                }

            }
        }

    }

}
