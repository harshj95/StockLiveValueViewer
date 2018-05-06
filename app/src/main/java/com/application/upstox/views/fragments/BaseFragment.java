package com.application.upstox.views.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.application.upstox.presenters.BasePresenter;
import com.application.upstox.views.BaseActivity;
import com.application.upstox.views.BaseView;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URI;
import java.net.URISyntaxException;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by harsh.jain on 5/5/18.
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView {

    private BaseActivity mActivity;
    private Unbinder mUnBinder;
    private ProgressDialog mProgressDialog;
    private Manager manager;
    protected Socket mSocket;

    protected @NonNull
    abstract P getPresenterInstance();

    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, view);
        mPresenter = getPresenterInstance();
        mPresenter.attachView(this);
        try {
            manager = new Manager(new URI("http://kaboom.rksv.net"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket = manager.socket("/watch");
        mSocket.connect();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
        }
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    public void showShortToast(String message) {
        Toast.makeText(getBaseActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        mSocket.disconnect();
        super.onDestroyView();
    }

    @Override
    public void showProgress(final String message) {
        getBaseActivity().showProgress(message);
    }

    @Override
    public void hideProgress() {
        getBaseActivity().hideProgress();
    }
}