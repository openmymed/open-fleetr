/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.sessions;

import java.io.IOException;
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
public class AuthenticatedNotificationSessionManager {

    private static AuthenticatedNotificationSessionManager sessionManager;
    private static HashMap<String, UserSession> userSessions;

    private static AuthenticatedNotificationSessionManager getInstance() {
        if (sessionManager == null) {
            synchronized(AuthenticatedNotificationSessionManager.class){
                if(sessionManager == null){
            sessionManager = new AuthenticatedNotificationSessionManager();
            }
        }    

    }
         return sessionManager;
}
    private AuthenticatedNotificationSessionManager() {
        userSessions = new HashMap();
    }

    public synchronized static UserSession get(String token){
        return AuthenticatedNotificationSessionManager.getInstance().userSessions.get(token);
    }
    
    public static void lock(String token) {
    AuthenticatedNotificationSessionManager.getInstance().userSessions.get(token).lock.lock();      
    }

    public static void unlock(String token) {
        AuthenticatedNotificationSessionManager.getInstance().userSessions.get(token).lock.unlock();
    }

    public synchronized static Set sessionsTokenSet() {
        return AuthenticatedNotificationSessionManager.getInstance().userSessions.keySet();
    }
    

    public synchronized static void addUserSession(String token, UserSession userSession) {
        if(userSessions.containsKey(token)){
            try {
                userSession.getUserSession().close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT,"User already has a connection"));
            } catch (IOException ex) {

            }
        }else{
        AuthenticatedNotificationSessionManager.getInstance().userSessions.put(token, userSession);
        }

    }

    public synchronized static void removeUserSession(String token) {
        AuthenticatedNotificationSessionManager.getInstance().userSessions.remove(token);
    }

    public synchronized static void closeAllSessions() {
        Set<String> sessionTokens = sessionsTokenSet();
        for (String token : sessionTokens) {
            UserSession userSession = AuthenticatedNotificationSessionManager.getInstance().userSessions.get(token);
            removeUserSession(token);
            try {
                userSession.getUserSession().close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY,"Server is being shut down"));
            } catch (IOException ex) {
                Logger.getLogger(AuthenticatedNotificationSessionManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
