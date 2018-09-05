package com.xuke.koinchart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.xuke.koinchart.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_time_chart)
    TextView tvTimeChart;
    @BindView(R.id.tv_kline_one_chart)
    TextView tvKlineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_time_chart, R.id.tv_kline_one_chart,R.id.tv_kline_two_chart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_time_chart:
                //分时图
                startActivity(new Intent(this, TimeChartActivity.class));
                break;
            case R.id.tv_kline_one_chart:
                //k线图1
                startActivity(new Intent(this, KlineChartOneActivity.class));
                break;
            case R.id.tv_kline_two_chart:
                //k线图2
                startActivity(new Intent(this, KlineChartTwoActivity.class));
                break;
        }
    }
}
