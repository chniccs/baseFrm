package site.chniccs.basefrm.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by chenchangsong on 2017/3/20.
 *
 */

public class UriUtil {
    public static void openUrl(String url, Activity activity){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        activity.startActivity(intent);
    }
}
