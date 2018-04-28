/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints;

import com.amt.entities.UserEntity;
import com.amt.utils.NotificationSessions;
import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.common.UserAccessControl;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
                NotificationSessions.sessions.put(session, session);
            } else {
                throw new AccessError(ERROR_TYPE.USER_NOT_AUTHORISED);
            }
        } catch (AccessError ex) {
            try {
                session.close();
            } catch (IOException ex1) {

            }
        }

    }

    @OnClose
    public void close(Session session) {
        NotificationSessions.sessions.remove(session);
        try {
            session.close();
        } catch (IOException ex) {
            Logger.getLogger(NotificationsEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public NotificationsEndpoint() {

    }

}
