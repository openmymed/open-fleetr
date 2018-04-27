/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints;

import com.amt.entities.HistoricalLocationEntity;
import com.amt.entities.UserEntity;
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
@WebServlet("vehicle/location/history/*")
public class HistoricalLocationEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
       UserAccessControl.authOperation(UserEntity.class, token, 2);
       return Persistence.list(HistoricalLocationEntity.class);
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doUpdate(JSONObject json, long resource, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doRead(long resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 2);
        JSONObject obj = new JSONObject();
        obj.put("vehicleId",resource);
        return Persistence.listByProperties(HistoricalLocationEntity.class, obj);
        
      }

    @Override
    public JSONObject doDelete(long resource, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }
    
}
