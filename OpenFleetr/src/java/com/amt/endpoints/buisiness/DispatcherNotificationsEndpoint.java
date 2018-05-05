/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.buisiness;

import com.amt.entities.auth.UserEntity;
import com.amt.entities.buisiness.NotificationEntity;
import com.amt.entities.management.GeographicalAreaEntity;
import com.amt.common.data.GEOSql;
import com.amt.entities.management.JurisdictionEntity;
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
        
        JSONObject query1 = new JSONObject();
        query1.put("dispatcherId", UserAccessControl.fetchUserByToken(UserEntity.class, string).get("id"));
        JSONObject jurisdictions = Persistence.listByProperties(JurisdictionEntity.class, query1);
        
        JSONObject orQuery = new JSONObject();
        for (Object key : jurisdictions.keySet()) {
            JSONObject jurisdiction = (JSONObject) jurisdictions.get(key);
            orQuery.put("geographicalAreaId", jurisdiction.get("geographicalAreaId"));
        }
        
        JSONObject andQuery = new JSONObject();
        andQuery.put("wasHandled",false);
        
        JSONObject query = new JSONObject();
        query.put("or", orQuery);
        query.put("and", andQuery);
        
        return Persistence.searchByProperties(NotificationEntity.class, query);        
    }
    
    @Override
    public JSONObject doCreate(JSONObject jsono, String string) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, string, 2);
        jsono.put("apiUser", UserAccessControl.fetchUserByToken(UserEntity.class, string).get("id"));
        jsono.put("wasSeen", false);
        jsono.put("wasHandled", false);
        JSONObject query = new JSONObject();
        query.put("geographicalAreaId", GEOSql.liesWithinPolygon(GeographicalAreaEntity.class, jsono));
        return Persistence.create(NotificationEntity.class, jsono);
    }
    
    @Override
    public JSONObject doUpdate(JSONObject jsono, long l, String string) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }
    
    @Override
    public JSONObject doRead(long l, String string) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, string, 3);
        JSONObject query1 = new JSONObject();
        query1.put("geographicalAreaId", l);
        query1.put("dispatcherId", UserAccessControl.fetchUserByToken(UserEntity.class, string).get("id"));
        JSONObject jurisdictions = Persistence.listByProperties(JurisdictionEntity.class, query1);
        if (jurisdictions != null) {
            JSONObject query2 = new JSONObject();
            query2.put("geographicalAreaId", l);
            query2.put("wasHandled",false);
            return Persistence.listByProperties(NotificationEntity.class, query2);            
        } else {
            throw new AccessError(ERROR_TYPE.USER_NOT_ALLOWED);
        }
    }
    
    @Override
    public JSONObject doDelete(long l, String string) throws AccessError {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
