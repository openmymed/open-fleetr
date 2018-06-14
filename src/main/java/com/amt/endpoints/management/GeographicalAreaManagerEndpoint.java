/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.management;

import com.amt.common.data.GEOSql;
import com.amt.entities.auth.User;
import com.amt.entities.management.GeographicalArea;
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
@WebServlet("/geographicalarea/manager/*")
public class GeographicalAreaManagerEndpoint extends AuthorisedEndpoint{

    @Override
    public JSONObject doList(String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 4);
        return Persistence.list(GeographicalArea.class);
    }

    @Override
    public JSONObject doCreate(JSONObject jsono, String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 4);
        return GEOSql.writePolygon(User.class, jsono);
    }

    @Override
    public JSONObject doUpdate(JSONObject jsono, long l, String string) throws AccessError {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject doRead(long l, String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 4);
        JSONObject response = new JSONObject();
        response.put("id",l);
        response.put("coordinates",GEOSql.readPolygon(GeographicalArea.class, l));
        return response;
    }

    @Override
    public JSONObject doDelete(long l, String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 4);
        return Persistence.delete(GeographicalArea.class,l);    
    
    }
    
}
