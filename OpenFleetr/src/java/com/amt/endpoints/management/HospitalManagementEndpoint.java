/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.management;

import com.amt.common.data.GEOSql;
import com.amt.entities.auth.UserEntity;
import com.amt.entities.management.GeographicalAreaEntity;
import com.amt.entities.management.HospitalEntity;
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
        UserAccessControl.authOperation(UserEntity.class, string, 3);
        return Persistence.list(HospitalEntity.class);
    }

    @Override
    public JSONObject doCreate(JSONObject jsono, String string) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, string, 4);
        long jurisdiction = GEOSql.liesWithinPolygon(GeographicalAreaEntity.class, jsono);
        jsono.put("geographicalAreaId", jurisdiction);
        return Persistence.create(HospitalEntity.class,jsono);

    }

    @Override
    public JSONObject doUpdate(JSONObject jsono, long l, String string) throws AccessError {
   UserAccessControl.authOperation(UserEntity.class, string, 4);
        long jurisdiction = GEOSql.liesWithinPolygon(GeographicalAreaEntity.class, jsono);
        if (jsono.containsKey("latitude") && jsono.containsKey("longitude")){
        jsono.put("geographicalAreaId", jurisdiction);
    }
        return Persistence.update(HospitalEntity.class,l,jsono);
    }

    @Override
    public JSONObject doRead(long l, String string) throws AccessError {
  UserAccessControl.authOperation(UserEntity.class, string, 3);
        return Persistence.read(HospitalEntity.class,l);    
    }

    @Override
    public JSONObject doDelete(long l, String string) throws AccessError {
    UserAccessControl.authOperation(UserEntity.class, string, 4);
    return Persistence.delete(HospitalEntity.class,l);    
    }
    
}
