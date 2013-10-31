package cn.chaoren.obj;

import android.os.Parcel;
import android.os.Parcelable;

public class Selection implements Parcelable
{
	public Selection() {
		// TODO Auto-generated constructor stub
	}
	public  final Creator<Selection> CREATOR = new Creator<Selection>() {
		
		@Override
		public Selection[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Selection[size];
		}
		
		@Override
		public Selection createFromParcel(Parcel source) {
			Selection selection = new Selection();
			selection.position = source.readInt();
			selection.guid = source.readString();
			int s = source.readInt();
			if (s == 1) {
				selection.selected = true;
			}else
			{
				selection.selected = false;
			}
			return selection;
		}
	};
	public int position;
	public String guid;
	public boolean selected;
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Selection) {
			Selection save = (Selection) o;
			return position == save.position;
		}
		return super.equals(o);
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(position);
		dest.writeString(guid);
		if (selected) {
			dest.writeInt(1);
		}else
		{
			dest.writeInt(0);
		}
	}
}