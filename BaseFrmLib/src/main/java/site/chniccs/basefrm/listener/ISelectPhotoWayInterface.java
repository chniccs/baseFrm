package site.chniccs.basefrm.listener;

/**
 * Created by chenchangsong on 2017/2/21.
 * 选择获取照片的方式
 */

public interface ISelectPhotoWayInterface {
    /**
     * 通过相机获得
     */
    void getByCamera();

    /**
     * 通过本地文件获得
     */
    void getByLocal();
}
