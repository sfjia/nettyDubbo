package com.jsf.nettydemo.demo2.net;

import com.jsf.nettydemo.demo2.net.response.Response;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-07-03 14:19'
 *
 *  响应凭证
 *
 */
public class ResponseFuture {

    private Response response;

    public Response getResult(){
        if(response != null){
            return response;
        }

        synchronized (this){
            if(response !=null){
                return response;
            }

            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return  response;
    }

    public void setResult(Response result){
        this.response = result;
        synchronized (this){
            this.notifyAll();
        }
    }
}
