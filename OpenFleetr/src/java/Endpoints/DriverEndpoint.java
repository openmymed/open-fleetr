/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Endpoints;

import Entities.DriverEntity;
import Entities.UserEntity;
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

@WebServlet("/driver/*")
public class DriverEndpoint extends AuthorisedEndpoint{

    @Override
    public JSONObject doList(String token) throws AccessError {
       UserAccessControl.authOperation(UserEntity.class, token, 2);
       return Persistence.list(DriverEntity.class);
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 3);
        
       return Persistence.create(DriverEntity.class,json);
    }

    @Override
    public JSONObject doUpdate(JSONObject json, int resource, String token) throws AccessError {
       UserAccessControl.authOperation(UserEntity.class, token, 3);
       return Persistence.update(DriverEntity.class,resource,json);
    }

    @Override
    public JSONObject doRead(int resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 3);
        JSONObject query = new JSONObject();
        query.put("userId", resource);
        return Persistence.readByProperties(DriverEntity.class,query);
    }

    @Override
    public JSONObject doDelete(int resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 3);
       return Persistence.delete(DriverEntity.class,resource);
    }
    
}
