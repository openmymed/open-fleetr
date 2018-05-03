/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.management;

import com.amt.entities.auth.UserEntity;
import com.amt.entities.management.ApiUserEntity;
import com.amt.entities.management.DispatcherEntity;
import com.amt.entities.management.DriverEntity;
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
@WebServlet("/user/api/management/*")
public class ApiUserManagementEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String string) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, string, 4);
        JSONObject readApiUsers = Persistence.list(ApiUserEntity.class);
        for (Object key : readApiUsers.keySet()) {
            JSONObject entry = (JSONObject) readApiUsers.get(key);
            JSONObject user = Persistence.read(UserEntity.class, (int) entry.get("userId"));
            entry.put("token", user.get("token"));
            readApiUsers.put(key, entry);
        }
        return readApiUsers;
    }

    @Override
    public JSONObject doCreate(JSONObject jsono, String string) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, string, 4);
        JSONObject userQuery = new JSONObject();
        userQuery.put("userName", Utils.getRandom(32));
        userQuery.put("password", Utils.getRandom(32));
        long createdUser = UserAccessControl.createNewUser(UserEntity.class, userQuery, 3);
        JSONObject userDetails = UserAccessControl.login(UserEntity.class, userQuery);
        userQuery.put("userId", userDetails.get("id"));
        jsono.put("userId", userDetails.get("id"));
        userQuery.put("token", userDetails.get("token"));
        userQuery.put("apiUserId", Persistence.create(ApiUserEntity.class, jsono).get("key"));
        return userQuery;
    }

    @Override
    public JSONObject doUpdate(JSONObject jsono, long l, String string) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, string, 4);
        jsono.remove("userId");
        return Persistence.update(ApiUserEntity.class, l, jsono);
    }

    @Override
    public JSONObject doRead(long l, String string) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, string, 4);
        JSONObject readApiUser = Persistence.read(ApiUserEntity.class,l);
        JSONObject user = Persistence.read(UserEntity.class, (int) readApiUser.get("userId"));
        readApiUser.put("token", user.get("token"));
        return readApiUser;
    }

    @Override
    public JSONObject doDelete(long l, String string) throws AccessError {
        JSONObject readApiUser = doRead(l,string);
        Persistence.delete(UserEntity.class, (long) readApiUser.get("userId"));
        return Persistence.delete(ApiUserEntity.class, l);
        
    }

}
