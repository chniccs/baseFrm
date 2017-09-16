package site.chniccs.basefrm.listener;

import java.util.LinkedHashMap;

/**
 * Created by chniccs on 2017/9/2 16:18.
 */

public interface IInputDialogClickListener {
    void onAgree(LinkedHashMap<String, String> inputs);
    void onDisagree();
}
