/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.utils;

import com.amt.common.cache.NotificationEntityCache;
import com.amt.common.data.GEOSql;
import com.amt.common.sessions.AuthenticatedNotificationSessionManager;
import com.amt.entities.auth.UserEntity;
import com.amt.common.sessions.UserSession;
import com.amt.entities.buisiness.CurrentLocationEntity;
import com.amt.entities.management.DispatcherEntity;
import com.amt.entities.management.JurisdictionEntity;
import com.amt.entities.management.VehicleEntity;
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
            user = UserAccessControl.fetchUserByToken(UserEntity.class, token);
            long level = (long) user.get("level");
            if (level > 2) {
                UserSession userSession = new UserSession(token, (long) user.get("id"), (long) user.get("level"), session);
                AuthenticatedNotificationSessionManager.addUserSession(token, userSession);

                JSONObject query1 = new JSONObject();
                query1.put("userId", user.get("id"));
                JSONObject dispatcher = Persistence.readByProperties(DispatcherEntity.class, query1);

                JSONObject query2 = new JSONObject();
                query2.put("dispatcherId", dispatcher.get("id"));
                JSONObject jurisdictions = Persistence.listByProperties(JurisdictionEntity.class, query2);

                for (Object key : jurisdictions.keySet()) {
                    JSONObject jurisdiction = (JSONObject) jurisdictions.get(key);
                    AuthenticatedNotificationSessionManager.setTokenAreaId((long) jurisdiction.get("geographicalAreaId"), token);
                }

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
        Set<String> tokens = AuthenticatedNotificationSessionManager.sessionsTokenSet();
        for (String token : tokens) {
            UserSession userSession = AuthenticatedNotificationSessionManager.get(token);
            if (userSession.getToken().equals(token)) {
                DoClose(token);
            }
        }
    }

    @OnError
    public void onError(Throwable t, Session session) throws Throwable {
        Set<String> tokens = AuthenticatedNotificationSessionManager.sessionsTokenSet();
        for (String token : tokens) {
            UserSession userSession = AuthenticatedNotificationSessionManager.get(token);
            if (userSession.getUserSession().equals(session)) {
                AuthenticatedNotificationSessionManager.lock(token);
                try {
                    userSession.getUserSession().close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "Goodbye"));
                } catch (IOException ex) {
                    Logger.getLogger(NotificationsEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    AuthenticatedNotificationSessionManager.removeUserSession(token);
                    AuthenticatedNotificationSessionManager.removeTokenGeographicalArea(token);

                }
            }
        }

    }

    public void DoClose(String token) {
        UserSession userSession = AuthenticatedNotificationSessionManager.get(token);
        AuthenticatedNotificationSessionManager.lock(token);
        try {
            userSession.getUserSession().close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Goodbye"));
        } catch (IOException ex) {
            Logger.getLogger(NotificationsEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            AuthenticatedNotificationSessionManager.removeUserSession(token);
            AuthenticatedNotificationSessionManager.removeTokenGeographicalArea(token);
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
                response.put("array",GEOSql.fetchNearestVehicles(CurrentLocationEntity.class, json));
                Set<String> tokens = AuthenticatedNotificationSessionManager.sessionsTokenSet();
                for (String token : tokens) {
                    UserSession userSession = AuthenticatedNotificationSessionManager.get(token);
                    if (userSession.getUserSession().equals(session)) {
                        AuthenticatedNotificationSessionManager.lock(token);
                        try {
                            session.getBasicRemote().sendText(response.toJSONString());
                        } catch (IOException ex) {

                        } finally {
                            AuthenticatedNotificationSessionManager.unlock(token);
                        }

                    }
                }
            } catch (AccessError | ParseException ex) {
                Logger.getLogger(NotificationsEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
