package com.lin.util;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.CollectionUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 佛祖保佑，此代码无bug，就算有也一眼看出
 * 功能描述
 *
 * @author: songlin
 * @date: 2023年02月02日 14:44
 */
@Slf4j
public class DingTalkUtils {
    /**
     * 钉钉群设置 webhook, 支持重置
     */
    private static final String ACCESS_TOKEN = "https://oapi.dingtalk.com/robot/send?access_token=0d0fae70cd56bd4effbb91cc884af2c2b485f6d915d56936277154d67b718872";

    private static final String SECRET = "SEC240d620adf";

    /**
     * 消息类型
     */
    private static final String MSG_TYPE_TEXT = "text";
    private static final String MSG_TYPE_LINK = "link";
    private static final String MSG_TYPE_MARKDOWN = "markdown";
    private static final String MSG_TYPE_ACTION_CARD = "actionCard";
    private static final String MSG_TYPE_FEED_CARD = "feedCard";

    /**
     * 客户端实例
     */
    private static DingTalkClient client;

    static {
        try {
            client = new DefaultDingTalkClient(ACCESS_TOKEN + sign());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    /**
     * 官方演示示例
     * title 是消息列表下透出的标题
     * text 是进入群后看到的消息内容
     * 注意事项：
     * 1.文本，链接，Markdown会存在覆盖，推送最后一个定义的消息
     * 2.自定义机器人关键字要包含在content或title中，text.setContent("");link.setTitle("");markdown.setTitle("");
     *
     * @return OapiRobotSendResponse
     */
    public static void sdkDemoJava() {
        //请求对象
        OapiRobotSendRequest request = new OapiRobotSendRequest();

        //链接
        request.setMsgtype("link");
        OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
        link.setMessageUrl("https://www.dingtalk.com/");
        link.setPicUrl("");
        link.setTitle("时代的火车向前开");
        link.setText("这个即将发布的新版本，创始人阿Q称它为红树林。而在此之前，每当面临重大升级，产品经理们都会取一个应景的代号，这一次，为什么是红树林");
        request.setLink(link);

        //Markdown
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle("南京的天气真好呀");
        markdown.setText("#### 南京的天气真好呀 @130****1239\n" +
                "> 9度，西北风1级，空气良89，相对温度73%\n\n" +
                "> ![screenshot](https://gw.alicdn.com/tfs/TB1ut3xxbsrBKNjSZFpXXcXhFXa-846-786.png)\n" +
                "> ###### 10点20分发布 [天气](http://www.thinkpage.cn/) \n");
        request.setMarkdown(markdown);

        //文本
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent("大家好！我是 DingTalkRobot 机器人，很高兴为你们服务!");
        request.setText(text);
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setAtMobiles(Arrays.asList("130****1239"));
        // isAtAll类型如果不为Boolean，请升级至最新SDK
        at.setIsAtAll(true);
        at.setAtUserIds(Arrays.asList("109929", "32099"));
        request.setAt(at);

        try {
            OapiRobotSendResponse response = DingTalkUtils.client.execute(request);
            System.out.println("【DingTalkUtils】消息发送演示示例 响应参数：" + JSON.toJSONString(response));
        } catch (ApiException e) {
            log.error("[ApiException]: 消息发送演示示例, 异常捕获{}", e.getMessage());
        }
    }

    /**
     * 发送普通文本消息
     *
     * @param content    文本消息
     * @param mobileList 指定@ 联系人
     * @param isAtAll    是否@ 全部联系人
     * @return OapiRobotSendResponse
     */
    public static OapiRobotSendResponse sendMessageByText(String content, List<String> mobileList, boolean isAtAll) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }

        //参数	参数类型	必须	说明
        //msgtype	String	是	消息类型，此时固定为：text
        //content	String	是	消息内容
        //atMobiles	Array	否	被@人的手机号(在content里添加@人的手机号)
        //isAtAll	bool	否	@所有人时：true，否则为：false
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        if (!CollectionUtils.isEmpty(mobileList)) {
            // 发送消息并@ 以下手机号联系人
            OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
            at.setAtMobiles(mobileList);
            at.setIsAtAll(isAtAll);
            request.setAt(at);
        }
        request.setMsgtype(DingTalkUtils.MSG_TYPE_TEXT);
        request.setText(text);

        OapiRobotSendResponse response = new OapiRobotSendResponse();
        try {
            response = DingTalkUtils.client.execute(request);
            System.out.println("【DingTalkUtils】发送普通文本消息 响应参数：" + JSON.toJSONString(response));
        } catch (ApiException e) {
            log.error("[发送普通文本消息]: 发送消息失败, 异常捕获{}", e.getMessage());
        }
        return response;
    }

