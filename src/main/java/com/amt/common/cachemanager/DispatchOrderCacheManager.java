/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.cachemanager;

import com.amt.common.cache.DispatchOrderCache;
import com.amt.common.sessions.DispatcherSessionManager;
import com.amt.common.sessions.DriverSession;
import com.amt.common.sessions.DriverSessionManager;
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

    private static String HOSTNAME = "unknown";
    private static final String RESPONSE_TYPE = "dispatchOrder";

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
                    Set keySet = differentialList.keySet();
                    Set<String> userTokenSet = DispatcherSessionManager.sessionsTokenSet();
                    ArrayList<Long> changedOrders = new ArrayList();

                    for (Object key : keySet) {

                        JSONObject dispatchOrder = (JSONObject) differentialList.get(key);
                        long vehicleId = (int)dispatchOrder.get("vehicleId");
                        long id = (int) dispatchOrder.get("id");
                        DriverSession ds = DriverSessionManager.getDriverSession(vehicleId);
                        if(ds!=null){
                            if ((int)dispatchOrder.get("status") !=2) {
                                System.out.println("queing");
                                ds.queue(id);
                            }
                        }
                        changedOrders.add(id);
                    }
                   
                    JSONObject resp = new JSONObject();
                    resp.put("server", DispatchOrderCacheManager.HOSTNAME);
                    resp.put("type", DispatchOrderCacheManager.RESPONSE_TYPE);
                    resp.put("array", changedOrders);
                    for (String token : userTokenSet) {
                        new Thread(() -> {
                            DispatcherSessionManager.lock(token);
                            try {
                                Session userSession = DispatcherSessionManager.get(token).getUserSession();
                                userSession.getBasicRemote().sendText(resp.toJSONString());
                            } catch (IOException ex) {
                                Logger.getLogger(VehicleCacheManager.class.getName()).log(Level.SEVERE, null, ex);
                            } finally {
                                DispatcherSessionManager.unlock(token);
                            }
                        }).start();
                    }
                }
            } catch (AccessError ex) {
                handleError(ex);
            } finally {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    break;
                }
            }
        }
    }

    public static void handleError(AccessError ex) {

    }

    public DispatchOrderCacheManager() {
        try {
            DispatchOrderCacheManager.HOSTNAME = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            DispatchOrderCacheManager.HOSTNAME = "unknown";
            Logger.getLogger(DispatchOrderCacheManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        DispatchOrderCache.getInstance();
    }
}
