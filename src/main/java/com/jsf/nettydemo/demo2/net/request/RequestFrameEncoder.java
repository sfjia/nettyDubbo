package com.jsf.nettydemo.demo2.net.request;

import com.jsf.nettydemo.demo2.util.LogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-07-03 11:13
 *
 * q请求消息编码器 自定义协议  用于客户端
 *
 *
 *
 * 自定义协议如下
 *      协议头：
 *          4字节的请求序列号  2字节的请求类型  2字节数据长度
 *      请求体：
 *          2字节服务调用长度 N字节的服务调用名 2字节请求参数长度  请求参数
 *
 *  其中请求类型为1：服务调用2：心跳包，如果是心跳包的话，只有协议头，也就是【4字节请求序列号，2字节请求类型，2字节数据长度】
 */
public class RequestFrameEncoder  extends MessageToByteEncoder<Request> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Request request, ByteBuf byteBuf) throws Exception {

        LogUtil.log("requestFrameEncoder  call  begin .... requestId:%s, requestType:%s ",request.getRequestId(),request.getRequestType());
        //写入 requestId
        byteBuf.writeInt(request.getRequestId());
        //写入 requestType
        byteBuf.writeShort(request.getRequestType());

        if(request.getRequestType() == 1 ){
            byte[] serviceData = request.getService().getBytes();
            byte[] paramData = request.getParams().getBytes();

            int slen, plen;
            //写入 2字节的 数据长度
            byteBuf.writeShort( (2 + (slen = serviceData.length) +
                                 2 + (plen = paramData.length )));
            byteBuf.writeShort(slen);
            byteBuf.writeBytes(serviceData);
            byteBuf.writeShort(plen);
            byteBuf.writeBytes(paramData);


        }else if(request.getRequestType() == 2){
            //心跳包
            byteBuf.writeShort(0);
        }

        LogUtil.log("requestFrameEncoder  call  end .... requestId:%s, requestType:%s ",request.getRequestId(),request.getRequestType());

    }
}
