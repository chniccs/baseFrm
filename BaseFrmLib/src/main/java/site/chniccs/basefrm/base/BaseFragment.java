package site.chniccs.basefrm.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import butterknife.ButterKnife;
import site.chniccs.basefrm.widget.LoadingDialog;

/**
 * Created by ccs on 17/1/6.
 * 基类fragment
 */

public abstract class BaseFragment<T extends IBasePresenter> extends Fragment implements IBaseView {
    public T mPresenter;
    private LoadingDialog mDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), null);
        ButterKnife.bind(this, view);
        mPresenter = createPresenter();
        setPresenter(mPresenter);
        mPresenter.subscribe();
        initView();
        init();
        return view;
    }

    protected void setPresenter(T presenter) {
    }

    protected abstract void initView();

    protected void init() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    private T createPresenter() {
        T presenterInstance = null;
        try {
            Class<T> presenter = (Class<T>) getPresenter();
            Constructor c = presenter.getDeclaredConstructor(new Class[]{IBaseView.class});
            c.setAccessible(true);
            presenterInstance = (T) c.newInstance(new Object[]{this});
        } catch (NoSuchMethodException | InvocationTargetException | java.lang.InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (presenterInstance == null) {
            throw new RuntimeException("请确认是否已经正确的创建了Presenter类,Presenter必须继承自当前页面的Contract中的内部类Presenter");
        }
        return presenterInstance;
    }

    protected abstract
    @LayoutRes
    int getLayout();

    protected abstract Class<?> getPresenter();

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
            mDialog = new LoadingDialog(this.getActivity());
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
