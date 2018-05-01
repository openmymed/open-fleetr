/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.utils;

import com.amt.common.cachemanager.CurrentDispatchOrderEntityCacheManager;
import com.amt.common.cachemanager.CurrentLocationEntityCacheManager;
import com.amt.common.cachemanager.CurrentStatusEntityCacheManager;
import com.amt.common.sessions.AuthenticatedNotificationSessionManager;
import com.tna.data.Access;
import com.tna.utils.Initialization;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author tareq
 */
@WebListener
public class Init extends Initialization {

    Thread locationPoll;
    Thread statusPoll;
    Thread dispatchPoll;

    @Override
    public void onInit() {
        Access.setHost("database");
        Access.setDatabase("OpenFleetr");
        Access.setUsername("app_user");
        Access.setPassword("pass1234");
        Access.pool.initialize(5);
        locationPoll = (new Thread(new CurrentLocationEntityCacheManager()));
        statusPoll = (new Thread(new CurrentStatusEntityCacheManager()));
        dispatchPoll = (new Thread(new CurrentDispatchOrderEntityCacheManager()));
        startThreads();
    }

    @Override
    public void onDestroy() {
        stopThreads();
        //AuthenticatedNotificationSessionManager.closeAllSessions();
    }

    public void startThreads() {
        locationPoll.start();
        statusPoll.start();
        dispatchPoll.start();

    }

    public void stopThreads() {
        locationPoll.interrupt();
        statusPoll.interrupt();
        dispatchPoll.interrupt();
    }
}
