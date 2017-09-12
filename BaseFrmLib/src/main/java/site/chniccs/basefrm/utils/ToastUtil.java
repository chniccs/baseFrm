package site.chniccs.basefrm.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by chniccs on 2017/6/19 13:48.
 */

public class ToastUtil {
    public static void show(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
