/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.cachemanager;

import com.amt.common.cache.CurrentDispatchOrderEntityCache;
import com.amt.common.cache.CurrentStatusEntityCache;
import com.amt.entities.CurrentDispatchOrderEntity;
import com.amt.utils.NotificationSessions;
import com.tna.common.AccessError;
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
            Timestamp now = new Timestamp(System.currentTimeMillis() - 10);
            try {
                JSONObject differentialList = Persistence.listNewerThan(CurrentDispatchOrderEntity.class, CurrentDispatchOrderEntityCache.cache.getTimeStamp());
                if (differentialList != null) {
                    Set keySet = differentialList.keySet();
                    for (Object key : keySet) {
                        JSONObject listItem = (JSONObject) differentialList.get(key);
                        long vehicleId = (int) listItem.get("vehicleId");
                        Set<Session> userSessionSet = NotificationSessions.sessionManager.sessions.keySet();
                        for (Session userSession : userSessionSet) {
                            try {
                                userSession.getBasicRemote().sendText("{\"dispatch\":" + vehicleId + "}");
                            } catch (IOException ex) {
                                Logger.getLogger(CurrentDispatchOrderEntityCacheManager.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        CurrentDispatchOrderEntityCache.cache.cache(vehicleId, listItem);
                    }
                }

            } catch (AccessError ex) {
                handleError(ex);
            } finally {
                CurrentDispatchOrderEntityCache.cache.setTimeStamp(now);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CurrentDispatchOrderEntityCacheManager.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

    }

    public static void handleError(AccessError ex) {

    }

    public CurrentDispatchOrderEntityCacheManager() {
        CurrentStatusEntityCache.getInstance();
    }
}
