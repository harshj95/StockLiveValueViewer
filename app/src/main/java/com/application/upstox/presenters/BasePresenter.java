package com.application.upstox.presenters;

import com.application.upstox.views.BaseView;

/**
 * Created by harsh.jain on 5/5/18.
 */

public interface BasePresenter<V extends BaseView> {

    void attachView(V view);

    void detachView();
}
