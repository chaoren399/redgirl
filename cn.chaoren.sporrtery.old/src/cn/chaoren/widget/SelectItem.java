package cn.chaoren.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.chaoren.R;

public class SelectItem extends LinearLayout {

	private Drawable onDrawable;
	private Drawable offDrawable;
	private boolean selected;

	private View view;
	
	private TextView text;
	@Override
	public boolean isSelected() {
		
		return selected;
	}
	
	public SelectItem setOnDrawable(Drawable onDrawable) {
		this.onDrawable = onDrawable;
		return this;
	}
	
	public SelectItem setOffDrawable(Drawable offDrawable) {
		this.offDrawable = offDrawable;
		view.setBackgroundDrawable(offDrawable);
		return this;
	}
	
	public SelectItem setText(String txt)
	{
		text.setText(txt);
		return this;
	}
	
	@Override
	public void setSelected(boolean selected) {
		
		super.setSelected(selected);
		this.selected = selected;
		view.setSelected(selected);
		text.setTextColor(Color.BLACK);
		if (selected) {
			view.setBackgroundDrawable(onDrawable);
		}else 
		{
			view.setBackgroundDrawable(offDrawable);
		}
	}
	
	public SelectItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	   init();
	}

	public SelectItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private View.OnClickListener onSelectChangeListener;
	
	public void setOnSelectChangeListener(
			View.OnClickListener onSelectChangeListener) {
		this.onSelectChangeListener = onSelectChangeListener;
	}
	
	@Override
	public boolean performClick() {
		if (onSelectChangeListener != null) {
			onSelectChangeListener.onClick(this);
		}
		return super.performClick();
	}
	
	/**
	 * @return
	 */
	public String getText()
	{
		if (text != null) {
			return text.getText().toString();
		}
		return "";
	}
	
	private void init()
	{
		
		setClickable(true);
	}
	
	public static SelectItem getInstance(Context context)
	{
		SelectItem item = (SelectItem) LayoutInflater.from(context).inflate(R.layout.setting_goal_item, null);
		item.view = item.findViewById(R.id.radio);
		item.text = (TextView) item.findViewById(R.id.titleText);
		item.text.setTextColor(Color.BLACK);
		return item;
	}
	
	public SelectItem getRadio(String title)
	{
		Resources resources = getContext().getResources();
		setOnDrawable(resources.getDrawable(R.drawable.icon12)).setOffDrawable(resources.getDrawable(R.drawable.icon11))
		.setText(title);
		return this;
	}
	
	public SelectItem getCheck(String title)
	{
		Resources resources = getContext().getResources();
		setOnDrawable(resources.getDrawable(R.drawable.icon10)).setOffDrawable(resources.getDrawable(R.drawable.icon09))
		.setText(title);
		return this;
	}
	
}
