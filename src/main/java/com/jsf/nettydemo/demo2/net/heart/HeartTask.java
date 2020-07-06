package com.jsf.nettydemo.demo2.net.heart;

import com.jsf.nettydemo.demo2.net.NettyClientConnection;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-07-03 15:23
 */
public class HeartTask implements Runnable {

    private NettyClientConnection clientConnection;

    public HeartTask(NettyClientConnection connection){
        this.clientConnection = connection;
    }

    @Override
    public void run() {
        System.out.println(" 发送心跳包 ");
        clientConnection.sendHeartMsg();
    }
}
