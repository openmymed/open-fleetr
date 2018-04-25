/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints;

import com.amt.entities.UserEntity;
import com.amt.entities.CurrentStatusEntity;
import com.amt.entities.HistoricalStatusEntity;
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
@WebServlet("/status/*")
public class CurrentStatusEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 2);
        return Persistence.list(CurrentStatusEntity.class);
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doUpdate(JSONObject json, int resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 1);
        JSONObject obj = new JSONObject();
        obj.put("vehicleId",resource);
        JSONObject obj2 = Persistence.readByProperties(CurrentStatusEntity.class,obj);
        Persistence.create(HistoricalStatusEntity.class, obj);
        return  Persistence.update(CurrentStatusEntity.class,(int)obj2.get("id"),json);
    }

    @Override
    public JSONObject doRead(int resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token,2);
        JSONObject obj = new JSONObject();
        obj.put("vehicleId",resource);
        return Persistence.readByProperties(CurrentStatusEntity.class,obj);
    }

    @Override
    public JSONObject doDelete(int resource, String token) throws AccessError {
        throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);
    }
    
}
