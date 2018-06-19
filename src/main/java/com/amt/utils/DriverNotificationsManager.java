/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.utils;

import com.amt.common.cachemanager.DispatchOrderCacheManager;
import com.amt.common.cachemanager.VehicleCacheManager;
import com.amt.common.sessions.DispatcherSessionManager;
import com.amt.common.sessions.DriverSessionManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public class DriverNotificationsManager implements Runnable {

    private static String HOSTNAME = "unknown";
    private static final String RESPONSE_TYPE = "dispatchOrder";

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            HashSet<Long> availableDriverVehicles = getAvailableDriverVehicles();
            for (Long vehicle : availableDriverVehicles) {
                Long orderId = DriverSessionManager.getDriverSession(vehicle).top();
                if (orderId != null) {
                    System.out.println("order to be sent : " + orderId);
                    new Thread(() -> {
                        JSONObject resp = new JSONObject();
                        resp.put("server", DriverNotificationsManager.HOSTNAME);
                        resp.put("type", DriverNotificationsManager.RESPONSE_TYPE);
                        resp.put("value", orderId);
                        DriverSessionManager.lock(vehicle);
                        try {
                            Session userSession = DriverSessionManager.getDriverSession(vehicle).getUserSession();
                            userSession.getBasicRemote().sendText(resp.toJSONString());
                            DriverSessionManager.setAvailable(vehicle, false);
                        } catch (IOException ex) {
                            Logger.getLogger(VehicleCacheManager.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            DriverSessionManager.unlock(vehicle);
                        }
                    }).start();
                }
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                break;
            }

        }
    }

    private HashSet<Long> getAvailableDriverVehicles() {
        HashSet<Long> availableDriverVehicles = new HashSet();
        Set<Long> keys = DriverSessionManager.getVehiclesKeyset();
        for (Long key : keys) {
            if (DriverSessionManager.getDriverSession(key).isAvailable()) {
                availableDriverVehicles.add(key);
            }
        }
        return availableDriverVehicles;
    }

    public DriverNotificationsManager() {
        try {
            DriverNotificationsManager.HOSTNAME = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            DriverNotificationsManager.HOSTNAME = "unknown";
            Logger.getLogger(DispatchOrderCacheManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
