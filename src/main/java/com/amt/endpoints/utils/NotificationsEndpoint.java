/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.utils;

import com.amt.common.cache.NotificationEntityCache;
import com.amt.common.data.GEOSql;
import com.amt.common.sessions.SessionManager;
import com.amt.entities.auth.User;
import com.amt.common.sessions.UserSession;
import com.amt.entities.management.Dispatcher;
import com.amt.entities.buisiness.Vehicle;
import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
            user = UserAccessControl.fetchUserByToken(User.class, token);
            long level = (long) user.get("level");
            if (level > 2) {
                UserSession userSession = new UserSession(token, (long) user.get("id"), (long) user.get("level"), session);
                SessionManager.addUserSession(token, userSession);
            } else {
                throw new AccessError(ERROR_TYPE.USER_NOT_AUTHORISED);
            }
        } catch (AccessError ex) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Goodbye"));
            } catch (IOException ex1) {

            }
        }

    }

    @OnClose
    public void close(Session session) {
        Set<String> tokens = SessionManager.sessionsTokenSet();
        for (String token : tokens) {
            UserSession userSession = SessionManager.get(token);
            if (userSession.getToken().equals(token)) {
                DoClose(token);
            }
        }
    }

    @OnError
    public void onError(Throwable t, Session session) throws Throwable {
        Set<String> tokens = SessionManager.sessionsTokenSet();
        for (String token : tokens) {
            UserSession userSession = SessionManager.get(token);
            if (userSession.getUserSession().equals(session)) {
                SessionManager.lock(token);
                try {
                    userSession.getUserSession().close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "Goodbye"));
                } catch (IOException ex) {
                    Logger.getLogger(NotificationsEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    SessionManager.removeUserSession(token);
                    SessionManager.removeTokenGeographicalArea(token);

                }
            }
        }

    }

    public void DoClose(String token) {
        UserSession userSession = SessionManager.get(token);
        SessionManager.lock(token);
        try {
            userSession.getUserSession().close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Goodbye"));
        } catch (IOException ex) {
            Logger.getLogger(NotificationsEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            SessionManager.removeUserSession(token);
            SessionManager.removeTokenGeographicalArea(token);
        }

    }

    public NotificationsEndpoint() {

    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        if (message != null && !"".equals(message)) {
            try {
                JSONObject json;
                json = (JSONObject) new JSONParser().parse(message);
                JSONObject response = new JSONObject();
                response.put("type", "recommendation");
                response.put("array",GEOSql.fetchNearestVehicles(Vehicle.class, json));
                Set<String> tokens = SessionManager.sessionsTokenSet();
                for (String token : tokens) {
                    UserSession userSession = SessionManager.get(token);
                    if (userSession.getUserSession().equals(session)) {
                        SessionManager.lock(token);
                        try {
                            session.getBasicRemote().sendText(response.toJSONString());
                        } catch (IOException ex) {

                        } finally {
                            SessionManager.unlock(token);
                        }

                    }
                }
            } catch (AccessError | ParseException ex) {
                Logger.getLogger(NotificationsEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
