package com.readforce.enums;

public enum ExpireTime {
	
	DEFAULT(3),
	VIERIFIED_TIME(10);
	
	private final long ExpireTime;
	
	ExpireTime(long ExpireTime) {
		this.ExpireTime = ExpireTime;
	}
	
	public long getTime() {
		return ExpireTime;
	}
	
}
