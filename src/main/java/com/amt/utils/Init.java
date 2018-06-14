/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.utils;

import com.amt.common.cachemanager.DispatchOrderCacheManager;
import com.amt.common.cachemanager.VehicleCacheManager;
import com.amt.common.sessions.SessionManager;
import com.tna.data.Access;
import com.tna.utils.Initialization;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author tareq
 */
@WebListener
public class Init extends Initialization {

    Thread vehiclePoll;
    Thread dispatchPoll;

    @Override
    public void onInit() {
        Access.setHost("localhost");
        Access.setDatabase("OpenFleetr");
        Access.setUsername("app_user");
        Access.setPassword("app_password");
        Access.pool.initialize(5);
        vehiclePoll = (new Thread(new VehicleCacheManager()));
        dispatchPoll = (new Thread(new DispatchOrderCacheManager()));
        startThreads();
    }

    @Override
    public void onDestroy() {
        stopThreads();
        SessionManager.closeAllSessions();
    }

    public void startThreads() {
        vehiclePoll.start();
        dispatchPoll.start();

    }

    public void stopThreads() {
        vehiclePoll.interrupt();
        dispatchPoll.interrupt();
    }
}
