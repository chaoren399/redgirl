package cn.chaoren.widget;

import cn.chaoren.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class ToggleView extends View {
	private Drawable onDrawable;
	private Drawable offDrawable;

	public ToggleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public ToggleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context mContext, AttributeSet attrs) {
		setClickable(true);
		TypedArray a = mContext.obtainStyledAttributes(attrs,
				R.styleable.ToggleView);
		on = a.getBoolean(R.styleable.ToggleView_on, false);
		onDrawable = a.getDrawable(R.styleable.ToggleView_onsrc);
		offDrawable = a.getDrawable(R.styleable.ToggleView_offsrc);
		setOn(on);
		a.recycle();
		/*setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				

			}
		});*/
	}

	public ToggleView(Context context) {
		super(context);

	}

	private boolean on;

	public boolean isOn() {
		return on;
	}

	
   @Override
    public boolean performClick() {
	   on = !on;
		setOn(on);
 	return super.performClick();
   }
	
	
	public void setOn(boolean on) {
		this.on = on;
		if (on) {
			setBackgroundDrawable(onDrawable);

		} else {
			setBackgroundDrawable(offDrawable);
		}
	}

}
