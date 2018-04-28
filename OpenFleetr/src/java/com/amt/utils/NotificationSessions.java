/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.utils;


import java.util.HashMap;
import javax.websocket.Session;

/**
 *
 * @author tareq
 */
public class NotificationSessions {
    public static NotificationSessions sessionManager  = NotificationSessions.getInstance();
    public static HashMap<Session,Session> sessions ;

    public static NotificationSessions getInstance() {
           if(sessionManager == null){
            return new NotificationSessions();
        }else{
            return sessionManager;
        }
    }
    
    private NotificationSessions(){
        sessions = new HashMap();
    }
    
    
}
