/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.management;

import com.amt.entities.auth.User;
import com.amt.entities.management.Dispatcher;
import com.amt.entities.management.Driver;
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
        UserAccessControl.authOperation(User.class, string, 4);
        JSONObject readDrivers = Persistence.list(Driver.class);
        for (Object key : readDrivers.keySet()) {
            JSONObject entry = (JSONObject) readDrivers.get(key);
            JSONObject user = Persistence.read(User.class, (int) entry.get("userId"));
            entry.put("userName", user.get("userName"));
            entry.put("password", user.get("password"));
            readDrivers.put(key, entry);

        }
        return readDrivers;
    }

    @Override
    public JSONObject doCreate(JSONObject jsono, String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 4);
        JSONObject userQuery = new JSONObject();
        userQuery.put("userName", Utils.getRandom(8));
        userQuery.put("password", Utils.getRandom(16));
        long createdUser = UserAccessControl.createNewUser(User.class, userQuery, 1);
        
        JSONObject driverQuery = new JSONObject();
        driverQuery.put("firstName", jsono.get("firstName"));
        driverQuery.put("lastName", jsono.get("lastName"));
        driverQuery.put("birthDate", jsono.get("birthDate"));
        driverQuery.put("phoneNumber", jsono.get("phoneNumber"));
        driverQuery.put("userId", createdUser);
        
        userQuery.put("driverId", Persistence.create(Driver.class, driverQuery).get("key"));
        return userQuery;
    }

    @Override
    public JSONObject doUpdate(JSONObject jsono, long l, String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 4);
        jsono.remove("userId");

        return Persistence.update(Driver.class, l, jsono);
    }

    @Override
    public JSONObject doRead(long l, String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 4);
        JSONObject readDriver = Persistence.read(Driver.class, l);
        JSONObject user = Persistence.read(User.class, (int) readDriver.get("userId"));
        readDriver.put("userName", user.get("userName"));
        readDriver.put("password", user.get("password"));
        return readDriver;

    }

    @Override
    public JSONObject doDelete(long l, String string) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

}
