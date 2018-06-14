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
        UserAccessControl.authOperation(User.class, token, 3);
        return Persistence.list(DispatchOrder.class);
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        JSONObject user = UserAccessControl.fetchUserByToken(User.class, token);
        if (user.get("level").equals(3)) {
            JSONObject dispatcherQuery = new JSONObject();
            dispatcherQuery.put("userId", user.get("id"));
            JSONObject readDispatcher = Persistence.readByProperties(Dispatcher.class, dispatcherQuery);

            json.put("creationDate", new Date().toString());
            json.put("status", 0);
            json.put("dispatcherId", readDispatcher.get("id"));
                    
            return Persistence.create(DispatchOrder.class, json);

        } else {
            throw new AccessError(ERROR_TYPE.USER_NOT_AUTHORISED);
        }

    }

    @Override
    public JSONObject doUpdate(JSONObject json, long resource, String token) throws AccessError {
        JSONObject user = UserAccessControl.fetchUserByToken(User.class, token);
        if (user.get("level").equals(3)) {
            return Persistence.update(DispatchOrder.class, resource, json);
        } else {
            throw new AccessError(ERROR_TYPE.USER_NOT_AUTHORISED);
        }
    }

    @Override
    public JSONObject doRead(long resource, String token) throws AccessError {
        UserAccessControl.authOperation(User.class, token, 3);
        JSONObject result = DispatchOrderCache.retreive((long) resource);
        if (result == null) {
            JSONObject obj = new JSONObject();
            obj.put("vehicleId", resource);
            result = Persistence.listByProperties(DispatchOrder.class, obj);
        }
        return result;
    }

    @Override
    public JSONObject doDelete(long resource, String token) throws AccessError {
        UserAccessControl.authOperation(User.class, token, 3);
        return Persistence.delete(DispatchOrder.class, resource);
    }

}
