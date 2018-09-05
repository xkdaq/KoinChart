package com.xuke.koinchart.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xuke.koinchart.R;

/**
 * Created by kekex on 2018/9/4.
 * 分时图
 */

public class TimeChartActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_chart);
    }
}
