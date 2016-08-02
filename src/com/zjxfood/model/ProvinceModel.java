package com.zjxfood.model;

import java.util.List;

public class ProvinceModel {
	private String name;
	private List<CityModel> cityList;
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProvinceModel() {
		super();
	}

	public ProvinceModel(String name, List<CityModel> cityList) {
		super();
		this.name = name;
		this.cityList = cityList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CityModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityModel> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + name + ", cityList=" + cityList + "]";
	}
	
}
