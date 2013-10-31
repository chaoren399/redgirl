package cn.chaoren.action;

import android.view.View;

public class ViewSelector {
   private View group;
   private View[]views;
   private int length;
   public ViewSelector(View parent) {
	  setParent(parent);
}
   public ViewSelector() {
	// TODO Auto-generated constructor stub
}
   public ViewSelector setParent(View parent)
   {
	   group = parent;
	   return this;
   }
   
   public ViewSelector setViews(int[]resIds,View.OnClickListener onClickListener)
   {
	 length = resIds.length;
	   if (length>0) {
		   views = new View[resIds.length];
		   for (int i = 0; i < length; i++) {
			views[i] = group.findViewById(resIds[i]);
			views[i].setOnClickListener(onClickListener);
		}
	}
	   
	   return this;
   }
   
   
   public ViewSelector setViews(int[] resIds)
   {
	   setViews(resIds, null);
	   return this;
   }
   
   
   public void setSelected(int resId)
   {
	   if (length > 0) {
		for (int i = 0; i < length; i++) {
			if (views[i].getId()==resId) {
				views[i].setSelected(true);
			}else
			{
				views[i].setSelected(false);
			}
		}
	}
   }
   
   public void performClick(int resId)
   {
	   if (length > 0) {
			for (int i = 0; i < length; i++) {
				if (views[i].getId()==resId) {
					views[i].performClick();
					break;
				}
			}
		}
   }
   
   public void setSelected(View view)
   {
	   if (length > 0) {
			for (int i = 0; i < length; i++) {
				if (views[i]==view) {
					views[i].setSelected(true);
				}else
				{
					views[i].setSelected(false);
				}
			}
		}
   }
}
