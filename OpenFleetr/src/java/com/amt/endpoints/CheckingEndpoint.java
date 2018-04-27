/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints;

import com.amt.entities.CurrentStatusEntity;
import com.amt.entities.DriverEntity;
import com.amt.entities.HistoricalStatusEntity;
import com.amt.entities.UserEntity;
import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
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
@WebServlet("vehicle/checking/*")
public class CheckingEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
        throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 2);
        long UserId = (long)UserAccessControl.fetchUserByToken(UserEntity.class, token).get("id");

        JSONObject query1 = new JSONObject();
        query1.put("userId", UserId);
        JSONObject readDriver = Persistence.readByProperties(DriverEntity.class, query1);

        JSONObject query2 = new JSONObject();
        query2.put("driverId", readDriver.get("id"));
        JSONObject readStatus = Persistence.readByProperties(CurrentStatusEntity.class, query2);

        if (readStatus == null) {
            throw new AccessError(ERROR_TYPE.ENTITY_NOT_FOUND);

        }

        if ((int) readStatus.get("status") != 2) {
            throw new AccessError(ERROR_TYPE.ENTITY_UNAVAILABLE);
        }
        String date  = new Date().toString();
        readStatus.put("checkInDate", date);
        readStatus.put("timeStamp",date);
        Persistence.create(HistoricalStatusEntity.class, readStatus);

        JSONObject query = new JSONObject();
        query.put("driverId", -1);
        query.put("status", 1);
        query.put("checkOutDate", "");
        query.put("checkInDate", "");
        query.put("notes", "");

        return Persistence.update(CurrentStatusEntity.class, (int) readStatus.get("id"), query);

    }

    @Override
    public JSONObject doUpdate(JSONObject json, long resource, String token) throws AccessError {
        
        UserAccessControl.authOperation(UserEntity.class, token, 2); 
        long DriverId = (long)UserAccessControl.fetchUserByToken(UserEntity.class, token).get("id");
        
        JSONObject query1 = new JSONObject();
        query1.put("driverId", DriverId);
        if ( Persistence.readByProperties(CurrentStatusEntity.class, query1) != null) {
            throw new AccessError(ERROR_TYPE.USER_NOT_ALLOWED);
        }

        JSONObject obj = new JSONObject();
        obj.put("vehicleId", resource);
        JSONObject readStatus = Persistence.readByProperties(CurrentStatusEntity.class, obj);
        if (readStatus == null || (int) readStatus.get("status") != 1) {
            throw new AccessError(ERROR_TYPE.ENTITY_UNAVAILABLE);

        } else {
            JSONObject query3 = new JSONObject();
            JSONObject query4 = new JSONObject();
            query3.put("userId", DriverId);
            
            query4.put("checkOutDate", new Date().toString());
            query4.put("status", 2);      
            query4.put("driverId", Persistence.readByProperties(DriverEntity.class, query3).get("id"));
            
            readStatus.put("timeStamp", new Date().toString());
            Persistence.create(HistoricalStatusEntity.class,readStatus);
            return Persistence.update(CurrentStatusEntity.class, (int) readStatus.get("id"), query4);
        }
    }

    @Override
    public JSONObject doRead(long resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 2);
        return Persistence.read(CurrentStatusEntity.class, resource);
    }

    @Override
    public JSONObject doDelete(long resource, String token) throws AccessError {
        throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);
    }

}
