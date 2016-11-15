/**
 * Copyright (c) 2015-2016, Javen Zhou  (javenlife@126.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.weixin.demo;

import java.util.List;

import com.jfinal.course.face.FaceService;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.jfinal.MsgControllerAdapter;
import com.jfinal.weixin.sdk.msg.in.InImageMsg;
import com.jfinal.weixin.sdk.msg.in.InLinkMsg;
import com.jfinal.weixin.sdk.msg.in.InLocationMsg;
import com.jfinal.weixin.sdk.msg.in.InShortVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InTextMsg;
import com.jfinal.weixin.sdk.msg.in.InVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InVoiceMsg;
import com.jfinal.weixin.sdk.msg.in.event.InCustomEvent;
import com.jfinal.weixin.sdk.msg.in.event.InFollowEvent;
import com.jfinal.weixin.sdk.msg.in.event.InLocationEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMassEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMenuEvent;
import com.jfinal.weixin.sdk.msg.in.event.InQrCodeEvent;
import com.jfinal.weixin.sdk.msg.in.event.InShakearoundUserShakeEvent;
import com.jfinal.weixin.sdk.msg.in.event.InTemplateMsgEvent;
import com.jfinal.weixin.sdk.msg.in.event.InVerifyFailEvent;
import com.jfinal.weixin.sdk.msg.in.event.InVerifySuccessEvent;
import com.jfinal.weixin.sdk.msg.in.speech_recognition.InSpeechRecognitionResults;
import com.jfinal.weixin.sdk.msg.out.News;
import com.jfinal.weixin.sdk.msg.out.OutNewsMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;
import com.jfinal.weixin.sdk.msg.out.OutVoiceMsg;
import com.jfinal.weixin.service.BaiduAmbitus;
import com.jfinal.weixin.service.BaiduWeatherService;
import com.jfinal.weixin.util.WeixinUtil;

/**
 * 将此 DemoController 在YourJFinalConfig 中注册路由， 并设置好weixin开发者中心的 URL 与 token ，使
 * URL 指向该 DemoController 继承自父类 WeixinController 的 index
 * 方法即可直接运行看效果，在此基础之上修改相关的方法即可进行实际项目开发
 */
public class WeixinMsgController extends MsgControllerAdapter
{
    public static String nearbyContent;// 附近
    public static String location;// 地理位置114.037125,22.645319
    public static String weahterContent;
    public String Regex = "[\\+ ~!@#%^-_=]?";
    static Log logger = Log.getLog(WeixinMsgController.class);
    private static final String helpStr = "发送 help 可获得帮助";

    /**
     * 如果要支持多公众账号，只需要在此返回各个公众号对应的 ApiConfig 对象即可 可以通过在请求 url 中挂参数来动态从数据库中获取
     * ApiConfig 属性值
     */
    public ApiConfig getApiConfig()
    {
        return WeixinUtil.getApiConfig();
    }

    /**
     * 实现父类抽方法，处理文本消息 本例子中根据消息中的不同文本内容分别做出了不同的响应，同时也是为了测试 jfinal weixin
     * sdk的基本功能： 本方法仅测试了 OutTextMsg、OutNewsMsg、OutMusicMsg 三种类型的OutMsg，
     * 其它类型的消息会在随后的方法中进行测试
     */
    protected void processInTextMsg(InTextMsg inTextMsg)
    {
        String msgContent = inTextMsg.getContent().trim();
        // 帮助提示
        if ("help".equalsIgnoreCase(msgContent) || "帮助".equals(msgContent))
        {
            OutTextMsg outMsg = new OutTextMsg(inTextMsg);
            outMsg.setContent(helpStr);
            render(outMsg);
        }
        // 其它文本消息直接返回原值 + 帮助提示
        else
        {
            renderOutTextMsg("\t文本消息已成功接收，内容为： " + inTextMsg.getContent() + "\n\n" + helpStr);
        }
    }

    /**
     * 实现父类抽方法，处理图片消息
     */
    protected void processInImageMsg(InImageMsg inImageMsg)
    {
//		OutImageMsg outMsg = new OutImageMsg(inImageMsg);
//		// 将刚发过来的图片再发回去
//		outMsg.setMediaId(inImageMsg.getMediaId());
//		render(outMsg);
        String picUrl = inImageMsg.getPicUrl();
        String respContent = FaceService.detect(picUrl);
        renderOutTextMsg(respContent);
    }

