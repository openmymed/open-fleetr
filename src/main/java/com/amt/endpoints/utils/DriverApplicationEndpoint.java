package com.amt.endpoints.utils;

import com.amt.common.sessions.DispatcherSession;
import com.amt.common.sessions.DispatcherSessionManager;
import com.amt.common.sessions.DriverSession;
import com.amt.common.sessions.DriverSessionManager;
import com.amt.entities.auth.User;
import com.amt.entities.buisiness.DispatchOrder;
import com.amt.entities.buisiness.Vehicle;
import com.amt.entities.management.Driver;
import com.tna.common.AccessError;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
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

@ServerEndpoint("/app/driver/{token}")
public class DriverApplicationEndpoint {

    private static final String AVAILABILITY = "availability";
    private static final String AVAILABLE = "available";
    private static final String BUSY = "busy";

    @OnOpen
    public void open(@PathParam("token") String token, Session session) {
        try {
            JSONObject user = UserAccessControl.fetchUserByToken(User.class, token);
            long level = (long) user.get("level");

            if (level == 1) {

                JSONObject driverQuery = new JSONObject();
                driverQuery.put("userId", user.get("id"));
                JSONObject readDriver = Persistence.readByProperties(Driver.class, driverQuery);
                JSONObject vehicleQuery = new JSONObject();
                vehicleQuery.put("driver", readDriver.get("id"));
                JSONObject readVehicle = Persistence.readByProperties(Vehicle.class, vehicleQuery);
                DriverSession driverSession = new DriverSession(token, session, (long) ((int) readVehicle.get("id")));
                fillQueue(driverSession);
                DriverSessionManager.putDriverSession(driverSession);
            } else {
                throw new AccessError(AccessError.ERROR_TYPE.USER_NOT_AUTHORISED);
            }
        } catch (AccessError ex) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, "Goodbye"));
            } catch (IOException ex1) {

            }
        }
    }

    @OnClose
    public void close(Session session) {
        Set<String> tokens = DispatcherSessionManager.sessionsTokenSet();
        for (String token : tokens) {
            DispatcherSession userSession = DispatcherSessionManager.get(token);
            if (userSession.getToken().equals(token)) {
                DoClose(token);
            }
        }
    }

    @OnError
    public void onError(Throwable t, Session session) throws Throwable {
        DriverSession userSession = DriverSessionManager.getDriverSession(session);
        userSession.setAvailable(false);
        try {
            userSession.getUserSession().close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "Goodbye"));
        } catch (IOException ex) {
            Logger.getLogger(DriverApplicationEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DriverSessionManager.unsubscribeDriver(userSession.getUserSession());
        }

    }

    public void DoClose(String token) {
        DriverSession userSession = DriverSessionManager.getDriverSession(token);
        userSession.setAvailable(false);
        try {
            userSession.getUserSession().close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Goodbye"));
        } catch (IOException ex) {
            Logger.getLogger(DriverApplicationEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DriverSessionManager.unsubscribeDriver(userSession.getUserSession());
        }

    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        if (message != null && !"".equals(message)) {
            JSONObject json = null;
            try {
                json = (JSONObject) new JSONParser().parse(message);
            } catch (ParseException ex) {
                Logger.getLogger(DriverApplicationEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                json = null;
            }
            if (json != null) {
                String action = (String) json.get("action");
                switch (action) {
                    case AVAILABILITY:
                        String status = (String) json.get("status");
                        switch (status) {
                            case AVAILABLE:
                                DriverSessionManager.getDriverSession(session).setAvailable(true);
                                break;
                            case BUSY:
                                DriverSessionManager.getDriverSession(session).setAvailable(true);
                                break;
                            default:
                                DriverSessionManager.getDriverSession(session).setAvailable(true);
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void fillQueue(DriverSession driverSession) {
        driverSession.lock();
        try {
            JSONObject query = new JSONObject();
            query.put("vehicleId", driverSession.getVehicleId());
            query.put("status", 0);
            JSONObject response1 = Persistence.listByProperties(DispatchOrder.class, query);
            query.put("status", 1);
            JSONObject response2 = Persistence.listByProperties(DispatchOrder.class, query);
            JSONObject response = new JSONObject();

            if (response1 != null) {
                response.putAll(response1);
            }
            if (response2 != null) {
                response.putAll(response2);
            }
            Set keyset = response.keySet();
            for (Object key : keyset) {
                System.out.println("queing initially");
                driverSession.queue((long) ((int) key));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally{
            driverSession.unlock();
        }
    }
}
