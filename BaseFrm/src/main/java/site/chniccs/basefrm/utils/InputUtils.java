package site.chniccs.basefrm.utils;

import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by chniccs on 2017/7/31 10:09.
 * 检查输入是否有空的工具
 */

public class InputUtils {
    public static boolean isEmpty(ArrayList<EditText> arrayList){
        boolean isEmpty=false;
        if(arrayList!=null){
            for (EditText editText : arrayList) {
                if(StringUtils.isEmpty(editText)){
                    isEmpty=true;
                    break;
                }
            }
        }
        return isEmpty;
    }
    public static String getInput(EditText et){
        return et.getText().toString().trim();
    }
}
