/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Endpoints;

import Entities.User;
import Entities.VehicleEntity;
import Entities.VehicleStatusEntity;
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
@WebServlet("/vehicle/*")
public class VehicleEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
      UserAccessControl.authOperation(User.class,token,2);
      return Persistence.list(VehicleEntity.class);
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        UserAccessControl.authOperation(User.class,token,3);
        JSONObject obj = Persistence.create(VehicleEntity.class,json);
        JSONObject obj2 = new JSONObject();
        obj2.put("vehicleId",obj.get("id"));
        obj2.put("driverId",-1);
        obj2.put("checkInDate","none");
        obj2.put("checkOutDate","none");
        Persistence.create(VehicleStatusEntity.class, obj2);
        return obj;
        
    }

    @Override
    public JSONObject doUpdate(JSONObject json, int resource, String token) throws AccessError {
         UserAccessControl.authOperation(User.class,token,3);
        return Persistence.update(VehicleEntity.class,resource,json);
    }

    @Override
    public JSONObject doRead(int resource, String token) throws AccessError {
        UserAccessControl.authOperation(User.class,token,2);
        return Persistence.read(VehicleEntity.class,resource);
    }

    @Override
    public JSONObject doDelete(int resource, String token) throws AccessError {
        UserAccessControl.authOperation(User.class,token,3);
        JSONObject obj = new JSONObject();
        obj.put("vehicleId",resource);
        JSONObject obj2 = Persistence.readByProperties(VehicleStatusEntity.class, obj);
        Persistence.delete(VehicleStatusEntity.class, (long) obj2.get("id"));
        return Persistence.delete(VehicleEntity.class,resource);
        
    }
    
}
