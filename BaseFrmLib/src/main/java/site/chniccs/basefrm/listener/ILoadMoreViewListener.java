package site.chniccs.basefrm.listener;

public  interface ILoadMoreViewListener
{
    void start();
    void stop();
    void onPushDistance(int pushDistance);
    void onPushEnable(boolean enable);
}
