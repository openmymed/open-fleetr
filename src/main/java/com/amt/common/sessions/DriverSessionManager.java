/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.sessions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.Session;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public class DriverSessionManager {

    private static DriverSessionManager sessionManager;
    private static HashMap<Long, DriverSession> vehicleDriverSessions;
    private static HashMap<Session, Long> sessionVehicleIds;
    private static ReentrantLock lock;

    public static DriverSessionManager getInstance() {
        if (DriverSessionManager.sessionManager == null) {
            synchronized (DriverSessionManager.class) {
                if (DriverSessionManager.sessionManager == null) {
                    DriverSessionManager.sessionManager = new DriverSessionManager();
                }
            }
        }
        return DriverSessionManager.sessionManager;
    }

    public static void lock(Long driverId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private DriverSessionManager() {
        DriverSessionManager.sessionVehicleIds = new HashMap();
        DriverSessionManager.vehicleDriverSessions = new HashMap();
        DriverSessionManager.lock = new ReentrantLock();
    }

    public static void setAvailable(Session session, boolean state) {
        Long sessionId = DriverSessionManager.getInstance().sessionVehicleIds.get(session);
        DriverSession driverSession = DriverSessionManager.getInstance().vehicleDriverSessions.get(sessionId);
        driverSession.lock();
        try {
            driverSession.setAvailable(state);
        } finally {
            DriverSessionManager.getInstance().vehicleDriverSessions.put(sessionId, driverSession);

            driverSession.unlock();
        }
    }

    public static boolean isAvailable(Session session) {

        Long sessionId = DriverSessionManager.getInstance().sessionVehicleIds.get(session);
        DriverSession driverSession = DriverSessionManager.getInstance().vehicleDriverSessions.get(sessionId);
        driverSession.lock();
        try {
            return driverSession.isAvailable();
        } finally {
            driverSession.unlock();
        }
    }

    public static void subscribeDriver(DriverSession driverSession) {
        DriverSessionManager.getInstance().lock();
        try {
            DriverSessionManager.getInstance().vehicleDriverSessions.put(driverSession.getVehicleId(), driverSession);
            DriverSessionManager.getInstance().sessionVehicleIds.put(driverSession.getUserSession(), driverSession.getVehicleId());
            DriverSessionManager.getInstance().lock();
        } finally {
            DriverSessionManager.getInstance().unlock();
        }
    }

    public static void unsubscribeDriver(Session session) {
        DriverSessionManager.getInstance().lock();
        try {
            Long sessionId = DriverSessionManager.getInstance().sessionVehicleIds.get(session);
            DriverSession driverSession = DriverSessionManager.getInstance().vehicleDriverSessions.get(sessionId);
            DriverSessionManager.getInstance().sessionVehicleIds.remove(session);
            DriverSessionManager.getInstance().vehicleDriverSessions.remove(sessionId);
            driverSession = null;
        } finally {
            DriverSessionManager.getInstance().unlock();
        }
    }
    
    public static Set<Long> getVehiclesKeyset(){
        return DriverSessionManager.getInstance().vehicleDriverSessions.keySet();
    }
    
    public static DriverSession getDriverSession(Long vehicleId){
        return DriverSessionManager.getInstance().vehicleDriverSessions.get(vehicleId);
    }
    
    public static DriverSession getDriverSession(String token){
        for (DriverSession session : DriverSessionManager.getInstance().vehicleDriverSessions.values()) {
            if(session.getToken().equals(token)){
                return session;
            }
        }
        return null;
    }
    
    public static void putDriverSession(DriverSession session){
        DriverSessionManager.getInstance().lock();
        try{
            DriverSessionManager.getInstance().vehicleDriverSessions.put(session.getVehicleId(), session);
            DriverSessionManager.getInstance().sessionVehicleIds.put(session.getUserSession(),session.getVehicleId());
        }finally{
            DriverSessionManager.getInstance().unlock();
        }
    }

    private static void lock() {
        DriverSessionManager.sessionManager.lock.lock();
    }

    private static void unlock() {
        DriverSessionManager.sessionManager.lock.unlock();

    }

}
