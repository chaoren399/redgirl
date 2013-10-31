package cn.chaoren.db;

public class MyBettingDO {

	public String numberString ;
	public int id;
	public int stake;//购买注数
	public int fee;//买的钱数
	public int buyTime;//购买时间
	public String foretx;//预测金额数
	public int play_time;//比赛结束时间
	public int status;//比赛状态
	public int win_status;//获胜状态
	public String win_match;//获胜的球队
	public String win_fee;//奖金
	public String type;//玩法
	public String bet_style;//过关方式
	public int beishu;
	
	
	
	
	public String getForetx() {
		return foretx;
	}
	public void setForetx(String foretx) {
		this.foretx = foretx;
	}
	public int getBeizhu() {
		return beishu;
	}
	public void setBeizhu(int beizhu) {
		this.beishu = beizhu;
	}
	public String getBet_style() {
		return bet_style;
	}
	public void setBet_style(String bet_style) {
		this.bet_style = bet_style;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWin_fee() {
		return win_fee;
	}
	public void setWin_fee(String win_fee) {
		this.win_fee = win_fee;
	}
	public int getWin_status() {
		return win_status;
	}
	public void setWin_status(int win_status) {
		this.win_status = win_status;
	}
	
	public String getWin_match() {
		return win_match;
	}
	public void setWin_match(String win_match) {
		this.win_match = win_match;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getPlay_time() {
		return play_time;
	}
	public void setPlay_time(int play_time) {
		this.play_time = play_time;
	}
	public String getNumberString() {
		return numberString;
	}
	public void setNumberString(String numberString) {
		this.numberString = numberString;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStake() {
		return stake;
	}
	public void setStake(int stake) {
		this.stake = stake;
	}
	public int getFee() {
		return fee;
	}
	public void setFee(int fee) {
		this.fee = fee;
	}
	public int getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(int buyTime) {
		this.buyTime = buyTime;
	}
	@Override
	public String toString() {
		return "MyBettingDO [numberString=" + numberString + ", id=" + id
				+ ", stake=" + stake + ", fee=" + fee + ", buyTime=" + buyTime
				+ ", foretx=" + foretx + ", play_time=" + play_time
				+ ", status=" + status + ", win_status=" + win_status
				+ ", win_match=" + win_match + ", win_fee=" + win_fee
				+ ", type=" + type + ", bet_style=" + bet_style + ", beizhu="
				+ beishu + "]";
	}

}
