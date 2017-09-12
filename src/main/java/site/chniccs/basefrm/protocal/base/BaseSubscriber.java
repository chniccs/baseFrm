package site.chniccs.basefrm.protocal.base;


import java.net.SocketTimeoutException;

import rx.Subscriber;
import site.chniccs.basefrm.base.BaseActivity;
import site.chniccs.basefrm.utils.NetUtils;
import site.chniccs.basefrm.utils.StringUtils;
import site.chniccs.basefrm.utils.ToastUtil;

/**
 * Created by chniccs on 2017/9/4 16:01.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {

    public BaseActivity context;

    public BaseSubscriber(BaseActivity context) {
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!NetUtils.hasNetwork(context)) {
            //抛出异常，中断执行
            ToastUtil.show(context, "当前网络不可用，请检查网络情况");
            this.unsubscribe();
            return;
        }
        // 显示进度条
        context.shodLoading();
    }

    @Override
    public void onError(Throwable e) {
        String msg = "网络异常";
        if (e instanceof SocketTimeoutException) {
            msg = "网络连接超时";
            ToastUtil.show(context, msg);
        }else {
            ToastUtil.show(context, StringUtils.isEmpty(e.getMessage()) ? msg : e.getMessage());
        }

        e.printStackTrace();
        //关闭等待进度条
        context.dismissLoading();
    }

    @Override
    public void onCompleted() {
        //关闭等待进度条
        context.dismissLoading();

    }


}
