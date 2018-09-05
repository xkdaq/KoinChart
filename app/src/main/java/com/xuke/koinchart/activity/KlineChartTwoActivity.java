package com.xuke.koinchart.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xuke.koinchart.R;
import com.xuke.koinchart.chart.InBoundYAxisRenderer;
import com.xuke.koinchart.chart.OffsetBarRenderer;
import com.xuke.koinchart.entity.KlineEntity;
import com.xuke.koinchart.util.CommentUtil;
import com.xuke.koinchart.util.OkHttpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kekex on 2018/9/4.
 * k线图2
 */

public class KlineChartTwoActivity extends Activity implements OkHttpUtil.OnDataListener {


    @BindView(R.id.cc_kl)
    CombinedChart ccKl;
    @BindView(R.id.bc_kl)
    BarChart bcKl;
    @BindView(R.id.tl_kl)
    TabLayout tlKl;

    private CandleDataSet candleSet;
    private LineDataSet lineSet5;
    private LineDataSet lineSet10;
    private LineDataSet lineSet30;
    private LineDataSet lineSetMin;
    private BarDataSet barSet;
    private boolean toLeft;
    private List<List<String>> dataList;
    private Map<Integer, String> xValues;

    private String URL = "https://openapi.dragonex.im/api/v1/market/kline/?symbol_id=103&st=%s&direction=%s&count=100&kline_type=%s";
    private final String[] KLINE = {"分时", "15分", "30分", "1小时", "1天"};
    private int[] KL_TYPE = {1, 3, 4, 5, 6};

