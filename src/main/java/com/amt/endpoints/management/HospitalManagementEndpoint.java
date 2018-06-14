/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.management;

import com.amt.common.data.GEOSql;
import com.amt.entities.auth.User;
import com.amt.entities.buisiness.Hospital;
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
@WebServlet("/hospital/manager/*")
public class HospitalManagementEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 3);
        return Persistence.list(Hospital.class);
    }

    @Override
    public JSONObject doCreate(JSONObject jsono, String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 4);
        return Persistence.create(Hospital.class, jsono);

    }

    @Override
    public JSONObject doUpdate(JSONObject jsono, long l, String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 4);
        return Persistence.update(Hospital.class, l, jsono);
    }

    @Override
    public JSONObject doRead(long l, String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 3);
        return Persistence.read(Hospital.class, l);
    }

    @Override
    public JSONObject doDelete(long l, String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 4);
        return Persistence.delete(Hospital.class, l);
    }

}
