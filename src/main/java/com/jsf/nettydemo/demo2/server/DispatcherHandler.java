package com.jsf.nettydemo.demo2.server;

import com.jsf.nettydemo.demo2.net.request.Request;
import com.jsf.nettydemo.demo2.net.response.Response;
import com.jsf.nettydemo.demo2.server.bs.ServiceExecutor;
import com.jsf.nettydemo.demo2.util.LogUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-07-03 15:38
 */
public class DispatcherHandler extends ChannelInboundHandlerAdapter {

    private static ExecutorService bussinessService = Executors.newFixedThreadPool(10);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = (Request) msg;
        //服务调用
        if(request.getRequestType() == 1){
            //这里有线程切换
            bussinessService.submit(new Task(request,ctx));

        }else {
            //心跳包
            LogUtil.log("服务器准备响应请求ID:" + request.getRequestId() + "心跳包");
            ctx.writeAndFlush(new Response(request.getRequestId(),(short)0,"ok"));
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx,cause);
    }

    private class Task implements Runnable{

        private Request request;
        private ChannelHandlerContext context;

        public Task(Request request, ChannelHandlerContext context){
            this.request = request;
            this.context = context;
        }

        @Override
        public void run() {
            /**
             * 这里的哲学 在业务线程中调用 ChannelHanlerContext 的 write方法，
             * netty会检测当前运行的线程是否在Channel所绑定的线程模型中，如果不在，会立即返回，并将任务提交给
             * 与Channel绑定的线程池，故与通道的读、写等操作，都会在Channel绑定的线程池中执行，也就是通常说的IO线程。
             * 如果在在业务线程中执行write方法，会发送一次线程切换，故dubbo提供了多种转发策略，例如心跳包，等没有必要在业务线程池
             * 中执行，而是直接在IO线程中执行的原因所在。
             */
            context.writeAndFlush(ServiceExecutor.execute(request));
        }
    }
}
