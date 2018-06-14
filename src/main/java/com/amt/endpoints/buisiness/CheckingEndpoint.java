/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.buisiness;

import com.amt.entities.management.Driver;
import com.amt.entities.history.StatusHistory;
import com.amt.entities.auth.User;
import com.amt.entities.buisiness.Vehicle;
import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import com.tna.endpoints.AuthorisedEndpoint;
import java.util.Date;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
@WebServlet("/vehicle/checking/*")
public class CheckingEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
        throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);

    }

    @Override
    public JSONObject doUpdate(JSONObject json, long resource, String token) throws AccessError {

        JSONObject user = UserAccessControl.fetchUserByToken(User.class, token);
        if (user.get("level").equals(1)) {
            JSONObject readVehicle = Persistence.read(Vehicle.class, resource);
            if (readVehicle.get("status").equals(1)) {
                JSONObject driverQuery = new JSONObject();
                driverQuery.put("userId", user.get("id"));

                JSONObject readDriver = Persistence.readByProperties(Driver.class, driverQuery);

                if (readVehicle.get("driver").equals(readDriver.get("id"))) {
                    JSONObject statusHistoryQuery = new JSONObject();
                    statusHistoryQuery.put("vehicleId", readVehicle.get("id"));
                    statusHistoryQuery.put("userId", user.get("id"));
                    statusHistoryQuery.put("from", readVehicle.get("status"));
                    statusHistoryQuery.put("to", 0);

                    readVehicle.put("driver", null);
                    readVehicle.put("status", 0);

                    JSONObject response = Persistence.update(Vehicle.class, resource, readVehicle);
                    Persistence.create(StatusHistory.class, statusHistoryQuery);
                    return response;
                } else {
                    throw new AccessError(ERROR_TYPE.USER_NOT_ALLOWED);
                }
            } else {
                throw new AccessError(ERROR_TYPE.ENTITY_UNAVAILABLE);
            }
        } else {
            throw new AccessError(ERROR_TYPE.USER_NOT_AUTHORISED);
        }
    }

    @Override
    public JSONObject doRead(long resource, String token) throws AccessError {
        JSONObject user = UserAccessControl.fetchUserByToken(User.class, token);
        if (user.get("level").equals(1)) {
            JSONObject readVehicle = Persistence.read(Vehicle.class, resource);
            if (readVehicle.get("status").equals(0)) {
                JSONObject driverQuery = new JSONObject();
                driverQuery.put("userId", user.get("id"));

                JSONObject readDriver = Persistence.readByProperties(Driver.class, driverQuery);

                JSONObject statusHistoryQuery = new JSONObject();
                statusHistoryQuery.put("vehicleId", readVehicle.get("id"));
                statusHistoryQuery.put("userId", user.get("id"));
                statusHistoryQuery.put("from", readVehicle.get("status"));
                statusHistoryQuery.put("to", 1);

                readVehicle.put("driver", readDriver.get("id"));
                readVehicle.put("status", 1);

                JSONObject response = Persistence.update(Vehicle.class, resource, readVehicle);
                Persistence.create(StatusHistory.class, statusHistoryQuery);
                return response;
            } else {
                throw new AccessError(ERROR_TYPE.ENTITY_UNAVAILABLE);
            }
        } else {
            throw new AccessError(ERROR_TYPE.USER_NOT_AUTHORISED);
        }

    }

    @Override
    public JSONObject doDelete(long resource, String token) throws AccessError {
        throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);
    }

}
