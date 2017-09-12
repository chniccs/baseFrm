package site.chniccs.basefrm.utils;

import com.google.gson.Gson;

/**
 * Created by chniccs on 2017/6/26 10:26.
 *
 */

public class GsonUtil {
    public static String toJosn(Object obj){
        Gson gson=new Gson();
        return gson.toJson(obj);
    }
}
