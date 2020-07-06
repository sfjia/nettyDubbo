package com.jsf.nettydemo.demo2.client;

import com.jsf.nettydemo.demo2.net.NettyClientConnection;
import com.jsf.nettydemo.demo2.net.ResponseFuture;
import com.jsf.nettydemo.demo2.util.LogUtil;

import java.util.concurrent.CountDownLatch;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-07-03 16:32
 */
public class CR implements Runnable{

    private NettyClientConnection clientConnection;
    private CountDownLatch count;

    public CR(NettyClientConnection connection,CountDownLatch count){
        this.clientConnection = connection;
        this.count = count;
    }

    @Override
    public void run() {

        try {
            ResponseFuture future = clientConnection.sendMsg("Lini.TopicService.sendTopic", "{\"req\":\"testSendTopic\"}");
            LogUtil.log("发送请求后，立即做其他事情");
            //然后获取结果，完成操作
            LogUtil.log("处理后，得出响应结果：requestId:%d,data:%s", future.getResult().getRequestId(),future.getResult().getData());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            count.countDown();
        }
    }
}