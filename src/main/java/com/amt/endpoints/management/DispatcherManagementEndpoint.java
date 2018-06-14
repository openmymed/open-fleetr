/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.management;

import com.amt.entities.management.Dispatcher;
import com.amt.entities.management.Driver;
import com.amt.entities.auth.User;
import com.amt.utils.Utils;
import com.tna.common.AccessError;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import com.tna.endpoints.AuthorisedEndpoint;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
@WebServlet("/user/dispatcher/manager/*")
public class DispatcherManagementEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 4);
        JSONObject dispatcherList =  Persistence.list(Dispatcher.class);
        for(Object key : dispatcherList.keySet()){
        JSONObject readDispatcher = (JSONObject) dispatcherList.get(key);
        JSONObject query1 = new JSONObject();
        JSONObject readUser = Persistence.read(User.class, (int)readDispatcher.get("userId"));
        readDispatcher.put("userName",readUser.get("userName"));
        readDispatcher.put("password",readUser.get("password"));
        dispatcherList.put(key, readDispatcher);
        }
        return dispatcherList;
        

    }

    @Override
    public JSONObject doCreate(JSONObject jsono, String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 4);
        
        JSONObject userQuery = new JSONObject();
        userQuery.put("userName", Utils.getRandom(8));
        userQuery.put("password", Utils.getRandom(16));
        long createdUser = UserAccessControl.createNewUser(User.class, userQuery, 2);
        
        JSONObject dispatcherQuery = new JSONObject();
        dispatcherQuery.put("firstName", jsono.get("firstName"));
        dispatcherQuery.put("lastName", jsono.get("lastName"));
        dispatcherQuery.put("birthDate", jsono.get("birthDate"));
        dispatcherQuery.put("phoneNumber", jsono.get("phoneNumber"));
        dispatcherQuery.put("userId", createdUser);
       
        userQuery.put("dispatcherId", Persistence.create(Dispatcher.class, dispatcherQuery).get("key"));
        return userQuery;
    }

    @Override
    public JSONObject doUpdate(JSONObject jsono, long l, String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 4);
        jsono.remove("userId");
        return Persistence.update(Dispatcher.class, l, jsono);
    }

    @Override
    public JSONObject doRead(long l, String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 4);
        JSONObject readDispatcher = Persistence.read(Dispatcher.class, l);
        JSONObject query1 = new JSONObject();
        query1.put("id",readDispatcher.get("userId"));
        JSONObject readUser = Persistence.readByProperties(User.class, query1);
        readDispatcher.put("userName",readUser.get("userName"));
        readDispatcher.put("password",readUser.get("password"));
        return readDispatcher;
    }

    @Override
    public JSONObject doDelete(long l, String string) throws AccessError {
        JSONObject read = doRead(l, string);
        Persistence.delete(Dispatcher.class, (long) read.get("id"));
        return Persistence.delete(User.class, (long) read.get("userId"));

    }

}
