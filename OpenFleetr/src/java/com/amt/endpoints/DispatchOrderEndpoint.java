/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints;

import com.amt.entities.CurrentLocationEntity;
import com.amt.entities.CurrentStatusEntity;
import com.amt.entities.CurrentDispatchOrderEntity;
import com.amt.entities.DriverEntity;
import com.amt.entities.HistoricalDispatchOrderEntity;
import com.amt.entities.HistoricalLocationEntity;
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
@WebServlet("vehicle/dispatch/*")
public class DispatchOrderEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 3);
        return Persistence.list(CurrentDispatchOrderEntity.class);
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 2);
        JSONObject user = UserAccessControl.fetchUserByToken(UserEntity.class, token);
        
        JSONObject query1 = new JSONObject();
        query1.put("userId",user.get("id"));
        JSONObject driver = Persistence.readByProperties(DriverEntity.class,query1);
        
        JSONObject query2 = new JSONObject();
        query2.put("driverId", driver.get("id"));
        JSONObject dispatchOrder = Persistence.readByProperties(CurrentDispatchOrderEntity.class,query2);
        
        if(dispatchOrder == null){
            throw new AccessError(ERROR_TYPE.USER_NOT_ALLOWED);
        }
        dispatchOrder.put("completionDate",new Date().toString());
        Persistence.create(HistoricalDispatchOrderEntity.class, dispatchOrder);
        return Persistence.delete(CurrentDispatchOrderEntity.class,(long)dispatchOrder.get("id"));

    }

    @Override
    public JSONObject doUpdate(JSONObject json, long resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 3);
        JSONObject query1 = new JSONObject();
        query1.put("vehicleId", resource);
        JSONObject readVehicleStatus = Persistence.readByProperties(CurrentStatusEntity.class, query1);

        if (readVehicleStatus != null && readVehicleStatus.containsKey("status")) {
            if ((long) readVehicleStatus.get("status") != 2) {
                throw new AccessError(ERROR_TYPE.ENTITY_UNAVAILABLE);
            } else {
                JSONObject readVehicleLocation = Persistence.readByProperties(CurrentLocationEntity.class, query1);
                if (readVehicleLocation == null) {
                    throw new AccessError(ERROR_TYPE.ENTITY_NOT_FOUND);
                }else{
                    
                    json.put("startLatitude",readVehicleLocation.get("latitude"));
                    json.put("startLongitude",readVehicleLocation.get("longitude"));
                    json.put("creationDate", new Date().toString());
                    json.put("status",1);
                    return Persistence.create(CurrentDispatchOrderEntity.class,json);
                }    
                }
            }else{
                throw new AccessError(ERROR_TYPE.ENTITY_NOT_FOUND);
        }
    }

    @Override
    public JSONObject doRead(long resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 3);
        return Persistence.read(CurrentDispatchOrderEntity.class, resource);
    }

    @Override
    public JSONObject doDelete(long resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 3);
        return Persistence.delete(CurrentDispatchOrderEntity.class, resource);
    }

}
