package demo.xy.com.xytdcq.demo1.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import demo.xy.com.xytdcq.R;

/**
 * 自定义TextView
 * 将文字旋转方向后显示
 */
public class VerticalTextView extends AppCompatTextView {

	public final static int ORIENTATION_UP_TO_DOWN = 0;
	public final static int ORIENTATION_DOWN_TO_UP = 1;
	public final static int ORIENTATION_LEFT_TO_RIGHT = 2;
	public final static int ORIENTATION_RIGHT_TO_LEFT = 3;

	Rect textbounds = new Rect();
	private int direction;

	public VerticalTextView(Context context) {
		super(context);
	}

	public VerticalTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.verticaltextview);
		direction = a.getInt(R.styleable.verticaltextview_direction, 0);
		a.recycle();

		requestLayout();
		invalidate();

	}
	
	public void setDirection(int direction) {
		this.direction = direction;
		
		requestLayout();
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		getPaint().getTextBounds(getText().toString(), 0, getText().length(),
				textbounds);
		if (direction == ORIENTATION_LEFT_TO_RIGHT
				|| direction == ORIENTATION_RIGHT_TO_LEFT) {
			setMeasuredDimension(measureHeight(widthMeasureSpec),
					measureWidth(heightMeasureSpec));
		} else if (direction == ORIENTATION_UP_TO_DOWN
				|| direction == ORIENTATION_DOWN_TO_UP) {
			setMeasuredDimension(measureWidth(widthMeasureSpec),
					measureHeight(heightMeasureSpec));
		}

	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = textbounds.height() + getPaddingTop()
					+ getPaddingBottom();
			// result = text_bounds.height();
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = textbounds.width() + getPaddingLeft() + getPaddingRight();
			// result = text_bounds.width();
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		 super.onDraw(canvas);
		
		canvas.save();
		
		int startX = 0;
		int startY = 0;
		int stopX = 0;
		int stopY = 0;
		Path path = new Path();
		if (direction == ORIENTATION_UP_TO_DOWN) {
			startX = (getWidth() - textbounds.height() >> 1);
			startY = (getHeight() - textbounds.width() >> 1);
			stopX = (getWidth() - textbounds.height() >> 1);
			stopY = (getHeight() + textbounds.width() >> 1);
			path.moveTo(startX, startY);
			path.lineTo(stopX, stopY);
		} else if (direction == ORIENTATION_DOWN_TO_UP) {
			startX = (getWidth() + textbounds.height() >> 1);
			startY = (getHeight() + textbounds.width() >> 1);
			stopX = (getWidth() + textbounds.height() >> 1);
			stopY = (getHeight() - textbounds.width() >> 1);
			path.moveTo(startX, startY);
			path.lineTo(stopX, stopY);
		} else if (direction == ORIENTATION_LEFT_TO_RIGHT) {
			startX = (getWidth() - textbounds.width() >> 1);
			startY = (getHeight() + textbounds.height() >> 1);
			stopX = (getWidth() + textbounds.width() >> 1);
			stopY = (getHeight() + textbounds.height() >> 1);
			path.moveTo(startX, startY);
			path.lineTo(stopX, stopY);
		} else if (direction == ORIENTATION_RIGHT_TO_LEFT) {
			startX = (getWidth() + textbounds.width() >> 1);
			startY = (getHeight() - textbounds.height() >> 1);
			stopX = (getWidth() - textbounds.width() >> 1);
			stopY = (getHeight() - textbounds.height() >> 1);
			path.moveTo(startX, startY);
			path.lineTo(stopX, stopY);
		}
		
		this.getPaint().setColor(this.getCurrentTextColor());
//		canvas.drawLine(startX, startY, stopX, stopY, this.getPaint());
		canvas.drawTextOnPath(getText().toString(), path, 0, 0, this.getPaint());
		
		canvas.restore();
	}
}

