package com.jsf.nettydemo.demo2.net.heart;

import com.jsf.nettydemo.demo2.net.NettyClientConnection;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-07-03 15:04
 */
public class HeartExecutorThreadPool extends ScheduledThreadPoolExecutor {

    private NettyClientConnection clientConnection;

    private int allowFailedCount;

    private int failCount;

   public HeartExecutorThreadPool (int coreSize) {
       super(coreSize);
   }

    public HeartExecutorThreadPool (int coreSize, NettyClientConnection connection , int allowFailedCount ) {
        super(coreSize);
        this.clientConnection = connection;
        this.allowFailedCount = allowFailedCount;
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        //目前这里粗糙的，只要认为发生了异常，就认为通道不可用
        if (t != null) {
            ///如果连续发生错误的次数大于等于允许的最大失败次数，则认为通道关闭
            if (++failCount == allowFailedCount) {

                clientConnection.close(); //关闭通道
            }
        } else {
            failCount = 0;
        }
    }
}
