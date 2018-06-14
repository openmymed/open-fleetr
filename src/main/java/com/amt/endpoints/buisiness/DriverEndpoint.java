/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.buisiness;

import com.amt.entities.management.Driver;
import com.amt.entities.auth.User;
import com.amt.utils.Utils;
import com.tna.common.AccessError;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import com.tna.endpoints.AuthorisedEndpoint;
import java.util.Random;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
@WebServlet("/user/driver/*")
public class DriverEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
        UserAccessControl.authOperation(User.class, token, 3);
        return Persistence.list(Driver.class);

    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);


    }

    @Override
    public JSONObject doUpdate(JSONObject json, long resource, String token) throws AccessError {
        throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);

    }

    @Override
    public JSONObject doRead(long resource, String token) throws AccessError {
        UserAccessControl.authOperation(User.class, token, 3);
        return Persistence.read(Driver.class,resource);
    }

    @Override
    public JSONObject doDelete(long resource, String token) throws AccessError {  
        throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);

    }

   
}
