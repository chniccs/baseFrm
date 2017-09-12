package site.chniccs.basefrm.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;


import site.chniccs.basefrm.R;
import site.chniccs.basefrm.listener.ISelectPhotoWayInterface;

/**
 * Created by chniccs on 2017/6/22 9:23.
 * 选择图片拍摄方式
 */

public class SelectPhotoPopwindow {
    private Context mContext;
    private View.OnClickListener mListener;
    private PopupWindow window;
    private ISelectPhotoWayInterface mISelectPhotoWayInterface;


    Activity ac;


    public SelectPhotoPopwindow(Context context, ISelectPhotoWayInterface iSelectPhotoWayInterface) {
        mContext = context;
        ac = (Activity) context;
        mISelectPhotoWayInterface = iSelectPhotoWayInterface;
    }


    /**
     * 显示popupWindow
     */
    public void showPopwindow(View showatview) {
        if (window == null) {


            // 利用layoutInflater获得View
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.pop_select_photo, null);

            // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

            window = new PopupWindow(view,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);

            // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
            window.setFocusable(true);


//        // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xffffff);
            window.setBackgroundDrawable(dw);
            window.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            Button btnTakePhoto = (Button) view.findViewById(R.id.pop_select_photo_by_camera_btn);
            Button btnTakeAlubm = (Button) view.findViewById(R.id.pop_select_photo_by_local_btn);


//        btnTakePhoto.setText(mContext.getString(R.string.take_photo));
//        btnTakeAlubm.setText(mContext.getString(R.string.select_from_album));


            mListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**
                     * 拍照
                     */
                    mISelectPhotoWayInterface.getByCamera();

                }
            };

            btnTakePhoto.setOnClickListener(mListener);

            btnTakeAlubm.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    mISelectPhotoWayInterface.getByLocal();
                                                }
                                            }

            );

            // 设置popWindow的显示和消失动画
            window.setAnimationStyle(R.style.mypopwindow_anim_style);
        }
        // 在底部显示
        window.showAtLocation(showatview,
                Gravity.BOTTOM, 0, 0);
    }
    public void dismiss(){
        if (window != null && window.isShowing()) {
            window.dismiss();
        }
    }

    public void dismissDilaog() {
        if (window != null) {
            window.dismiss();
            window = null;
            mISelectPhotoWayInterface = null;
        }
    }
}