    private int index = 0;//TabLayout选中下标
    private Gson gson = new GsonBuilder().create();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    private CombinedData combinedData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_kline_chart_two);
        ButterKnife.bind(this);

        for (int i = 0; i < KLINE.length; i++) {
            TextView v = (TextView) LayoutInflater.from(this).inflate(R.layout.item_tab_kline, null);
            v.setText(KLINE[i]);
            tlKl.addTab(tlKl.newTab().setCustomView(v), i == index);
        }
        tlKl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                index = tab.getPosition();
                initData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //初始化表格
        initChart();
        //初始化数据
        initData();
    }

    /**
     * 初始化chart
     */
    private void initChart() {
        //K线
        ccKl.setNoDataTextColor(getResources().getColor(R.color.gray8B));//无数据时提示文字的颜色
        ccKl.setDescription(null);//取消描述
        ccKl.getLegend().setEnabled(false);//取消图例
        ccKl.setDragDecelerationEnabled(false);//不允许甩动惯性滑动  和moveView方法有冲突 设置为false
        ccKl.setMinOffset(0);//设置外边缘偏移量
        ccKl.setExtraBottomOffset(6);//设置底部外边缘偏移量 便于显示X轴
        ccKl.setScaleEnabled(false);//不可缩放
        ccKl.setAutoScaleMinMaxEnabled(true);//自适应最大最小值
        ccKl.setDrawOrder(new CombinedChart.DrawOrder[]{CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE}); //绘制顺序,先绘制条形再绘制条线

        //K线 x轴
        XAxis xac = ccKl.getXAxis();
        xac.setPosition(XAxis.XAxisPosition.BOTTOM);
        xac.setGridColor(getResources().getColor(R.color.black3B));//网格线颜色
        xac.setTextColor(getResources().getColor(R.color.gray8B));//标签颜色
        xac.setTextSize(8);//标签字体大小
        xac.setAxisLineColor(getResources().getColor(R.color.black3B));//轴线颜色
        xac.disableAxisLineDashedLine();//取消轴线虚线设置
        xac.setAvoidFirstLastClipping(true);//避免首尾端标签被裁剪
        xac.setLabelCount(5, true);//强制显示2个标签

        //K线 左Y轴
        YAxis axisLeft = ccKl.getAxisLeft();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);      //标签显示在内侧;OUTSIDE_CHART外侧
        axisLeft.setGridColor(getResources().getColor(R.color.black3B));  //网格颜色
        axisLeft.setTextColor(getResources().getColor(R.color.gray8B));   //文字颜色
        axisLeft.setTextSize(8);  //文字大小
        axisLeft.setLabelCount(5, true);  //label个数,强制设置标签计数
        axisLeft.enableGridDashedLine(5, 4, 0);//横向网格线设置为虚线

        //K线 右Y轴
        YAxis axisRight = ccKl.getAxisRight();
        //axisRight.setEnabled(false);         //不绘制右轴
        axisRight.setDrawLabels(false);      //不绘制右轴标签
        axisRight.setDrawGridLines(false);   //不绘制网格
        axisRight.setDrawAxisLine(false);    //不绘制沿轴线

        //蜡烛图
        candleSet = new CandleDataSet(new ArrayList<CandleEntry>(), "Kline");
        candleSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        candleSet.setDrawHorizontalHighlightIndicator(false);
        candleSet.setHighlightLineWidth(0.5f);
        candleSet.setHighLightColor(getResources().getColor(R.color.brown));
        candleSet.setShadowWidth(0.7f);
        candleSet.setIncreasingColor(getResources().getColor(R.color.redEB)); //上涨设置为红色
        candleSet.setIncreasingPaintStyle(Paint.Style.FILL);   //fill:实心填充  stroke:空心描边  fill_and_stroke  填充描边
        candleSet.setDecreasingColor(getResources().getColor(R.color.green4C));//下跌设置为绿色
        candleSet.setDecreasingPaintStyle(Paint.Style.FILL);   //fill:实心填充  stroke:空心描边  fill_and_stroke  填充描边
        candleSet.setNeutralColor(getResources().getColor(R.color.redEB));
        candleSet.setShadowColorSameAsCandle(true);
        candleSet.setDrawValues(false);
        candleSet.setHighlightEnabled(false);
        //5分均线
        lineSet5 = new LineDataSet(new ArrayList<Entry>(), "MA5");
        lineSet5.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineSet5.setColor(getResources().getColor(R.color.purple));
        lineSet5.setDrawCircles(false);
        lineSet5.setDrawValues(false);
        lineSet5.setHighlightEnabled(false);
        //10分均线
        lineSet10 = new LineDataSet(new ArrayList<Entry>(), "MA10");
        lineSet10.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineSet10.setColor(getResources().getColor(R.color.yellow));
        lineSet10.setDrawCircles(false);
        lineSet10.setDrawValues(false);
        lineSet10.setHighlightEnabled(false);
        //30分均线
        lineSet30 = new LineDataSet(new ArrayList<Entry>(), "MA30");
        lineSet30.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineSet30.setColor(getResources().getColor(R.color.white));
        lineSet30.setDrawCircles(false);
        lineSet30.setDrawValues(false);
        lineSet30.setHighlightEnabled(false);
        //分时线
        lineSetMin = new LineDataSet(new ArrayList<Entry>(), "Minutes");
        lineSetMin.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineSetMin.setColor(Color.WHITE);
        lineSetMin.setDrawCircles(false);
        lineSetMin.setDrawValues(false);
        lineSetMin.setDrawFilled(true);
        lineSetMin.setHighlightEnabled(false);
        lineSetMin.setFillColor(getResources().getColor(R.color.gray8B));
        lineSetMin.setFillAlpha(60);


        //成交量
        bcKl.setNoDataTextColor(getResources().getColor(R.color.gray8B));
        bcKl.setDescription(null);
        bcKl.getLegend().setEnabled(false);
        bcKl.setDragDecelerationEnabled(false);   //不允许甩动惯性滑动
        bcKl.setMinOffset(0);   //设置外边缘偏移量
        bcKl.setScaleEnabled(false);//不可缩放
        bcKl.setAutoScaleMinMaxEnabled(true);//自适应最大最小值
        //自定义Y轴标签位置
        bcKl.setRendererLeftYAxis(new InBoundYAxisRenderer(bcKl.getViewPortHandler(), bcKl.getAxisLeft(),
                bcKl.getTransformer(YAxis.AxisDependency.LEFT)));
        //设置渲染器控制颜色、偏移，以及高亮
        bcKl.setRenderer(new OffsetBarRenderer(bcKl, bcKl.getAnimator(), bcKl.getViewPortHandler(), -0.5f)
                .setHighlightWidthSize(0.5f, CommentUtil.sp2px(this, 8)));

        //x轴
        XAxis xAxis = bcKl.getXAxis();
        xAxis.setEnabled(false);

        //左Y轴
        YAxis axisLeft1 = bcKl.getAxisLeft();
        axisLeft1.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);//标签显示在内侧
        axisLeft1.setDrawAxisLine(false);
        axisLeft1.setGridColor(getResources().getColor(R.color.black3B));
        axisLeft1.setTextColor(getResources().getColor(R.color.gray8B));
        axisLeft1.setTextSize(8);
        axisLeft1.setLabelCount(2, true);
        axisLeft1.setAxisMinimum(0);
        axisLeft1.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value == 0 ? "" : value + "";
            }
        });

        //右Y轴
        YAxis axisRight1 = bcKl.getAxisRight();
        //axisRight1.setEnabled(false);         //不绘制右轴
        axisRight1.setDrawLabels(false);      //不绘制右轴标签
        axisRight1.setDrawGridLines(false);   //不绘制网格
        axisRight1.setDrawAxisLine(false);    //不绘制沿轴线

        //柱状图
        barSet = new BarDataSet(new ArrayList<BarEntry>(), "VOL");
        barSet.setHighLightColor(getResources().getColor(R.color.brown));
        barSet.setColors(getResources().getColor(R.color.redEB), getResources().getColor(R.color.green4C));
        barSet.setDrawValues(false);
        barSet.setHighlightEnabled(false);

    }


    /**
     * 初始化数据
     */
    private void initData() {
        clearChart();
        toLeft = true;
        getData("0");
    }

    private void getData(String time) {
        String url = String.format(URL, time, toLeft ? "2" : "1", KL_TYPE[index]);
        OkHttpUtil.getJSON(url, this);
    }

    /**
     * 每次刷新数据之前清空chart
     */
    private void clearChart() {
        if (dataList == null) {
            dataList = new ArrayList<>();
        } else {
            dataList.clear();
        }
        if (xValues == null) {
            xValues = new HashMap<>();
        } else {
            xValues.clear();
        }
        ccKl.setNoDataText("加载中...");
        ccKl.clear();
        bcKl.setNoDataText("加载中...");
        bcKl.clear();
    }


    /**
     * 网络请求成功
     */
    @Override
    public void onResponse(String url, String json) {
        Log.e("loge", "Kline: " + json);
        KlineEntity kl = gson.fromJson(json, KlineEntity.class);
        if (kl.isOk()) {
            int size = xValues.size();
            List<List<String>> lists = kl.getData().getLists();
            if (lists.size() <= 0) {
                return;
            }
            if (lists.size() == 1) {
                long time = Long.parseLong(lists.get(0).get(6)) * 1000;     //x时间轴的时间戳
                String x = sdf.format(new Date(time));                      //x时间轴的格式时间
                if (!xValues.containsValue(x)) {
                    handleData(lists, size);
                }
            } else {
                handleData(lists, size);
            }
        }
    }


    /**
     * size是指追加数据之前，已有的数据个数
     */
    private void handleData(List<List<String>> lists, int size) {
        if (toLeft) {
            dataList.addAll(0, lists);//添加到左侧
        } else {
            dataList.addAll(lists);
        }

        configData();
        if (xValues.size() > 0) {
            int x = xValues.size() - (toLeft ? size : 0);
            //如果设置了惯性甩动 move方法将会无效
            if (!toLeft && size > 0) {
                ccKl.moveViewToAnimated(x, 0, YAxis.AxisDependency.LEFT, 200);
                bcKl.moveViewToAnimated(x + (-0.5f), 0, YAxis.AxisDependency.LEFT, 200);
            } else {
                ccKl.moveViewToX(x);
                bcKl.moveViewToX(x + (-0.5f));
            }
            ccKl.notifyDataSetChanged();
            bcKl.notifyDataSetChanged();
        }
    }

    /**
     * 取数据
     */
    private void configData() {
        if (dataList.size() == 0) {
            ccKl.setNoDataText("暂无相关数据");
            ccKl.clear();
            bcKl.setNoDataText("暂无相关数据");
            bcKl.clear();
        } else {
            if (combinedData == null) {
                combinedData = new CombinedData();
            }
            xValues.clear();
            List<CandleEntry> candleValues = candleSet.getValues();
            candleValues.clear();
            List<Entry> ma5Values = lineSet5.getValues();
            ma5Values.clear();
            List<Entry> ma10Values = lineSet10.getValues();
            ma10Values.clear();
            List<Entry> ma30Values = lineSet30.getValues();
            ma30Values.clear();
            List<Entry> minValues = lineSetMin.getValues();
            minValues.clear();
            List<BarEntry> barValues = barSet.getValues();
            barValues.clear();
            for (int i = 0; i < dataList.size(); i++) {
                List<String> k = dataList.get(i);
                Date d = new Date(Long.parseLong(k.get(6)) * 1000);     //6.毫秒
                String x = sdf.format(d);                               //显示日期
                if (xValues.containsValue(x)) {                         //x重复
                    dataList.remove(i);
                    i--;
                } else {
                    xValues.put(i, x);
                    float open = Float.parseFloat(k.get(4));            //4.open
                    float close = Float.parseFloat(k.get(1));           //1.close
                    candleValues.add(new CandleEntry(i, Float.parseFloat(k.get(2)), Float.parseFloat(k.get(3)), open, close, x)); //2.max  3.min
                    minValues.add(new Entry(i, close, x));
                    barValues.add(new BarEntry(i, Float.parseFloat(k.get(8)), close >= open ? 0 : 1));  //8.volume交易量
                    if (i >= 4) {
                        ma5Values.add(new Entry(i, getMA(i, 5)));
                        if (i >= 9) {
                            ma10Values.add(new Entry(i, getMA(i, 10)));
                            if (i >= 29) {
                                ma30Values.add(new Entry(i, getMA(i, 30)));
                            }
                        }
                    }
                }
            }
            candleSet.setValues(candleValues);
            lineSet5.setValues(ma5Values);
            lineSet10.setValues(ma10Values);
            lineSet30.setValues(ma30Values);
            lineSetMin.setValues(minValues);

            if (tlKl.getSelectedTabPosition() == 0) {
                combinedData.removeDataSet(candleSet);                      //分时图时移除蜡烛图
                combinedData.setData(new LineData(lineSetMin));             //分时线
            } else {
                combinedData.setData(new CandleData(candleSet));            //蜡烛图
                combinedData.setData(new LineData(lineSet5, lineSet10, lineSet30));    //5分线 10分线 30分线
            }

            ccKl.setData(combinedData);
            float xMax = xValues.size() - 0.5F;       //默认X轴最大值是 xValues.size() - 1
            ccKl.getXAxis().setAxisMaximum(xMax);     //使最后一个显示完整

            barSet.setValues(barValues);
            BarData barData = new BarData(barSet);
            barData.setBarWidth(1 - candleSet.getBarSpace() * 2);//使Candle和Bar宽度一致
            bcKl.setData(barData);
            bcKl.getXAxis().setAxisMaximum(xMax + (-0.5f));//保持边缘对齐
            ccKl.setVisibleXRange(52, 52);//设置显示X轴个数的上下限，竖屏固定52个
            bcKl.setVisibleXRange(52, 52);
        }
    }

    private float getMA(int index, int maxCount) {
        int count = 1;
        float sum = Float.parseFloat(dataList.get(index).get(1));
        while (count < maxCount) {
            if (--index < 0) {
                break;
            }
            sum += Float.parseFloat(dataList.get(index).get(1));
            count++;
        }
        return sum / count;
    }


    /**
     * 网络请求失败
     */
    @Override
    public void onFailure(String url, String error) {

    }
}
