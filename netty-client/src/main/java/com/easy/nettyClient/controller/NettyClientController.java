package com.easy.nettyClient.controller;

import com.easy.nettyClient.NettyClient;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;

@RestController
@Scope("prototype")
public class NettyClientController {
    final int count=10000;
    @Autowired
    private NettyClient nettyClient;

    @GetMapping("/send")
    public String send(String msg) {
        MyTask myTask=new MyTask();
        myTask.setNettyClient(nettyClient);
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("nettyclient-pool-%d").build();
        ExecutorService executorService = new ThreadPoolExecutor(2, count*2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        executorService.execute(myTask);
        //executorService.shutdown();
        return "OK";
    }
}
class MyTask implements Runnable {
    public void setNettyClient(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    private NettyClient nettyClient;
    @Override
    public void run() {
        try {
            while(true) {
                nettyClient.sendMsg(Thread.currentThread().getName()+" sent message at "+System.currentTimeMillis());
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
