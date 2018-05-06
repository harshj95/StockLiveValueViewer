package com.application.upstox.views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.application.upstox.R;
import com.application.upstox.presenters.BasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by harsh.jain on 5/5/18.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {

    private Unbinder mUnBinder;

    private ProgressDialog mProgressDialog = null;

    protected @NonNull
    abstract P getPresenterInstance();

    protected P mPresenter;

    public void showShortToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenterInstance();
        mPresenter.attachView(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mUnBinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        mUnBinder.unbind();
        super.onDestroy();
    }

    @Override
    public void showProgress(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mProgressDialog == null || !mProgressDialog.isShowing()) {
                    mProgressDialog = new ProgressDialog(BaseActivity.this);
                    mProgressDialog.setMessage(message);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                }
            }
        });
    }

    @Override
    public void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }
}