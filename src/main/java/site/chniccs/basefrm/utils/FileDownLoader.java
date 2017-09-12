package site.chniccs.basefrm.utils;

import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.URLUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import rx.Subscriber;

/**
 * Created by chniccs on 2017/9/2 16:45.
 * 下载者
 */

public class FileDownLoader {
    public static final String APK = "application/vnd.android.package-archive";
    public static final String PDF = "application/pdf";
    public static final String ZIP = "application/zip, application/x-compressed-zip";
    private OkHttpClient mClient;
    private String mPath;

    public FileDownLoader() {
        mClient = new OkHttpClient.Builder().build();
    }

    public void download(@NonNull String url, @Nullable String dest,
                         Subscriber<? super FileInfo> subscriber) throws IOException {
        if (!URLUtil.isNetworkUrl(url)) {
            throw new RuntimeException("invalid url");
        }
        Uri uri = Uri.parse(url);
        String filename = uri.getLastPathSegment();// get fileName
        File root;
        if (!TextUtils.isEmpty(dest)) {
            root = new File(dest);
        } else {
            root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        }
        if (!root.exists()) {
            root.mkdirs();
        }
        File output = new File(root, filename);
        if (output.exists()) {
            output.delete();
        }
        mPath = output.getAbsolutePath();
        Request request = new Request.Builder().url(uri.toString()).build();
        Response response = mClient.newCall(request).execute();

        long contentLength = response.body().contentLength();

        // fill fileInfo
        FileInfo currentFile = new FileInfo();
        currentFile.setFileSize(contentLength);
        currentFile.setFileName(filename);
        currentFile.setFileUrl(url);
        currentFile.setFileType(response.body().contentType().type());

        BufferedSink sink = Okio.buffer(Okio.sink(output));
        Buffer buffer = sink.buffer();

        long total = 0;
        long len;
        int bufferSize = 200 * 1024; //200kb
        long temp=0;

        BufferedSource source = response.body().source();
        while ((len = source.read(buffer, bufferSize)) != -1) {
            sink.emit();
            total += len;
            currentFile.setCurrentSize(total);
            subscriber.onNext(currentFile);// onNext
//            if(total-temp>=1000*1024){
//                subscriber.onNext(currentFile);// onNext
//                temp=total;
//            }
//            LogUtil.d(total);
        }
        source.close();
        sink.close();
        subscriber.onCompleted();
    }
    public String getPath(){
        return mPath;
    }

}

