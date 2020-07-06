package com.jsf.nettydemo.demo2.net.response;

import com.jsf.nettydemo.demo2.util.LogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-07-03 14:02
 *
 *    响应信息解码器  用于客户端
 *
 *  响应报文格式
 *     4字节的请求序列号 2字节的执行状态码 2字节数据长度 多字节的响应数据信息(data)
 */
public class ResponseFrameDecoder extends ByteToMessageDecoder {

    private static final int HEAD_LENGTH = 8;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {

        int r = in.readableBytes();

        LogUtil.log("ResponseFrameDecoder 累积缓存区中可读长度:%d", r);

        if(r < HEAD_LENGTH ) {
            LogUtil.log("ResponseFrameDecoder 当前累积缓存区可读数据长度为%d,小于协议头的长度%d", r, HEAD_LENGTH);
            return;
        }

        int currentIdex = in.readerIndex();

        int requestId = in.getInt(currentIdex); //请求 号
        short code = in.getShort(currentIdex + 4);  // 状态码
        short dataLength = in.getShort(currentIdex + 4 + 2);// 响应数据长度

        LogUtil.log("ResponseFrameDecoder requestId:%d,code:%d,contentLength:%d", requestId, code, dataLength);

        if(r < ( HEAD_LENGTH + dataLength)){
            LogUtil.log("ResponseFrameDecoder 可读长度:%d,需要包长度:%d", r, ( HEAD_LENGTH + dataLength));
            return;
        }

        in.skipBytes(HEAD_LENGTH);
        String data = null;
        if(dataLength > 0){
            byte[] dst = new byte[dataLength];
            in.readBytes(dst,0,dataLength);
            data = new String(dst);
        }else {
            data = "";
        }

        list.add(new Response(requestId,code,data));
        LogUtil.log("ResponseFrameDecoder 成功解析服务端响应信息并准备给下游处理器处理");
    }
}
