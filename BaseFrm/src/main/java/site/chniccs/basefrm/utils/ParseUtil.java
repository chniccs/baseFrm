package site.chniccs.basefrm.utils;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by chenchangsong on 2017/2/27.
 */

public class ParseUtil<E> {

    private final Gson mGson;

    public ParseUtil() {
        mGson = new Gson();
    }

    public E parse(String result, Class<?> cls) {
        try {
            return (E) mGson.fromJson(result, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
