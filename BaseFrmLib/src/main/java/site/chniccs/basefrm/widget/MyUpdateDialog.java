package site.chniccs.basefrm.widget;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import site.chniccs.basefrm.R;
import site.chniccs.basefrm.listener.IOnUpdateDialogClickListener;
import site.chniccs.basefrm.widget.base.BaseDialog;

/**
 * Created by chniccs on 2017/9/2 15:57.
 */

public class MyUpdateDialog extends UpdateDialog {
    private LinearLayout mBtnContainer;
    private LinearLayout mBtnProgressContainer;
    private LinearLayout mBtnInstallContainer;
    private ProgressButton mBtnProgress;

    public MyUpdateDialog(Activity context) {
        super(context);
    }

    @Override
    protected void canOperationView() {
    }


    @Override
    public void init() {
        TextView tvAgree = (TextView) mContent.findViewById(R.id.agree);
        TextView tvDisagree = (TextView) mContent.findViewById(R.id.disagree);
        mBtnContainer = (LinearLayout) mContent.findViewById(R.id.ll_btn_contianer);
        mBtnProgressContainer = (LinearLayout) mContent.findViewById(R.id.ll_btn_progress);
        mBtnProgress = (ProgressButton) mContent.findViewById(R.id.btn_progress);
        mBtnProgress.setProgressDrawable(new ColorDrawable(Color.parseColor("#F35F33")));
        mBtnProgress.setProgressEnable(true);
        mBtnInstallContainer = (LinearLayout) mContent.findViewById(R.id.ll_install_container);
        tvAgree.setOnClickListener(this);
        tvDisagree.setOnClickListener(this);
        mBtnInstallContainer.setOnClickListener(this);
    }

    @Override
    public int getChildView() {
        return R.layout.dialog_update;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.agree) {
            mIOnUpdateDialogClickListener.onAgree();
            mBtnProgressContainer.setVisibility(View.VISIBLE);
            mBtnContainer.setVisibility(View.GONE);
        } else if (id == R.id.disagree) {
            mIOnUpdateDialogClickListener.onDisagree();
        } else if (id == R.id.ll_install_container) {
            mIOnUpdateDialogClickListener.toInstall();
        }
    }

    public void setBtnContainerHide() {
        mBtnContainer.setVisibility(View.GONE);
    }


    @Override
    public void setProgress(int progress) {
        mBtnProgress.setProgress(progress);
        mBtnProgress.setText(progress + "%");
    }

    @Override
    public void downloadFailed() {

    }

    @Override
    public void downloadSuccess() {
        mBtnInstallContainer.setVisibility(View.VISIBLE);
    }
}
