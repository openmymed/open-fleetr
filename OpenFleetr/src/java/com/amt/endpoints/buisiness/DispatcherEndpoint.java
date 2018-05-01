/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.buisiness;

import com.amt.entities.management.DispatcherEntity;
import com.amt.entities.auth.UserEntity;
import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import com.tna.endpoints.AuthorisedEndpoint;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
@WebServlet("/user/dispatcher/*")
public class DispatcherEndpoint extends AuthorisedEndpoint{

    @Override
    public JSONObject doList(String string) throws AccessError {
       JSONObject user = UserAccessControl.fetchUserByToken(UserEntity.class,string);
       JSONObject query = new JSONObject();
       query.put("userId",user.get("id"));
       return Persistence.readByProperties(DispatcherEntity.class,query);      
    }

    @Override
    public JSONObject doCreate(JSONObject jsono, String string) throws AccessError {
       JSONObject readDispatcher = doList(string);  
       return Persistence.update(DispatcherEntity.class, UserEntity.class, jsono, (long)readDispatcher.get("id"), string);
       
    }

    @Override
    public JSONObject doUpdate(JSONObject jsono, long l, String string) throws AccessError {      
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doRead(long l, String string) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doDelete(long l, String string) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);

    }
    
}
