package site.chniccs.basefrm.listener;

/**
 * {@link site.chniccs.basefrm.widget.SuperSwipeRefreshLayout} 的FooterView回调接口，如果需要实现自定义的FooterView并且要获取状态回调，就去实现这个接口
 */
public  interface ILoadMoreViewListener
{
    void start();
    void stop();
    void onPushDistance(int pushDistance);
    void onPushEnable(boolean enable);
}
