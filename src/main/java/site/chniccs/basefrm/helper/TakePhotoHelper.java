package site.chniccs.basefrm.helper;

import android.net.Uri;
import android.os.Environment;

import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;

import java.io.File;

/**
 * Created by ccs on 16/11/8.
 * takephoto的帮助类
 */

public class TakePhotoHelper {
    public static CompressConfig getHeadFaceConfig(){
        return new CompressConfig.Builder().setMaxSize(50*1024).setMaxPixel(800).create();
    }
    public static CropOptions getHeadFaceCropOptions(){
        CropOptions cropOptions = new CropOptions.Builder().setAspectX(200).setAspectY(200).setWithOwnCrop(false).create();;
        return cropOptions;
    }
    public static CompressConfig getCommentConfig(){
        return new CompressConfig.Builder().setMaxSize(200*1024).setMaxPixel(800).create();
    }
    public static CropOptions getCommentCropOptions(){
        CropOptions cropOptions = new CropOptions.Builder().setAspectX(500).setAspectY(500).setWithOwnCrop(false).create();;
        return cropOptions;
    }
    public static Uri getUri(){
        try {
            File file=new File(Environment.getExternalStorageDirectory(), "/camera_photos/"+ System.currentTimeMillis() + ".jpg");
            if (!file.getParentFile().exists())file.getParentFile().mkdirs();
            return Uri.fromFile(file);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
