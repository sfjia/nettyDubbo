package com.jsf.nettydemo.demo1;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-06-22 15:49
 */
public class HelloServiceImp implements HelloService {
    @Override
    public String hello(String name) {
        System.out.println("收到客户端消息 " + name);
        String res = "默认回复！";
        if(null != name ){
            res = "你好，客户端，我已经收到消息「"+name+"」!";
        }else {
            res = "你好，客户端，我已经收到消息!";
        }
        return res;
    }
}
