package com.jsf.nettydemo.demo2.net.request;

import java.io.Serializable;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-07-03 10:48
 *
 * 请求信息
 */
public class Request implements Serializable {
    //请求号
    private int requestId;

    //请求类型 1:服务调用 2：数据心跳
    private short requestType;

    //数据包 数据部分长度
    private short contentLength;

    //调用服务名(服务名.方法名),请求类型为1时必须
    private String service;

    //请求参数 json个数字符串 请求类型为1时必须
    private String params;


    public Request() {}

    public Request(int requestId, short requestType) {
        this.requestId = requestId;
        this.requestType = requestType;
    }
    public Request(int requestId, short requestType,String service, String params) {
        this.requestId = requestId;
        this.requestType = requestType;
        this.service = service;
        this.params = params;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public short getRequestType() {
        return requestType;
    }

    public void setRequestType(short requestType) {
        this.requestType = requestType;
    }

    public short getContentLength() {
        return contentLength;
    }

    public void setContentLength(short contentLength) {
        this.contentLength = contentLength;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
