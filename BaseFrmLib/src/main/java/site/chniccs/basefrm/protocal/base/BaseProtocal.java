package site.chniccs.basefrm.protocal.base;

import android.app.Application;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import site.chniccs.basefrm.interceptor.BaseInterceptor;
import site.chniccs.basefrm.utils.CookiesManager;

public abstract class BaseProtocal<T> {
    private static int TIME_OUT = 15000;
    private static boolean RETRY_ON_FAILED = true;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static OkHttpClient okHttpClient;
    private static OkHttpClient mCookiesClient;
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    private static BaseInterceptor mInterceptor;
    private static BaseInterceptor mCookieInterceptor;
    private T api;
    private T apiWithCookies;

    public static boolean isRetryOnFailed() {
        return RETRY_ON_FAILED;
    }

    public static void setRetryOnFailed(boolean retryOnFailed) {
        RETRY_ON_FAILED = retryOnFailed;
    }

    public static int getTimeOut() {
        return TIME_OUT;
    }

    public static void setTimeOut(int timeOut) {
        TIME_OUT = timeOut;
    }

    abstract String getBaseUrl();

    //设置拦截器
    public static void setInterceptor(BaseInterceptor interceptor) {
        mInterceptor = interceptor;
    }

    public static void setCookieInterceptor(BaseInterceptor interceptor) {
        mCookieInterceptor = interceptor;
    }

    public T getApi() {
        if (this.api == null) {
//            添加通用拦截器，用于添加参数
            if (okHttpClient == null) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                if (mInterceptor != null) {
                    builder.addInterceptor(mInterceptor);
                }
                okHttpClient = builder
                        .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                        .retryOnConnectionFailure(RETRY_ON_FAILED)
                        .build();
            }
            this.api = new Retrofit.Builder().client(okHttpClient)
                    .client(okHttpClient)
                    .baseUrl(getBaseUrl())
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build()
                    .create(getApiClass());

        }
        return (T) this.api;
    }

    /**
     * 添加带cookie持久化功能的网络协议类
     *
     * @param application 应用
     * @return 网络协议类
     */
    public T getApiWithCookie(@NonNull Application application) {
        if (this.apiWithCookies == null) {
            if (mCookiesClient == null) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                if (mCookieInterceptor != null) {
                    builder.addInterceptor(mCookieInterceptor);
                }
                mCookiesClient = builder
                        .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                        .retryOnConnectionFailure(RETRY_ON_FAILED)
                        .cookieJar(new CookiesManager(application))// 加入cookie管理
                        .build();
            }
            this.apiWithCookies = new Retrofit.Builder().client(mCookiesClient)
                    .baseUrl(getBaseUrl())
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build()
                    .create(getApiClass());
        }
        return (T) this.apiWithCookies;
    }

    protected abstract Class<T> getApiClass();
}
