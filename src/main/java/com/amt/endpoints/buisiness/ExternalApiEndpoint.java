/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.buisiness;

import com.amt.common.data.GEOSql;
import com.amt.entities.auth.User;
import com.amt.entities.buisiness.DispatchOrder;
import com.amt.entities.buisiness.NotificationEntity;
import com.amt.entities.management.ApiUser;
import com.amt.entities.management.GeographicalArea;
import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import com.tna.endpoints.AuthorisedEndpoint;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
@WebServlet("/api/*")
public class ExternalApiEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String string) throws AccessError {
        long apiUser = getApiUser(string);
        JSONObject andQuery = new JSONObject();
        andQuery.put("wasHandled", false);
        andQuery.put("apiUser", apiUser);
        return Persistence.readByProperties(NotificationEntity.class, andQuery);
    }

    @Override
    public JSONObject doCreate(JSONObject jsono, String string) throws AccessError {
        long apiUser = getApiUser(string);
        jsono.put("apiUser", apiUser);
        jsono.put("wasSeen", false);
        jsono.put("wasHandled", false);
        JSONObject query = new JSONObject();
        query.put("geographicalAreaId", GEOSql.liesWithinPolygon(GeographicalArea.class, jsono));
        return Persistence.create(NotificationEntity.class, jsono);
    }

    @Override
    public JSONObject doUpdate(JSONObject jsono, long l, String string) throws AccessError {
        throw new AccessError(ERROR_TYPE.USER_NOT_ALLOWED);
    }

    @Override
    public JSONObject doRead(long l, String string) throws AccessError {
        long apiUser = getApiUser(string);
        boolean complete = false;
        JSONObject query = new JSONObject();
        query.put("apiUser", apiUser);
        query.put("id", l);
        JSONObject response = new JSONObject();
        JSONObject notification = Persistence.readByProperties(NotificationEntity.class, query);
        if (notification != null) {
            if ((boolean) (notification.get("wasHandled")) == true) {
                response = Persistence.read(DispatchOrder.class, (long) notification.get("dispatchOrderId"));
                response.put("status", "processed");

            } else {
                response.put("status", "processing");
            }
        } else {
            throw new AccessError(ERROR_TYPE.ENTITY_NOT_FOUND);
        }
        return response;
    }

    @Override
    public JSONObject doDelete(long l, String string) throws AccessError {
        throw new AccessError(ERROR_TYPE.USER_NOT_ALLOWED);
    }

    private long getApiUser(String token) throws AccessError {
        UserAccessControl.authOperation(User.class, token, 2);
        JSONObject query1 = new JSONObject();
        query1.put("userId", UserAccessControl.fetchUserByToken(User.class, token).get("id"));
        JSONObject apiUser = Persistence.readByProperties(ApiUser.class, query1);
        if (apiUser == null) {
            throw new AccessError(AccessError.ERROR_TYPE.USER_NOT_ALLOWED);
        } else {
            return (long) apiUser.get("id");
        }

    }

}
