package com.jsf.nettydemo.demo1;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-06-22 16:08
 */
public class ClientInitBootstrap {

    public static final String provideName = "service#impl#";

    public static void main(String[] args) {
        NettyClient customer = new NettyClient();
        HelloService bean = (HelloService)customer.getBean(HelloService.class, provideName);
        String result = bean.hello("hello");
        System.out.println("result = " + result);
    }
}
