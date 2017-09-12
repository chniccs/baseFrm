package site.chniccs.basefrm.interceptor;


import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import site.chniccs.basefrm.utils.StringUtils;

/**
 * Created by chniccs on 2017/9/7 10:15.
 * 党建的通用拦截器
 */

public class DjInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (request.method().equals("GET")) {
            //添加公共参数
            HttpUrl.Builder builder = request.url()
                    .newBuilder();
//            if (!StringUtils.isEmpty(App.getToken())) {
//                builder.addQueryParameter("token", App.getToken());
//            }
            HttpUrl httpUrl = builder
                    .addQueryParameter("time", System.currentTimeMillis() + "")
                    .addQueryParameter("from", "app")
                    .build();
            request = request.newBuilder().url(httpUrl).build();

        } else if (request.method().equals("POST")) {

            if (request.body() instanceof FormBody) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                FormBody formBody = (FormBody) request.body();

                //把原来的参数添加到新的构造器，（因为没找到直接添加，所以就new新的）
                for (int i = 0; i < formBody.size(); i++) {
                    bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                }
//                if (!StringUtils.isEmpty(App.getToken())) {
//                    bodyBuilder.addEncoded("token", App.getToken());
//                }
                formBody = bodyBuilder
                        .addEncoded("from", "app")
                        .addEncoded("time", System.currentTimeMillis() + "")
                        .build();
                request = request.newBuilder().post(formBody).build();
            }
        }
        return chain.proceed(request);
    }
}
