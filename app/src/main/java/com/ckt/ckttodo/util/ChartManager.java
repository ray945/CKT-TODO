package com.ckt.ckttodo.util;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ChartManager {

    /**
     * 计划进度和实际进度展示
     * @param mLineChart
     * @param xValues x坐标为 周/月/季度/年 y坐标为时间
     * @param lineName1 计划进度
     * @param yValue1
     * @param lineName2 实际进度
     * @param yValue2
     */
    public static void initDoubleLineChart(LineChart mLineChart, final ArrayList<String> xValues,
                                           String lineName1, ArrayList<Entry> yValue1,
                                           String lineName2, ArrayList<Entry> yValue2) {
        float lineWidth = 4;
        //设置是否支持触控缩放操作
        mLineChart.setTouchEnabled(false);
        mLineChart.setScaleEnabled(false);

        //设置描述文字
        mLineChart.setDescription(null);

        //设置X轴样式
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineWidth(lineWidth);
        xAxis.setAxisLineColor(ColorTemplate.getHoloBlue());
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xValues.get((int) value);
            }
        });

        //设置左边Y轴样式
        YAxis yAxisLeft = mLineChart.getAxisLeft();
        yAxisLeft.setAxisLineWidth(lineWidth);
        yAxisLeft.setAxisLineColor(ColorTemplate.getHoloBlue());
        yAxisLeft.setStartAtZero(true);
        yAxisLeft.setDrawGridLines(false);

        //设置右边Y轴样式
        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        LineDataSet dataSet1 = new LineDataSet(yValue1, lineName1);
        lineDataSetInit(dataSet1, Color.RED);

        LineDataSet dataSet2 = new LineDataSet(yValue2, lineName2);
        lineDataSetInit(dataSet2, Color.BLUE);

        //构建一个类型为LineDataSet的ArrayList 用来存放所有y的LineDataSet,他是构建最终加入LineChart数据集所需要的参数
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet1);
        dataSets.add(dataSet2);

        //构建一个LineData  将dataSets放入
        LineData lineData = new LineData(dataSets);
        mLineChart.setData(lineData);

        //设置动画效果
        mLineChart.animateXY(2000, 2000);
        mLineChart.invalidate();
    }

    private static void lineDataSetInit(LineDataSet dataSet, int color) {
        dataSet.setColor(color);
        dataSet.setCircleColor(color);
        dataSet.setValueTextColor(color);
        dataSet.setDrawValues(true);
    }

    /**
     * 每周/月/季度/年 的时间使用情况
     * @param pieChart
     * @param yValues PieEntry中存入的是(时间，类别{生活|工作|学习..})。
     * @param centerLabel 周/月/季度/年
     */
    public static void initPieChart(PieChart pieChart, ArrayList<PieEntry> yValues, String centerLabel) {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setCenterText(centerLabel);
        pieChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(55f);
        pieChart.setTransparentCircleRadius(58f);
        pieChart.setRotationAngle(0);

        PieDataSet dataSet = new PieDataSet(yValues, null);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(6f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextColor(Color.BLACK);
        pieData.setValueTextSize(11f);
        pieChart.setData(pieData);
        pieChart.animateY(2000, Easing.EasingOption.EaseOutQuad);

        pieChart.invalidate();

        //设置图例
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setEnabled(false);
    }

    public static void initBarChart(BarChart barChart) {
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
    }

}
