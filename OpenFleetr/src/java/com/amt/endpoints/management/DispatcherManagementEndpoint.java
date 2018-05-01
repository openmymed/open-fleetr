/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.management;

import com.amt.entities.management.DispatcherEntity;
import com.amt.entities.management.DriverEntity;
import com.amt.entities.auth.UserEntity;
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
public class DispatcherManagementEndpoint extends AuthorisedEndpoint{

    @Override
    public JSONObject doList(String string) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, string, 4);
        return Persistence.list(DispatcherEntity.class);
      
    }

    @Override
    public JSONObject doCreate(JSONObject jsono, String string) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, string, 4);
        JSONObject userQuery = new JSONObject();
        userQuery.put("userName", Utils.getRandom(8));
        userQuery.put("password", Utils.getRandom(16));
        long createdUser = UserAccessControl.createNewUser(UserEntity.class, userQuery, 2);
        JSONObject dispatcherQuery = new JSONObject();
        dispatcherQuery.put("firstName", jsono.get("firstName"));
        dispatcherQuery.put("lastName", jsono.get("lastName"));
        dispatcherQuery.put("birthDate", jsono.get("birthDate"));
        dispatcherQuery.put("phoneNumber", jsono.get("phoneNumber"));
        dispatcherQuery.put("userId", createdUser);
        userQuery.put("dispatcherId", Persistence.create(DispatcherEntity.class, dispatcherQuery).get("key"));
        return userQuery;
    }

    @Override
    public JSONObject doUpdate(JSONObject jsono, long l, String string) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, string, 4);
        return Persistence.update(DispatcherEntity.class,l,jsono);      
    }

    @Override
    public JSONObject doRead(long l, String string) throws AccessError {
    UserAccessControl.authOperation(UserEntity.class, string, 4);
    return Persistence.read(DispatcherEntity.class,l);
    }

    @Override
    public JSONObject doDelete(long l, String string) throws AccessError {
        JSONObject  read = doRead(l,string);
        Persistence.delete(DispatcherEntity.class, (long) read.get("id"));
        return Persistence.delete(UserEntity.class, (long) read.get("userId"));
        
    }
    
}
