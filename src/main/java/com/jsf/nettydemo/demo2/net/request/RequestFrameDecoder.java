package com.jsf.nettydemo.demo2.net.request;

import com.jsf.nettydemo.demo2.util.LogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-07-03 11:35
 *
 *
 *
 *
 * 请求消息   解码器，实现自定义协议,,,用于服务端
 *
 * 自定义协议如下
 *      请求头：
 *          4字节的请求序列号 2字节的请求类型 2字节数据长度
 *      请求体：
 *          2字节服务调用长度 N字节的服务调用名 2字节请求参数长度 请求参数
 *
 * 其中请求类型为1：服务调用2：心跳包，如果是心跳包的话，只有协议头，也就是【4字节请求序列号，2字节请求类型，2字节数据长度】
 *
 * @author prestigeding@126.com
 *
 */
public class RequestFrameDecoder extends ByteToMessageDecoder {

    private static final int HEAD_LENGTH = 8;

    /*
     *
     * channelHandlerContext :  Handler执行环境
     *
     * in : 当前累积缓存区字节
     *
     * list : 解码后的消息
     *
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {

        LogUtil.log("RequestFrameDecoder call begin ... ");

        int r = in.readableBytes();
        LogUtil.log("RequestFrameDecoder 当前累积缓存区可读数据长度为%d", r);

        if(r < HEAD_LENGTH){
            LogUtil.log("RequestFrameDecoder 当前累积缓存区可读数据长度为%d,小于协议头的长度%d", r, HEAD_LENGTH);
            return;
        }

        //获取当前 位置
        int currentIndex = in.readerIndex();

        int requestId = in.getInt(currentIndex);  // 0-3字节 请求id

        short requestType = in.getShort(currentIndex + 4); // 4-5 字节  请求类型

        short contentLength = in.getShort(currentIndex + 4 + 2);// 6-8 //数据长度

        LogUtil.log("RequestFrameDecoder requestId:%d,requestType:%d,contentLength:%d", requestId, requestType, contentLength);

        Request request = null;
        if(requestType == 1) {
            //服务调用
            if (r < (HEAD_LENGTH + contentLength)) {
                LogUtil.log("RequestFrameDecoder 当前累积缓存区可读数据长度为%d,小于协议头的长度%d", r, HEAD_LENGTH + contentLength);
                return;
            }

            // 直接读取
            in.skipBytes(HEAD_LENGTH);

            short slen = in.readShort();
            byte[] serviceData = new byte[slen];
            in.readBytes(serviceData,0,slen);

            short plen = in.readShort();
            byte[] paramData = new byte[plen];
            in.readBytes(paramData,0,plen);

            list.add(request = new Request(requestId,requestType,new String(serviceData),new String(paramData)));
            //上层用不到
            request.setContentLength(contentLength);

            LogUtil.log("RequestFrameDecoder 成功从服务端响应中解析出业务包,requestId:%d", requestId);
        }else if (requestType == 2){
            //心跳 包
            in.skipBytes(HEAD_LENGTH);

            list.add(request = new Request(requestId,requestType));

            //上层用不到
            request.setContentLength(contentLength);

            LogUtil.log("RequestFrameDecoder 成功从服务端响应中解析出业务包,requestId:%d", requestId);
        }




    }
}
