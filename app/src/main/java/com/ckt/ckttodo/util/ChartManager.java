package com.ckt.ckttodo.util;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
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

/**
 * Created by zhiwei.li
 */

public class ChartManager {

    //展示这周的每一天
    public static final int DAY_FLAG = 7;
    private static String[] mDays = new String[]{
            "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
    };

    //展示这个月的每一周
    //该数据结构还有问题
    public static final int WEEK_FLAG = 5;
    private static String[] mWeeks = new String[]{
            "Week1", "Week2", "Week3", "Week4", "Week5"
    };

    //展示这个季度的每一月 通过季度来判断选择是哪几个月
    public static final int MONTH_FLAG = 12;
    private static String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    //展示今年的每一季度
    public static final int QUARTER_FLAG = 4;
    private static String[] mQuarters = new String[]{
            "Q1", "Q2", "Q3", "Q4"
    };

    private static final int COLOR_LIVE = Color.rgb(240, 238, 70);
    private static final int COLOR_WORK = Color.rgb(60, 220, 78);
    private static final int COLOR_STUDY = Color.rgb(61, 16, 255);


    /**
     * 计划进度和实际进度展示
     *
     * @param mLineChart
     * @param xValues    x坐标为 周/月/季度/年 y坐标为时间
     * @param lineName1  计划进度
     * @param yValue1
     * @param lineName2  实际进度
     * @param yValue2
     */
    public static void initDoubleLineChart(LineChart mLineChart, final String[] xValues,
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
                return xValues[(int) value];
            }
        });

        //设置左边Y轴样式
        YAxis yAxisLeft = mLineChart.getAxisLeft();
        yAxisLeft.setAxisLineWidth(lineWidth);
        yAxisLeft.setAxisLineColor(ColorTemplate.getHoloBlue());
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setEnabled(false);

        //设置右边Y轴样式
        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        LineDataSet dataSet1 = new LineDataSet(yValue1, lineName1);
        lineDataSetInit(dataSet1, ColorTemplate.getHoloBlue());
        LineDataSet dataSet2 = new LineDataSet(yValue2, lineName2);
        lineDataSetInit(dataSet2, Color.RED);

        //构建一个类型为LineDataSet的ArrayList 用来存放所有y的LineDataSet,他是构建最终加入LineChart数据集所需要的参数
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet1);
        dataSets.add(dataSet2);

        //构建一个LineData  将dataSets放入
        LineData lineData = new LineData(dataSets);
        mLineChart.setData(lineData);

        //设置动画效果
