package com.jerry.android.uimotion.switcher;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jerry.android.uimotion.R;
import com.jerry.android.uimotion.utils.DisplayUtil;

/**
 * 自定义开关
 * <p>
 * Created by Jerry on 2017/4/11.
 */

public class SwitcherView extends View implements View.OnClickListener
{
	public SwitcherView(Context context)
	{
		this(context, null);
	}

	public SwitcherView(Context context, @Nullable AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	private int textSize, borderWidth;
	private int bgColor, fgColor, borderColor, textColorDefault, textColorSelected;
	private String leftText, rightText;

	private float selectorOffset = 0;
	private ValueAnimator selectorOffsetAnim;

	private RectF contentRect = new RectF(), borderRect = new RectF();
	private Paint textPaint, borderPaint, bgPaint, fgPaint;

	private OnCheckedStateChangedListener listener;
	private boolean isLeft = true;

	public SwitcherView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);

		if (attrs != null)
		{
			TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitcherView);
			textSize = typedArray.getDimensionPixelSize(R.styleable.SwitcherView_switcher_text_size, DisplayUtil.sp2px(context, 14));
			borderWidth = typedArray.getDimensionPixelSize(R.styleable.SwitcherView_switcher_border_width, DisplayUtil.dip2px(context, 1));
			bgColor = typedArray.getColor(R.styleable.SwitcherView_switcher_bg_color, 0xFFF7F7F7);
			fgColor = typedArray.getColor(R.styleable.SwitcherView_switcher_fg_color, 0xFF338BF8);
			borderColor = typedArray.getColor(R.styleable.SwitcherView_switcher_border_color, 0xFFEFF2F7);
			textColorDefault = typedArray.getColor(R.styleable.SwitcherView_switcher_text_color_default, 0xFF39424A);
			textColorSelected = typedArray.getColor(R.styleable.SwitcherView_switcher_text_color_selected, 0xFFFFFFFF);
			leftText = typedArray.getString(R.styleable.SwitcherView_switcher_left_text);
			if (leftText == null)
			{
				leftText = "    ";
			}
			rightText = typedArray.getString(R.styleable.SwitcherView_switcher_right_text);
			if (rightText == null)
			{
				rightText = "    ";
			}
			typedArray.recycle();
		}

		initPaints();

		this.setOnClickListener(this);
	}

	private void initAnim()
	{
		selectorOffsetAnim = ValueAnimator.ofFloat(0, contentRect.width() / 2f);
		selectorOffsetAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				selectorOffset = (float) animation.getAnimatedValue();
				invalidate();
			}
		});
	}

	private void initPaints()
	{
		textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(textSize);
		textPaint.setColor(textColorDefault);

		borderPaint = new Paint();
		borderPaint.setAntiAlias(true);
		borderPaint.setStrokeWidth(borderWidth);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setColor(borderColor);

		bgPaint = new Paint();
		bgPaint.setAntiAlias(true);
		bgPaint.setStyle(Paint.Style.FILL);
		bgPaint.setColor(bgColor);

		fgPaint = new Paint(bgPaint);
		fgPaint.setColor(fgColor);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.drawRoundRect(contentRect, contentRect.height() / 2, contentRect.height() / 2, bgPaint);
		canvas.drawRoundRect(contentRect.left + selectorOffset, contentRect.top, contentRect.centerX() + selectorOffset, contentRect.bottom, contentRect.height() / 2, contentRect.height() / 2, fgPaint);
		canvas.drawRoundRect(borderRect, borderRect.height() / 2, borderRect.height() / 2, borderPaint);

		Paint.FontMetrics fm = textPaint.getFontMetrics();
		int textOffsetY = (int) Math.ceil(-fm.descent - fm.ascent) / 2;
		textPaint.setColor(argbEvaluate(selectorOffset / contentRect.width() * 2f, textColorSelected, textColorDefault));
		canvas.drawText(leftText, contentRect.left + contentRect.width() / 4 - textPaint.measureText(leftText) / 2, contentRect.centerY() + textOffsetY, textPaint);
		textPaint.setColor(textColorDefault);

		textPaint.setColor(argbEvaluate(1 - selectorOffset / contentRect.width() * 2f, textColorSelected, textColorDefault));
		canvas.drawText(rightText, contentRect.left + contentRect.width() * 3 / 4 - textPaint.measureText(rightText) / 2, contentRect.centerY() + textOffsetY, textPaint);
		textPaint.setColor(textColorDefault);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int wholeWidth = (int) (Math.max(textPaint.measureText(leftText), textPaint.measureText(rightText)) * 2 + textSize * 3) + borderWidth;
		Paint.FontMetrics fm = textPaint.getFontMetrics();
		int textHeight = (int) Math.ceil(fm.descent - fm.ascent);
		int wholeHeight = (int) (textHeight * 1.6f) + borderWidth;

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int viewWidth;
		int viewHeight;

		//Measure Width
		if (widthMode == MeasureSpec.EXACTLY)
		{
			//Must be this size
			viewWidth = widthSize;
		}
		else if (widthMode == MeasureSpec.AT_MOST)
		{
			//Can't be bigger than...
			viewWidth = Math.min(wholeWidth, widthSize);
		}
		else
		{
			//Be whatever you want
			viewWidth = wholeWidth;
		}

		//Measure Height
		if (heightMode == MeasureSpec.EXACTLY)
		{
			//Must be this size
			viewHeight = heightSize;
		}
		else if (heightMode == MeasureSpec.AT_MOST)
		{
			//Can't be bigger than...
			viewHeight = Math.min(wholeHeight, heightSize);
		}
		else
		{
			//Be whatever you want
			viewHeight = wholeHeight;
		}

		//MUST CALL THIS
		setMeasuredDimension(viewWidth, viewHeight);

		borderRect.set(viewWidth / 2 - wholeWidth / 2, viewHeight / 2 - wholeHeight / 2, viewWidth / 2 + wholeWidth / 2, viewHeight / 2 + wholeHeight / 2);
		borderRect.inset(borderWidth / 2, borderWidth / 2);
		// 将边框宽度去除
		contentRect.set(borderRect);
		contentRect.inset(borderWidth / 2, borderWidth / 2);
		initAnim();
	}

	@Override
	public void onClick(View v)
	{
		if (isLeft)
		{
			selectorOffsetAnim.setFloatValues((float) selectorOffsetAnim.getAnimatedValue(), contentRect.width() / 2f);
			selectorOffsetAnim.start();
		}
		else
		{
			selectorOffsetAnim.setFloatValues((float) selectorOffsetAnim.getAnimatedValue(), 0f);
			selectorOffsetAnim.start();
		}

		isLeft = !isLeft;
		if (listener != null)
		{
			listener.onStateChanged(isLeft, isLeft ? leftText : rightText);
		}
	}

	/**
	 * 颜色估值器
	 *
	 * @param fraction   当前颜色变化进度
	 * @param startValue 开始颜色
	 * @param endValue   终止颜色
	 * @return 中间颜色
	 */
	public int argbEvaluate(float fraction, int startValue, int endValue)
	{
		int startA = (startValue >> 24) & 0xff;
		int startR = (startValue >> 16) & 0xff;
		int startG = (startValue >> 8) & 0xff;
		int startB = startValue & 0xff;

		int endA = (endValue >> 24) & 0xff;
		int endR = (endValue >> 16) & 0xff;
		int endG = (endValue >> 8) & 0xff;
		int endB = endValue & 0xff;

		return (startA + (int) (fraction * (endA - startA))) << 24 | (startR + (int) (fraction * (endR - startR))) << 16 | (startG + (int) (fraction * (endG - startG))) << 8 | (startB + (int) (fraction * (endB - startB)));
	}

	public void setOnCheckedStateChangedListener(OnCheckedStateChangedListener listener)
	{
		this.listener = listener;
	}

	/**
	 * 开关状态改变监听
	 */
	interface OnCheckedStateChangedListener
	{
		void onStateChanged(boolean isLeftChecked, String checkedStr);
	}
}
