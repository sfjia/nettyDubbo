package com.jsf.nettydemo.demo1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-06-22 16:09
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context; //上下文
    private String result;
    private String para;

    /**
     *
     * @param ctx
     * @throws Exception
     *
     * 与服务器的链接创建后，就会被调用，这个方法第一个被调用（1）
     *
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive  被调用 。。。。  " );
        context = ctx;
    }


    /**
     * 收到服务器的数据后，调用方法（4）
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(" channelRead 被调用。。。 " );
        result = msg.toString();
        System.out.println("back result  = " + result);
        notify(); //唤醒等待线程
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    /**
     * 被代理对象，发送数据给服务器，-> wait - > 等待被唤醒（channelRead） -> 返回结果 （3） ->  5
     * @return
     * @throws Exception
     */
    @Override
    public synchronized Object call() throws Exception {
        System.out.println(" call1 被调用 。。。" );
        context.writeAndFlush(para);
        //惊醒wait
        wait(); //等待 channelRead 方法获取到服务器的结果后，唤醒
        System.out.println("call2 被调用 。。。 ");
        return result;
    }

    //
    void setPara(String para){
        System.out.println("setPara " );
        this.para = para;
    }
}