//        mLineChart.animateXY(2000, 2000);
        mLineChart.invalidate();
    }

    private static void lineDataSetInit(LineDataSet dataSet, int color) {
        dataSet.setColor(color);
        dataSet.setCircleColor(color);
        dataSet.setValueTextColor(color);
        dataSet.setLineWidth(2);
        dataSet.setDrawValues(true);
    }

    /**
     * 周 展示 当天的时间分配情况
     * 月 展示 本周的时间分配情况
     * 季度 展示 本月的时间分配情况
     * 年展示本季度的时间分配情况
     *
     * @param pieChart
     * @param yValues     PieEntry中存入的是(时间，类别{生活|工作|学习..})。
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
//        pieChart.animateY(2000, Easing.EasingOption.EaseOutQuad);

        pieChart.invalidate();

        //设置图例
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setEnabled(true);
    }

    public static void initBarChart(BarChart barChart) {
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
    }

    public static void initCombinedChart(CombinedChart chart, int FLAG) {

        // draw bars behind lines
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });

        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = chart.getAxisRight();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);
        rightAxis.setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        switch (FLAG) {
            case DAY_FLAG:
                initCombinedXAxis(xAxis, mDays);
                initCombinedData(xAxis, chart, DAY_FLAG);
                break;
            case WEEK_FLAG:
                initCombinedXAxis(xAxis, mWeeks);
                initCombinedData(xAxis, chart, WEEK_FLAG);
                break;
            case MONTH_FLAG:
                initCombinedXAxis(xAxis, mMonths);
                initCombinedData(xAxis, chart, MONTH_FLAG);
                break;
            case QUARTER_FLAG:
                initCombinedXAxis(xAxis, mQuarters);
                initCombinedData(xAxis, chart, QUARTER_FLAG);
                break;
        }

    }

    private static void initCombinedData(XAxis xAxis, CombinedChart chart, int itemCount) {
        CombinedData data = new CombinedData();
        data.setData(generateLineData(itemCount));
        data.setData(generateBarData(itemCount));

        xAxis.setLabelCount(itemCount - 1);
        xAxis.setAxisMaximum(data.getXMax() + 0.25f);
        chart.setData(data);
        chart.invalidate();
    }


    private static void initCombinedXAxis(XAxis xAxis, final String[] mTexts) {
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mTexts[(int) value % mTexts.length];
            }
        });
    }

    private static LineData generateLineData(int itemcount) {


        ArrayList<Entry> entries1 = new ArrayList<Entry>();
        ArrayList<Entry> entries2 = new ArrayList<>();
        ArrayList<Entry> entries3 = new ArrayList<>();

        for (int index = 0; index < itemcount; index++) {
            entries1.add(new Entry(index + 0.17f, getRandom(15, 5)));
            entries2.add(new Entry(index + 0.5f, getRandom(25, 5)));
            entries3.add(new Entry(index + 0.83f, getRandom(35, 5)));
        }

        LineDataSet set1 = new LineDataSet(entries1, "Line DataSet1");
        set1.setColor(COLOR_WORK);
        set1.setLineWidth(2.5f);
        set1.setCircleColor(COLOR_WORK);
        set1.setCircleRadius(5f);
        set1.setFillColor(COLOR_WORK);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setDrawValues(true);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(COLOR_WORK);

        LineDataSet set2 = new LineDataSet(entries2, "Line DataSet2");
        set2.setColor(COLOR_STUDY);
        set2.setLineWidth(2.5f);
        set2.setCircleColor(COLOR_STUDY);
        set2.setCircleRadius(5f);
        set2.setFillColor(COLOR_STUDY);
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.setDrawValues(true);
        set2.setValueTextSize(10f);
        set2.setValueTextColor(COLOR_STUDY);


        LineDataSet set3 = new LineDataSet(entries3, "Line DataSet3");
        set3.setColor(COLOR_LIVE);
        set3.setLineWidth(2.5f);
        set3.setCircleColor(COLOR_LIVE);
        set3.setCircleRadius(5f);
        set3.setFillColor(COLOR_LIVE);
        set3.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set3.setDrawValues(true);
        set3.setValueTextSize(10f);
        set3.setValueTextColor(COLOR_LIVE);

        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        return new LineData(set1, set2, set3);
    }

    private static BarData generateBarData(int itemcount) {
        ArrayList<BarEntry> entries1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entries2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entries3 = new ArrayList<>();

        for (int index = 0; index < itemcount; index++) {
            entries1.add(new BarEntry(0, getRandom(25, 25)));
            // stacked
            entries2.add(new BarEntry(0, getRandom(13, 12)));
            entries3.add(new BarEntry(0, getRandom(20, 20)));
        }

        BarDataSet set1 = new BarDataSet(entries1, "Bar 1");
        set1.setColor(COLOR_WORK);
        set1.setValueTextColor(COLOR_WORK);
        set1.setValueTextSize(10f);
        set1.setDrawValues(false);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarDataSet set2 = new BarDataSet(entries2, "Bar 2");
        set2.setColor(COLOR_STUDY);
        set2.setDrawValues(false);
        set2.setValueTextColor(COLOR_STUDY);
        set2.setValueTextSize(10f);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarDataSet set3 = new BarDataSet(entries3, "Bar 3");
        set3.setColor(COLOR_LIVE);
        set3.setValueTextColor(COLOR_LIVE);
        set3.setValueTextSize(10f);
        set3.setDrawValues(false);
        set3.setAxisDependency(YAxis.AxisDependency.LEFT);


        float groupSpace = 0.1f;
        float barSpace = 0.03f; // x3 dataset
        float barWidth = 0.27f; // x3 dataset
        // (0.27 + 0.03) * 3 + 0.1 = 1.00 -> interval per "group"

        BarData d = new BarData(set1, set2, set3);
        d.setBarWidth(barWidth);

        // make this BarData object grouped
        d.groupBars(0, groupSpace, barSpace); // start at x = 0

        return d;
    }

    private static float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

}
