package com.application.upstox.views.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.upstox.R;
import com.application.upstox.contracts.StockDisplayContract;
import com.application.upstox.model.Data;
import com.application.upstox.presenters.StockDisplayPresenter;
import com.application.upstox.util.CommonLib;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by harsh.jain on 5/5/18.
 */

public class MainFragment extends BaseFragment<StockDisplayContract.Presenter> implements StockDisplayContract.View, View.OnClickListener {

    @BindView(R.id.tvDaily)
    protected TextView tvDaily;
    @BindView(R.id.tvMonthly)
    protected TextView tvMonthly;
    @BindView(R.id.tvQuarterly)
    protected TextView tvQuarterly;
    @BindView(R.id.tvYTD)
    protected TextView tvYTD;
    @BindView(R.id.tvYearly)
    protected TextView tvYearly;
    @BindView(R.id.tvAll)
    protected TextView tvAll;
    @BindView(R.id.trendContainer)
    LinearLayout trendContainer;
    @BindView(R.id.dataContainer)
    LinearLayout dataContainer;
    @BindView(R.id.tvRealTimePrice)
    protected TextView tvRealTimePrice;
    @BindView(R.id.lineChart)
    protected LineChart mLineChart;
    @BindView(R.id.tvDayHigh)
    protected TextView tvDayHigh;
    @BindView(R.id.tvDayLow)
    protected TextView tvDayLow;
    @BindView(R.id.tvOpen)
    protected TextView tvOpen;
    @BindView(R.id.tvClose)
    protected TextView tvClose;
    @BindView(R.id.tvAvgVolume)
    protected TextView tvAvgVolume;
    @BindView(R.id.tvVolume)
    protected TextView tvVolume;
    @BindView(R.id.tv52WkHigh)
    protected TextView tv52WkHigh;
    @BindView(R.id.tv52WkLow)
    protected TextView tv52WkLow;
    protected TextView current;
    @BindColor(R.color.white)
    int white;
    @BindColor(R.color.black)
    int black;
    @BindColor(R.color.colorPrimary)
    int primary;

    @OnClick(R.id.tvSell)
    void sellButtonClick() {
        mPresenter.sellButtonClick();
    }

    @OnClick(R.id.tvBuy)
    void buyButtonClick() {
        mPresenter.buyButtonClick();
    }

    @NonNull
    @Override
    protected StockDisplayContract.Presenter getPresenterInstance() {
        return new StockDisplayPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        setListeners();
        mPresenter.check();
    }

    void initView()
    {
        tvDaily.setTextColor(white);
        tvDaily.setBackgroundColor(primary);
        current = (TextView) tvDaily;
        mLineChart.setNoDataText("");
        mLineChart.getLegend().setEnabled(false);
    }

    void setListeners()
    {
        tvDaily.setOnClickListener(this);
        tvMonthly.setOnClickListener(this);
        tvQuarterly.setOnClickListener(this);
        tvYTD.setOnClickListener(this);
        tvYearly.setOnClickListener(this);
        tvAll.setOnClickListener(this);
    }

    @Override
    public void showErrorMessage(String message) {
        showShortToast(message);
    }

    @Override
    public void connected() {
        mPresenter.getLiveData(mSocket, getBaseActivity());
        mPresenter.getHistoricalData();
    }

    @Override
    public void chartHistoricalData(List<Data> dataList) {
        mLineChart.setVisibility(View.VISIBLE);
        trendContainer.setVisibility(View.VISIBLE);
        dataContainer.setVisibility(View.VISIBLE);

        List<Entry> entries = new ArrayList<Entry>();

        long time = dataList.get(dataList.size()-1).getTimestamp();

        long volume = 0;

        for(int i=dataList.size()-1; i>=0;i--)
        {
            Data data = dataList.get(i);

            volume+= data.getVolume();

            long diff = Math.abs(time-data.getTimestamp());
            int toAdd = (int) diff/86400;
            entries.add(new Entry(toAdd, data.getClose()));
        }

        LineDataSet dataSet = new LineDataSet(entries,"");
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        LineData lineData = new LineData(dataSet);
        mLineChart.setData(lineData);
        mLineChart.invalidate();

        YAxis axisRight = mLineChart.getAxisRight();
        LimitLine low = new LimitLine(CommonLib.getLowest(dataList), String.valueOf(CommonLib.getLowest(dataList)));
        LimitLine high = new LimitLine(CommonLib.getHighest(dataList), String.valueOf(CommonLib.getHighest(dataList)));
        high.setLineColor(Color.BLACK);
        low.setLineColor(Color.BLACK);

        axisRight.addLimitLine(low);
        axisRight.addLimitLine(high);

        tvDayHigh.setText(String.valueOf(dataList.get(0).getHigh()));
        tvDayLow.setText(String.valueOf(dataList.get(0).getLow()));
        tvOpen.setText(String.valueOf(dataList.get(0).getOpen()));
        tvClose.setText(String.valueOf(dataList.get(0).getClose()));
        tvAvgVolume.setText(String.valueOf((int) volume/dataList.size()));
        tvVolume.setText(String.valueOf((int) dataList.get(0).getVolume()));
        tv52WkHigh.setText(String.valueOf(CommonLib.getHighest(dataList)));
        tv52WkLow.setText(String.valueOf(CommonLib.getLowest(dataList)));
    }

    @Override
    public void showLiveData(final String value) {
        hideProgress();
        getBaseActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvRealTimePrice.setText("$"+value);
            }
        });
    }

    @Override
    public void sellButtonFinished() {
        showShortToast("Sold");
    }

    @Override
    public void buyButtonFinished() {
        showShortToast("Bought");
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSocket.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tvDaily:
                tvDaily.setTextColor(white);
                tvDaily.setBackgroundColor(primary);
                current.setTextColor(black);
                current.setBackgroundColor(white);
                current = tvDaily;
                break;

            case R.id.tvMonthly:
                tvMonthly.setTextColor(white);
                tvMonthly.setBackgroundColor(primary);
                current.setTextColor(black);
                current.setBackgroundColor(white);
                current = tvMonthly;
                break;

            case R.id.tvQuarterly:
                tvQuarterly.setTextColor(white);
                tvQuarterly.setBackgroundColor(primary);
                current.setTextColor(black);
                current.setBackgroundColor(white);
                current = tvQuarterly;
                break;

            case R.id.tvYTD:
                tvYTD.setTextColor(white);
                tvYTD.setBackgroundColor(primary);
                current.setTextColor(black);
                current.setBackgroundColor(white);
                current = tvYTD;
                break;

            case R.id.tvYearly:
                tvYearly.setTextColor(white);
                tvYearly.setBackgroundColor(primary);
                current.setTextColor(black);
                current.setBackgroundColor(white);
                current = tvYearly;
                break;

            case R.id.tvAll:
                tvAll.setTextColor(white);
                tvAll.setBackgroundColor(primary);
                current.setTextColor(black);
                current.setBackgroundColor(white);
                current = tvAll;
                break;

        }
    }
}
