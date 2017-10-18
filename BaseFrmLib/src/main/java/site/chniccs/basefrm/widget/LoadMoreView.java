package site.chniccs.basefrm.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import site.chniccs.basefrm.R;
import site.chniccs.basefrm.listener.ILoadMoreViewListener;
import site.chniccs.basefrm.widget.progress.MaterialProgressDrawable;

/**
 * Created by chniccs on 2017/8/26 14:10.
 */

public class LoadMoreView extends LinearLayout implements ILoadMoreViewListener {

    private ImageView mLoadingView;
    private MaterialProgressDrawable mMpd;
    private final int CIRCLE_BG_LIGHT = 0x00FAFAFA;
    private int[] colors = {0xFF808080};
    private int tvColor = 0xFF808080;
    private String tvContent = "加载更多...";

    public void setColors(int[] colorArr) {
        colors = colorArr;
    }

    public void setTextColor(int color) {
        tvColor = color;
    }

    public void setTextColor(String content) {
        tvContent = content;
    }

    public LoadMoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreView(Context context) {
        super(context);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.loadmore_layout, this, false);
        mLoadingView = (ImageView) view.findViewById(R.id.loadmore_iv);
        TextView tv = (TextView) view.findViewById(R.id.loadmore_tv);
        tv.setTextColor(tvColor);
        tv.setText(tvContent);
        mMpd = new MaterialProgressDrawable(getContext(), mLoadingView);
//        mMpd.setBackgroundColor(CIRCLE_BG_LIGHT);
//        mMpd.setColorSchemeColors(colors);
//        //设置圈圈的各种大小
//        mMpd.updateSizes(MaterialProgressDrawable.DEFAULT);
//        mMpd.setAlpha(255);
//        mMpd.setStartEndTrim(0f, 0.8f);
//        mMpd.setArrowScale(1f); //0~1之间
//        mMpd.setProgressRotation(1);
        mLoadingView.setImageDrawable(mMpd);
        initSet();
        addView(view);
    }
    @Override
    public void start() {
        mMpd.start();
    }
    @Override
    public void stop() {
        mMpd.stop();
        initSet();
    }

    @Override
    public void onPushDistance(int pushDistance) {

    }

    @Override
    public void onPushEnable(boolean enable) {

    }

    private void initSet(){
        mMpd.setBackgroundColor(CIRCLE_BG_LIGHT);
        mMpd.setColorSchemeColors(colors);
        //设置圈圈的各种大小
        mMpd.updateSizes(MaterialProgressDrawable.DEFAULT);
        mMpd.setAlpha(255);
        mMpd.setStartEndTrim(0f, 0.8f);
        mMpd.setArrowScale(1f); //0~1之间
        mMpd.setProgressRotation(1);
    }
}
