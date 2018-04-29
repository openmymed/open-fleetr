/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.cachemanager;

import com.amt.common.cache.CurrentLocationEntityCache;
import com.amt.entities.CurrentLocationEntity;
import com.tna.common.AccessError;
import com.tna.common.AuthenticatedNotificationSessionManager;
import com.tna.data.Persistence;
import java.io.IOException;
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
public class CurrentLocationEntityCacheManager implements Runnable {

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            long systemTime = System.currentTimeMillis();
            Timestamp now = new Timestamp(systemTime - (systemTime % 1000));
            Timestamp cacheTime = CurrentLocationEntityCache.getTimeStamp();
            CurrentLocationEntityCache.setTimeStamp(now);

            try {
                JSONObject differentialList = Persistence.listNewerThan(CurrentLocationEntity.class, cacheTime);
                if (differentialList != null) {
                    ArrayList<Long> changedVehicleIds = new ArrayList();
                    Set keySet = differentialList.keySet();
                    Set<Session> userSessionSet = AuthenticatedNotificationSessionManager.sessionsSet();
                    for (Object key : keySet) {
                        JSONObject listItem = (JSONObject) differentialList.get(key);
                        long vehicleId = (int) listItem.get("vehicleId");
                        changedVehicleIds.add(vehicleId);
                        CurrentLocationEntityCache.cache(vehicleId, listItem);
                    }
                    for (Session userSession : userSessionSet) {
                        new Thread(() -> {
                            AuthenticatedNotificationSessionManager.checkout(userSession);
                            try {
                                userSession.getBasicRemote().sendText("{\"location\":" + Arrays.toString(changedVehicleIds.toArray()) + "}");
                            } catch (IOException ex) {
                                Logger.getLogger(CurrentLocationEntityCacheManager.class.getName()).log(Level.SEVERE, null, ex);
                            } finally {
                                AuthenticatedNotificationSessionManager.checkin(userSession);
                            }
                        }).start();
                    }

                }
            } catch (AccessError ex) {
                handleError(ex);
            } finally {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    break;
                }
            }
        }
    }

    public static void handleError(AccessError ex) {

    }

    public CurrentLocationEntityCacheManager() {
        CurrentLocationEntityCache.getInstance();
    }
}
