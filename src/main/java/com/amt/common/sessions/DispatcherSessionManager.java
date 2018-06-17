/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.sessions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.Session;

/**
 *
 * @author tareq
 */
public class DispatcherSessionManager {

    private static DispatcherSessionManager sessionManager;
    private static HashMap<String, DispatcherSession> userSessions;
    private static HashMap<Long, ArrayList<String>> geographicalAreas;
    private static HashMap<Session,Integer> sessionState;

    private static DispatcherSessionManager getInstance() {
        if (sessionManager == null) {
            synchronized (DispatcherSessionManager.class) {
                if (sessionManager == null) {
                    sessionManager = new DispatcherSessionManager();
                }
            }

        }
        return sessionManager;
    }

    public static void unlock(Long driverId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private DispatcherSessionManager() {
        userSessions = new HashMap();
        geographicalAreas = new HashMap();
        sessionState = new HashMap();
    }

    public static void startAction(Session s){
        DispatcherSessionManager.getInstance().sessionState.put(s,0);
    }
    public Integer getSessionState(Session s){
       Integer state = DispatcherSessionManager.getInstance().sessionState.get(s);
       if(state == null){
           startAction(s);
           state = 0;
       }
       return state;
    }
    
    public void setSessionState(Session s, Integer i){
        DispatcherSessionManager.getInstance().sessionState.put(s,i);
    }
    
    public static ArrayList<String> getByArea(long areaId) {
        return DispatcherSessionManager.getInstance().geographicalAreas.get(areaId);
    }

    public synchronized static void setTokenAreaId(long areaId, String token) {
      if(!DispatcherSessionManager.getInstance().geographicalAreas.containsKey(areaId)){
          DispatcherSessionManager.getInstance().geographicalAreas.put(areaId, new ArrayList<>());    
      }
      DispatcherSessionManager.getInstance().geographicalAreas.get(areaId).add(token);

    }

    public synchronized static void removeTokenGeographicalArea(String token) {

        Set<Long> keySet = DispatcherSessionManager.getInstance().geographicalAreas.keySet();
        for (Long key : keySet) {
            while (DispatcherSessionManager.getInstance().geographicalAreas.get(key).remove(token)) {
            }
        }
    }

    public synchronized static DispatcherSession get(String token) {
        return DispatcherSessionManager.getInstance().userSessions.get(token);
    }

    public static void lock(String token) {
        DispatcherSessionManager.getInstance().userSessions.get(token).lock.lock();
    }

    public static void unlock(String token) {
        DispatcherSessionManager.getInstance().userSessions.get(token).lock.unlock();
    }

    public synchronized static Set sessionsTokenSet() {
        return DispatcherSessionManager.getInstance().userSessions.keySet();
    }

    public synchronized static void addUserSession(String token, DispatcherSession userSession) {
        if (DispatcherSessionManager.getInstance().userSessions.containsKey(token)) {
            try {
                userSession.getUserSession().close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "User already has a connection"));
            } catch (IOException ex) {

            }
        } else {
            DispatcherSessionManager.getInstance().userSessions.put(token, userSession);
        }

    }

    public synchronized static void removeUserSession(String token) {
        DispatcherSessionManager.getInstance().userSessions.remove(token);
    }

    public synchronized static void closeAllSessions() {
        Set<String> sessionTokens = sessionsTokenSet();
        for (String token : sessionTokens) {
            DispatcherSession userSession = DispatcherSessionManager.getInstance().userSessions.get(token);
            removeUserSession(token);
            try {
                userSession.getUserSession().close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, "Server is being shut down"));
            } catch (IOException ex) {
                Logger.getLogger(DispatcherSessionManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
