/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.cachemanager;

import com.amt.common.cache.CurrentDispatchOrderEntityCache;
import com.amt.entities.CurrentDispatchOrderEntity;
import com.tna.common.AccessError;
import com.tna.common.AuthenticatedNotificationSessionManager;
import com.tna.data.Persistence;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public class CurrentDispatchOrderEntityCacheManager implements Runnable {

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            long systemTime = System.currentTimeMillis();
            Timestamp now = new Timestamp(systemTime - (systemTime % 1000));
            Timestamp cacheTime = CurrentDispatchOrderEntityCache.getTimeStamp();

            try {
                JSONObject differentialList = Persistence.listNewerThan(CurrentDispatchOrderEntity.class, cacheTime);
                if (differentialList != null) {
                    Set keySet = differentialList.keySet();
                                            Set<Session> userSessionSet = AuthenticatedNotificationSessionManager.sessionsSet();

                    for (Object key : keySet) {
                        JSONObject listItem = (JSONObject) differentialList.get(key);
                        long vehicleId = (int) listItem.get("vehicleId");
                        for (Session userSession : userSessionSet) {
                            new Thread(() -> {
                               AuthenticatedNotificationSessionManager.checkout(userSession);
                                try {
                                    userSession.getBasicRemote().sendText("{\"dispatchOrder\":" + vehicleId + "}");
                                } catch (IOException ex) {
                                    Logger.getLogger(CurrentDispatchOrderEntityCacheManager.class.getName()).log(Level.SEVERE, null, ex);
                                }finally{
                                AuthenticatedNotificationSessionManager.checkin(userSession);
                                }
                            }).start();
                       CurrentDispatchOrderEntityCache.cache(vehicleId, listItem);

                    }
                }
            }} catch (AccessError ex) {
                handleError(ex);
            } finally {
                CurrentDispatchOrderEntityCache.setTimeStamp(now);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    break;
                }
            }
        }
    }

    public static void handleError(AccessError ex) {

    }

    public   CurrentDispatchOrderEntityCacheManager() {
        CurrentDispatchOrderEntityCache.getInstance();
    }
}
