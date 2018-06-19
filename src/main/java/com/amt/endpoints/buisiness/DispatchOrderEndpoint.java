/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.buisiness;

import com.amt.common.cache.DispatchOrderCache;

import com.amt.entities.buisiness.DispatchOrder;

import com.amt.entities.auth.User;
import com.amt.entities.management.Dispatcher;

import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import com.tna.endpoints.AuthorisedEndpoint;
import java.util.Date;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
@WebServlet("/vehicle/dispatch/*")
public class DispatchOrderEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
        UserAccessControl.authOperation(User.class, token, 1);
        JSONObject query = new JSONObject();
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
        return response;
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        JSONObject user = UserAccessControl.fetchUserByToken(User.class, token);
        if ((long) user.get("level") == 3) {

            json.put("creationDate", new Date().toString());
            json.put("status", 0);
            json.put("userId", user.get("id"));

            return Persistence.create(DispatchOrder.class, json);

        } else {
            throw new AccessError(ERROR_TYPE.USER_NOT_AUTHORISED);
        }

    }

    @Override
    public JSONObject doUpdate(JSONObject json, long resource, String token) throws AccessError {
        JSONObject user = UserAccessControl.fetchUserByToken(User.class, token);
        if ((long) user.get("level") == 3) {
            return Persistence.update(DispatchOrder.class, resource, json);
        } else {
            throw new AccessError(ERROR_TYPE.USER_NOT_AUTHORISED);
        }
    }

    @Override
    public JSONObject doRead(long resource, String token) throws AccessError {
        UserAccessControl.authOperation(User.class, token, 1);
        return Persistence.read(DispatchOrder.class, resource);
    }

    @Override
    public JSONObject doDelete(long resource, String token) throws AccessError {
        UserAccessControl.authOperation(User.class, token, 3);
        return Persistence.delete(DispatchOrder.class, resource);
    }

}
