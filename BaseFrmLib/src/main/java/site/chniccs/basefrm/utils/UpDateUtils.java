package site.chniccs.basefrm.utils;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import site.chniccs.basefrm.listener.IOnDialogClickListener;
import site.chniccs.basefrm.widget.UpdateDialog;

/**
 * 更新工具类
 */

public class UpDateUtils {
    private Activity mActivity;
    private String mAppid;
    private static final String apkUrl = "http://olcuw5kr7.bkt.clouddn.com/app-release.apk"; //apk下载地址
    private static final String savePath = getSavaPth(); //apk保存到SD卡的路径
    private static final String saveFileName = savePath + System.currentTimeMillis() + "update.apk"; //完整路径名

    private String serverVersion = ""; //从服务器获取的版本号
    private String clientVersion; //客户端当前的版本号
    private String updateDescription = "更新描述"; //更新内容描述信息
    private boolean forceUpdate = true; //是否强制更新
    private UpdateDialog mUpdateDialog;
    public static final int UPDATE = 1001;
    public static final int DONE = 1002;
    private UpdateHandler mHandler;
    private Subscription mSubscription;


    /**
     * 构造函数
     * @param activity 引用
     * @param appid 传入APPLICATION_ID
     */
    public UpDateUtils(Activity activity, String appid) {
        mActivity = activity;
        mAppid = appid;
    }

    private static String getSavaPth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Environment.getExternalStorageDirectory().getPath() + "/dangjian/";
        } else {
            return Environment.getExternalStorageDirectory().getPath() + "/dangjian/";
        }
    }


    public void updata() {
        showNoticeDialog(false);
    }

    private void showNoticeDialog(boolean force) {
        mUpdateDialog = new UpdateDialog(mActivity, new IOnDialogClickListener() {
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
        });
        mUpdateDialog.setCancelable(false);
        mUpdateDialog.setCanceledOnTouchOutside(false);
        mUpdateDialog.show();

        mHandler = new UpdateHandler(mUpdateDialog);
    }


    float progress = 0;

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
            }

            @Override
            public void onNext(FileInfo fileInfo) {
                progress = (((float) fileInfo.getCurrentSize()) / ((float) fileInfo.getFileSize()));
                int percent = (int) (progress * 100);
                Message message = mHandler.obtainMessage();
                message.what = UPDATE;
                message.obj = percent;
                mHandler.sendMessage(message);
            }
        });

    }

    public static final Observable.Transformer schedulersTransformer =
            new Observable.Transformer<Observable, Observable>() {
                @Override
                public Observable<Observable> call(Observable<Observable> observable) {
                    return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
                }
            };

    /**
     * 应用 Schedulers .方便 compose() 简化代码
     */
    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulersTransformer;
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
                            mUpdateDialogWeakReference.get().setInstallContainerShow();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            return super.getMessageName(message);
        }
    }

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

