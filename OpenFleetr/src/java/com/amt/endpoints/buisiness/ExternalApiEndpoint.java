/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.buisiness;

import com.amt.common.data.GEOSql;
import com.amt.entities.auth.UserEntity;
import com.amt.entities.buisiness.CurrentDispatchOrderEntity;
import com.amt.entities.buisiness.NotificationEntity;
import com.amt.entities.management.ApiUserEntity;
import com.amt.entities.management.GeographicalAreaEntity;
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
        query.put("geographicalAreaId", GEOSql.liesWithinPolygon(GeographicalAreaEntity.class, jsono));
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
        JSONObject notification = null;
        while (!complete) {
            notification = Persistence.readByProperties(NotificationEntity.class, query);
            if (notification != null) {
                if ((boolean) (notification.get("wasHandled")) == false) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        complete = true;
                    }
                } else {
                    complete = true;
                }
            } else {
                throw new AccessError(ERROR_TYPE.ENTITY_NOT_FOUND);
            }
        }
        return Persistence.read(CurrentDispatchOrderEntity.class, (long) notification.get("dispatchOrderId"));
    }

    @Override
    public JSONObject doDelete(long l, String string) throws AccessError {
        throw new AccessError(ERROR_TYPE.USER_NOT_ALLOWED);
    }

    private long getApiUser(String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 2);
        JSONObject query1 = new JSONObject();
        query1.put("userId", UserAccessControl.fetchUserByToken(UserEntity.class, token).get("id"));
        JSONObject apiUser = Persistence.readByProperties(ApiUserEntity.class, query1);
        if (apiUser == null) {
            throw new AccessError(AccessError.ERROR_TYPE.USER_NOT_ALLOWED);
        } else {
            return (long) apiUser.get("id");
        }

    }

}
