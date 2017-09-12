package site.chniccs.basefrm.widget.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import site.chniccs.basefrm.R;


/**
 * Created by chenchangsong on 16/9/2.
 * dialog的基类
 */
public abstract class BaseDialog extends Dialog implements View.OnClickListener {

    private Activity mContext;
    protected RelativeLayout mContainer;
    /**
     * 透明背景
     */
    public int TARNSPARENT_STYLE=R.style.content_dialog_transparent;
    public View mContent;

    public BaseDialog(Activity context) {
        super(context, R.style.content_dialog);//自定义的dialog样式
        mContext = context;

    }
    protected BaseDialog(Activity context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    protected boolean needTransparent() {
        return false;
    }

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mContainer = (RelativeLayout)inflater.inflate(R.layout.base_dialog_layout,null);
        mContent = inflater.inflate(getChildView(), mContainer);
//        mContainer.addView(mContent);
        setContentView(mContainer);

        mContainer.post(new Runnable() {
            @Override
            public void run() {
                canOperationView();
            }
        });
        init();
        setWidthAndAnim();


    }

    /**
     * 设置宽度及显示时的动画
     */
    protected void setWidthAndAnim(){
        Window dialogWindow = getWindow();
        dialogWindow.setWindowAnimations(R.style.dialog_none_animation);//设置动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 1.0); // 高度设置为屏幕的宽度
        dialogWindow.setAttributes(lp);
    }

    /**
     * 可以操作ui了
     */
    protected abstract void canOperationView();

    /**
     * 初始化操作
     */
    public abstract void init();


    public abstract int getChildView() ;

    public void destory(){
        if(mContext!=null){
            mContext=null;
        }
    }
}
