package site.chniccs.basefrm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import site.chniccs.basefrm.R;


/**
 *
 * @类名:	RatioLayout
 * @创建者:	陈长松
 * @创建时间:	2015年10月16日	上午9:05:44
 * @描述: 限制显示视图的宽高比的控件
 *
 */
public class RatioLayout extends FrameLayout
{
	private final static int	RELATIVE_WIDTH	= 0;
	private final static int	RELATIVE_HEIGHT	= 1;

	private float				mRatio;
	private int					mRelative		= RELATIVE_WIDTH;

	public RatioLayout(Context context) {
		this(context, null);
	}

	public RatioLayout(Context context, AttributeSet set) {
		super(context, set);

		TypedArray ta = context.obtainStyledAttributes(set, R.styleable.RatioLayout);
		mRatio = ta.getFloat(R.styleable.RatioLayout_ratio, 0);
		mRelative = ta.getInt(R.styleable.RatioLayout_relative, RELATIVE_WIDTH);
		ta.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{

		// 获得宽度值
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);

		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightsMode = MeasureSpec.getMode(heightMeasureSpec);

		// 测量孩子
		// 1.已知宽高比，宽度，计算高度
		if (widthMode == MeasureSpec.EXACTLY && mRatio != 0 && mRelative == RELATIVE_WIDTH)
		{
			// 获得孩子的宽度
			int width = widthSize - getPaddingLeft() - getPaddingRight();

			// 通过宽度计算高度
			int height = (int) (width / mRatio + 0.5f);

			// 期望孩子的宽度和高度
			measureChildren(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
							MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));

			// 设置自己的高度
			setMeasuredDimension(widthSize, height + getPaddingTop() + getPaddingBottom());
		}

		else if (heightsMode == MeasureSpec.EXACTLY && mRatio != 0 && mRelative == RELATIVE_HEIGHT)
		{
			// 2.已知宽高比，高度，计算宽度
			int height = heightSize - getPaddingTop() - getPaddingBottom();

			// 根据高度算宽度
			int width = (int) (height * mRatio + 0.5f);

			// 期望孩子的宽度和高度
			measureChildren(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
							MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));

			// 设置自己的高度
			setMeasuredDimension(width + getPaddingLeft() + getPaddingRight(), heightSize);
		}

		else
		{
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	public void reMeasure(int widthMeasureSpec,int heightMeasureSpec) {
		measure(widthMeasureSpec,heightMeasureSpec);
	}
	public void setRatio(float ratio){
		mRatio=ratio;
	}
}
