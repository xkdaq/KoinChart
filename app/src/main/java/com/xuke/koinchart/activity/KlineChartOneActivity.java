package com.xuke.koinchart.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.xuke.koinchart.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kekex on 2018/9/4.
 * k线图1
 */

public class KlineChartOneActivity extends Activity {

    @BindView(R.id.combinedchart)
    CombinedChart combinedchart;
    @BindView(R.id.barchart)
    BarChart barchart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_kline_chart_one);
        ButterKnife.bind(this);

    }
}
