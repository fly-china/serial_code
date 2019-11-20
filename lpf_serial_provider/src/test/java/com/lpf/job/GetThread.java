package com.lpf.job;

import java.util.concurrent.CountDownLatch;

/**
 * Created by ishare on 17-6-16.
 */
public class GetThread extends Thread {
    String url="";
    Job job;
    CountDownLatch latch;
    GetJobInf getJobInf=new GetJobInf();
    public GetThread(String name,CountDownLatch latch) {
        url=name;
        this.latch=latch;
    }

    @Override
    public void run() {
        job=getJobInf.getJob(url);
        latch.countDown();
    }
}
