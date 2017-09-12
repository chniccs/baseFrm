package site.chniccs.basefrm.base;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import site.chniccs.basefrm.R;
import site.chniccs.basefrm.widget.LoadingDialog;

/**
 * Created by ccs on 17/1/6.
 * 基类activity
 */

public abstract class BaseActivity<T extends IBasePresenter> extends AppCompatActivity implements IBaseView {
    protected T mPresenter;
    private LoadingDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        setStatusBar();
        ButterKnife.bind(this);
        if (needEventBus()) {
            EventBus.getDefault().register(this);
        }
        mPresenter = (T) getPresenter();
        setPresenter(mPresenter);
        initView();
        init();
        mPresenter.subscribe();
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.post(new Runnable() {
            @Override
            public void run() {
                mPresenter.start();
            }
        });
    }
    protected void setPresenter(T presenter){
    }

    //设置状态栏，子类可以重写
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getStatusBarColor());
    }

    //设置状态栏颜色，子类可以重写
    protected
    @ColorInt
    int getStatusBarColor() {
        int color = getResources().getColor(R.color.colorPrimary);
        return color;
    }

    protected boolean needEventBus() {
        return false;
    }

    protected abstract void initView();

    protected void init() {
    }

    ;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (needEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        try {
            mPresenter.unsubscribe();
            if (mDialog != null) {
                if (mDialog.isShowing()) {
                    dismissLoading();
                }
                mDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //    获取布局
    protected abstract
    @LayoutRes
    int getLayout();

    protected abstract
    @NonNull
    IBasePresenter getPresenter();

    @Override
    public void shodLoading() {
        showDialog();
    }

    @Override
    public void dismissLoading() {
        dismiss();
    }

    public void showDialog() {
        if (mDialog == null) {
            mDialog = new LoadingDialog(this);
        }
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
