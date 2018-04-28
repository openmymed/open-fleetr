/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.utils;

import com.amt.common.cache.CurrentLocationEntityCache;
import com.amt.common.cachemanager.CurrentDispatchOrderEntityCacheManager;
import com.amt.common.cachemanager.CurrentLocationEntityCacheManager;
import com.amt.common.cachemanager.CurrentStatusEntityCacheManager;
import com.tna.data.Access;
import com.tna.utils.Initialization;
import java.util.ArrayList;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author tareq
 */
@WebListener
public class Init extends Initialization {
    ArrayList<Thread> pollThreads;
    @Override
    public void onInit() {
       pollThreads = new ArrayList();
       Access.setHost("localhost");
       Access.setDatabase("OpenFleetr");
       Access.setUsername("api_user");
       Access.setPassword("pass1234");     
       Access.pool.initialize(5);
       pollThreads.add(new Thread(new CurrentLocationEntityCacheManager()));
       pollThreads.add(new Thread(new CurrentStatusEntityCacheManager()));
       pollThreads.add(new Thread(new CurrentDispatchOrderEntityCacheManager()));
       startThreads();
    }

    @Override
    public void onDestroy() {
        stopThreads();
    }
    
    public void startThreads(){
        pollThreads.forEach((thread) -> {
            thread.start();
        });
    }
    
    public void stopThreads(){
        pollThreads.forEach((thread) -> {
            thread.interrupt();
        });
    }
}
