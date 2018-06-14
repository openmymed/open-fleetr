/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.management;

import com.amt.entities.auth.User;
import com.amt.entities.management.Jurisdiction;
import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import com.tna.endpoints.AuthorisedEndpoint;
import com.tna.utils.JSON;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
@WebServlet("/user/dispatcher/manager/jurisdiction/*")
public class JurisdictionEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 4);
        return Persistence.list(Jurisdiction.class);
    }

    @Override
    public JSONObject doCreate(JSONObject jsono, String string) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doUpdate(JSONObject jsono, long l, String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 4);
        JSONObject result = null;
        if(jsono.get("function")=="remove"){
            JSONObject query = new JSONObject();
            query.put("dispatcherId",l);
            JSONObject dispatcherJurisdictions = Persistence.listByProperties(Jurisdiction.class,query);
            for(Object key : dispatcherJurisdictions.keySet()){
                JSONObject entry = (JSONObject) dispatcherJurisdictions.get(key);
                if(entry.get("geographicalAreaId")==jsono.get("geographicalAreaId")){
                    Persistence.delete(Jurisdiction.class, (long) entry.get("id"));
                    result = JSON.successResponse();
                }
            }    
        }else if(jsono.get("function")=="add"){
            JSONObject query = new JSONObject();
            query.put("dispatcherId",l);
            query.put("geographicalAreaId",jsono.get("geographicalAreaId"));
            result = Persistence.create(Jurisdiction.class, query);
        }else{
            throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
        }   
    return result;
    }
    

    @Override
    public JSONObject doRead(long l, String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 4);
        JSONObject query = new JSONObject();
        query.put("dispatcherId",l);
        return Persistence.listByProperties(Jurisdiction.class,query);
    }

    @Override
    public JSONObject doDelete(long l, String string) throws AccessError {
      UserAccessControl.authOperation(User.class, string, 4);
      JSONObject query =  new JSONObject();
      query.put("dispatcherId",l);
      return Persistence.deleteByProperties(Jurisdiction.class, query);
    }
    
}
