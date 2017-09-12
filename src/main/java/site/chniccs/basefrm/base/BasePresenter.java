package site.chniccs.basefrm.base;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ccs on 17/1/6.
 *
 */

public abstract class BasePresenter<T extends IBaseView> implements IBasePresenter {
    public T mView;
    protected BaseActivity mContext;
    @NonNull
    protected CompositeSubscription mSubscriptions;
    public BasePresenter(IBaseView view){
        mView= (T) view;
        mSubscriptions=new CompositeSubscription();
        if ((view instanceof Fragment)) {
            this.mContext = (BaseActivity) ((Fragment)view).getActivity();
        }
        if ((view instanceof Activity)) {
            this.mContext = ((BaseActivity)view);
        }
    }
    protected void addSubscription(Subscription s){
        mSubscriptions.add(s);
    }
    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void start() {

    }
}
