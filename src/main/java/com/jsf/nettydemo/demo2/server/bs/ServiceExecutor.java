package com.jsf.nettydemo.demo2.server.bs;

import com.jsf.nettydemo.demo2.net.request.Request;
import com.jsf.nettydemo.demo2.net.response.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-07-03 15:38
 */
public class ServiceExecutor {

    public static final Map<String, String> serversMap;

    private static final String result_format = "{\"serviceType\":\"%s\",\"params\":\"%s\",\"responseDate\":\"%s\"}";

    static {
        serversMap = new HashMap<String, String>();
        serversMap.put("Lini.TopicService.sendTopic", "S01");
        serversMap.put("Lini.TopicService.topics", "S02");
    }


    public static Response execute(Request request){
        System.out.println("ServiceExecutor 业务执行中。。。。");
        Response response = new Response();

        Random random = new Random();

        try {
            Thread.sleep(random.nextInt(3)*1000);
            response.setRequestId(request.getRequestId());
            String service = request.getService();
            System.out.println("服务调用 service = " + service);
            if(serversMap.containsKey(service)){

                response.setCode((short) 0);
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format = sdf.format(date);

                response.setData(String.format(result_format,serversMap.get(service),request.getParams(),format));

            }else {
                response.setCode((short) 101);
            }
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
            response.setCode((short)1);
        }
        return response;
    }
}