    /**
     * 发送link 类型消息
     *
     * @param title      消息标题
     * @param text       消息内容
     * @param messageUrl 点击消息后跳转的url
     * @param picUrl     插入图片的url
     * @return OapiRobotSendResponse
     */
    public static OapiRobotSendResponse sendMessageByLink(String title, String text, String messageUrl, String picUrl) {
        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(text) || StringUtils.isEmpty(messageUrl)) {
            return null;
        }
        //参数	参数类型	必须	说明
        //msgtype	String	是	消息类型，此时固定为：link
        //title	String	是	消息标题
        //text	String	是	消息内容。如果太长只会部分展示
        //messageUrl	String	是	点击消息跳转的URL
        //picUrl	String	否	图片URL
        OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
        link.setTitle(title);
        link.setText(text);
        link.setMessageUrl(messageUrl);
        link.setPicUrl(picUrl);

        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype(DingTalkUtils.MSG_TYPE_LINK);
        request.setLink(link);

        OapiRobotSendResponse response = new OapiRobotSendResponse();
        try {
            response = DingTalkUtils.client.execute(request);
            System.out.println("【DingTalkUtils】发送link 响应参数：" + JSON.toJSONString(response));
        } catch (ApiException e) {
            log.error("[发送link 类型消息]: 发送消息失败, 异常捕获{}", e.getMessage());
        }
        return response;
    }


    /**
     * 发送Markdown 编辑格式的消息
     *
     * @param title        标题
     * @param markdownText 支持markdown 编辑格式的文本信息
     * @param mobileList   消息@ 联系人
     * @param isAtAll      是否@ 全部
     * @return OapiRobotSendResponse
     */
    public static OapiRobotSendResponse sendMessageByMarkdown(String title, String markdownText, List<String> mobileList, boolean isAtAll) {
        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(markdownText)) {
            return null;
        }

        //参数	类型	必选	说明
        //msgtype	String	是	此消息类型为固定markdown
        //title	String	是	首屏会话透出的展示内容
        //text	String	是	markdown格式的消息
        //atMobiles	Array	否	被@人的手机号(在text内容里要有@手机号)
        //isAtAll	bool	否	@所有人时：true，否则为：false
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(title);
        markdown.setText(markdownText);

        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype(DingTalkUtils.MSG_TYPE_MARKDOWN);
        request.setMarkdown(markdown);
        if (!CollectionUtils.isEmpty(mobileList)) {
            OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
            at.setIsAtAll(isAtAll);
            at.setAtMobiles(mobileList);
            request.setAt(at);
        }

        OapiRobotSendResponse response = new OapiRobotSendResponse();
        try {
            response = DingTalkUtils.client.execute(request);
            System.out.println("【DingTalkUtils】发送link 响应参数：" + JSON.toJSONString(response));
        } catch (ApiException e) {
            log.error("[发送link 类型消息]: 发送消息失败, 异常捕获{}", e.getMessage());
        }
        return response;
    }

    /**
     * 整体跳转ActionCard类型的消息发送
     *
     * @param title          消息标题, 会话消息会展示标题
     * @param markdownText   markdown格式的消息
     * @param singleTitle    单个按钮的标题
     * @param singleURL      单个按钮的跳转链接
     * @param btnOrientation 是否横向排列(true 横向排列, false 纵向排列)
     * @param hideAvatar     是否隐藏发消息者头像(true 隐藏头像, false 不隐藏)
     * @return OapiRobotSendResponse
     */
    public static OapiRobotSendResponse sendMessageByActionCardSingle(String title, String markdownText, String singleTitle, String singleURL, boolean btnOrientation, boolean hideAvatar) {
        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(markdownText)) {
            return null;
        }
        //参数	类型	必选	说明
        //    msgtype	string	true	此消息类型为固定actionCard
        //    title	string	true	首屏会话透出的展示内容
        //    text	string	true	markdown格式的消息
        //    singleTitle	string	true	单个按钮的方案。(设置此项和singleURL后btns无效)
        //    singleURL	string	true	点击singleTitle按钮触发的URL
        //    btnOrientation	string	false	0-按钮竖直排列，1-按钮横向排列
        //    hideAvatar	string	false	0-正常发消息者头像，1-隐藏发消息者头像
        OapiRobotSendRequest.Actioncard actionCard = new OapiRobotSendRequest.Actioncard();
        actionCard.setTitle(title);
        actionCard.setText(markdownText);
        actionCard.setSingleTitle(singleTitle);
        actionCard.setSingleURL(singleURL);
        // 此处默认为0
        actionCard.setBtnOrientation(btnOrientation ? "1" : "0");
        // 此处默认为0
        actionCard.setHideAvatar(hideAvatar ? "1" : "0");

        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype(DingTalkUtils.MSG_TYPE_ACTION_CARD);
        request.setActionCard(actionCard);
        OapiRobotSendResponse response = new OapiRobotSendResponse();
        try {
            response = DingTalkUtils.client.execute(request);
            System.out.println("【DingTalkUtils】整体跳转ActionCard类型的发送消息 响应参数：" + JSON.toJSONString(response));
        } catch (ApiException e) {
            log.error("[发送ActionCard 类型消息]: 整体跳转ActionCard类型的发送消息失败, 异常捕获{}", e.getMessage());
        }
        return response;
    }

    /**
     * 独立跳转ActionCard类型 消息发送
     *
     * @param title          标题
     * @param markdownText   文本
     * @param btns           按钮列表
     * @param btnOrientation 是否横向排列(true 横向排列, false 纵向排列)
     * @param hideAvatar     是否隐藏发消息者头像(true 隐藏头像, false 不隐藏)
     * @return OapiRobotSendResponse
     */
    public static OapiRobotSendResponse sendMessageByActionCardMulti(String title, String markdownText, List<OapiRobotSendRequest.Btns> btns, boolean btnOrientation, boolean hideAvatar) {
        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(markdownText) || CollectionUtils.isEmpty(btns)) {
            return null;
        }
        //参数	类型	必选	说明
        //msgtype	string	true	此消息类型为固定actionCard
        //title	string	true	首屏会话透出的展示内容
        //text	string	true	markdown格式的消息
        //btns	array	true	按钮的信息：title-按钮方案，actionURL-点击按钮触发的URL
        //btnOrientation	string	false	0-按钮竖直排列，1-按钮横向排列
        //hideAvatar	string	false	0-正常发消息者头像，1-隐藏发消息者头像
        OapiRobotSendRequest.Actioncard actionCard = new OapiRobotSendRequest.Actioncard();
        actionCard.setTitle(title);
        actionCard.setText(markdownText);
        // 此处默认为0
        actionCard.setBtnOrientation(btnOrientation ? "1" : "0");
        // 此处默认为0
        actionCard.setHideAvatar(hideAvatar ? "1" : "0");

        actionCard.setBtns(btns);

        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype(DingTalkUtils.MSG_TYPE_ACTION_CARD);
        request.setActionCard(actionCard);
        OapiRobotSendResponse response = new OapiRobotSendResponse();
        try {
            response = DingTalkUtils.client.execute(request);
            System.out.println("【DingTalkUtils】独立跳转ActionCard类型发送消息 响应参数：" + JSON.toJSONString(response));
        } catch (ApiException e) {
            log.error("[发送ActionCard 类型消息]: 独立跳转ActionCard类型发送消息失败, 异常捕获{}", e.getMessage());
        }
        return response;
    }

    /**
     * 发送FeedCard类型消息
     *
     * @param links
     * @return OapiRobotSendResponse
     */
    public static OapiRobotSendResponse sendMessageByFeedCard(List<OapiRobotSendRequest.Links> links) {
        if (CollectionUtils.isEmpty(links)) {
            return null;
        }

        //msgtype	string	true	  此消息类型为固定feedCard
        //title	string	true	      单条信息文本
        //messageURL	string	true  点击单条信息到跳转链接
        //picURL	string	true	  单条信息后面图片的URL
        OapiRobotSendRequest.Feedcard feedcard = new OapiRobotSendRequest.Feedcard();
        feedcard.setLinks(links);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype(DingTalkUtils.MSG_TYPE_FEED_CARD);
        request.setFeedCard(feedcard);
        OapiRobotSendResponse response = new OapiRobotSendResponse();
        try {
            response = DingTalkUtils.client.execute(request);
            System.out.println("【DingTalkUtils】独立跳转ActionCard类型发送消息 响应参数：" + JSON.toJSONString(response));
        } catch (ApiException e) {
            log.error("[发送ActionCard 类型消息]: 独立跳转ActionCard类型发送消息失败, 异常捕获{}", e.getMessage());
        }
        return response;
    }

    /**
     * 获取签名
     * 把timestamp+"\n"+密钥当做签名字符串，使用HmacSHA256算法计算签名，然后进行Base64 encode，最后再把签名参数再进行urlEncode，得到最终的签名（需要使用UTF-8字符集）。
     * timestamp 当前时间戳，单位是毫秒，与请求调用时间误差不能超过1小时。
     * secret 密钥，机器人安全设置页面，加签一栏下面显示的SEC开头的字符串。
     *
     * @param
     * @return java.lang.String
     */
    private static String sign() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Long timestamp = System.currentTimeMillis();
        String stringToSign = timestamp + "\n" + SECRET;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(SECRET.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        return "&timestamp=" + timestamp + "&sign=" + sign;
    }

    /**
     * 测试
     *
     * @param args
     * @return void
     */
    public static void mainsad(String args[]) {
        //官方演示示例
        //sdkDemoJava();

        //发送普通文本消息
        //ArrayList<String> userList = ListUtil.toList("130****1239");
        //sendMessageByText("大家好！我是 DingTalkRobot 机器人，很高兴为你们服务!", userList, true);

        //发送link 类型消息
        //String title = "时代的火车向前开";
        //String text = "这个即将发布的新版本，创始人阿Q称它为红树林。而在此之前，每当面临重大升级，产品经理们都会取一个应景的代号，这一次，为什么是红树林";
        //String messageUrl = "https://www.dingtalk.com/";
        //String picUrl = "https://gw.alicdn.com/tfs/TB1ut3xxbsrBKNjSZFpXXcXhFXa-846-786.png";
        //sendMessageByLink(title, text, messageUrl, picUrl);

        //发送Markdown 编辑格式的消息
        //String title = "南京的天气真好呀";
        //String markdownText = "#### 南京的天气真好呀 @130****1239\n" +
        //        "> 9度，西北风1级，空气良89，相对温度73%\n\n" +
        //        "> ![screenshot](https://gw.alicdn.com/tfs/TB1ut3xxbsrBKNjSZFpXXcXhFXa-846-786.png)\n" +
        //        "> ###### 10点20分发布 [天气](http://www.thinkpage.cn/) \n";
        //List<String> mobileList = ListUtil.toList("130****1239");
        //boolean isAtAll = true;
        //sendMessageByMarkdown(title, markdownText, mobileList, isAtAll);

        //整体跳转ActionCard类型的消息发送
        //String title = "南京的天气真好呀";
        //String markdownText = "#### 南京的天气真好呀 @130****1239\n" +
        //        "> 9度，西北风1级，空气良89，相对温度73%\n\n" +
        //        "> ![screenshot](https://gw.alicdn.com/tfs/TB1ut3xxbsrBKNjSZFpXXcXhFXa-846-786.png)\n" +
        //        "> ###### 10点20分发布 [天气](http://www.thinkpage.cn/) \n";
        //String singleTitle = "百度一下,啥也不知道";
        //String singleURL = "https://www.baidu.com/";
        //boolean btnOrientation = true;
        //boolean hideAvatar = false;
        //sendMessageByActionCardSingle(title, markdownText, singleTitle, singleURL, btnOrientation, hideAvatar);

        //独立跳转ActionCard类型 消息发送
        //String title = "南京的天气真好呀";
        //String markdownText = "#### 南京的天气真好呀 @130****1239\n" +
        //        "> 9度，西北风1级，空气良89，相对温度73%\n\n" +
        //        "> ![screenshot](https://gw.alicdn.com/tfs/TB1ut3xxbsrBKNjSZFpXXcXhFXa-846-786.png)\n" +
        //        "> ###### 10点20分发布 [天气](http://www.thinkpage.cn/) \n";
        //
        //OapiRobotSendRequest.Btns btn = new OapiRobotSendRequest.Btns();
        //btn.setTitle("按钮-钉钉开放文档");
        //btn.setActionURL("https://open.dingtalk.com/");
        //List<OapiRobotSendRequest.Btns> btns = ListUtil.toList(btn);
        //boolean btnOrientation = true;
        //boolean hideAvatar = false;
        //sendMessageByActionCardMulti(title, markdownText, btns, btnOrientation, hideAvatar);

        //发送FeedCard类型消息
        OapiRobotSendRequest.Links link = new OapiRobotSendRequest.Links();
        link.setTitle("消息工程服务:文超说今晚在醉仙楼摆两桌");
//        link.setMessageURL("https://www.dingtalk.com/");
//        link.setPicURL("https://gw.alicdn.com/tfs/TB1ut3xxbsrBKNjSZFpXXcXhFXa-846-786.png");
        List<OapiRobotSendRequest.Links> links = ListUtil.toList(link);
        sendMessageByFeedCard(links);

    }

    public static void main(String[] args) {
//        OapiRobotSendRequest.Links link = new OapiRobotSendRequest.Links();
//        link.setTitle("消息工程服务:文超说今晚在醉仙楼摆两桌");
////        link.setMessageURL("https://www.dingtalk.com/");
////        link.setPicURL("https://gw.alicdn.com/tfs/TB1ut3xxbsrBKNjSZFpXXcXhFXa-846-786.png");
//        List<OapiRobotSendRequest.Links> links = ListUtil.toList(link);
        List<String> mobileList =new ArrayList<>();
        mobileList.add("13544385327");
        sendMessageByText("消息工程服务:唱，跳，rap，篮球", mobileList, true);
//        sendMessageByFeedCard(links);
    }
}
