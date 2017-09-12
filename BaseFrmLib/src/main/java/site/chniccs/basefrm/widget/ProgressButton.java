package site.chniccs.basefrm.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * @项目名: 自定义的进度条
 * @类名: ProgressButton
 */
public class ProgressButton extends AppCompatButton
{
	private boolean		mProgressEnable;
	private Drawable mProgressDrawable;
	private int			mProgress;
	private int			mMax;

	public ProgressButton(Context context) {
		super(context);
	}

	public ProgressButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setProgressEnable(boolean enable)
	{
		this.mProgressEnable = enable;
	}

	public void setProgressDrawable(Drawable drawable)
	{
		this.mProgressDrawable = drawable;
	}

	public void setProgress(int progress)
	{
		this.mProgress = progress;

		// 触发draw
		invalidate();
	}

	public void setMax(int max)
	{
		this.mMax = max;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{

		// 画进度
		if (mProgressEnable && mProgressDrawable != null)
		{
			// 绘制进度条
			int left = 0;
			int top = 0;
			int right = 0;//

			if (mMax == 0)
			{
				mMax = 100;
			}
			right = (int) (getMeasuredWidth() * mProgress * 1f / mMax + 0.5f);

			int bottom = getMeasuredHeight();
			mProgressDrawable.setBounds(left, top, right, bottom);

			mProgressDrawable.draw(canvas);
		}

		// 画文本内容
		super.onDraw(canvas);
	}
}
