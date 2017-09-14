package site.chniccs.basefrm.base;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import com.jaeger.library.StatusBarUtil;
import org.greenrobot.eventbus.EventBus;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import butterknife.ButterKnife;
import site.chniccs.basefrm.R;
import site.chniccs.basefrm.widget.LoadingDialog;

/**
 * Created by ccs on 17/1/6.
 * 基类activity
 */

public abstract class BaseActivity<T extends IBasePresenter> extends AppCompatActivity implements IBaseView {
    public T mPresenter;
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
        mPresenter =  createPresenter();
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
        return getResources().getColor(R.color.colorPrimary);
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
            throw new RuntimeException("请确定已经正确的创建了Presenter类,Presenter必须继承自当前页面的Contract中的内部类Presenter");
        }
        return presenterInstance;
    }
    //    获取布局
    protected abstract
    @LayoutRes
    int getLayout();

    protected abstract
    Class<?> getPresenter();

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
