/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.management;

import com.amt.entities.auth.UserEntity;
import com.amt.entities.management.DispatcherEntity;
import com.amt.entities.management.DriverEntity;
import com.amt.utils.Utils;
import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import com.tna.endpoints.AuthorisedEndpoint;
import com.tna.entities.AuthorisedEntity;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
@WebServlet("/user/driver/manager/*")
public class DriverManagementEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String string) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, string, 4);
        JSONObject readDrivers = Persistence.list(DriverEntity.class);
        for (Object key : readDrivers.keySet()) {
            JSONObject entry = (JSONObject) readDrivers.get(key);
            JSONObject user = Persistence.read(UserEntity.class, (int) entry.get("userId"));
            entry.put("userName", user.get("userName"));
            entry.put("password", user.get("password"));
            readDrivers.put(key, entry);

        }
        return readDrivers;
    }

    @Override
    public JSONObject doCreate(JSONObject jsono, String string) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, string, 4);
        JSONObject userQuery = new JSONObject();
        userQuery.put("userName", Utils.getRandom(8));
        userQuery.put("password", Utils.getRandom(16));
        long createdUser = UserAccessControl.createNewUser(UserEntity.class, userQuery, 1);
        
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
        jsono.remove("userId");

        return Persistence.update(DriverEntity.class, l, jsono);
    }

    @Override
    public JSONObject doRead(long l, String string) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, string, 4);
        JSONObject readDriver = Persistence.read(DriverEntity.class, l);
        JSONObject user = Persistence.read(UserEntity.class, (int) readDriver.get("userId"));
        readDriver.put("userName", user.get("userName"));
        readDriver.put("password", user.get("password"));
        return readDriver;

    }

    @Override
    public JSONObject doDelete(long l, String string) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

}
