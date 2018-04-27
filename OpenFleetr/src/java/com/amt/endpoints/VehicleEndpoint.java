/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints;

import com.amt.entities.CurrentLocationEntity;
import com.amt.entities.UserEntity;
import com.amt.entities.VehicleEntity;
import com.amt.entities.CurrentStatusEntity;
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
@WebServlet("/vehicle/*")
public class VehicleEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
      UserAccessControl.authOperation(UserEntity.class,token,2);
      return Persistence.list(VehicleEntity.class);
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class,token,3);
        JSONObject obj = Persistence.create(VehicleEntity.class,json);
        JSONObject obj2 = new JSONObject();
        obj2.put("vehicleId",(long)obj.get("key"));
        obj2.put("driverId",-1);
        obj2.put("checkInDate","none");
        obj2.put("checkOutDate","none");
        obj2.put("status",1);
        obj2.put("notes","");
        Persistence.create(CurrentStatusEntity.class, obj2);
        JSONObject obj3 = new JSONObject();
        obj3.put("vehicleId",(long)obj.get("key"));
        obj3.put("longitude", 0.0);
        obj3.put("latitude", 0.0);
        obj3.put("timeStamp",new Date().toString());
        Persistence.create(CurrentLocationEntity.class,obj3);
        return obj;
        
    }

    @Override
    public JSONObject doUpdate(JSONObject json, long resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class,token,3);
        return Persistence.update(VehicleEntity.class,resource,json);
    }

    @Override
    public JSONObject doRead(long resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class,token,2);
        return Persistence.read(VehicleEntity.class,resource);
    }

    @Override
    public JSONObject doDelete(long resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class,token,3);
        JSONObject obj = new JSONObject();
        obj.put("vehicleId",resource);
        JSONObject obj2 = Persistence.readByProperties(CurrentStatusEntity.class, obj);
        Persistence.delete(CurrentStatusEntity.class, (long) obj2.get("id"));
        return Persistence.delete(VehicleEntity.class,resource);
        
    }
    
}
