package com.liuj.huabo.util;

public class TimeStats {
	private String resourceName;
	private long starttime;
	private long endtime;
	private long successtime;	//成功请求时间
	private long errortime;		//错误时间
	private int successcount;	//成功次数
	private int errorcount;		//失败次数
	private long maxtime;		//最大时间
	private long mintime;		//最小时间

	public String getResourceName() {
		return resourceName;
	}

	public String getStarttime() {
		return DateUtils.formatTimestamp(starttime);
	}

	public String getEndtime() {
		return DateUtils.formatTimestamp(endtime);
	}

	public int getSuccesscount() {
		return successcount;
	}

	public long getMaxtime() {
		return maxtime;
	}

	public long getMintime() {
		return mintime;
	}

	public long getAvgSuccessTime(){
		return successtime / Math.max(successcount, 1);
	}

	public long getAvgErrorTime(){
		return errortime / Math.max(errorcount, 1);
	}

	public long getAvgTime(){
		return (errortime + successtime) / Math.max(errorcount + successcount, 1);
	}

	public int getTotalcount() {
		return errorcount + successcount;
	}

	public int getErrorcount() {
		return errorcount;
	}

	TimeStats(String resourceName) {
		this.resourceName = resourceName;
		reset();
	}

	/**
	 * @param lasttime 处理前的时间
	 */
	public void addCountAndTime(long lasttime, boolean error){
		this.endtime = System.currentTimeMillis();
		long elapsed = this.endtime - lasttime;
		if(error){
			this.errortime += elapsed;
			this.errorcount++;
		}else{
			this.successtime += elapsed;
			this.successcount++;
		}
		if(this.mintime == 0){
			this.mintime = elapsed;
		} else {
			this.mintime = Math.min(this.mintime, elapsed);
		}
		this.maxtime = Math.max(this.maxtime, elapsed);
	}
	void reset(){
		this.starttime = System.currentTimeMillis();
		this.endtime = starttime;
		this.successtime = 0;
		this.successcount = 0;
		this.maxtime = 0;
		this.mintime = 0;
	}
}