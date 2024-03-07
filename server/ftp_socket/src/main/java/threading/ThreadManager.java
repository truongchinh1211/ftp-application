/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author User
 */
public class ThreadManager {

    private static ThreadManager instance;
    private ExecutorService executorService;

    public static ThreadManager getInstance() {
        if (instance != null) {
            return instance;
        }

        instance = new ThreadManager();
        instance.executorService = Executors.newCachedThreadPool();
        return instance;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
