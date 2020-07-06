package com.jsf.nettydemo.demo2.server;

import com.jsf.nettydemo.demo2.net.request.RequestFrameDecoder;
import com.jsf.nettydemo.demo2.net.response.ResponseFrameEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-06-22 17:15
 */
public class Server {

    public static void main(String[] args) {

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();


        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            ChannelFuture channelFuture = serverBootstrap.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().
                                    addLast(new ResponseFrameEncoder())
                                    .addLast(new RequestFrameDecoder())
                                    .addLast(new DispatcherHandler());
                        }
                    }).bind("127.0.0.1", 8848);
            System.out.println("服务器启动并在8848端口监听");

            channelFuture.channel().close().sync();

            System.out.println("通道被关闭");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }

        System.out.println("服务器停止");
    }
}
