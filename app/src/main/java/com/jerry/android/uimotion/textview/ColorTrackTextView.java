package com.jerry.android.uimotion.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jerry.android.uimotion.R;
import com.jerry.android.uimotion.utils.DisplayUtil;

/**
 * 颜色变化TextView
 * <p>
 * Created by Jerry on 2017/4/18.
 */

public class ColorTrackTextView extends View
{
	private static final int DIRECTION_LEFT = 0;
	private static final int DIRECTION_RIGHT = 1;
	public static final int DIRECTION_TOP = 2;
	public static final int DIRECTION_BOTTOM = 3;

	private String text = "请设置要显示的文字";
	private int textSize, originTextColor = Color.BLACK, changedTextColor = Color.RED;
	private float changeProgress = 0f;
	private int changeDirection = DIRECTION_LEFT;

	private Paint textPaint = new Paint();
	private Rect textBound = new Rect();

	private Rect textRect = new Rect();
	private Rect originTextRect = new Rect();
	private Rect changedTextRect = new Rect();

	public ColorTrackTextView(Context context)
	{
		this(context, null);
	}

	public ColorTrackTextView(Context context, @Nullable AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public ColorTrackTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);

		if (attrs != null)
		{
			TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);

			text = array.getString(R.styleable.ColorTrackTextView_color_track_text);
			if (text == null)
			{
				text = "请设置要显示的文字";
			}
			textSize = array.getDimensionPixelSize(R.styleable.ColorTrackTextView_color_track_text_size, DisplayUtil.dip2px(context, 16));
			originTextColor = array.getColor(R.styleable.ColorTrackTextView_color_track_origin_text_color, Color.BLACK);
			changedTextColor = array.getColor(R.styleable.ColorTrackTextView_color_track_changed_text_color, Color.RED);
			changeProgress = array.getFraction(R.styleable.ColorTrackTextView_color_track_change_progress, 1, 1, 0f);
			changeDirection = array.getInt(R.styleable.ColorTrackTextView_color_track_change_direction, DIRECTION_LEFT);

			array.recycle();
		}

		textPaint.setAntiAlias(true);
		textPaint.setTextSize(textSize);
		textPaint.setColor(originTextColor);

		textPaint.getTextBounds(text, 0, text.length(), textBound);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int viewWidth;
		int viewHeight;

		// 将范围扩大来解决canvas.drawText偏右的问题
		textBound.inset(-textBound.left, 0);

		//Measure Width
		if (widthMode == MeasureSpec.EXACTLY)
		{
			//Must be this size
			viewWidth = widthSize;
		}
		else if (widthMode == MeasureSpec.AT_MOST)
		{
			//Can't be bigger than...
			viewWidth = Math.min(textBound.width(), widthSize);
		}
		else
		{
			//Be whatever you want
			viewWidth = textBound.width();
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
			viewHeight = Math.min(textBound.height(), heightSize);
		}
		else
		{
			//Be whatever you want
			viewHeight = textBound.height();
		}

		//MUST CALL THIS
		setMeasuredDimension(viewWidth, viewHeight);

		textRect.set(textBound);
		textRect.offsetTo((int) (viewWidth / 2f - textBound.width() / 2f), (int) (viewHeight / 2f - textBound.height() / 2f));
	}

	public void setChangeProgress(float changeProgress)
	{
		this.changeProgress = changeProgress;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		switch (changeDirection)
		{
			case DIRECTION_LEFT:
				originTextRect.set((int) (textRect.left + changeProgress * textRect.width()), textRect.top, textRect.right, textRect.bottom);
				drawClipText(canvas, originTextRect, originTextColor);
				changedTextRect.set(textRect.left, textRect.top, (int) (textRect.width() * changeProgress), textRect.bottom);
				drawClipText(canvas, changedTextRect, changedTextColor);
				break;

			case DIRECTION_RIGHT:
				originTextRect.set(textRect.left, textRect.top, (int) (textRect.right - textRect.width() * changeProgress), textRect.bottom);
				drawClipText(canvas, originTextRect, originTextColor);
				changedTextRect.set((int) (textRect.right - textRect.width() * changeProgress), textRect.top, textRect.right, textRect.bottom);
				drawClipText(canvas, changedTextRect, changedTextColor);
				break;

			case DIRECTION_TOP:
				originTextRect.set(textRect.left, (int) (textRect.top + textRect.height() * changeProgress), textRect.right, textRect.bottom);
				drawClipText(canvas, originTextRect, originTextColor);
				changedTextRect.set(textRect.left, textRect.top, textRect.right, (int) (textRect.height() * changeProgress));
				drawClipText(canvas, changedTextRect, changedTextColor);
				break;

			case DIRECTION_BOTTOM:
				originTextRect.set(textRect.left, textRect.top, textRect.right, (int) (textRect.bottom - textRect.height() * changeProgress));
				drawClipText(canvas, originTextRect, originTextColor);
				changedTextRect.set(textRect.left, (int) (textRect.bottom - textRect.height() * changeProgress), textRect.right, textRect.bottom);
				drawClipText(canvas, changedTextRect, changedTextColor);
				break;

			default:
				break;
		}
	}

	private void drawClipText(Canvas canvas, Rect clipRect, int textColor)
	{
		Paint.FontMetrics fm = textPaint.getFontMetrics();
		int textOffsetY = (int) Math.ceil(-fm.descent - fm.ascent) / 2;
		textPaint.setColor(textColor);
		canvas.save(Canvas.CLIP_SAVE_FLAG);
		canvas.clipRect(clipRect);
		canvas.drawText(text, textRect.left, textRect.centerY() + textOffsetY, textPaint);
		canvas.restore();
	}

	public float getChangeProgress()
	{
		return changeProgress;
	}
}
