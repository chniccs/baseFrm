package site.chniccs.basefrm.base;

import android.content.Intent;
import android.os.Bundle;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;

import site.chniccs.basefrm.listener.ISelectPhotoWayInterface;

/**
 * Created by chenchangsong on 2017/2/21.
 * 图片选择的activity基类
 */

public abstract class BaseTakePhotoActivity<T extends IBasePresenter> extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener, ISelectPhotoWayInterface {

    private static final String TAG = BaseTakePhotoActivity.class.getName();
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    protected T mTPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setPresenter(IBasePresenter presenter) {
        mTPresenter= (T) mPresenter;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

//    @Override
//    public void takeSuccess(TResult result) {
//        Log.i(TAG, "takeSuccess：" + result.getImage().getPath());
//    }

//    @Override
//    public void takeFail(TResult result, String msg) {
//        Log.i(TAG, "takeFail:" + msg);
//    }
//
//    @Override
//    public void takeCancel() {
//        Log.i(TAG, getResources().getString(R.string.msg_operation_canceled));
//    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }
}
