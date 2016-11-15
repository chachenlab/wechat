package com.jfinal.weixin.template;

import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.TemplateData;
import com.jfinal.weixin.sdk.api.TemplateMsgApi;
import com.jfinal.weixin.sdk.jfinal.ApiController;
import com.jfinal.weixin.util.WeixinUtil;

/**
 * Created by L.cm on 2016/8/31.
 */
public class TemplateController extends ApiController {
    /**
     * 如果要支持多公众账号，只需要在此返回各个公众号对应的 ApiConfig 对象即可 可以通过在请求 url 中挂参数来动态从数据库中获取
     * ApiConfig 属性值
     */
    public ApiConfig getApiConfig() {
        return WeixinUtil.getApiConfig();
    }

    public void index() {
        // 模板消息，发送测试：pass
        ApiResult result = TemplateMsgApi.send(TemplateData.New()
                // 消息接收者
                .setTouser("oOGf-jgjmwxFVU66D-lFO2AFK8ic")
                // 模板id
                .setTemplate_id("UsOTFJfm7-XfskPd5p2wlBXYUjdwjUTZxnMXbOfVQ0A")
                .setTopcolor("#eb414a")
                .setUrl("http://m.xxxx.cn/qrcode/t/xxxxxx")

                // 模板参数
                .add("first", "验票成功！\n", "#999")
                .add("keyword1", "xxxxxx", "#999")
                .add("keyword2", "2014年12月27日 19:30", "#999")
                .add("keyword3", "xxxxx", "#999")
                .add("keyword4", "xxxxxxxx", "#999")
                .add("keyword5", "xxx元", "#999")
                .add("remark", "\nxxxxxxxxxx。", "#999")
                .build());

        renderText(result.getJson());
    }
}
