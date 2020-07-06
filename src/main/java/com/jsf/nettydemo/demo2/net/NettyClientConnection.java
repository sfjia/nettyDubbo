package com.jsf.nettydemo.demo2.net;

import com.jsf.nettydemo.demo2.net.heart.HeartExecutorThreadPool;
import com.jsf.nettydemo.demo2.net.heart.HeartTask;
import com.jsf.nettydemo.demo2.net.request.Request;
import com.jsf.nettydemo.demo2.net.request.RequestFrameEncoder;
import com.jsf.nettydemo.demo2.net.response.Response;
import com.jsf.nettydemo.demo2.net.response.ResponseFrameDecoder;
import com.jsf.nettydemo.demo2.util.LogUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-07-03 14:25
 *
 *
 * netty 客户端 connection 一个NettyConnection 就是一条长连接
 *
 */
public class NettyClientConnection {

    private String serverIp;

    private Integer port;

    private boolean connected ;

    private Bootstrap bootstrap;

    private NioEventLoopGroup workGroup;

    private ChannelFuture channelFuture;

    //客户端请求链接
    private ConcurrentHashMap<Integer, ResponseFuture> requestMap = new ConcurrentHashMap<Integer, ResponseFuture>();

    private AtomicInteger seq = new AtomicInteger(0);

    private HeartExecutorThreadPool heartExecutorThreadPool;


    public NettyClientConnection(String serverIp, int port){
        this.serverIp = serverIp;
        this.port = port;
        bootstrap = new Bootstrap();
        connected = false;
        workGroup = new NioEventLoopGroup();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline()
                                .addLast(new RequestFrameEncoder())
                                .addLast(new ResponseFrameDecoder())
                                .addLast(new DispathcherHandler());
                    }
                });
    }


    /**
     * 建立链接
     */
    private void connect(){
        if(!connected){
            synchronized (this){
                try {
                    if(!connected){
                         channelFuture = bootstrap.connect(serverIp, port).sync();
                         heartExecutorThreadPool = new HeartExecutorThreadPool(0,this,3);
                         connected = true;
                         heartExecutorThreadPool.scheduleAtFixedRate(new HeartTask(this),1,3, TimeUnit.SECONDS);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new RuntimeException("服务链接异常");
                }
            }
        }
    }

    /**
     * 关闭服务
     */
    public void close() {
        try {
            channelFuture.channel().close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
           workGroup.shutdownGracefully();
           if(heartExecutorThreadPool != null){
               heartExecutorThreadPool.shutdownNow();
           }
        }
    }

    /**
     *
     * @param service
     * @param params
     * @return
     *
     * 发送 服务调用
     */
    public ResponseFuture sendMsg(String service,String params){
        int reqId = seq.getAndIncrement();
        System.out.println("客户端requestId:" + reqId);
        return send(new Request(reqId,(short)1,service,params));
    }

    /**
     * 发送心跳包
     * @return
     */
    public ResponseFuture sendHeartMsg() {
        int reqId = seq.getAndIncrement();
        return send(new Request(reqId,(short)2));
    }

    private ResponseFuture send(Request request) {
        if(!connected){
            connect();
        }
        ResponseFuture responseFuture = new ResponseFuture();
        requestMap.putIfAbsent(request.getRequestId(),responseFuture);
        channelFuture.channel().writeAndFlush(request);
        return responseFuture;
    }

    private class DispathcherHandler extends ChannelInboundHandlerAdapter {
        /**
         * 得到服务端的响应后，放入结果中，供客户端使用结果
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                throws Exception {
            LogUtil.log("客户端收到服务端响应，并开始处理数据");
            Response response = (Response)msg;
            LogUtil.log("服务端响应requestId:", response.getRequestId());
            ResponseFuture f = requestMap.remove(response.getRequestId());
            if(f != null) {
                f.setResult(response);
            } else {
                LogUtil.log("警告：客户端丢失请求信息");
            }

        }
    }
}

