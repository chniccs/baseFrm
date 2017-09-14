package site.chniccs.basefrm.widget;

import android.app.Activity;

import site.chniccs.basefrm.listener.IDownloadListener;
import site.chniccs.basefrm.listener.IOnUpdateDialogClickListener;
import site.chniccs.basefrm.widget.base.BaseDialog;

/**
 * Created by chniccs on 2017/9/14 16:22.
 * 基类的或下载dialog
 */

public abstract class UpdateDialog extends BaseDialog implements IDownloadListener {
    protected IOnUpdateDialogClickListener mIOnUpdateDialogClickListener;

    protected UpdateDialog(Activity context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public UpdateDialog(Activity context) {
        super(context);
    }

    public void setIOnUpdateDialogClickListener(IOnUpdateDialogClickListener listener) {
        mIOnUpdateDialogClickListener = listener;
    }

}
