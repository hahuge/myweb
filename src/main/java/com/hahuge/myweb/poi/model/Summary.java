package com.hahuge.myweb.poi.model;

import com.hahuge.myweb.commom.model.Entity;

public class Summary implements Entity<String> {
	
	private static final long serialVersionUID = -6654585105745995312L;
	
	private String id;
	//姓名
	private String name;
	//面积
	private String area;
	//一卡通账号
	private String accNum;
	//村名
	private String village;
	//年份
	private String year;
	
	@Override
	public String getId() {
		return id;
	}
	@Override
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAccNum() {
		return accNum;
	}
	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}
	public String getVillage() {
		return village;
	}
	public void setVillage(String village) {
		this.village = village;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	@Override
	public String toString() {
		return "Summary [id=" + id + ", name=" + name + ", area=" + area + ", accNum=" + accNum + ", village=" + village
				+ ", year=" + year + "]";
	}
}
