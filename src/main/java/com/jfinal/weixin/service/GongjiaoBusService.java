package com.jfinal.weixin.service;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jfinal.weixin.entity.GongjiaoBus;
import com.jfinal.weixin.entity.Segment;
import com.jfinal.weixin.sdk.utils.HttpUtils;
import com.jfinal.weixin.sdk.utils.XmlHelper;
import com.jfinal.weixin.util.WeixinUtil;

/**
 * @author Javen
 * @Email javenlife@126.com
 * 公交驾乘查询  公交换乘查询，该接口根据起点和终点信息查询公交换乘方案。
 */
public class GongjiaoBusService {
	private static List<GongjiaoBus> getGongjiaoBus(String city,String start_addr,String end_addr){
		String requestUrl = "http://openapi.aibang.com/bus/transfer?app_key=0a64bf2d8ee2248700e8bc32e6aaa3fe&city={city}&start_addr={start_addr}&end_addr={end_addr}";
	    // 对城市和线路进行编码
	    requestUrl = requestUrl.replace("{city}", WeixinUtil.urlEncodeUTF8(city));
	    requestUrl = requestUrl.replace("{start_addr}", WeixinUtil.urlEncodeUTF8(start_addr));
	    requestUrl = requestUrl.replace("{end_addr}", WeixinUtil.urlEncodeUTF8(end_addr));
	    // 处理名称、作者中间的空格
	    requestUrl = requestUrl.replaceAll("\\+", "%20");
	    System.out.println(requestUrl);
	    String xml = HttpUtils.get(requestUrl, null);
		return parseGongjiaoBus(xml);

	}
	private static List<GongjiaoBus> parseGongjiaoBus(String xml) {
		List<GongjiaoBus> gongjiaoBus = new ArrayList<GongjiaoBus>();
		XmlHelper xmlHelper = XmlHelper.of(xml);
		String num = xmlHelper.getString("//result_num");
		if ("0".equals(num)) {
			return gongjiaoBus;
		}
		NodeList busList = xmlHelper.getNodeList("//buses/bus");
		for (int i = 0; i < busList.getLength(); i++) {
			Node busNode = busList.item(i);
			String dist  = xmlHelper.getString(busNode, "dist");
			String time  = xmlHelper.getString(busNode, "time");
			String footDist = xmlHelper.getString(busNode, "foot_dist");
			String lastFootDist = xmlHelper.getString(busNode, "last_foot_dist");
			List<Segment> list = new ArrayList<Segment>();
			NodeList segmentList = xmlHelper.getNodeList(busNode, "segments/segment");
			for (int j = 0; j < segmentList.getLength(); j++) {
				Node segmentNode  = segmentList.item(j);
				String lineName   = xmlHelper.getString("line_name");
				String stats      = xmlHelper.getString("stats");
				String footDist2  = xmlHelper.getString("foot_dist");
				list.add(new Segment(lineName, stats, footDist2));
			}
			gongjiaoBus.add(new GongjiaoBus(dist, time, footDist, lastFootDist, list));
		}
		return gongjiaoBus;
	}
	
	
	public static String getgetGongjiaoBusSer(String city,String start_addr,String end_addr){
		List<GongjiaoBus> bus= getGongjiaoBus(city, start_addr, end_addr);
		StringBuffer buffer=new StringBuffer();
		if (bus!=null) {
			for (int i = 0; i < 5; i++) {
				buffer.append("\ue132 方案"+(i+1)+"\n");
				buffer.append("总距离："+bus.get(i).getDist()+"m 时间："+bus.get(i).getTime()+"分钟 ");
				String foot=bus.get(i).getFoot_dist();
				if (!foot.equals("0")) {
					buffer.append("步行距离："+bus.get(i).getFoot_dist()+"m \n\n");
				}else {
					buffer.append("\n\n");
				}
				String last_foot_dist=bus.get(i).getLast_foot_dist();
				if (!last_foot_dist.equals("0")) {
					buffer.append("\ue231 先步行"+last_foot_dist+"m到公交站\n");
				}
				List<Segment> list=bus.get(i).getList();
				for (int j = 0; j < list.size(); j++) {
					String ch="乘";
					if (j>0) {
						ch="换乘";
					}
					buffer.append("\ue231"+ch+list.get(j).getLine_name().substring(0,list.get(j).getLine_name().indexOf("("))+",途径站点:"+list.get(j).getStats()+"\n");
					String foot_dist=list.get(j).getFoot_dist();
					if (!foot_dist.equals("0")) {
						if (list.size()>j+1 && list.get(j+1).getLine_name()!=null) {
							buffer.append("\ue231 再步行"+foot_dist+"m到达"+list.get(j+1).getLine_name());
						}else {
							buffer.append("\ue231 再步行"+foot_dist+"m到达"+end_addr);
						}
					}
					buffer.append("\n\n");
				}
				
			}
			return buffer.toString();
		}
		return null;
		
	}
	
	public static void main(String[] args) {
		List<GongjiaoBus> list = getGongjiaoBus("深圳", "油松派出所", "深圳北站");
		for (GongjiaoBus gongjiaoBus : list) {
			System.out.println(gongjiaoBus.toString());
		}
	}
}
