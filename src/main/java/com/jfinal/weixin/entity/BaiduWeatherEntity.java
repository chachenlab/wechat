package com.jfinal.weixin.entity;

import com.jfinal.weixin.sdk.utils.JsonUtils;

/**
 * @author Javen
 * @Email javenlife@126.com
 */
public class BaiduWeatherEntity {
	private String currentCity;
	private String date;
	private String resDate;
	private String dayPictureUrl;
	private String nightPictureUrl;
	private String weather;
	private String wind;
	private String temperature;
	private String pm25;
	private String other;
	
	public BaiduWeatherEntity() {
	}
	
	public BaiduWeatherEntity(String currentCity, String date, String resDate,
			String dayPictureUrl, String nightPictureUrl, String weather,
			String wind, String temperature, String pm25, String other) {
		this.currentCity = currentCity;
		this.date = date;
		this.resDate = resDate;
		this.dayPictureUrl = dayPictureUrl;
		this.nightPictureUrl = nightPictureUrl;
		this.weather = weather;
		this.wind = wind;
		this.temperature = temperature;
		this.pm25 = pm25;
		this.other = other;
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getResDate() {
		return resDate;
	}
	public void setResDate(String resDate) {
		this.resDate = resDate;
	}
	public String getDayPictureUrl() {
		return dayPictureUrl;
	}
	public void setDayPictureUrl(String dayPictureUrl) {
		this.dayPictureUrl = dayPictureUrl;
	}
	public String getNightPictureUrl() {
		return nightPictureUrl;
	}
	public void setNightPictureUrl(String nightPictureUrl) {
		this.nightPictureUrl = nightPictureUrl;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getWind() {
		return wind;
	}
	public void setWind(String wind) {
		this.wind = wind;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getCurrentCity() {
		return currentCity;
	}
	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}
	public String getPm25() {
		return pm25;
	}
	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
