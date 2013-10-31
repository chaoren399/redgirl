package cn.chaoren.widget;

import java.util.ArrayList;

import cn.chaoren.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;

public class SelectorLayout extends LinearLayout {
    private int SELCT_MODE = 1;//1,单选；2，多选；0，不能选
	private LinearLayout currentLayout;
	private LayoutParams params;
	private int count = -1;
	private ArrayList<SelectItem> selectItemList;
	private ArrayList<SelectItem> selectedItemList;
	private  int CONTS_PER_LINE = 4;
	private int dividerHeight;
	public SelectorLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a =context.obtainStyledAttributes(attrs, R.styleable.SelectorLayout);
		dividerHeight = a.getDimensionPixelSize(R.styleable.SelectorLayout_dividerHeight, 0);
		init();
	}

	public SelectorLayout(Context context) {
		super(context);
		init();
	}
    
	public void setCountPerLine(int count) {
		this.CONTS_PER_LINE = count;
	}
	
	public SelectorLayout setMode(int mode)
	{
		SELCT_MODE = mode;
		return this;
	}
	
	public ArrayList<SelectItem> getSelectedItemList() {
		return selectedItemList;
	}
	
	private void init()
	{
		setOrientation(VERTICAL);
	    createNewLine(0);
	    count--;
		params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		selectItemList = new ArrayList<SelectItem>();
		selectedItemList = new ArrayList<SelectItem>();
	}
	
	protected void createNewLine(int divider)
	{
		count=0;
		currentLayout = new LinearLayout(getContext());
		currentLayout.setOrientation(HORIZONTAL);
		LayoutParams param = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		param.topMargin = divider;
		addView(currentLayout,param);
	}
	
	public SelectorLayout addItem(SelectItem item)
	{
		item.setOnSelectChangeListener(onSelectChangeListener);
		selectItemList.add(item);
		count++;
		if (count >= CONTS_PER_LINE) {
			createNewLine(dividerHeight);
		}

		currentLayout.addView(item, params);
		return this;
	}
	
	
	public String getSingleValue()
	{
		if (selectedItemList!= null&&selectedItemList.size()>0) {
			SelectItem item = selectedItemList.get(0);
			return item.getText();
		}
		return "";
	}
	
	public String[]getSelectedValues()
	{
		if (selectedItemList!= null&&selectedItemList.size()>0) {
			int size = selectedItemList.size();
			String[]values= new String[size];
			SelectItem item=null;
			for (int i = 0; i < size; i++) {
				item = selectedItemList.get(i);
				values[i]=item.getText();						
			}
			return values;
		}
		return null;
	}
	
	public void setSelecteds(String[] values)
	{
		int length = selectItemList.size();
		SelectItem item =null;
		for (int i = 0; i < length; i++) {
			item = selectItemList.get(i);
			item.setSelected(false);
		}
		selectedItemList.clear();
		int valuesLength = values.length;
		for (int j = 0; j < valuesLength; j++) {			
			for (int i = 0; i < length; i++) {
				item = selectItemList.get(i);
				if (values[j].equals(item.getText())) {
					item.setSelected(true);
					selectedItemList.add(item);
				}
			}
		}
		
	}
	
	/**mode=1
	 * @param value
	 */
	public void setSelected(String value)
	{
		if (value==null) {
			return;
		}
		int length = selectItemList.size();
		SelectItem item = null;
		for (int i = 0; i < length; i++) {
			item = selectItemList.get(i);
			if (value.equals(item.getText())) {
				selectedItemList.clear();
				selectedItemList.add(item);
				item.setSelected(true);
				break;
			}else
			{
				item.setSelected(false);
			}
		}
	}
	
	private OnClickListener onSelectChangeListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (SELCT_MODE == 1) {
			
				selectedItemList.clear();
				selectedItemList.add((SelectItem)v);
				int size = selectItemList.size();
				SelectItem item =null;
				for (int i = 0; i < size; i++) {
					item = selectItemList.get(i);
					if (item == v) {
						item.setSelected(true);
					}else
					{
						item.setSelected(false);
					}
				}
			}else if (SELCT_MODE == 2) {
				SelectItem item = (SelectItem) v;
				boolean select = item.isSelected();
				selectedItemList.remove(v);
				item.setSelected(!select);
				if (!select) {
					selectedItemList.add((SelectItem)v);
				}
			}
			
		}
	};
	
}
