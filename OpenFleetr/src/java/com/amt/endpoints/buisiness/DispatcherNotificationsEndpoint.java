/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.buisiness;

import com.amt.entities.auth.UserEntity;
import com.amt.entities.buisiness.NotificationEntity;
import com.amt.entities.management.DispatcherEntity;
import com.amt.entities.management.GeographicalAreaEntity;
import com.amt.common.data.GEOSql;
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
@WebServlet("/user/dispatcher/notifications/*")
public class DispatcherNotificationsEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String string) throws AccessError {
       UserAccessControl.authOperation(UserEntity.class, string, 3);
       JSONObject query = new JSONObject();
       query.put("dispatcherId",UserAccessControl.fetchUserByToken(UserEntity.class, string).get("id"));
       query.put("wasHandled",false);
       return Persistence.listByProperties(NotificationEntity.class,query); 
    }

    @Override
    public JSONObject doCreate(JSONObject jsono, String string) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, string, 2);
        jsono.put("apiUser",UserAccessControl.fetchUserByToken(UserEntity.class, string).get("id"));
        jsono.put("wasSeen",false);
        jsono.put("wasHandled",false);
        JSONObject query = new JSONObject();
        query.put("geographicalAreaId",GEOSql.liesWithinPolygon(GeographicalAreaEntity.class, jsono));
        jsono.put("dispatcherId",Persistence.readByProperties(DispatcherEntity.class, query).get("id"));
        return Persistence.create(NotificationEntity.class, jsono);
    }

    @Override
    public JSONObject doUpdate(JSONObject jsono, long l, String string) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doRead(long l, String string) throws AccessError {
       UserAccessControl.authOperation(UserEntity.class, string, 3);
       JSONObject query = new JSONObject();
       query.put("dispatcherId",UserAccessControl.fetchUserByToken(UserEntity.class, string).get("id"));
       query.put("id",l);
       return Persistence.readByProperties(NotificationEntity.class,query);    
    }

    @Override
    public JSONObject doDelete(long l, String string) throws AccessError {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
