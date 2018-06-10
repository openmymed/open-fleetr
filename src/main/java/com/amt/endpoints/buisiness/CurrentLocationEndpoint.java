package com.amt.endpoints.buisiness;

import com.amt.common.cache.CurrentLocationEntityCache;
import com.amt.entities.buisiness.CurrentLocationEntity;
import com.amt.entities.management.DriverEntity;
import com.amt.entities.history.HistoricalLocationEntity;
import com.amt.entities.auth.UserEntity;
import com.amt.entities.buisiness.CurrentStatusEntity;
import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import com.tna.endpoints.AuthorisedEndpoint;
import java.util.Date;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author tareq
 */
@WebServlet("/vehicle/location/*")
public class CurrentLocationEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 2);
        return Persistence.list(CurrentLocationEntity.class);
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 1);
        JSONObject query1 = new JSONObject();
        query1.put("userId", (long) UserAccessControl.fetchUserByToken(UserEntity.class, token).get("id"));
        JSONObject readDriver = Persistence.readByProperties(DriverEntity.class, query1);

        JSONObject query2 = new JSONObject();
        query2.put("driverId", readDriver.get("id"));
        JSONObject readStatus = Persistence.readByProperties(CurrentStatusEntity.class, query2);

        if (readStatus != null && readStatus.containsKey("vehicleId")) {

            Persistence.create(HistoricalLocationEntity.class, json);
            return Persistence.update(CurrentLocationEntity.class, (int) readStatus.get("vehicleId"), json);
        } else {
            throw new AccessError(ERROR_TYPE.ENTITY_UNAVAILABLE);
        }

    }

    @Override
    public JSONObject doUpdate(JSONObject json, long resource, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doRead(long resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 2);
        JSONObject result = CurrentLocationEntityCache.retreive((long) resource);
        if (result == null) {
            JSONObject obj = new JSONObject();
            obj.put("vehicleId", resource);
            result = Persistence.readByProperties(CurrentLocationEntity.class, obj);
        }
        return result;
    }

    @Override
    public JSONObject doDelete(long resource, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

}
