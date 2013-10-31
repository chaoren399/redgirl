package cn.chaoren.obj;

import android.os.Parcel;
import android.os.Parcelable;





//封装
public class Information implements Parcelable{
    public int id;
    public String guid;
    public String title;
    public String date;
    public String prefile;
    public String edt;
    public Information() {
		// TODO Auto-generated constructor stub
	}
    
    public static final Creator<Information> CREATOR = new Creator<Information>() {
		
		@Override
		public Information[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Information[size];
		}
		
		@Override
		public Information createFromParcel(Parcel source) {
			Information info = new Information();
			info.id = source.readInt();
			info.title = source.readString();
			info.date = source.readString();
			info.prefile = source.readString();
			info.guid = source.readString();
			info.edt = source.readString();
			return info;
		}
	};
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeString(date);
		dest.writeString(prefile);
		dest.writeString(guid);
		dest.writeString(edt);
	}
}
