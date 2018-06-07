/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.cachemanager;

import com.amt.common.cache.NotificationEntityCache;
import com.amt.common.sessions.AuthenticatedNotificationSessionManager;
import com.amt.entities.buisiness.NotificationEntity;
import com.tna.common.AccessError;
import com.tna.data.Persistence;
import java.io.IOException;
import java.net.InetAddress;
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
public class NotificationThread implements Runnable {
   @Override
    public void run() {
        while (!Thread.interrupted()) {
            long systemTime = System.currentTimeMillis();
            Timestamp now = new Timestamp(systemTime);
            Timestamp cacheTime = NotificationEntityCache.getTimeStamp();
            NotificationEntityCache.setTimeStamp(now);

            try {
                JSONObject differentialList = Persistence.listNewerThan(NotificationEntity.class, cacheTime);
                if (differentialList != null) {
                    Set keySet = differentialList.keySet();
                    for(Object key : keySet){
                        JSONObject notification = (JSONObject) differentialList.get(key);
                        long areaId = (long) notification.get("geographicalAreaId");
                        AuthenticatedNotificationSessionManager.getByArea(areaId).forEach((token)-> new Thread(() -> {
                            AuthenticatedNotificationSessionManager.lock(token);
                            try {
                                Session userSession = AuthenticatedNotificationSessionManager.get(token).getUserSession();
                                userSession.getBasicRemote().sendText("{\"server\":\""+InetAddress.getLocalHost().getHostName()+"\";\"type\":\"notification\";\"value\":" +  notification.get("id") + "}");
                            } catch (IOException ex) {
                                Logger.getLogger(NotificationThread.class.getName()).log(Level.SEVERE, null, ex);
                            } finally {
                                AuthenticatedNotificationSessionManager.unlock(token);
                            }
                        }).start());
                    }
                }
            } catch (AccessError ex) {
                handleError(ex);
            } finally {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    break;
                }
            }
        }
    }
        public static void handleError(AccessError ex) {

    }

    public NotificationThread() {
        NotificationEntityCache.getInstance();
    }
       

}

    