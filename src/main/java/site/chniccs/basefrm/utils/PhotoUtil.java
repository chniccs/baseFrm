package site.chniccs.basefrm.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;


import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by chenchangsong on 2017/2/21.
 */

public class PhotoUtil {
    public static Bitmap getBitmapFromUri(Uri uri, Context mContext) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getBitmapFromFile(File file, Context context) {
        // 设置参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
        BitmapFactory.decodeFile(file.getPath(), options);
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1; // 默认像素压缩比例，压缩为原图的1/2
        int minLen = Math.min(height, width); // 原图的最小边长
        if (minLen > 600) { // 如果原始图像的最小边长大于800px（此处单位我认为是dp，而非px）
            float ratio = (float) minLen / 800.0f; // 计算像素压缩比例
            inSampleSize = (int) ratio;
        }
        options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
        options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
//        options.inPreferredConfig=Bitmap.Config.RGB_565;
        Bitmap bm = BitmapFactory.decodeFile(file.getPath(), options); // 解码文件
        return bm;
    }
    public static Bitmap getBitmapFromFile(File file,int maxLen) {
        // 设置参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
        BitmapFactory.decodeFile(file.getPath(), options);
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1; // 默认像素压缩比例，压缩为原图的1/2
        int minLen = Math.min(height, width); // 原图的最小边长
        if (minLen > maxLen) { // 如果原始图像的最小边长大于maxLen px（此处单位我认为是dp，而非px）
            float ratio = (float) minLen / (maxLen-1.0f+1); // 计算像素压缩比例
            inSampleSize = (int) ratio;
        }
        options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
        options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
//        options.inPreferredConfig=Bitmap.Config.RGB_565;
        Bitmap bm = BitmapFactory.decodeFile(file.getPath(), options); // 解码文件
        return bm;
    }
    public static String toByte(Bitmap bmp){
        if(bmp==null){
            return null;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);//把bitmap100%高质量压缩 到 output对象里
        bmp.recycle();//自由选择是否进行回收
        byte[] result = output.toByteArray();//转换成功了
        String encode = Base64.encodeToString(result,Base64.DEFAULT);
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encode;
    }
    public static String toByte(Bitmap bmp, boolean recycle){
        if(bmp==null){
            return null;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);//把bitmap100%高质量压缩 到 output对象里
        if(recycle){
            bmp.recycle();//自由选择是否进行回收
        }
        byte[] result = output.toByteArray();//转换成功了
        String encode = Base64.encodeToString(result,Base64.DEFAULT);
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encode;
    }
    public static String toByte(Bitmap bmp, boolean recycle,int pz){
        if(bmp==null){
            return null;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
        bmp.compress(Bitmap.CompressFormat.JPEG, pz, output);//把bitmap100%高质量压缩 到 output对象里
        if(recycle){
            bmp.recycle();//自由选择是否进行回收
        }
        byte[] result = output.toByteArray();//转换成功了
        String encode = Base64.encodeToString(result,Base64.DEFAULT);
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encode;
    }
    /**
     * base64转为bitmap
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
