package site.chniccs.basefrm.interceptor;


import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by chniccs on 2017/9/7 10:15.
 * 通用拦截器
 */

public abstract class BaseInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        LinkedHashMap<String, String> paramsMap = setCommonParams();
        if (request.method().equals("GET")) {
            //添加公共参数
            HttpUrl.Builder builder = request.url().newBuilder();
            if (paramsMap != null) {
                for (Map.Entry<String, String> stringStringEntry : paramsMap.entrySet()) {
                    builder.addQueryParameter(stringStringEntry.getKey(), stringStringEntry.getValue());
                }
            }
            HttpUrl httpUrl = builder.build();
            request = request.newBuilder().url(httpUrl).build();
        } else if (request.method().equals("POST")) {
            if (request.body() instanceof FormBody) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                FormBody formBody = (FormBody) request.body();
                //把原来的参数添加到新的构造器，（因为没找到直接添加，所以就new新的）
                for (int i = 0; i < formBody.size(); i++) {
                    bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                }
                if (paramsMap != null) {
                    for (Map.Entry<String, String> stringStringEntry : paramsMap.entrySet()) {
                        bodyBuilder.addEncoded(stringStringEntry.getKey(), stringStringEntry.getValue());
                    }
                }
                formBody = bodyBuilder.build();
                request = request.newBuilder().post(formBody).build();
            }
        }
        return chain.proceed(request);
    }

    /**
     * 设置通用的请求参数
     *
     * @return 参数组成的map
     */
    abstract LinkedHashMap<String, String> setCommonParams();
}
