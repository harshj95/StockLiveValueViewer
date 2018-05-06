package com.application.upstox.presenters;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.application.upstox.api.ApiClient;
import com.application.upstox.contracts.StockDisplayContract;
import com.application.upstox.model.Data;
import com.application.upstox.model.InitialResponse;
import com.application.upstox.util.ExecutorSupplier;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by harsh.jain on 5/6/18.
 */

public class StockDisplayPresenter extends BasePresenterImpl<StockDisplayContract.View> implements StockDisplayContract.Presenter, Callback<List<String>> {

    private StockDisplayContract.View mView;

    public StockDisplayPresenter(StockDisplayContract.View view) {
        this.mView = view;
    }

    @Override
    public void check() {
        ExecutorSupplier.getInstance().getWorkerThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Call<InitialResponse> c  = ApiClient.getApiInterface().check();
                c.enqueue(new Callback<InitialResponse>() {
                    @Override
                    public void onResponse(Call<InitialResponse> call, Response<InitialResponse> response) {
                        InitialResponse resp = response.body();
                        if (resp.getStatus().equals("OK"))
                            mView.connected();
                        else
                            mView.showErrorMessage("Couldn't Connect");
                    }

                    @Override
                    public void onFailure(Call<InitialResponse> call, Throwable t) {
                        mView.showErrorMessage("Couldn't Connect");
                    }
                });
            }
        });
    }

    @Override
    public void getHistoricalData() {
        mView.showProgress("Fetching Data...");
        getHistoricalData(2);
    }

    @Override
    public void getLiveData(final Socket mSocket, final Activity activity) {
        mView.showProgress("Fetching Data...");
        ExecutorSupplier.getInstance().getWorkerThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
            JSONObject obj = new JSONObject();
            try {
                obj.put("state", true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mSocket.emit("sub", obj);

            mSocket.on("data", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    final Ack ack = (Ack) args[args.length-1];

                    Data data = new Data(args[0].toString());

                    mView.showLiveData(String.valueOf(data.getClose()));

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ack.call(1);
                                }
                            }, 2000);
                        }
                    });
                }
            });
            }
        });
    }

    @Override
    public void sellButtonClick() {

        //Selling stock logic here
        mView.sellButtonFinished();
    }

    @Override
    public void buyButtonClick() {

        //Buying stock logic here
        mView.buyButtonFinished();
    }

    void getHistoricalData(final int interval)
    {
        ExecutorSupplier.getInstance().getWorkerThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Call<List<String>> c  = ApiClient.getApiInterface().getHistoricalData(interval);
                c.enqueue(StockDisplayPresenter.this);
            }
        });
    }

    @Override
    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
        mView.hideProgress();
        List<String> historicalDataResponse = response.body();

        if (historicalDataResponse==null)
            mView.showErrorMessage("Couldn't find data");
        else
        {
            List<Data> dataList = new ArrayList<>();
            for(String item : historicalDataResponse)
            {
                dataList.add(new Data(item));
            }

            mView.chartHistoricalData(dataList);
        }
    }

    @Override
    public void onFailure(Call<List<String>> call, Throwable t) {
        mView.hideProgress();
        mView.showErrorMessage("Some error occurred");
        Log.d("API_CALL", t.toString());
    }
}