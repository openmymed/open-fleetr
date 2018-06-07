/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.management;

import com.amt.entities.buisiness.CurrentLocationEntity;
import com.amt.entities.auth.UserEntity;
import com.amt.entities.management.VehicleEntity;
import com.amt.entities.buisiness.CurrentStatusEntity;
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
@WebServlet("/vehicle/manager/*")
public class VehicleEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
      UserAccessControl.authOperation(UserEntity.class,token,3);
      return Persistence.list(VehicleEntity.class);
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class,token,4);
        JSONObject createdVehicle = Persistence.create(VehicleEntity.class,json);
        
        JSONObject statusQuery = new JSONObject();
        statusQuery.put("vehicleId",(long)createdVehicle.get("key"));
        statusQuery.put("driverId",null);
        statusQuery.put("checkInDate","none");
        statusQuery.put("checkOutDate","none");
        statusQuery.put("status",1);
        statusQuery.put("notes","");
        Persistence.create(CurrentStatusEntity.class, statusQuery);
        
        JSONObject locationQuery = new JSONObject();
        locationQuery.put("vehicleId",(long)createdVehicle.get("key"));
        locationQuery.put("longitude", 0.0);
        locationQuery.put("latitude", 0.0);
        locationQuery.put("timeStamp",new Date().toString());
        Persistence.create(CurrentLocationEntity.class,locationQuery);
        return createdVehicle;
        
    }

    @Override
    public JSONObject doUpdate(JSONObject json, long resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class,token,4);
        return Persistence.update(VehicleEntity.class,resource,json);
    }

    @Override
    public JSONObject doRead(long resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class,token,3);
        return Persistence.read(VehicleEntity.class,resource);
    }

    @Override
    public JSONObject doDelete(long resource, String token) throws AccessError {
        
        UserAccessControl.authOperation(UserEntity.class,token,4);
        JSONObject query = new JSONObject();
        query.put("vehicleId",resource);
        JSONObject readStatus = Persistence.readByProperties(CurrentStatusEntity.class, query);
        Persistence.delete(CurrentStatusEntity.class, (long) readStatus.get("id"));
        
        JSONObject readLocation = Persistence.readByProperties(CurrentLocationEntity.class, query);
        Persistence.delete(CurrentLocationEntity.class,(long)readLocation.get("id"));
        
        return Persistence.delete(VehicleEntity.class,resource);
        
    }
    
}
