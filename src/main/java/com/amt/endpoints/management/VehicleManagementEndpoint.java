/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.management;

import com.amt.entities.auth.User;
import com.amt.entities.buisiness.Vehicle;
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
public class VehicleManagementEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
      UserAccessControl.authOperation(User.class,token,3);
      return Persistence.list(Vehicle.class);
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        UserAccessControl.authOperation(User.class,token,4);
        json.put("driver",null);
        json.put("status",0);
        json.put("latitude",0.0);
        json.put("longitude",0.0);
        return Persistence.create(Vehicle.class,json);        
    }

    @Override
    public JSONObject doUpdate(JSONObject json, long resource, String token) throws AccessError {
        UserAccessControl.authOperation(User.class,token,4);
        return Persistence.update(Vehicle.class,resource,json);
    }

    @Override
    public JSONObject doRead(long resource, String token) throws AccessError {
        UserAccessControl.authOperation(User.class,token,3);
        return Persistence.read(Vehicle.class,resource);
    }

    @Override
    public JSONObject doDelete(long resource, String token) throws AccessError {
        
        UserAccessControl.authOperation(User.class,token,4);
        return Persistence.delete(Vehicle.class,resource);
        
    }
    
}
