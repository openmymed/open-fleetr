/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.utils;

import com.amt.common.sessions.AuthenticatedNotificationSessionManager;
import com.amt.entities.auth.UserEntity;
import com.amt.common.sessions.UserSession;
import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.common.UserAccessControl;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
@ServerEndpoint("/notifications/{token}")
public class NotificationsEndpoint {

    @OnOpen
    public void open(@PathParam("token") String token, Session session) {
        JSONObject user;
        try {
            user = UserAccessControl.fetchUserByToken(UserEntity.class, token);
            if ((long) user.get("level") >= 2) {
                UserSession userSession = new UserSession(token, (long) user.get("id"), (long) user.get("level"), session);
                AuthenticatedNotificationSessionManager.addUserSession(token, userSession);
            } else {
                throw new AccessError(ERROR_TYPE.USER_NOT_AUTHORISED);
            }
        } catch (AccessError ex) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE,"Goodbye"));
            } catch (IOException ex1) {

            }
        }

    }

    @OnClose
    public void close(Session session) {
        Set<String> tokens = AuthenticatedNotificationSessionManager.sessionsTokenSet();
        for(String token : tokens){
            UserSession userSession = AuthenticatedNotificationSessionManager.get(token);
            if(userSession.getToken().equals(token)){
                close(token);
            }
    }
    }
    public void close(String token){
        UserSession userSession = AuthenticatedNotificationSessionManager.get(token);
        AuthenticatedNotificationSessionManager.lock(token);
                try {
                    userSession.getUserSession().close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE,"Goodbye"));       
                } catch (IOException ex) {
                    Logger.getLogger(NotificationsEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                }finally{
                AuthenticatedNotificationSessionManager.removeUserSession(token);
                AuthenticatedNotificationSessionManager.unlock(token);
                }
                
            }
        
    

    public NotificationsEndpoint() {

    }

}
