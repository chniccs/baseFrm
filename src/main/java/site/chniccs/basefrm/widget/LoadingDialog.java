package site.chniccs.basefrm.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;


import site.chniccs.basefrm.R;
import site.chniccs.basefrm.widget.base.BaseDialogTransparent;
import site.chniccs.basefrm.widget.progress.MaterialProgressDrawable;

/**
 * Created by ccs on 16/9/26.
 * 加载中的dialog
 */
public class LoadingDialog extends BaseDialogTransparent {

    private ImageView mLoadingView;
    private MaterialProgressDrawable mMpd;
    private final int CIRCLE_BG_LIGHT = 0x00FAFAFA;
//    private int[] colors = {
//            0xFFFF0000,0xFFFF7F00,0xFFFFFF00,0xFF00FF00
//            ,0xFF00FFFF,0xFF0000FF,0xFF8B00FF};
    private int[] colors={0xFFFFFFFF};

    public LoadingDialog(Activity context) {
        super(context);
    }

    protected LoadingDialog(Activity context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void canOperationView() {

    }

    @Override
    public void init() {
        mLoadingView = (ImageView) mContent.findViewById(R.id.dialog_anchuloading_iv);
        mMpd = new MaterialProgressDrawable(getContext(),mLoadingView);
        mMpd.setBackgroundColor(CIRCLE_BG_LIGHT);
        mMpd.setColorSchemeColors(colors);
        //设置圈圈的各种大小
        mMpd.updateSizes(MaterialProgressDrawable.LARGE);
        mMpd.setAlpha(255);
        mMpd.setStartEndTrim(0f, 0.8f);
        mMpd.setArrowScale(1f); //0~1之间
        mMpd.setProgressRotation(1);
        mLoadingView.setImageDrawable(mMpd);
    }

    @Override
    public int getChildView() {
        return R.layout.dialog_loading_layout;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void show() {
        super.show();

        mMpd.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mMpd.stop();
    }
}
