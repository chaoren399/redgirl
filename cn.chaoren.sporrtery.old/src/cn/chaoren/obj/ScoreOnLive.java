package cn.chaoren.obj;

public class ScoreOnLive {
   public String time;//进球时间
   public String ho;
   public int eid;//1,进球;2,黄牌;3,红牌
  // public String player;//球员
   public String ao;
   
   @Override
	public boolean equals(Object o) {
		ScoreOnLive s = (ScoreOnLive) o;
		if (s != null) {
			if (time != null && s.time!= null) {
				return time.trim().equals(s.time.trim());
			}
		}
		return super.equals(o);
	}
}
