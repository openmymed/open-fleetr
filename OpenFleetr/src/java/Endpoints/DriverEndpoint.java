/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Endpoints;

import Entities.DriverEntity;
import Entities.UserEntity;
import com.tna.common.AccessError;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import com.tna.endpoints.AuthorisedEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
@WebServlet("/driver/*")
public class DriverEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 2);
        JSONObject drivers = Persistence.list(DriverEntity.class);

        for (Object key : drivers.keySet()) {
            JSONObject entry = (JSONObject) drivers.get(key);
            JSONObject user = Persistence.read(UserEntity.class, (int) entry.get("userId"));
            entry.put("userName", user.get("userName"));
            entry.put("password", user.get("password"));
            drivers.put(key, entry);
        }
        return drivers;

    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 3);
        JSONObject userQuery = new JSONObject();
        userQuery.put("userName", getRandom(8));
        userQuery.put("password", getRandom(16));
        long createdUser = UserAccessControl.createNewUser(UserEntity.class, userQuery, 2);

        JSONObject driverQuery = new JSONObject();
        driverQuery.put("firstName", json.get("firstName"));
        driverQuery.put("lastName", json.get("lastName"));
        driverQuery.put("birthDate", json.get("birthDate"));
        driverQuery.put("phoneNumber", json.get("phoneNumber"));
        driverQuery.put("userId", createdUser);

        userQuery.put("driverId", Persistence.create(DriverEntity.class, driverQuery).get("key"));

        return userQuery;

    }

    @Override
    public JSONObject doUpdate(JSONObject json, int resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 3);
        return Persistence.update(DriverEntity.class, resource, json);
    }

    @Override
    public JSONObject doRead(int resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 3);
        JSONObject query = new JSONObject();
        JSONObject driver = Persistence.read(DriverEntity.class, resource);
        JSONObject user = Persistence.read(UserEntity.class, (int) driver.get("userId"));
        driver.put("userName", user.get("userName"));
        driver.put("password", user.get("password"));
        return driver;
    }

    @Override
    public JSONObject doDelete(int resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 3);
        JSONObject json = new JSONObject();
        JSONObject driver = Persistence.read(DriverEntity.class, resource);
        Persistence.delete(UserEntity.class,(int)driver.get("userId"));
        return Persistence.delete(DriverEntity.class,resource);
        
    }

    private String getRandom(int length) {
        String subset = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            int index = r.nextInt(subset.length());
            char c = subset.charAt(index);
            sb.append(c);
        }
        return sb.toString();
    }
}
