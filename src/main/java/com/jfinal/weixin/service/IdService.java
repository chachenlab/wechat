/**
 * Copyright (c) 2015-2016, Javen Zhou  (javenlife@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.weixin.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.kit.ParaMap;
/**
 * @author Javen
 * 2016年1月27日
 * 身份证查询
 */
public class IdService {
	private static  String httpUrl="http://apis.baidu.com/apistore/idservice/id";
	private static final String APIKEY=PropKit.get("apiKey");
	
	public static String getIdInfo(String id){
		System.out.println(id);
		String result=request(httpUrl,id);
		System.out.println(result);
		JSONObject resultObject = JSON.parseObject(result);
		Integer errNum = resultObject.getInteger("errNum");
		StringBuffer sbf=new StringBuffer();
		if (errNum==0) {
			JSONObject retDataObject = resultObject.getJSONObject("retData");
			
			sbf.append("身份证号:"+id).append("\n");
			sbf.append("查询结果如下:").append("\n\n");
			String sex=retDataObject.getString("sex");
			if (sex.equals("M")) {
				sex="\ue201 男";
			}else if(sex.equals("F")){
				sex="\ue51f 女";
			}else {
				sex="\ue429 未知";
			}
			sbf.append("性别:"+sex).append("\n");
			sbf.append("生日:"+retDataObject.getString("birthday")).append("\n");
			sbf.append("归属地:"+retDataObject.getString("address")).append("\n");
			
		}else {
			sbf.append("\ue336").append(resultObject.getString("retMsg"));
		}
		return sbf.toString();
	}
	
	public static String request(String httpUrl, String httpArg) {
	    return HttpKit.get(httpUrl, ParaMap.create("id", httpArg).getData(), ParaMap.create("apikey", APIKEY).getData());
	}
	
	public static String getGuide(){
		StringBuffer buffer = new StringBuffer();
        buffer.append("\ue321 身份证查询操作指南：").append("\n\n");  
        buffer.append("身份证@身份证号码").append("\n");  
        buffer.append("回复“?”显示主菜单");  
        return buffer.toString();
	}
	public static void main(String[] args) {
		System.out.println(getIdInfo("420117199210027116"));
	}
}
