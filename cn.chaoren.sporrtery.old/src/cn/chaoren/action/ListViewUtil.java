package cn.chaoren.action;

import com.mobclick.android.f;

import android.view.View;
import android.view.ViewGroup;

public class ListViewUtil {
    public interface ViewFactory
    {
    	public View makeView(Object object,Object tag);
    	public ViewGroup makeLineView();
    }
    public ListViewUtil(ViewFactory factory) {
    	this.factory = factory;
		makeLineView();
	}
	private ViewGroup parent;
    private ViewGroup itemPerLine;
    private int countPerLine;
    private int count =-1;
    public void setCountPerLine(int countPerLine) {
		this.countPerLine = countPerLine;
	}
    
    
    public void setItemPerLine(ViewGroup itemPerLine) {
		this.itemPerLine = itemPerLine;
	}
    
    public void setParent(ViewGroup parent) {
		this.parent = parent;
	}
    
    public void addView(View view)
    {
    	if (count == countPerLine-1) {
			makeLineView();
		}
    	count++;
    	itemPerLine.addView(view);
    }
    
    private ViewFactory factory;
    
    public void setFactory(ViewFactory factory) {
		this.factory = factory;
	}
    
    public void makeLineView()
    {
    	itemPerLine = factory.makeLineView();
    	count = 0;
    }
    
    public void makeView(Object param,Object tag)
    {
    	addView(factory.makeView(param, tag));
    }
    
}
