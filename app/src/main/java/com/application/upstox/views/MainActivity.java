package com.application.upstox.views;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.application.upstox.R;
import com.application.upstox.contracts.MainActivityContract;
import com.application.upstox.presenters.MainActivityPresenter;
import com.application.upstox.views.fragments.MainFragment;

public class MainActivity extends BaseActivity<MainActivityContract.Presenter> implements MainActivityContract.View {

    @NonNull
    @Override
    protected MainActivityContract.Presenter getPresenterInstance() {
        return new MainActivityPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showMainFragment();
    }

    void showMainFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .add(R.id.frame_container, new MainFragment(), MainFragment.class.getSimpleName());
        transaction.commit();
    }

    @Override
    public void showErrorMessage(String message) {
        showShortToast(message);
    }
}