    /**
     * 实现父类抽方法，处理语音消息
     */
    protected void processInVoiceMsg(InVoiceMsg inVoiceMsg)
    {
        OutVoiceMsg outMsg = new OutVoiceMsg(inVoiceMsg);
        // 将刚发过来的语音再发回去
        outMsg.setMediaId(inVoiceMsg.getMediaId());
        render(outMsg);
    }

    /**
     * 实现父类抽方法，处理视频消息
     */
    protected void processInVideoMsg(InVideoMsg inVideoMsg)
    {
        /*
         * 腾讯 api 有 bug，无法回复视频消息，暂时回复文本消息代码测试 OutVideoMsg outMsg = new
		 * OutVideoMsg(inVideoMsg); outMsg.setTitle("OutVideoMsg 发送");
		 * outMsg.setDescription("刚刚发来的视频再发回去"); // 将刚发过来的视频再发回去，经测试证明是腾讯官方的 api
		 * 有 bug，待 api bug 却除后再试 outMsg.setMediaId(inVideoMsg.getMediaId());
		 * render(outMsg);
		 */
        OutTextMsg outMsg = new OutTextMsg(inVideoMsg);
        outMsg.setContent("\t视频消息已成功接收，该视频的 mediaId 为: " + inVideoMsg.getMediaId());
        render(outMsg);
    }

    @Override
    protected void processInShortVideoMsg(InShortVideoMsg inShortVideoMsg)
    {
        OutTextMsg outMsg = new OutTextMsg(inShortVideoMsg);
        outMsg.setContent("\t视频消息已成功接收，该视频的 mediaId 为: " + inShortVideoMsg.getMediaId());
        render(outMsg);
    }

    /**
     * 实现父类抽方法，处理地址位置消息
     */
    protected void processInLocationMsg(InLocationMsg inLocationMsg)
    {
//		OutTextMsg outMsg = new OutTextMsg(inLocationMsg);
//		outMsg.setContent("已收到地理位置消息:" + "\nlocation_X = " + inLocationMsg.getLocation_X() + "\nlocation_Y = "
//				+ inLocationMsg.getLocation_Y() + "\nscale = " + inLocationMsg.getScale() + "\nlabel = "
//				+ inLocationMsg.getLabel());
//		render(outMsg);

        String Location_X = inLocationMsg.getLocation_X();
        String Location_Y = inLocationMsg.getLocation_Y();
        System.out.println("Location_X:" + Location_X + " Location_Y:"
                + Location_Y);
        location = Location_Y + "," + Location_X;

        String respContent = "";
        if (StrKit.isBlank(nearbyContent) && StrKit.isBlank(weahterContent))
        {
            respContent = "您发送的是地理位置消息！\n\n 1、查询天气 直接回复【天气】\n2、查询附近 如：附近酒店";
            renderOutTextMsg(respContent);
        } else
        {
            if (!StrKit.isBlank(nearbyContent))
            {
                List<News> ambitusService = BaiduAmbitus.getAmbitusService(nearbyContent, location);
                if (ambitusService.size() > 0)
                {
                    OutNewsMsg outMsg = new OutNewsMsg(inLocationMsg);
                    outMsg.addNews(ambitusService);
                    render(outMsg);
                    nearbyContent = null;
                    location = null;
                    return;
                } else
                {
                    respContent = "\ue252 查询周边失败，请检查。";
                    renderOutTextMsg(respContent);
                }
            } else if (!StrKit.isBlank(weahterContent))
            {
                respContent = BaiduWeatherService.getWeatherService(location);
                weahterContent = null;
                location = null;
                renderOutTextMsg(respContent);
            }

        }
    }

