/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Endpoints;

import Entities.DriverEntity;
import Entities.User;
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
       UserAccessControl.authOperation(User.class, token, 2);
       return Persistence.list(DriverEntity.class);
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        UserAccessControl.authOperation(User.class, token, 3);
       return Persistence.create(DriverEntity.class,json);
    }

    @Override
    public JSONObject doUpdate(JSONObject json, int resource, String token) throws AccessError {
         UserAccessControl.authOperation(User.class, token, 3);
       return Persistence.update(DriverEntity.class,resource,json);
    }

    @Override
    public JSONObject doRead(int resource, String token) throws AccessError {
        UserAccessControl.authOperation(User.class, token, 3);
       return Persistence.read(DriverEntity.class,resource);
    }

    @Override
    public JSONObject doDelete(int resource, String token) throws AccessError {
        UserAccessControl.authOperation(User.class, token, 3);
       return Persistence.delete(DriverEntity.class,resource);
    }
    
}
