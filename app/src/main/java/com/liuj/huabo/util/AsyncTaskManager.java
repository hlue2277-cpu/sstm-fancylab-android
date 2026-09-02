package com.liuj.huabo.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liujun on 2020/4/2.
 */
public class AsyncTaskManager {
    private static   ExecutorService dbService;

    static {
         dbService = Executors.newFixedThreadPool(5);
    }

    private static AsyncTaskManager _instance;

    private AsyncTaskManager(){

    }

    public static AsyncTaskManager getInstance(){
        if(_instance == null){
            synchronized (AsyncTaskManager.class) {
                if(_instance == null) {
                    _instance = new AsyncTaskManager();
                }
            }
        }
        return _instance;
    }


    /**
     * 执行一个任务
     * @param task
     */
    public void executeTask(Runnable task){
        dbService.execute(task);
    }



}
