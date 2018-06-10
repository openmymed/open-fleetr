/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.utils;

import com.amt.common.cachemanager.CurrentDispatchOrderEntityCacheManager;
import com.amt.common.cachemanager.CurrentLocationEntityCacheManager;
import com.amt.common.cachemanager.CurrentStatusEntityCacheManager;
import com.amt.common.cachemanager.NotificationThread;
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
    Thread notificationThread;

    @Override
    public void onInit() {
        Access.setHost("localhost");
        Access.setDatabase("OpenFleetr");
        Access.setUsername("app_user");
        Access.setPassword("app_password");
        Access.pool.initialize(5);
        locationPoll = (new Thread(new CurrentLocationEntityCacheManager()));
        statusPoll = (new Thread(new CurrentStatusEntityCacheManager()));
        dispatchPoll = (new Thread(new CurrentDispatchOrderEntityCacheManager()));
        notificationThread = (new Thread(new NotificationThread()));
        startThreads();
    }

    @Override
    public void onDestroy() {
        stopThreads();
        AuthenticatedNotificationSessionManager.closeAllSessions();
    }

    public void startThreads() {
        locationPoll.start();
        statusPoll.start();
        dispatchPoll.start();
        notificationThread.start();

    }

    public void stopThreads() {
        locationPoll.interrupt();
        statusPoll.interrupt();
        dispatchPoll.interrupt();
        notificationThread.interrupt();
    }
}
