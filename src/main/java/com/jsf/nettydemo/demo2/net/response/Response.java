package com.jsf.nettydemo.demo2.net.response;

import java.io.Serializable;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-07-03 13:52
 *
 *
 * 服务响应
 *
 */
public class Response implements Serializable {
    //请求号
    private int requestId;
    //响应状态 0：成功  1：失败（非 0 失败）
    private short code;
    // 响应数据
    private String data;

    public Response() {
    }

    public Response(int requestId, short code, String data) {
        this.requestId = requestId;
        this.code = code;
        this.data = data;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public short getCode() {
        return code;
    }

    public void setCode(short code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
