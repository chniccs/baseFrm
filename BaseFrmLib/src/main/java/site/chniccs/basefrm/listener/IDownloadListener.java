package site.chniccs.basefrm.listener;

/**
 * Created by chniccs on 2017/9/14 16:24.
 * 下载的监听器
 */

public interface IDownloadListener {
    void setProgress(int progress);
    void downloadFailed();
    void downloadSuccess();
}
