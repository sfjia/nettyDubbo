package com.jsf.nettydemo.demo1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-06-22 15:59
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("msg = " + msg);
        String start = "service#impl#";
        if(msg.toString().startsWith(start)){
            String hello = new HelloServiceImp().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(hello);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(" 服务调用异常！！！！ " );
        ctx.close();
    }
}
