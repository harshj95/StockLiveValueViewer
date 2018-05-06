package com.application.upstox.presenters;

import com.application.upstox.contracts.MainActivityContract;

/**
 * Created by harsh.jain on 5/5/18.
 */

public class MainActivityPresenter extends BasePresenterImpl<MainActivityContract.View> implements MainActivityContract.Presenter {

    private MainActivityContract.View mView;

    public MainActivityPresenter(MainActivityContract.View view)
    {
        this.mView = view;
    }

    @Override
    public void sendErrorMessage(String message) {
        mView.showErrorMessage(message);
    }
}