    @Override
    protected void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent)
    {
        if (InQrCodeEvent.EVENT_INQRCODE_SUBSCRIBE.equals(inQrCodeEvent.getEvent()))
        {
            logger.debug("扫码未关注：" + inQrCodeEvent.getFromUserName());
            OutTextMsg outMsg = new OutTextMsg(inQrCodeEvent);
            outMsg.setContent("感谢您的关注，二维码内容：" + inQrCodeEvent.getEventKey());
            render(outMsg);
        }
        if (InQrCodeEvent.EVENT_INQRCODE_SCAN.equals(inQrCodeEvent.getEvent()))
        {
            logger.debug("扫码已关注：" + inQrCodeEvent.getFromUserName());
            String key = inQrCodeEvent.getEventKey();
            renderOutTextMsg(key);
        }
    }

    @Override
    protected void processInLocationEvent(InLocationEvent inLocationEvent)
    {
        logger.debug("发送地理位置事件：" + inLocationEvent.getFromUserName());
        OutTextMsg outMsg = new OutTextMsg(inLocationEvent);
        outMsg.setContent("地理位置是：\n" + inLocationEvent.getLatitude() + "\n" + inLocationEvent.getLongitude());
        render(outMsg);
    }

    @Override
    protected void processInMassEvent(InMassEvent inMassEvent)
    {
        logger.debug("测试方法：processInMassEvent()");
        renderNull();
    }

    /**
     * 实现父类抽方法，处理自定义菜单事件
     */
    protected void processInMenuEvent(InMenuEvent inMenuEvent)
    {
        logger.debug("菜单事件：" + inMenuEvent.getFromUserName());
        OutTextMsg outMsg = new OutTextMsg(inMenuEvent);
        outMsg.setContent("菜单事件内容是：" + inMenuEvent.getEventKey());
        render(outMsg);
    }

    @Override
    protected void processInSpeechRecognitionResults(InSpeechRecognitionResults inSpeechRecognitionResults)
    {
        logger.debug("语音识别事件：" + inSpeechRecognitionResults.getFromUserName());
        OutTextMsg outMsg = new OutTextMsg(inSpeechRecognitionResults);
        outMsg.setContent("语音识别内容是：" + inSpeechRecognitionResults.getRecognition());
        render(outMsg);
    }

    /**
     * 实现父类抽方法，处理链接消息 特别注意：测试时需要发送我的收藏中的曾经收藏过的图文消息，直接发送链接地址会当做文本消息来发送
     */
    protected void processInLinkMsg(InLinkMsg inLinkMsg)
    {
        OutNewsMsg outMsg = new OutNewsMsg(inLinkMsg);
        outMsg.addNews("链接消息已成功接收", "链接使用图文消息的方式发回给你，还可以使用文本方式发回。点击图文消息可跳转到链接地址页面，是不是很好玩 :)",
                "http://mmbiz.qpic.cn/mmbiz/zz3Q6WSrzq1ibBkhSA1BibMuMxLuHIvUfiaGsK7CC4kIzeh178IYSHbYQ5eg9tVxgEcbegAu22Qhwgl5IhZFWWXUw/0",
                inLinkMsg.getUrl());
        render(outMsg);
    }

    @Override
    protected void processInCustomEvent(InCustomEvent inCustomEvent)
    {
        System.out.println("processInCustomEvent() 方法测试成功");
    }

    /**
     * 实现父类抽方法，处理关注/取消关注消息
     */
    protected void processInFollowEvent(InFollowEvent inFollowEvent)
    {
        OutTextMsg outMsg = new OutTextMsg(inFollowEvent);
        outMsg.setContent("感谢关注 JFinal Weixin 极速开发服务号，为您节约更多时间，去陪恋人、家人和朋友 :) \n\n\n " + helpStr);
        // 如果为取消关注事件，将无法接收到传回的信息
        render(outMsg);
    }

    // 处理接收到的模板消息是否送达成功通知事件
    protected void processInTemplateMsgEvent(InTemplateMsgEvent inTemplateMsgEvent)
    {
        String status = inTemplateMsgEvent.getStatus();
        renderOutTextMsg("模板消息是否接收成功：" + status);
    }

    @Override
    protected void processInShakearoundUserShakeEvent(InShakearoundUserShakeEvent inShakearoundUserShakeEvent)
    {
        logger.debug("摇一摇周边设备信息通知事件：" + inShakearoundUserShakeEvent.getFromUserName());
        OutTextMsg outMsg = new OutTextMsg(inShakearoundUserShakeEvent);
        outMsg.setContent("摇一摇周边设备信息通知事件UUID：" + inShakearoundUserShakeEvent.getUuid());
        render(outMsg);
    }

    @Override
    protected void processInVerifySuccessEvent(InVerifySuccessEvent inVerifySuccessEvent)
    {
        logger.debug("资质认证成功通知事件：" + inVerifySuccessEvent.getFromUserName());
        OutTextMsg outMsg = new OutTextMsg(inVerifySuccessEvent);
        outMsg.setContent("资质认证成功通知事件：" + inVerifySuccessEvent.getExpiredTime());
        render(outMsg);
    }

    @Override
    protected void processInVerifyFailEvent(InVerifyFailEvent inVerifyFailEvent)
    {
        logger.debug("资质认证失败通知事件：" + inVerifyFailEvent.getFromUserName());
        OutTextMsg outMsg = new OutTextMsg(inVerifyFailEvent);
        outMsg.setContent("资质认证失败通知事件：" + inVerifyFailEvent.getFailReason());
        render(outMsg);
    }
}
