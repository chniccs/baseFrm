package site.chniccs.basefrm.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import site.chniccs.basefrm.listener.IOnUpdateDialogClickListener;
import site.chniccs.basefrm.widget.MyUpdateDialog;
import site.chniccs.basefrm.widget.UpdateDialog;

/**
 * 更新工具类
 */

public class UpDateUtils implements IOnUpdateDialogClickListener {
    private Activity mActivity;
    private String mAppid;
    private static final String apkUrl = "http://olcuw5kr7.bkt.clouddn.com/app-release.apk"; //apk下载地址
    private static String savePath; //apk保存到SD卡的路径

    private String serverVersion = ""; //从服务器获取的版本号
    private String clientVersion; //客户端当前的版本号
    private String updateDescription = "更新描述"; //更新内容描述信息
    private boolean forceUpdate = true; //是否强制更新
    private UpdateDialog mUpdateDialog;
    public static final int UPDATE = 1001;
    public static final int FAILED = 1002;
    public static final int DONE = 1003;
    private UpdateHandler mHandler;
    private Subscription mSubscription;
    private float mProgress = 0;


    /**
     * 构造函数
     *
     * @param activity 引用
     * @param appid    传入APPLICATION_ID
     * @param dir      自定义下载目录,不传入默认是当前应用的包名
     */
    public UpDateUtils(Activity activity, String appid, String dir) {
        mActivity = activity;
        mAppid = appid;
        savePath = getSavaPth(dir);
    }

    /**
     * 构造函数
     *
     * @param activity 引用
     * @param appid    传入APPLICATION_ID
     */
    public UpDateUtils(Activity activity, String appid) {
        this(activity, appid, null);
    }

    private String getSavaPth(String dirName) {
        if (TextUtils.isEmpty(dirName)) {
            dirName = "/" + mActivity.getPackageName() + "/";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Environment.getExternalStorageDirectory().getPath() + dirName;
        } else {
            return Environment.getExternalStorageDirectory().getPath() + dirName;
        }
    }

    //自定义设置dialog的方法
    public void setDialog(UpdateDialog updateDialog) {
        mUpdateDialog = updateDialog;
    }

    public void updata() {
        showNoticeDialog(false);
    }

    /*--------dialog回调 start----*/
    @Override
    public void onAgree() {
        downloadAPK();
    }

    @Override
    public void onDisagree() {
    }

    @Override
    public void toInstall() {
        installAPK(mActivity);
    }

    /*--------dialog回调 end----*/
    private void showNoticeDialog(boolean force) {
        if (mUpdateDialog == null) {
            mUpdateDialog = new MyUpdateDialog(mActivity);
        }
        mUpdateDialog.setIOnUpdateDialogClickListener(this);
        mUpdateDialog.setCancelable(false);
        mUpdateDialog.setCanceledOnTouchOutside(false);
        mUpdateDialog.show();
        mHandler = new UpdateHandler(mUpdateDialog);
    }


    /**
     * 下载apk的线程
     */
    private void downloadAPK() {

        mSubscription = downloadFile(apkUrl, savePath, new Subscriber<FileInfo>() {
            @Override
            public void onCompleted() {
                LogUtil.d(Thread.currentThread().getName() + "onCompleted ");
                Message message = mHandler.obtainMessage();
                message.what = DONE;
                mHandler.sendMessage(message);
                installAPK(mActivity);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                LogUtil.e(e.getMessage());
                Message message = mHandler.obtainMessage();
                message.what = FAILED;
                mHandler.sendMessage(message);

            }

            @Override
            public void onNext(FileInfo fileInfo) {
                mProgress = (((float) fileInfo.getCurrentSize()) / ((float) fileInfo.getFileSize()));
                int percent = (int) (mProgress * 100);
                Message message = mHandler.obtainMessage();
                message.what = UPDATE;
                message.obj = percent;
                mHandler.sendMessage(message);
            }
        });
    }

    private Subscription downloadFile(final String url, final String dest, Subscriber<FileInfo> subscriber) {
        return Observable.create(new Observable.OnSubscribe<FileInfo>() {
            @Override
            public void call(Subscriber<? super FileInfo> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    try {
                        new FileDownLoader().download(url, dest, subscriber);
                    } catch (IOException e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .onBackpressureBuffer()// 一定要调用onBackpressureBuffer()方法,防止下载过快,导致MissingBackpressureException异常
                .subscribe(subscriber);
    }


    /**
     * 下载完成后自动安装apk
     */
    private void installAPK(Activity activity) {
        Uri uri = Uri.parse(apkUrl);
        String filename = uri.getLastPathSegment();// get fileName
        File apkFile = new File(savePath, filename);
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(activity, mAppid + ".fileprovider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        activity.startActivity(intent);
    }

    public static class UpdateHandler extends Handler {
        WeakReference<UpdateDialog> mUpdateDialogWeakReference;

        public UpdateHandler(UpdateDialog updateDialog) {
            mUpdateDialogWeakReference = new WeakReference<UpdateDialog>(updateDialog);
        }

        @Override
        public String getMessageName(Message message) {

            switch (message.what) {
                case UPDATE:
                    if (mUpdateDialogWeakReference != null) {
                        try {
                            mUpdateDialogWeakReference.get().setProgress((Integer) message.obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case DONE:
                    if (mUpdateDialogWeakReference != null) {
                        try {
                            mUpdateDialogWeakReference.get().downloadSuccess();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case FAILED:
                    if (mUpdateDialogWeakReference != null) {
                        try {
                            mUpdateDialogWeakReference.get().downloadFailed();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            return super.getMessageName(message);
        }
    }

    /**
     * 在activity中最好主动调用
     */
    public void onDestory() {
        mActivity = null;
        if (mUpdateDialog != null) {
            mUpdateDialog.dismiss();
            mUpdateDialog = null;
        }
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

}

