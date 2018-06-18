/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.buisiness;

import com.amt.entities.auth.User;
import com.amt.entities.buisiness.Hospital;
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
@WebServlet("/hospital/*")
public class HospitalEndpoint  extends AuthorisedEndpoint{

    @Override
    public JSONObject doList(String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 2);
        return Persistence.list(Hospital.class);
    }

    @Override
    public JSONObject doCreate(JSONObject jsono, String string) throws AccessError {
        throw new AccessError(ERROR_TYPE.USER_NOT_ALLOWED);
    }

    @Override
    public JSONObject doUpdate(JSONObject jsono, long l, String string) throws AccessError {
        throw new AccessError(ERROR_TYPE.USER_NOT_ALLOWED);
    }

    @Override
    public JSONObject doRead(long l, String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 2);
        return Persistence.read(Hospital.class,l);    
    }

    @Override
    public JSONObject doDelete(long l, String string) throws AccessError {
        throw new AccessError(ERROR_TYPE.USER_NOT_ALLOWED);
    }
    
}
