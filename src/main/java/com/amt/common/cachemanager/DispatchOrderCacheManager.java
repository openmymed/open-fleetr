/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.cachemanager;

import com.amt.common.cache.DispatchOrderCache;
import com.amt.common.sessions.AuthenticatedNotificationSessionManager;
import com.amt.entities.buisiness.DispatchOrder;
import com.tna.common.AccessError;
import com.tna.data.Persistence;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public class DispatchOrderCacheManager implements Runnable {

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            long systemTime = System.currentTimeMillis();
            Timestamp now = new Timestamp(systemTime);
            Timestamp cacheTime = DispatchOrderCache.getTimeStamp();
            DispatchOrderCache.setTimeStamp(now);

            try {
                JSONObject differentialList = Persistence.listNewerThan(DispatchOrder.class, cacheTime);
                if (differentialList != null) {
                    ArrayList<Long> changedVehicleIds = new ArrayList();
                    Set keySet = differentialList.keySet();
                    Set<String> userTokenSet = AuthenticatedNotificationSessionManager.sessionsTokenSet();

                    for (Object key : keySet) {
                        JSONObject listItem = (JSONObject) differentialList.get(key);
                        long vehicleId = (int) listItem.get("vehicleId");
                        changedVehicleIds.add(vehicleId);
                        DispatchOrderCache.cache(vehicleId, listItem);

                    }
                    JSONObject resp = new JSONObject();
                    try {
                        resp.put("server",InetAddress.getLocalHost().getHostName());
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(DispatchOrderCacheManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    resp.put("type","dispatchOrder");
                    resp.put("array",changedVehicleIds);
                    for (String token : userTokenSet) {
                        new Thread(() -> {
                            AuthenticatedNotificationSessionManager.lock(token);
                            try {
                                Session userSession = AuthenticatedNotificationSessionManager.get(token).getUserSession();
                                userSession.getBasicRemote().sendText(resp.toJSONString());
                            } catch (IOException ex) {
                                Logger.getLogger(DispatchOrderCacheManager.class.getName()).log(Level.SEVERE, null, ex);
                            } finally {
                                AuthenticatedNotificationSessionManager.unlock(token);
                            }
                        }).start();
                    }
                }
            } catch (AccessError ex) {
                handleError(ex);
            } finally {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    break;
                }
            }
        }
    }

    public static void handleError(AccessError ex) {

    }

    public DispatchOrderCacheManager() {
        DispatchOrderCache.getInstance();
    }
}
