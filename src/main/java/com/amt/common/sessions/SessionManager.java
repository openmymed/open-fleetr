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
public class SessionManager {

    private static SessionManager sessionManager;
    private static HashMap<String, UserSession> userSessions;
    private static HashMap<Long, ArrayList<String>> geographicalAreas;
    private static HashMap<Session,Integer> sessionState;

    private static SessionManager getInstance() {
        if (sessionManager == null) {
            synchronized (SessionManager.class) {
                if (sessionManager == null) {
                    sessionManager = new SessionManager();
                }
            }

        }
        return sessionManager;
    }

    private SessionManager() {
        userSessions = new HashMap();
        geographicalAreas = new HashMap();
        sessionState = new HashMap();
    }

    public static void startAction(Session s){
        SessionManager.getInstance().sessionState.put(s,0);
    }
    public Integer getSessionState(Session s){
       Integer state = SessionManager.getInstance().sessionState.get(s);
       if(state == null){
           startAction(s);
           state = 0;
       }
       return state;
    }
    
    public void setSessionState(Session s, Integer i){
        SessionManager.getInstance().sessionState.put(s,i);
    }
    
    public static ArrayList<String> getByArea(long areaId) {
        return SessionManager.getInstance().geographicalAreas.get(areaId);
    }

    public synchronized static void setTokenAreaId(long areaId, String token) {
      if(!SessionManager.getInstance().geographicalAreas.containsKey(areaId)){
          SessionManager.getInstance().geographicalAreas.put(areaId, new ArrayList<>());    
      }
      SessionManager.getInstance().geographicalAreas.get(areaId).add(token);

    }

    public synchronized static void removeTokenGeographicalArea(String token) {

        Set<Long> keySet = SessionManager.getInstance().geographicalAreas.keySet();
        for (Long key : keySet) {
            while (SessionManager.getInstance().geographicalAreas.get(key).remove(token)) {
            }
        }
    }

    public synchronized static UserSession get(String token) {
        return SessionManager.getInstance().userSessions.get(token);
    }

    public static void lock(String token) {
        SessionManager.getInstance().userSessions.get(token).lock.lock();
    }

    public static void unlock(String token) {
        SessionManager.getInstance().userSessions.get(token).lock.unlock();
    }

    public synchronized static Set sessionsTokenSet() {
        return SessionManager.getInstance().userSessions.keySet();
    }

    public synchronized static void addUserSession(String token, UserSession userSession) {
        if (SessionManager.getInstance().userSessions.containsKey(token)) {
            try {
                userSession.getUserSession().close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "User already has a connection"));
            } catch (IOException ex) {

            }
        } else {
            SessionManager.getInstance().userSessions.put(token, userSession);
        }

    }

    public synchronized static void removeUserSession(String token) {
        SessionManager.getInstance().userSessions.remove(token);
    }

    public synchronized static void closeAllSessions() {
        Set<String> sessionTokens = sessionsTokenSet();
        for (String token : sessionTokens) {
            UserSession userSession = SessionManager.getInstance().userSessions.get(token);
            removeUserSession(token);
            try {
                userSession.getUserSession().close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, "Server is being shut down"));
            } catch (IOException ex) {
                Logger.getLogger(SessionManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
