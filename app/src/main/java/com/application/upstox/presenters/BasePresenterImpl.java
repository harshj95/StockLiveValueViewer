package com.application.upstox.presenters;

import com.application.upstox.views.BaseView;

/**
 * Created by harsh.jain on 5/5/18.
 */

public class BasePresenterImpl<V extends BaseView> implements BasePresenter<V> {

    protected V mView;

    @Override
    public void attachView(V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    public V getView() {
        return mView;
    }
}
