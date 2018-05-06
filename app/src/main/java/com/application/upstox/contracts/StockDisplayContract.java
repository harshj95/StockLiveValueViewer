package com.application.upstox.contracts;

import android.app.Activity;

import com.application.upstox.model.Data;
import com.application.upstox.presenters.BasePresenter;
import com.application.upstox.views.BaseView;
import com.github.nkzawa.socketio.client.Socket;

import java.util.List;

/**
 * Created by harsh.jain on 5/6/18.
 */

public class StockDisplayContract {

    public interface View extends BaseView {
        void showErrorMessage(String message);
        void connected();
        void chartHistoricalData(List<Data> dataList);
        void showLiveData(String value);
        void sellButtonFinished();
        void buyButtonFinished();
    }

    public interface Presenter extends BasePresenter<View> {
        void check();
        void getHistoricalData();
        void getLiveData(Socket socket, Activity activity);
        void sellButtonClick();
        void buyButtonClick();
    }
}
