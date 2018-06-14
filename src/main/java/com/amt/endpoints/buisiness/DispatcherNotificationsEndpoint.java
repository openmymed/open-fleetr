/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.buisiness;

import com.amt.entities.auth.User;
import com.amt.entities.buisiness.NotificationEntity;
import com.amt.entities.management.GeographicalArea;
import com.amt.common.data.GEOSql;
import com.amt.entities.management.ApiUser;
import com.amt.entities.management.Jurisdiction;
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
        UserAccessControl.authOperation(User.class, string, 3);

        JSONObject query1 = new JSONObject();
        query1.put("dispatcherId", UserAccessControl.fetchUserByToken(User.class, string).get("id"));
        JSONObject jurisdictions = Persistence.listByProperties(Jurisdiction.class, query1);

        JSONObject orQuery = new JSONObject();
        for (Object key : jurisdictions.keySet()) {
            JSONObject jurisdiction = (JSONObject) jurisdictions.get(key);
            orQuery.put("geographicalAreaId", jurisdiction.get("geographicalAreaId"));
        }

        JSONObject andQuery = new JSONObject();
        andQuery.put("wasHandled", false);

        JSONObject query = new JSONObject();
        query.put("or", orQuery);
        query.put("and", andQuery);

        return Persistence.searchByProperties(NotificationEntity.class, query);
    }

    @Override
    public JSONObject doCreate(JSONObject jsono, String string) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doUpdate(JSONObject jsono, long l, String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 3);
        JSONObject query1 = new JSONObject();
        query1.put("dispatcherId", UserAccessControl.fetchUserByToken(User.class, string).get("id"));
        query1.put("id", l);
        JSONObject notification = Persistence.readByProperties(Jurisdiction.class, query1);
        if (notification != null) {
            JSONObject update = new JSONObject();
            Object dispatchOrderId = jsono.get("dispatchOrderId");
            if (dispatchOrderId != null) {
                update.put("dispatchOrderId", dispatchOrderId);
                update.put("wasHandled", true);
                return Persistence.update(NotificationEntity.class, l, update);
            } else {

                throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
            }

        } else {
            throw new AccessError(ERROR_TYPE.ENTITY_NOT_FOUND);
        }

    }

    @Override
    public JSONObject doRead(long l, String string) throws AccessError {
        UserAccessControl.authOperation(User.class, string, 3);
        JSONObject query1 = new JSONObject();
        query1.put("geographicalAreaId", l);
        query1.put("dispatcherId", UserAccessControl.fetchUserByToken(User.class, string).get("id"));
        JSONObject jurisdictions = Persistence.listByProperties(Jurisdiction.class, query1);
        if (jurisdictions != null) {
            JSONObject query2 = new JSONObject();
            query2.put("geographicalAreaId", l);
            query2.put("wasHandled", false);
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
