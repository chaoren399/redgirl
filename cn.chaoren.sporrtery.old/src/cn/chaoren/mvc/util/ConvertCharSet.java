package cn.chaoren.mvc.util;


/**
 * 字符串转化
 * @author chaoren
 *
 */

public class ConvertCharSet {
	
	    //转换为%E4%BD%A0形式
	    public static String toUtf8String(String s) {
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < s.length(); i++) {
	            char c = s.charAt(i);
	            if (c >= 0 && c <= 255) {
	                sb.append(c);
	            } else {
	                byte[] b;
	                try {
	                    b = String.valueOf(c).getBytes("utf-8");
	                } catch (Exception ex) {
	                    System.out.println(ex);
	                    b = new byte[0];
	                }
	                for (int j = 0; j < b.length; j++) {
	                    int k = b[j];
	                    if (k < 0)
	                        k += 256;
	                    sb.append("%" + Integer.toHexString(k).toUpperCase());
	                }
	            }
	        }
	        return sb.toString();
	    }
    
      
	

}
