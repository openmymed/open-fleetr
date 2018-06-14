/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.buisiness;

import com.amt.common.data.GEOSql;
import com.amt.entities.auth.User;
import com.amt.entities.buisiness.DispatchOrder;
import com.amt.entities.management.ApiUser;
import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import com.tna.endpoints.AuthorisedEndpoint;
import java.util.Date;
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
        JSONObject user = UserAccessControl.fetchUserByToken(User.class, string);
        System.out.println((long)user.get("level") == 2);
        if ((long)user.get("level") == 2) {
            boolean complete = false;
            JSONObject query = new JSONObject();
            query.put("userId", user.get("id"));
            query.put("status !", 2);
            return Persistence.listByProperties(DispatchOrder.class, query);
        } else {
            throw new AccessError(ERROR_TYPE.USER_NOT_AUTHORISED);
        }
    }

    @Override
    public JSONObject doCreate(JSONObject json, String string) throws AccessError {
        JSONObject user = UserAccessControl.fetchUserByToken(User.class, string);
        if ((long)user.get("level") == 2) {

            json.put("creationDate", new Date().toString());
            json.put("status", 0);
            json.put("userId", user.get("id"));

            return Persistence.create(DispatchOrder.class, json);

        } else {
            throw new AccessError(ERROR_TYPE.USER_NOT_AUTHORISED);
        }
    }

    @Override
    public JSONObject doUpdate(JSONObject jsono, long l, String string) throws AccessError {
        throw new AccessError(ERROR_TYPE.USER_NOT_ALLOWED);
    }

    @Override
    public JSONObject doRead(long l, String string) throws AccessError {
        JSONObject user = UserAccessControl.fetchUserByToken(User.class, string);
        if ((long)user.get("level") == 2) {
            boolean complete = false;
            JSONObject query = new JSONObject();
            query.put("id", l);
            query.put("userId", user.get("id"));
            query.put("status !", 2);
            return Persistence.listByProperties(DispatchOrder.class, query);
        } else {
            throw new AccessError(ERROR_TYPE.USER_NOT_AUTHORISED);
        }
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
