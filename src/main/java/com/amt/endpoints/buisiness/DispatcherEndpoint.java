/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.buisiness;

import com.amt.entities.auth.User;
import com.amt.entities.management.Dispatcher;
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
@WebServlet("/user/dispatcher/*")
public class DispatcherEndpoint extends AuthorisedEndpoint{

    @Override
    public JSONObject doList(String token) throws AccessError {
        JSONObject user = UserAccessControl.fetchUserByToken(User.class, token);
        if((long)user.get("level") == 3){
            JSONObject dispatcherQuery = new JSONObject();
            dispatcherQuery.put("userId",user.get("id"));
            return Persistence.readByProperties(Dispatcher.class, dispatcherQuery);
        }else{
          throw new AccessError(ERROR_TYPE.USER_NOT_ALLOWED);
    }
        
    }

    @Override
    public JSONObject doCreate(JSONObject data, String token) throws AccessError {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject doUpdate(JSONObject data, long resource, String token) throws AccessError {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject doRead(long resource, String token) throws AccessError {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject doDelete(long resource, String token) throws AccessError {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
