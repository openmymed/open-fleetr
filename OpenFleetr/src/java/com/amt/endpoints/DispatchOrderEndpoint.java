/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints;

import com.amt.entities.DispatchOrderEntity;
import com.amt.entities.HistoricalDispatchOrderEntity;
import com.amt.entities.UserEntity;
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
@WebServlet("/dispatch/*")
public class DispatchOrderEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 3);
        return Persistence.list(DispatchOrderEntity.class);
    }

    @Override
    public JSONObject doCreate(JSONObject jsono, String string) throws AccessError {
       throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doUpdate(JSONObject json, int resource, String token) throws AccessError {
        throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);
    }
    
    @Override
    public JSONObject doRead(int resource, String token) throws AccessError {
       UserAccessControl.authOperation(UserEntity.class, token, 3);
        return Persistence.read(DispatchOrderEntity.class,resource);
    }

    @Override
    public JSONObject doDelete(int resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 3);
        return Persistence.delete(DispatchOrderEntity.class,resource);    
    }
    
}
