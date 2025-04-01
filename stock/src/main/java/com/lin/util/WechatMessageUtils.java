package com.lin.util;

import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.Message;
import com.zjiecode.wxpusher.client.bean.MessageResult;
import com.zjiecode.wxpusher.client.bean.Result;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 微信发送信息工具类
 */
public class WechatMessageUtils {
    public static void sendMessageToWechat(String content, String url) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            Message message = new Message();
            message.setAppToken("AT_kU68ar0NGaOp2yldxnN95i3V2xTk3s49");
            message.setContentType(Message.CONTENT_TYPE_HTML);
            message.setContent(content);
            message.setUid("UID_mP7iO03SrfIxm1KGa9xNrIMEEgNG");
            message.setUrl(url);//
            Result<List<MessageResult>> messageResult = WxPusher.send(message);

        }, executor);
    }
}
