package com.jsf.nettydemo.demo2.client;

import com.jsf.nettydemo.demo2.net.NettyClientConnection;

import java.util.concurrent.CountDownLatch;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-06-22 17:17
 *
 * 模拟一个长链接
 */
public class Client {

    public static void main(String[] args) {
        System.out.println("客户端准备建立到服务器的连接");
        NettyClientConnection connection = new NettyClientConnection("127.0.0.1",8848);
        System.out.println("客户端开始发送链接到服务器 ");

        int n = 10 ;
        CountDownLatch count = new CountDownLatch(n);

        for (int i = 0; i < n; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Thread(new CR(connection,count)).start();
        }

        try {
            count.await();
            System.out.println("客户端关闭连接");
            connection.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(" 客户端退出！ ");

    }
}
