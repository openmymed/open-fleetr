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
import java.util.Date;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
@WebServlet("/vehicle/status/*")
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
    public JSONObject doUpdate(JSONObject json, long resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 1);
        JSONObject query1 = new JSONObject();
        query1.put("vehicleId",resource);
        JSONObject query2 = Persistence.readByProperties(CurrentStatusEntity.class,query1);
        query2.put("timeStamp", new Date().toString());
        Persistence.create(HistoricalStatusEntity.class, query2);
        return  Persistence.update(CurrentStatusEntity.class,(int)query2.get("id"),json);
    }

    @Override
    public JSONObject doRead(long resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token,2);
        JSONObject obj = new JSONObject();
        obj.put("vehicleId",resource);
        return Persistence.readByProperties(CurrentStatusEntity.class,obj);
    }

    @Override
    public JSONObject doDelete(long resource, String token) throws AccessError {
        throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);
    }
    
}
