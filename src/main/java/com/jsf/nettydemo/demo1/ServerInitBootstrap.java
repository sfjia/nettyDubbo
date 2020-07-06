package com.jsf.nettydemo.demo1;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-06-22 15:52
 */
public class ServerInitBootstrap {

    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1",8848);
    }
}
