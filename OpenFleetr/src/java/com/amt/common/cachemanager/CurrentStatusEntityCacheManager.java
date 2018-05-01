/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.cachemanager;

import com.amt.common.cache.CurrentStatusEntityCache;
import com.amt.common.sessions.AuthenticatedNotificationSessionManager;
import com.amt.entities.buisiness.CurrentStatusEntity;
import com.tna.common.AccessError;
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
public class CurrentStatusEntityCacheManager implements Runnable {

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            long systemTime = System.currentTimeMillis();
            Timestamp now = new Timestamp(systemTime);
            Timestamp cacheTime = CurrentStatusEntityCache.getTimeStamp();
            CurrentStatusEntityCache.setTimeStamp(now);

            try {
                JSONObject differentialList = Persistence.listNewerThan(CurrentStatusEntity.class, cacheTime);
                if (differentialList != null) {
                    ArrayList<Long> changedVechicleIds = new ArrayList();
                    Set keySet = differentialList.keySet();
                    Set<Session> userSessionSet = AuthenticatedNotificationSessionManager.sessionsSet();

                    for (Object key : keySet) {
                        JSONObject listItem = (JSONObject) differentialList.get(key);
                        long vehicleId = (int) listItem.get("vehicleId");
                        changedVechicleIds.add(vehicleId);
                        CurrentStatusEntityCache.cache(vehicleId, listItem);
                    }
                    for (Session userSession : userSessionSet) {
                        new Thread(() -> {
                            AuthenticatedNotificationSessionManager.checkout(userSession);
                            try {
                                userSession.getBasicRemote().sendText("{\"type:\"\"status\",\"array\":" + Arrays.toString(changedVechicleIds.toArray()) + "}");
                            } catch (IOException ex) {
                                Logger.getLogger(CurrentDispatchOrderEntityCacheManager.class.getName()).log(Level.SEVERE, null, ex);
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
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    break;
                }
            }
        }
    }

    public static void handleError(AccessError ex) {

    }

    public CurrentStatusEntityCacheManager() {
        CurrentStatusEntityCache.getInstance();
    }
}
