package site.chniccs.basefrm.base;

/**
 * Created by ccs on 17/1/6.
 */

public interface IBasePresenter {
    /**
     * 订阅，可以开始进行一些操作了，如数据获取
     */
    void subscribe();

    /**
     * 进行释放操作
     */
    void unsubscribe();

    /**
     * 可以进行界面上的操作了
     */
    void start();



}
