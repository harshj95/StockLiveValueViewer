package com.application.upstox.contracts;

import android.content.Context;

import com.application.upstox.presenters.BasePresenter;
import com.application.upstox.views.BaseView;

import java.util.List;

/**
 * Created by harsh.jain on 5/5/18.
 */

public class MainActivityContract {

    public interface View extends BaseView {
        void showErrorMessage(String message);
    }

    public interface Presenter extends BasePresenter<View> {
        void sendErrorMessage(String message);
    }
}
