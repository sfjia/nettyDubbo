package com.jsf.nettydemo.demo2.net.response;

import com.jsf.nettydemo.demo2.util.LogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-07-03 13:57
 *
 * 响应信息 编码器，，用于服务器
 *
 * 响应报文格式
 * 4字节的请求序列号 2字节的执行状态码 2字节数据长度 多字节的响应数据信息(data)
 *
 */
public class ResponseFrameEncoder extends MessageToByteEncoder<Response> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Response response, ByteBuf byteBuf) throws Exception {

        LogUtil.log("ResponseFrameEncoder:服务端编码响应,requestId:%d,code:%d,data:%s", response.getRequestId(), response.getCode(), response.getData());

        byteBuf.writeInt(response.getRequestId());

        byteBuf.writeShort(response.getCode());

        byte[] datas = response.getData().getBytes();

        byteBuf.writeShort(datas.length);

        byteBuf.writeBytes(datas);

        LogUtil.log("ResponseFrameEncoder:服务端编码响应成功");


    }
}
