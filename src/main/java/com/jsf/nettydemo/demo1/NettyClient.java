package com.jsf.nettydemo.demo1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-06-22 16:09
 */
public class NettyClient {

    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NettyClientHandler clientHandler;

    private int count = 0;

    public Object getBean(final Class<?> serviceClass,final String provideName){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class[]{serviceClass},(proxy,method,args)->{
            System.out.println(" proxy,method,args  进入...."+  ++count  +"次" );
            if(clientHandler == null){
                initClient();
            }
            clientHandler.setPara(provideName+args[0]);
            return executor.submit(clientHandler).get();
        });
    }

    //初始化客户端
    private void initClient() {
        clientHandler = new NettyClientHandler();
        NioEventLoopGroup  group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        ChannelPipeline pipeline = sc.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(clientHandler);
                    }
                });

        try {
            bootstrap.connect("127.0.0.1",8848).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
