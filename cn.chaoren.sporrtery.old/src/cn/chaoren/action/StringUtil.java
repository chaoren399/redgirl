package cn.chaoren.action;

public class StringUtil {
     public String optString(String str,String def)
     {
    	 if (str == null) {
			return def;
		}
    	 return str;
     }
     
     public int optInt(String str,int def)
     {
    	 int temp = def;
    	 try {
			temp = new Integer(str);
		} catch (Exception e) {
			temp = def;
		}
    	 return temp;
     }
}
