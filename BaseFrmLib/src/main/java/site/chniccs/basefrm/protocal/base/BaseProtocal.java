package site.chniccs.basefrm.protocal.base;

import android.app.Application;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import site.chniccs.basefrm.interceptor.BaseInterceptor;
import site.chniccs.basefrm.utils.CookiesManager;

public abstract class BaseProtocal<T> {
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static OkHttpClient okHttpClient;
    private static OkHttpClient mInterceptorClient = new OkHttpClient();
    private static OkHttpClient mCookiesClient = new OkHttpClient();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    private static BaseInterceptor mInterceptor;
    private T api;
    private T apiHasInterceptor;
    private T apiWithCookies;


    abstract String getBaseUrl();
    //设置拦截器
    public static void setInterceptor(BaseInterceptor interceptor) {
        mInterceptor = interceptor;
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
                        .connectTimeout(1500, TimeUnit.MILLISECONDS)
                        .retryOnConnectionFailure(true)
                        .build();
                this.api = new Retrofit.Builder().client(okHttpClient)
                        .client(okHttpClient)
                        .baseUrl(getBaseUrl())
                        .addConverterFactory(gsonConverterFactory)
                        .addCallAdapterFactory(rxJavaCallAdapterFactory)
                        .build()
                        .create(getApiClass());
            }

        }
        return (T) this.api;
//        if (this.api == null) {
//            this.api = new Retrofit.Builder().client(okHttpClient)
//                    .baseUrl(getBaseUrl())
//                    .addConverterFactory(gsonConverterFactory)
//                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
//                    .build()
//                    .create(getApiClass());
//        }
//        return (T) this.api;
    }

    /**
     * 添加带有拦截器的网络协议类
     *
     * @param baseInterceptor 拦截器
     * @return 网络协议类
     */
    public T getApi(Interceptor baseInterceptor) {
        if (this.apiHasInterceptor == null) {
            OkHttpClient.Builder builder = mInterceptorClient.newBuilder().addInterceptor(baseInterceptor);
            this.apiHasInterceptor = new Retrofit.Builder().client(builder.build())
                    .baseUrl(getBaseUrl())
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build()
                    .create(getApiClass());
        }
        return (T) this.apiHasInterceptor;
    }

    /**
     * 添加带cookie持久化功能的网络协议类
     *
     * @param application 应用
     * @return 网络协议类
     */
    public T getApiWithCookie(@NonNull Application application) {
        if (this.apiWithCookies == null) {
//            加入cookie管理
            OkHttpClient.Builder builder = mCookiesClient.newBuilder();
            builder.cookieJar(new CookiesManager(application));
            this.apiWithCookies = new Retrofit.Builder().client(builder.build())
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
